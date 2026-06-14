import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Mail, Lock, LogIn, AlertCircle, Sparkles, Eye, EyeOff } from 'lucide-react';

const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((p) => ({ ...p, [name]: value }));
    if (errors[name]) setErrors((p) => ({ ...p, [name]: '' }));
    setApiError('');
  };

  const validate = () => {
    const e = {};
    if (!formData.email) e.email = 'Email is required';
    else if (!/\S+@\S+\.\S+/.test(formData.email)) e.email = 'Enter a valid email';
    if (!formData.password) e.password = 'Password is required';
    setErrors(e);
    return !Object.keys(e).length;
  };

  const handleSubmit = async (ev) => {
    ev.preventDefault();
    if (!validate()) return;
    setSubmitting(true);
    setApiError('');
    const result = await login(formData.email, formData.password);
    if (result.success) navigate('/');
    else { setApiError(result.message); setSubmitting(false); }
  };

  return (
    <div style={{
      minHeight: '100vh',
      background: 'var(--bg-base)',
      display: 'flex',
    }}>
      {/* Left Panel — Branding */}
      <div style={{
        flex: 1,
        background: 'linear-gradient(145deg, #0d1130 0%, #141935 50%, #0d1130 100%)',
        borderRight: '1px solid var(--border-subtle)',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: 48,
        position: 'relative',
        overflow: 'hidden',
      }} className="sm-hide">

        {/* Decorative blobs */}
        <div style={{
          position: 'absolute', width: 360, height: 360, borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(99,102,241,0.12) 0%, transparent 70%)',
          top: '10%', left: '5%', pointerEvents: 'none'
        }} />
        <div style={{
          position: 'absolute', width: 300, height: 300, borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(139,92,246,0.10) 0%, transparent 70%)',
          bottom: '15%', right: '0%', pointerEvents: 'none'
        }} />

        <div style={{ position: 'relative', textAlign: 'center', maxWidth: 380 }}>
          <div style={{
            width: 70, height: 70, borderRadius: 20, margin: '0 auto 28px',
            background: 'linear-gradient(135deg, #4f46e5, #7c3aed)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            boxShadow: '0 8px 32px rgba(99,102,241,0.40)'
          }}>
            <Sparkles size={34} color="#fff" />
          </div>

          <h1 style={{ fontSize: 34, fontWeight: 800, color: 'var(--text-primary)', letterSpacing: '-0.03em', margin: '0 0 16px' }}>
            Land Your Dream Job
          </h1>
          <p style={{ fontSize: 16, color: 'var(--text-secondary)', lineHeight: 1.7, margin: '0 0 48px' }}>
            Practice with AI-powered mock interviews tailored to your specific role. Get real feedback, improve your answers, and build confidence.
          </p>

          {/* Feature list */}
          {['Realistic AI interviewer', 'Detailed score & breakdown', 'Strengths & growth areas', 'Unlimited mock sessions'].map((f) => (
            <div key={f} style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 14 }}>
              <div style={{
                width: 22, height: 22, borderRadius: 6, flexShrink: 0,
                background: 'rgba(99,102,241,0.15)',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                fontSize: 12, color: 'var(--brand-400)', fontWeight: 700
              }}>✓</div>
              <span style={{ fontSize: 14, color: 'var(--text-secondary)' }}>{f}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Right Panel — Form */}
      <div style={{
        width: 480, flexShrink: 0,
        display: 'flex', flexDirection: 'column',
        alignItems: 'center', justifyContent: 'center',
        padding: 48,
        background: 'var(--bg-base)'
      }}>
        <div style={{ width: '100%', maxWidth: 380 }}>
          <div style={{ marginBottom: 36 }}>
            <h2 style={{ fontSize: 28, fontWeight: 800, color: 'var(--text-primary)', margin: '0 0 8px', letterSpacing: '-0.02em' }}>
              Welcome back
            </h2>
            <p style={{ color: 'var(--text-secondary)', fontSize: 15, margin: 0 }}>
              Sign in to continue your interview prep
            </p>
          </div>

          {apiError && (
            <div style={{
              display: 'flex', alignItems: 'center', gap: 10,
              padding: '12px 16px', borderRadius: 10, marginBottom: 20,
              background: 'rgba(248,113,113,0.08)',
              border: '1px solid rgba(248,113,113,0.25)',
              color: '#f87171', fontSize: 13, fontWeight: 500
            }}>
              <AlertCircle size={15} />
              {apiError}
            </div>
          )}

          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            {/* Email */}
            <div>
              <label className="form-label">Email Address</label>
              <div style={{ position: 'relative' }}>
                <Mail size={16} style={{ position: 'absolute', left: 14, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                <input
                  name="email" type="email"
                  placeholder="you@company.com"
                  value={formData.email} onChange={handleChange}
                  className={`form-input has-icon ${errors.email ? 'error' : ''}`}
                  style={errors.email ? { borderColor: 'rgba(248,113,113,0.6)' } : {}}
                />
              </div>
              {errors.email && <div style={{ fontSize: 12, color: 'var(--red-400)', marginTop: 5 }}>{errors.email}</div>}
            </div>

            {/* Password */}
            <div>
              <label className="form-label">Password</label>
              <div style={{ position: 'relative' }}>
                <Lock size={16} style={{ position: 'absolute', left: 14, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                <input
                  name="password" type={showPassword ? 'text' : 'password'}
                  placeholder="••••••••"
                  value={formData.password} onChange={handleChange}
                  className="form-input has-icon"
                  style={{ paddingRight: 44, ...(errors.password ? { borderColor: 'rgba(248,113,113,0.6)' } : {}) }}
                />
                <button type="button" onClick={() => setShowPassword(p => !p)} style={{
                  position: 'absolute', right: 14, top: '50%', transform: 'translateY(-50%)',
                  background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)',
                  padding: 0, display: 'flex'
                }}>
                  {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
              {errors.password && <div style={{ fontSize: 12, color: 'var(--red-400)', marginTop: 5 }}>{errors.password}</div>}
            </div>

            <button type="submit" className="btn btn-primary btn-lg" disabled={submitting} style={{ marginTop: 8, width: '100%' }}>
              <LogIn size={17} />
              {submitting ? 'Signing in...' : 'Sign In'}
            </button>
          </form>

          <div style={{ textAlign: 'center', marginTop: 28, paddingTop: 24, borderTop: '1px solid var(--border-subtle)' }}>
            <span style={{ fontSize: 14, color: 'var(--text-secondary)' }}>
              Don't have an account?{' '}
              <Link to="/register" style={{ color: 'var(--brand-400)', fontWeight: 600, textDecoration: 'none' }}>
                Create one free
              </Link>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
