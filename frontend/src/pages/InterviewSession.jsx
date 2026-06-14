import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import interviewService from '../services/interviewService';
import messageService from '../services/messageService';
import { Cpu, User, Send, Sparkles, ArrowLeft, Crown, CheckCircle, Briefcase, AlertTriangle, Clock } from 'lucide-react';

// Timer
const Timer = ({ running }) => {
  const [s, setS] = useState(0);
  useEffect(() => {
    if (!running) return;
    const t = setInterval(() => setS(p => p + 1), 1000);
    return () => clearInterval(t);
  }, [running]);
  const mm = String(Math.floor(s / 60)).padStart(2, '0');
  const ss = String(s % 60).padStart(2, '0');
  return (
    <div style={{
      display: 'flex', alignItems: 'center', gap: 8,
      padding: '6px 14px', borderRadius: 9,
      background: 'var(--bg-elevated)', border: '1px solid var(--border-default)',
      fontSize: 14, fontWeight: 700, fontFamily: 'monospace', color: 'var(--text-primary)'
    }}>
      <Clock size={14} color="var(--brand-400)" style={{ animation: running ? 'pulse 2s infinite' : 'none' }} />
      {mm}:{ss}
    </div>
  );
};

// Chat Bubble
const ChatBubble = ({ msg, isAi, ts }) => (
  <div style={{
    display: 'flex', alignItems: 'flex-start', gap: 12,
    flexDirection: isAi ? 'row' : 'row-reverse',
    marginBottom: 20, animation: 'fadeIn 0.3s ease'
  }}>
    {/* Avatar */}
    <div style={{
      width: 34, height: 34, borderRadius: 10, flexShrink: 0,
      background: isAi ? 'rgba(99,102,241,0.12)' : 'rgba(139,92,246,0.12)',
      border: `1px solid ${isAi ? 'rgba(99,102,241,0.25)' : 'rgba(139,92,246,0.25)'}`,
      display: 'flex', alignItems: 'center', justifyContent: 'center'
    }}>
      {isAi ? <Cpu size={16} color="var(--brand-400)" /> : <User size={16} color="var(--violet-400)" />}
    </div>

    <div style={{ maxWidth: '72%', display: 'flex', flexDirection: 'column', gap: 4, alignItems: isAi ? 'flex-start' : 'flex-end' }}>
      <div style={{ fontSize: 11, color: 'var(--text-muted)', fontWeight: 600 }}>
        {isAi ? 'AI Interviewer' : 'You'}{ts ? ` · ${ts}` : ''}
      </div>
      <div style={{
        padding: '12px 16px', borderRadius: isAi ? '4px 16px 16px 16px' : '16px 4px 16px 16px',
        background: isAi ? 'var(--bg-elevated)' : 'linear-gradient(135deg, #4f46e5, #6d28d9)',
        border: isAi ? '1px solid var(--border-subtle)' : 'none',
        color: 'var(--text-primary)',
        fontSize: 14, lineHeight: 1.65, whiteSpace: 'pre-wrap',
        boxShadow: isAi ? 'none' : '0 4px 16px rgba(99,102,241,0.25)'
      }}>
        {msg}
      </div>
    </div>
  </div>
);

