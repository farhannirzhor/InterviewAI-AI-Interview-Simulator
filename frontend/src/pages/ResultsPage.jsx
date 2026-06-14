import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import messageService from '../services/messageService';
import { ArrowLeft, AlertTriangle, Lightbulb, Award, Activity, RotateCcw, Check, CheckCircle, Target } from 'lucide-react';

const getScoreColor = (score) => {
  if (score >= 85) return '#34d399';
  if (score >= 70) return '#6ee7b7';
  if (score >= 55) return '#fbbf24';
  if (score >= 40) return '#f97316';
  return '#f87171';
};

const getLabel = (score) => {
  if (score >= 85) return 'Highly Ready';
  if (score >= 70) return 'Ready';
  if (score >= 55) return 'Moderately Ready';
  if (score >= 40) return 'Needs Improvement';
  return 'Not Ready';
};

const SkillBar = ({ name, value, max, color }) => {
  const pct = Math.round((value / max) * 100);
  return (
    <div style={{ marginBottom: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline', marginBottom: 6 }}>
        <span style={{ fontSize: 13, fontWeight: 600, color: 'var(--text-primary)' }}>{name}</span>
        <span style={{ fontSize: 12, fontFamily: 'monospace', color: 'var(--text-secondary)' }}>{value}/{max}</span>
      </div>
      <div style={{ height: 8, background: 'var(--bg-muted)', borderRadius: 999, overflow: 'hidden' }}>
        <div style={{
          height: '100%', borderRadius: 999, width: `${pct}%`,
          background: color, transition: 'width 0.8s ease'
        }} />
      </div>
    </div>
  );
};

const ResultsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [evaluation, setEvaluation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [generating, setGenerating] = useState(false);

  useEffect(() => { fetchEvaluation(); }, [id]);

  const fetchEvaluation = async () => {
    try {
      setLoading(true);
      const r = await messageService.getSavedEvaluation(id);
      if (r.success && r.data) { setEvaluation(r.data); setLoading(false); }
      else generateEvaluation();
    } catch (e) {
      if (e.response?.status === 404 || e.response?.status === 500) generateEvaluation();
      else { setError('Failed to fetch evaluation.'); setLoading(false); }
    }
  };

  const generateEvaluation = async () => {
    try {
      setGenerating(true); setLoading(true); setError('');
      const r = await messageService.generateEvaluation(id);
      if (r.success && r.data) setEvaluation(r.data);
      else setError('Unable to parse the AI evaluation response.');
    } catch (e) {
      setError(e.response?.data?.message || 'AI evaluation failed. Make sure Ollama is running locally.');
    } finally { setGenerating(false); setLoading(false); }
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: 'calc(100vh - 65px)', gap: 20, padding: 24 }}>
        <div style={{ width: 56, height: 56, border: '4px solid var(--bg-elevated)', borderTopColor: 'var(--brand-500)', borderRadius: '50%', animation: 'spin 0.9s linear infinite' }} />
        <div style={{ textAlign: 'center', maxWidth: 400 }}>
          <div style={{ fontSize: 17, fontWeight: 700, color: 'var(--text-primary)', marginBottom: 8 }}>
            {generating ? 'Generating AI Evaluation…' : 'Loading Results…'}
          </div>
          <div style={{ fontSize: 14, color: 'var(--text-secondary)', lineHeight: 1.6 }}>
            {generating ? 'Gemma3 is analyzing your conversation, scoring your answers, and preparing detailed feedback. This may take up to a minute.' : 'Retrieving your saved evaluation…'}
          </div>
        </div>
        {generating && (
          <div style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 4 }}>
            Tip: Ensure Ollama is running with <code style={{ background: 'var(--bg-elevated)', padding: '2px 7px', borderRadius: 5, fontFamily: 'monospace' }}>ollama run gemma3</code>
          </div>
        )}
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: 'calc(100vh - 65px)', padding: 24, textAlign: 'center' }}>
        <div style={{ width: 64, height: 64, borderRadius: 20, background: 'rgba(248,113,113,0.10)', border: '1px solid rgba(248,113,113,0.25)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 20px' }}>
          <AlertTriangle size={30} color="var(--red-400)" />
        </div>
        <h3 style={{ fontSize: 22, fontWeight: 800, color: 'var(--text-primary)', margin: '0 0 10px' }}>Evaluation Error</h3>
        <p style={{ fontSize: 14, color: 'var(--text-secondary)', maxWidth: 420, margin: '0 auto 28px', lineHeight: 1.7 }}>{error}</p>
        <div style={{ display: 'flex', gap: 12 }}>
          <Link to="/"><button className="btn btn-secondary"><ArrowLeft size={15} /> Dashboard</button></Link>
          <button className="btn btn-primary" onClick={generateEvaluation}><RotateCcw size={15} /> Retry</button>
        </div>
      </div>
    );
  }

  const score = evaluation?.readinessScore || 0;
  const label = getLabel(score);
  const scoreColor = getScoreColor(score);
  const bd = evaluation?.scoreBreakdown || {};
  const circumference = 2 * Math.PI * 54;
  const dashOffset = circumference - (circumference * score) / 100;

  const metrics = [
    { name: 'Technical Knowledge', value: bd.technicalKnowledge || 0, max: 25, color: 'linear-gradient(90deg,#4f46e5,#818cf8)' },
    { name: 'Communication Skills', value: bd.communicationSkills || 0, max: 20, color: 'linear-gradient(90deg,#7c3aed,#a78bfa)' },
    { name: 'Problem Solving', value: bd.problemSolving || 0, max: 20, color: 'linear-gradient(90deg,#6d28d9,#c084fc)' },
    { name: 'Relevant Experience', value: bd.relevantExperience || 0, max: 20, color: 'linear-gradient(90deg,#059669,#34d399)' },
    { name: 'Cultural Fit & Attitude', value: bd.culturalFitAttitude || 0, max: 15, color: 'linear-gradient(90deg,#b45309,#fbbf24)' },
  ];

  return (
    <div style={{ maxWidth: 1100, margin: '0 auto', padding: '28px 24px' }}>

      {/* Nav */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: 8, color: 'var(--text-muted)', textDecoration: 'none', fontSize: 13, fontWeight: 500 }}
          onMouseEnter={e => e.currentTarget.style.color = 'var(--text-primary)'}
          onMouseLeave={e => e.currentTarget.style.color = 'var(--text-muted)'}
        >
          <ArrowLeft size={14} /> Back to Dashboard
        </Link>
        <span style={{ fontSize: 12, color: 'var(--text-muted)' }}>
          Evaluated: {evaluation.completedAt ? evaluation.completedAt.substring(0, 10) : 'Today'}
        </span>
      </div>

      {/* Top Row: Score + Summary */}
      <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: 20, marginBottom: 20 }}>

        {/* Score Donut */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '32px 24px', textAlign: 'center' }}>
          <div style={{ fontSize: 11, fontWeight: 700, color: 'var(--text-muted)', letterSpacing: '0.10em', textTransform: 'uppercase', marginBottom: 20 }}>
            Readiness Score
          </div>
          <div style={{ position: 'relative', width: 130, height: 130 }}>
            <svg width="130" height="130" viewBox="0 0 130 130" style={{ transform: 'rotate(-90deg)' }}>
              <circle cx="65" cy="65" r="54" fill="none" stroke="var(--bg-muted)" strokeWidth="10" />
              <circle cx="65" cy="65" r="54" fill="none" stroke={scoreColor} strokeWidth="10"
                strokeDasharray={circumference} strokeDashoffset={dashOffset}
                strokeLinecap="round" style={{ transition: 'stroke-dashoffset 1.2s ease' }}
              />
            </svg>
            <div style={{ position: 'absolute', inset: 0, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
              <span style={{ fontSize: 30, fontWeight: 900, color: scoreColor, fontFamily: 'monospace', letterSpacing: '-0.02em' }}>{score}</span>
              <span style={{ fontSize: 11, color: 'var(--text-muted)', fontWeight: 600 }}>/ 100</span>
            </div>
          </div>
          <div style={{
            marginTop: 20, padding: '6px 16px', borderRadius: 8,
            background: `${scoreColor}18`, border: `1px solid ${scoreColor}40`,
            fontSize: 12, fontWeight: 700, color: scoreColor, letterSpacing: '0.06em', textTransform: 'uppercase'
          }}>
            {label}
          </div>
          <div style={{ marginTop: 14, fontSize: 12, color: 'var(--text-muted)', lineHeight: 1.5 }}>
            Role: <span style={{ color: 'var(--text-primary)', fontWeight: 600 }}>{evaluation.jobRole}</span>
          </div>
        </div>

        {/* Summary */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 14 }}>
            <div style={{ width: 36, height: 36, borderRadius: 10, background: 'rgba(99,102,241,0.12)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Award size={18} color="var(--brand-400)" />
            </div>
            <div>
              <div style={{ fontSize: 15, fontWeight: 800, color: 'var(--text-primary)' }}>AI Assessment Summary</div>
              <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>{evaluation.totalMessages || 0} conversation turns analyzed</div>
            </div>
          </div>
          <div style={{ height: 1, background: 'var(--border-subtle)', margin: '0 0 14px' }} />
          <p style={{ fontSize: 14, color: 'var(--text-secondary)', lineHeight: 1.75, flex: 1, margin: 0, whiteSpace: 'pre-wrap' }}>
            {evaluation.summary || 'No summary available for this session.'}
          </p>
        </div>
      </div>

      {/* Bottom Row: Breakdown + Strengths/Areas */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20, marginBottom: 20 }}>

        {/* Skill Breakdown */}
        <div className="card">
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 20 }}>
            <Activity size={17} color="var(--brand-400)" />
            <span style={{ fontSize: 15, fontWeight: 700, color: 'var(--text-primary)' }}>Skill Breakdown</span>
          </div>
          {metrics.map(m => <SkillBar key={m.name} {...m} />)}
        </div>

        {/* Strengths & Areas */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="card" style={{ flex: 1 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 14 }}>
              <CheckCircle size={16} color="var(--emerald-400)" />
              <span style={{ fontSize: 14, fontWeight: 700, color: 'var(--emerald-400)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>Strengths</span>
            </div>
            {(evaluation.strengths || []).length ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                {evaluation.strengths.map((s, i) => (
                  <div key={i} style={{ display: 'flex', alignItems: 'flex-start', gap: 10, fontSize: 13, color: 'var(--text-secondary)', lineHeight: 1.6 }}>
                    <div style={{ width: 6, height: 6, borderRadius: '50%', background: 'var(--emerald-400)', marginTop: 6, flexShrink: 0 }} />
                    {s}
                  </div>
                ))}
              </div>
            ) : <p style={{ color: 'var(--text-muted)', fontSize: 13 }}>No strengths detailed.</p>}
          </div>

          <div className="card" style={{ flex: 1 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 14 }}>
              <Target size={16} color="var(--yellow-400)" />
              <span style={{ fontSize: 14, fontWeight: 700, color: 'var(--yellow-400)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>Areas for Growth</span>
            </div>
            {(evaluation.areasForImprovement || []).length ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                {evaluation.areasForImprovement.map((a, i) => (
                  <div key={i} style={{ display: 'flex', alignItems: 'flex-start', gap: 10, fontSize: 13, color: 'var(--text-secondary)', lineHeight: 1.6 }}>
                    <div style={{ width: 6, height: 6, borderRadius: '50%', background: 'var(--yellow-400)', marginTop: 6, flexShrink: 0 }} />
                    {a}
                  </div>
                ))}
              </div>
            ) : <p style={{ color: 'var(--text-muted)', fontSize: 13 }}>No areas specified.</p>}
          </div>
        </div>
      </div>

      {/* Full Feedback */}
      {evaluation.detailedFeedback && (
        <div className="card" style={{ marginBottom: 16 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 14 }}>
            <Check size={16} color="var(--brand-400)" />
            <span style={{ fontSize: 15, fontWeight: 700, color: 'var(--text-primary)' }}>Detailed Feedback</span>
          </div>
          <p style={{ fontSize: 14, color: 'var(--text-secondary)', lineHeight: 1.8, margin: 0, whiteSpace: 'pre-wrap' }}>
            {evaluation.detailedFeedback}
          </p>
        </div>
      )}

      {/* Recommendations */}
      {evaluation.recommendation && (
        <div className="card" style={{ background: 'linear-gradient(135deg, rgba(99,102,241,0.05) 0%, rgba(139,92,246,0.05) 100%)', border: '1px solid rgba(99,102,241,0.18)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 14 }}>
            <Lightbulb size={17} color="var(--yellow-400)" />
            <span style={{ fontSize: 15, fontWeight: 700, color: 'var(--text-primary)' }}>Recommendations</span>
          </div>
          <p style={{ fontSize: 14, color: 'var(--text-secondary)', lineHeight: 1.8, margin: 0, whiteSpace: 'pre-wrap' }}>
            {evaluation.recommendation}
          </p>
        </div>
      )}

    </div>
  );
};

export default ResultsPage;
