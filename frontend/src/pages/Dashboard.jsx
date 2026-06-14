import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import interviewService from '../services/interviewService';
import {
  Sparkles, Play, Briefcase, Calendar, MessageSquare,
  Trash2, ChevronRight, TrendingUp, Award, Clock,
  PlusCircle, AlertCircle, FileText, BarChart3, Zap
} from 'lucide-react';

const PRESETS = [
  { role: 'Software Engineer', desc: 'We are seeking a Software Engineer proficient in Java, Spring Boot, and database systems. You will build scalable microservices, design RESTful APIs, and write robust unit tests. Must have strong CS fundamentals and experience with cloud platforms (AWS/GCP).' },
  { role: 'Frontend Developer', desc: 'Looking for a React Frontend Developer with experience in modern JavaScript, HTML5, CSS3, Tailwind CSS, and state management tools like Redux or Zustand. You will create responsive, pixel-perfect user interfaces with great attention to performance.' },
  { role: 'Product Manager', desc: 'Seeking a Product Manager to define roadmap requirements, collaborate with design and engineering teams, and drive the product lifecycle. Experience writing PRDs, conducting user research, and working with agile teams is required.' },
  { role: 'Data Scientist', desc: 'We need a Data Scientist proficient in Python, ML libraries (scikit-learn, PyTorch), and SQL. You will build predictive models, analyze large datasets, and present actionable insights to business stakeholders.' },
];

const StatusBadge = ({ status }) => {
  const cfg = {
    ACTIVE: { bg: 'rgba(99,102,241,0.12)', color: 'var(--brand-400)', border: 'rgba(99,102,241,0.25)', label: 'Active' },
    COMPLETED: { bg: 'rgba(52,211,153,0.10)', color: 'var(--emerald-400)', border: 'rgba(52,211,153,0.25)', label: 'Completed' },
  }[status] || { bg: 'var(--bg-elevated)', color: 'var(--text-muted)', border: 'var(--border-default)', label: status };
  return (
    <span style={{
      fontSize: 11, fontWeight: 700, letterSpacing: '0.05em',
      padding: '3px 9px', borderRadius: 6,
      background: cfg.bg, color: cfg.color,
      border: `1px solid ${cfg.border}`,
    }}>
      {cfg.label}
    </span>
  );
};