const InterviewSession = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const bottomRef = useRef(null);

  const [interview, setInterview] = useState(null);
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);
  const [input, setInput] = useState('');
  const [status, setStatus] = useState({ messageCount: 0, messageLimit: 20, limitReached: false, remaining: 20 });
  const [finishing, setFinishing] = useState(false);

  useEffect(() => { loadData(); }, [id]);
  useEffect(() => { bottomRef.current?.scrollIntoView({ behavior: 'smooth' }); }, [messages, sending]);

  const loadData = async () => {
    try {
      setLoading(true);
      const res = await interviewService.getInterviewById(id);
      if (res.success && res.data) {
        setInterview(res.data);
        // Initialise status from the interview response
        const mc = res.data.messageCount || 0;
        const lim = 20;
        setStatus({
          messageCount: mc,
          messageLimit: lim,
          limitReached: res.data.limitReached || false,
          remaining: Math.max(0, lim - mc)
        });
        if (res.data.messages?.length) {
          setMessages(res.data.messages);
          setLoading(false);
        } else {
          await triggerOpening();
        }
      } else {
        console.error('Failed to load interview:', res);
        setLoading(false);
      }
    } catch (e) {
      console.error('Error loading interview:', e);
      // Only navigate away on 404 (interview truly not found)
      if (e.response?.status === 404 || e.response?.status === 403) {
        navigate('/');
      } else {
        setLoading(false);
      }
    }
  };

  const triggerOpening = async () => {
    try {
      const r = await messageService.startInterview(id);
      if (r.success && r.data) setMessages([r.data]);
      fetchStatus();
    } finally { setLoading(false); }
  };

  const fetchStatus = async () => {
    try {
      const r = await interviewService.getMessageCount(id);
      if (r.success && r.data) setStatus({ messageCount: r.data.messageCount, messageLimit: r.data.messageLimit || 20, limitReached: r.data.limitReached, remaining: r.data.remaining });
    } catch {}
  };

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim() || sending || status.limitReached) return;
    const text = input;
    setInput('');
    setSending(true);
    const tempMsg = { role: 'USER', content: text, _temp: true };
    setMessages(p => [...p, tempMsg]);
    try {
      const r = await messageService.sendMessage(id, text);
      if (r.success && r.data) {
        const { userMessage, aiMessage, messageCount, limitReached, remaining } = r.data;
        setMessages(p => {
          const filtered = p.filter(m => !m._temp);
          const additions = [userMessage, aiMessage].filter(Boolean);
          return [...filtered, ...additions];
        });
        setStatus({ messageCount, messageLimit: 20, limitReached, remaining });
      }
    } catch (err) {
      setMessages(p => p.filter(m => !m._temp));
      setInput(text);
      alert(err.response?.data?.message || 'Failed to send. Check your connection.');
    } finally { setSending(false); }
  };

  const handleFinish = async () => {
    if (!window.confirm('Submit your responses and generate your AI evaluation?')) return;
    try {
      setFinishing(true);
      const r = await messageService.finishInterview(id);
      if (r.success) navigate(`/results/${id}`);
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to finish. Try again.');
    } finally { setFinishing(false); }
  };

  const usedPercent = (status.messageCount / status.messageLimit) * 100;
  const progressColor = status.remaining <= 3 ? 'danger' : status.remaining <= 8 ? 'warning' : '';

  if (loading) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: 'calc(100vh - 65px)', gap: 16 }}>
        <div style={{ width: 48, height: 48, borderRadius: '50%', border: '3px solid var(--bg-elevated)', borderTopColor: 'var(--brand-500)', animation: 'spin 0.8s linear infinite' }} />
        <div style={{ color: 'var(--text-secondary)', fontSize: 14 }}>Warming up the AI interviewer…</div>
      </div>
    );
  }

  return (
    <div style={{ height: 'calc(100vh - 65px)', display: 'flex', overflow: 'hidden' }}>

      {/* Left Panel */}
      <div style={{
        width: 280, flexShrink: 0,
        background: 'var(--bg-surface)', borderRight: '1px solid var(--border-subtle)',
        display: 'flex', flexDirection: 'column', padding: 20, gap: 16,
        overflowY: 'auto'
      }}>
        {/* Back */}
        <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: 8, color: 'var(--text-muted)', textDecoration: 'none', fontSize: 13, fontWeight: 500, transition: 'color 0.2s' }}
          onMouseEnter={e => e.currentTarget.style.color = 'var(--text-primary)'}
          onMouseLeave={e => e.currentTarget.style.color = 'var(--text-muted)'}
        >
          <ArrowLeft size={14} /> Dashboard
        </Link>

        {/* Role Card */}
        <div className="card" style={{ padding: 16 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 10 }}>
            <Briefcase size={14} color="var(--brand-400)" />
            <span style={{ fontSize: 11, fontWeight: 700, color: 'var(--brand-400)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Active Role</span>
          </div>
          <div style={{ fontSize: 16, fontWeight: 700, color: 'var(--text-primary)', marginBottom: 14 }}>{interview?.jobRole}</div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 10 }}>
            <span style={{ fontSize: 12, color: 'var(--text-muted)' }}>Elapsed time</span>
            <Timer running={!status.limitReached} />
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontSize: 12, color: 'var(--text-muted)' }}>Progress</span>
            <span style={{ fontSize: 12, fontWeight: 700, color: 'var(--text-primary)', fontFamily: 'monospace' }}>
              {status.messageCount}/{status.messageLimit}
            </span>
          </div>
          {/* Progress bar */}
          <div style={{ marginTop: 8 }} className="progress-track">
            <div className={`progress-fill ${progressColor}`} style={{ width: `${usedPercent}%` }} />
          </div>
        </div>

        {/* Limit Alert */}
        {status.limitReached ? (
          <div style={{ padding: 14, borderRadius: 12, background: 'rgba(248,113,113,0.08)', border: '1px solid rgba(248,113,113,0.22)' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, color: 'var(--red-400)', fontSize: 13, fontWeight: 600, marginBottom: 10 }}>
              <AlertTriangle size={14} /> Limit Reached
            </div>
            <p style={{ fontSize: 12, color: 'var(--text-secondary)', margin: '0 0 12px', lineHeight: 1.6 }}>
              Upgrade to Premium for unlimited sessions.
            </p>
            <Link to="/payment">
              <button className="btn btn-primary" style={{ width: '100%', fontSize: 13, justifyContent: 'center' }}>
                <Crown size={13} /> Upgrade Now
              </button>
            </Link>
          </div>
        ) : status.remaining <= 5 ? (
          <div style={{ padding: 14, borderRadius: 12, background: 'rgba(251,191,36,0.07)', border: '1px solid rgba(251,191,36,0.20)' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, color: 'var(--yellow-400)', fontSize: 13, fontWeight: 600, marginBottom: 4 }}>
              <AlertTriangle size={14} /> {status.remaining} turns left
            </div>
            <p style={{ fontSize: 12, color: 'var(--text-secondary)', margin: 0 }}>
              Make your remaining answers count!
            </p>
          </div>
        ) : null}

        {/* Finish Button */}
        <button className="btn btn-outline" onClick={handleFinish} disabled={finishing}
          style={{ justifyContent: 'center', borderColor: 'rgba(52,211,153,0.35)', color: 'var(--emerald-400)', marginTop: 'auto' }}
          onMouseEnter={e => e.currentTarget.style.background = 'rgba(52,211,153,0.08)'}
          onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
        >
          <CheckCircle size={15} />
          {finishing ? 'Analyzing...' : 'Finish & Evaluate'}
        </button>
      </div>

      {/* Chat Area */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>

        {/* Chat Header */}
        <div style={{
          padding: '14px 24px', borderBottom: '1px solid var(--border-subtle)',
          display: 'flex', alignItems: 'center', gap: 12,
          background: 'var(--bg-base)'
        }}>
          <div style={{ width: 36, height: 36, borderRadius: 10, background: 'rgba(99,102,241,0.12)', border: '1px solid rgba(99,102,241,0.25)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Sparkles size={17} color="var(--brand-400)" />
          </div>
          <div>
            <div style={{ fontSize: 14, fontWeight: 700, color: 'var(--text-primary)' }}>AI Interviewer</div>
            <div style={{ fontSize: 12, color: 'var(--emerald-400)', fontWeight: 500 }}>● Active Session</div>
          </div>
        </div>

        {/* Messages */}
        <div style={{ flex: 1, overflowY: 'auto', padding: '24px 28px' }}>
          {messages.map((m, i) => (
            <ChatBubble key={i}
              msg={m.content}
              isAi={m.sender === 'AI' || m.sender === 'SYSTEM'}
              ts={m.timestamp ? m.timestamp.substring(11, 16) : null}
            />
          ))}
          {sending && (
            <div style={{ display: 'flex', alignItems: 'flex-start', gap: 12, marginBottom: 20 }}>
              <div style={{ width: 34, height: 34, borderRadius: 10, background: 'rgba(99,102,241,0.12)', border: '1px solid rgba(99,102,241,0.25)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Cpu size={16} color="var(--brand-400)" style={{ animation: 'spin 1s linear infinite' }} />
              </div>
              <div style={{ padding: '12px 16px', borderRadius: '4px 16px 16px 16px', background: 'var(--bg-elevated)', border: '1px solid var(--border-subtle)', display: 'flex', alignItems: 'center', gap: 6 }}>
                <span className="dot-bounce" /><span className="dot-bounce" /><span className="dot-bounce" />
              </div>
            </div>
          )}
          <div ref={bottomRef} />
        </div>

        {/* Input */}
        <div style={{ padding: '16px 24px', borderTop: '1px solid var(--border-subtle)', background: 'var(--bg-base)' }}>
          <form onSubmit={handleSend} style={{ display: 'flex', gap: 12 }}>
            <input
              type="text"
              value={input}
              onChange={e => setInput(e.target.value)}
              placeholder={status.limitReached ? 'Session limit reached. Click Finish & Evaluate.' : 'Type your response here…'}
              disabled={sending || status.limitReached}
              className="form-input"
              style={{ flex: 1, borderRadius: 12, padding: '13px 18px' }}
            />
            <button type="submit" className="btn btn-primary"
              disabled={!input.trim() || sending || status.limitReached}
              style={{ padding: '0 20px', borderRadius: 12, flexShrink: 0 }}
            >
              <Send size={17} />
            </button>
          </form>
          <div style={{ fontSize: 11, color: 'var(--text-muted)', marginTop: 8, textAlign: 'center' }}>
            {status.remaining} exchanges remaining · Powered by Gemma3 AI
          </div>
        </div>
      </div>
    </div>
  );
};

export default InterviewSession;