const StatCard = ({ icon: Icon, label, value, color = 'var(--brand-400)', bg = 'rgba(99,102,241,0.10)' }) => (
  <div className="card" style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
    <div style={{ width: 48, height: 48, borderRadius: 14, background: bg, display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
      <Icon size={22} color={color} />
    </div>
    <div>
      <div style={{ fontSize: 12, color: 'var(--text-muted)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.07em', marginBottom: 4 }}>{label}</div>
      <div style={{ fontSize: 26, fontWeight: 800, color: 'var(--text-primary)', letterSpacing: '-0.02em', lineHeight: 1 }}>{value}</div>
    </div>
  </div>
);

const Dashboard = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [jobRole, setJobRole] = useState('');
  const [jobDescription, setJobDescription] = useState('');
  const [formError, setFormError] = useState('');
  const [creating, setCreating] = useState(false);

  useEffect(() => { fetchInterviews(); }, []);

  const fetchInterviews = async () => {
    try {
      const res = await interviewService.getMyInterviews();
      if (res.success && res.data) setInterviews(res.data);
    } catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  const handleStartInterview = async (e) => {
    e.preventDefault();
    if (!jobRole.trim() || !jobDescription.trim()) { setFormError('Both fields are required.'); return; }
    if (jobDescription.trim().length < 10) { setFormError('Description must be at least 10 characters.'); return; }
    try {
      setCreating(true); setFormError('');
      const res = await interviewService.createInterview(jobRole, jobDescription);
      if (res.success && res.data) navigate(`/interview/${res.data.id}`);
      else setFormError(res.message || 'Failed to start session');
    } catch (err) {
      setFormError(err.response?.data?.message || 'Failed to start interview. Try again.');
    } finally { setCreating(false); }
  };

  const handleDelete = async (id, e) => {
    e.stopPropagation();
    if (!window.confirm('Delete this interview session permanently?')) return;
    try {
      const res = await interviewService.deleteInterview(id);
      if (res.success) setInterviews(p => p.filter(i => i.id !== id));
    } catch (err) { console.error(err); }
  };

  const totalInterviews = interviews.length;
  const completed = interviews.filter(i => i.status === 'COMPLETED');
  const avgScore = completed.length
    ? (completed.reduce((a, c) => a + (c.readinessScore || 0), 0) / completed.length).toFixed(1)
    : '—';

  return (
    <div style={{ padding: '32px 28px', maxWidth: 1200, margin: '0 auto' }}>

      {/* Welcome Banner */}
      <div style={{
        background: 'linear-gradient(135deg, #141935 0%, #1c2345 50%, #141935 100%)',
        border: '1px solid var(--border-subtle)',
        borderRadius: 20, padding: '32px 36px', marginBottom: 28,
        position: 'relative', overflow: 'hidden'
      }}>
        {/* Decorative element */}
        <div style={{
          position: 'absolute', right: -40, top: -60, width: 300, height: 300, borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(99,102,241,0.12) 0%, transparent 70%)',
          pointerEvents: 'none'
        }} />
        <div style={{ position: 'relative' }}>
          <div style={{
            display: 'inline-flex', alignItems: 'center', gap: 7,
            fontSize: 12, fontWeight: 700, letterSpacing: '0.07em',
            color: 'var(--brand-400)', textTransform: 'uppercase',
            padding: '5px 12px', borderRadius: 8,
            background: 'rgba(99,102,241,0.10)',
            border: '1px solid rgba(99,102,241,0.20)',
            marginBottom: 16
          }}>
            <Sparkles size={13} />
            AI-Powered Mock Interviews
          </div>
          <h1 style={{ fontSize: 30, fontWeight: 800, color: 'var(--text-primary)', margin: '0 0 10px', letterSpacing: '-0.03em' }}>
            Welcome back, {user?.name?.split(' ')[0] || 'there'} 👋
          </h1>
          <p style={{ fontSize: 15, color: 'var(--text-secondary)', margin: 0, maxWidth: 560, lineHeight: 1.6 }}>
            Configure a new mock session below, review past interviews, or check your AI evaluation scores.
          </p>
        </div>
      </div>

      {/* Stats Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 16, marginBottom: 28 }}>
        <StatCard icon={Briefcase} label="Total Sessions" value={totalInterviews} />
        <StatCard icon={Award} label="Avg. Readiness" value={`${avgScore}${avgScore !== '—' ? '%' : ''}`}
          color="var(--emerald-400)" bg="rgba(52,211,153,0.10)" />
        <StatCard icon={BarChart3} label="Completed" value={`${completed.length}/${totalInterviews}`}
          color="var(--violet-400)" bg="rgba(139,92,246,0.10)" />
      </div>

      {/* Main Content */}
      <div style={{ display: 'grid', gridTemplateColumns: '340px 1fr', gap: 20 }}>

        {/* Left: Configure Form */}
        <div>
          <div className="card" style={{ position: 'sticky', top: 80 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 20 }}>
              <div style={{ width: 34, height: 34, borderRadius: 10, background: 'rgba(99,102,241,0.12)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <PlusCircle size={17} color="var(--brand-400)" />
              </div>
              <span style={{ fontSize: 15, fontWeight: 700, color: 'var(--text-primary)' }}>New Session</span>
            </div>

            {formError && (
              <div style={{
                display: 'flex', alignItems: 'center', gap: 8,
                padding: '10px 14px', borderRadius: 9, marginBottom: 14,
                background: 'rgba(248,113,113,0.08)',
                border: '1px solid rgba(248,113,113,0.20)',
                color: 'var(--red-400)', fontSize: 13
              }}>
                <AlertCircle size={14} /> {formError}
              </div>
            )}

            <form onSubmit={handleStartInterview} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
              <div>
                <label className="form-label">Target Job Role *</label>
                <input
                  type="text" placeholder="e.g. Senior Software Engineer"
                  value={jobRole} onChange={e => { setJobRole(e.target.value); setFormError(''); }}
                  className="form-input"
                />
              </div>

              <div>
                <label className="form-label">Job Description *</label>
                <textarea
                  placeholder="Paste the job requirements here..."
                  rows={5} value={jobDescription}
                  onChange={e => { setJobDescription(e.target.value); setFormError(''); }}
                  className="form-input"
                  style={{ resize: 'vertical', lineHeight: 1.6 }}
                />
              </div>

              {/* Presets */}
              <div>
                <div style={{ fontSize: 11, fontWeight: 700, color: 'var(--text-muted)', letterSpacing: '0.08em', textTransform: 'uppercase', marginBottom: 8 }}>
                  Quick Presets
                </div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
                  {PRESETS.map(p => (
                    <button key={p.role} type="button"
                      onClick={() => { setJobRole(p.role); setJobDescription(p.desc); setFormError(''); }}
                      style={{
                        padding: '5px 11px', borderRadius: 7, fontSize: 12, fontWeight: 500,
                        background: 'var(--bg-elevated)', color: 'var(--text-secondary)',
                        border: '1px solid var(--border-default)', cursor: 'pointer',
                        transition: 'all 0.2s'
                      }}
                      onMouseEnter={e => { e.target.style.borderColor = 'var(--border-accent)'; e.target.style.color = 'var(--text-primary)'; }}
                      onMouseLeave={e => { e.target.style.borderColor = 'var(--border-default)'; e.target.style.color = 'var(--text-secondary)'; }}
                    >
                      {p.role}
                    </button>
                  ))}
                </div>
              </div>

              <button type="submit" className="btn btn-primary" disabled={creating}
                style={{ width: '100%', justifyContent: 'center', marginTop: 4 }}>
                <Play size={15} />
                {creating ? 'Initializing AI...' : 'Start Interview'}
              </button>
            </form>
          </div>
        </div>

        {/* Right: Sessions List */}
        <div>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 14 }}>
            <span style={{ fontSize: 15, fontWeight: 700, color: 'var(--text-primary)' }}>
              Your Sessions
            </span>
            <span style={{ fontSize: 13, color: 'var(--text-muted)' }}>{interviews.length} total</span>
          </div>

          {loading ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
              {[1, 2, 3].map(i => (
                <div key={i} className="card" style={{ height: 80 }}>
                  <div className="animate-pulse" style={{ background: 'var(--bg-elevated)', borderRadius: 8, height: '100%' }} />
                </div>
              ))}
            </div>
          ) : interviews.length === 0 ? (
            <div className="card" style={{ textAlign: 'center', padding: '52px 24px' }}>
              <div style={{ width: 64, height: 64, borderRadius: 18, margin: '0 auto 16px', background: 'var(--bg-elevated)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <FileText size={28} color="var(--text-muted)" />
              </div>
              <div style={{ fontSize: 16, fontWeight: 700, color: 'var(--text-primary)', marginBottom: 8 }}>No sessions yet</div>
              <div style={{ fontSize: 14, color: 'var(--text-secondary)', maxWidth: 300, margin: '0 auto' }}>
                Use the form on the left to configure and start your first mock interview.
              </div>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
              {interviews.map((iv) => {
                const isActive = iv.status === 'ACTIVE';
                return (
                  <div key={iv.id} className="card card-hover"
                    onClick={() => navigate(isActive ? `/interview/${iv.id}` : `/results/${iv.id}`)}
                    style={{ display: 'flex', alignItems: 'center', gap: 16, padding: '18px 20px' }}
                  >
                    {/* Icon */}
                    <div style={{
                      width: 44, height: 44, borderRadius: 12, flexShrink: 0,
                      background: isActive ? 'rgba(99,102,241,0.10)' : 'rgba(52,211,153,0.08)',
                      display: 'flex', alignItems: 'center', justifyContent: 'center'
                    }}>
                      {isActive ? <Zap size={20} color="var(--brand-400)" /> : <Award size={20} color="var(--emerald-400)" />}
                    </div>

                    {/* Info */}
                    <div style={{ flex: 1, minWidth: 0 }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 5 }}>
                        <span style={{ fontSize: 15, fontWeight: 700, color: 'var(--text-primary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                          {iv.jobRole}
                        </span>
                        <StatusBadge status={iv.status} />
                      </div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 16, fontSize: 12, color: 'var(--text-muted)' }}>
                        <span style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
                          <Calendar size={12} />
                          {iv.createdAt ? iv.createdAt.replace('T', ' ').substring(0, 16) : 'N/A'}
                        </span>
                        <span style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
                          <MessageSquare size={12} />
                          {iv.messageCount || 0} messages
                        </span>
                      </div>
                    </div>

                    {/* Score & Actions */}
                    <div style={{ display: 'flex', alignItems: 'center', gap: 12, flexShrink: 0 }}>
                      {!isActive && iv.readinessScore != null && (
                        <div style={{ textAlign: 'right' }}>
                          <div style={{ fontSize: 11, color: 'var(--text-muted)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.05em' }}>Score</div>
                          <div style={{ fontSize: 22, fontWeight: 800, color: 'var(--emerald-400)', letterSpacing: '-0.02em', lineHeight: 1.1 }}>
                            {iv.readinessScore}%
                          </div>
                        </div>
                      )}
                      <button
                        onClick={e => handleDelete(iv.id, e)}
                        className="btn btn-danger btn-sm btn-icon"
                        title="Delete session"
                      >
                        <Trash2 size={14} />
                      </button>
                      <ChevronRight size={18} color="var(--text-muted)" />
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
