import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { User, Mail, Lock, AlertCircle, Sparkles, Eye, EyeOff, CheckCircle } from 'lucide-react';

const Register = () => {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ name: '', email: '', password: '', confirmPassword: '' });
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [showPwd, setShowPwd] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((p) => ({ ...p, [name]: value }));
    if (errors[name]) setErrors((p) => ({ ...p, [name]: '' }));
    setApiError('');
  };

  const validate = () => {
    const e = {};
    if (!formData.name) e.name = 'Full name is required';
    else if (formData.name.length < 2) e.name = 'Name must be at least 2 characters';
    if (!formData.email) e.email = 'Email is required';
    else if (!/\S+@\S+\.\S+/.test(formData.email)) e.email = 'Enter a valid email';
    if (!formData.password) e.password = 'Password is required';
    else if (formData.password.length < 6) e.password = 'Minimum 6 characters';
    if (formData.password !== formData.confirmPassword) e.confirmPassword = 'Passwords do not match';
    setErrors(e);
    return !Object.keys(e).length;
  };

  const handleSubmit = async (ev) => {
    ev.preventDefault();
    if (!validate()) return;
    setSubmitting(true);
    const result = await register(formData.name, formData.email, formData.password);
    if (result.success) navigate('/');
    else { setApiError(result.message); setSubmitting(false); }
  };

  const strength = formData.password.length >= 10 ? 3 : formData.password.length >= 6 ? 2 : formData.password.length > 0 ? 1 : 0;
  const strengthLabel = ['', 'Weak', 'Good', 'Strong'][strength];
  const strengthColor = ['', '#f87171', '#fbbf24', '#34d399'][strength];

  return (
    <div style={{
      minHeight: '100vh', background: 'var(--bg-base)',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      padding: 24, position: 'relative', overflow: 'hidden'
    }}>
      {/* Decorative blobs */}
      <div style={{
        position: 'absolute', width: 500, height: 500, borderRadius: '50%',
        background: 'radial-gradient(circle, rgba(99,102,241,0.08) 0%, transparent 70%)',
        top: '-100px', right: '-100px', pointerEvents: 'none'
      }} />
      <div style={{
        position: 'absolute', width: 400, height: 400, borderRadius: '50%',
        background: 'radial-gradient(circle, rgba(139,92,246,0.07) 0%, transparent 70%)',
        bottom: '-80px', left: '-80px', pointerEvents: 'none'
      }} />

      <div style={{ width: '100%', maxWidth: 460, position: 'relative' }}>
        {/* Card */}
        <div className="card card-glow" style={{ padding: 40 }}>
          {/* Header */}
          <div style={{ textAlign: 'center', marginBottom: 32 }}>
            <div style={{
              width: 56, height: 56, borderRadius: 16, margin: '0 auto 20px',
              background: 'linear-gradient(135deg, #4f46e5, #7c3aed)',
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              boxShadow: '0 6px 24px rgba(99,102,241,0.35)'
            }}>
              <Sparkles size={26} color="#fff" />
            </div>
            <h2 style={{ fontSize: 26, fontWeight: 800, color: 'var(--text-primary)', margin: '0 0 8px', letterSpacing: '-0.02em' }}>
              Create your account
            </h2>
            <p style={{ color: 'var(--text-secondary)', fontSize: 14, margin: 0 }}>
              Start your AI interview preparation journey
            </p>
          </div>

          {apiError && (
            <div style={{
              display: 'flex', alignItems: 'center', gap: 10,
              padding: '12px 16px', borderRadius: 10, marginBottom: 20,
              background: 'rgba(248,113,113,0.08)',
              border: '1px solid rgba(248,113,113,0.25)',
              color: '#f87171', fontSize: 13
            }}>
              <AlertCircle size={15} />
              {apiError}
            </div>
          )}

          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
            {/* Full Name */}
            <div>
              <label className="form-label">Full Name</label>
              <div style={{ position: 'relative' }}>
                <User size={16} style={{ position: 'absolute', left: 14, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                <input name="name" type="text" placeholder="John Doe"
                  value={formData.name} onChange={handleChange}
                  className="form-input has-icon"
                  style={errors.name ? { borderColor: 'rgba(248,113,113,0.6)' } : {}}
                />
              </div>
              {errors.name && <div style={{ fontSize: 12, color: 'var(--red-400)', marginTop: 5 }}>{errors.name}</div>}
            </div>

            {/* Email */}
            <div>
              <label className="form-label">Email Address</label>
              <div style={{ position: 'relative' }}>
                <Mail size={16} style={{ position: 'absolute', left: 14, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                <input name="email" type="email" placeholder="you@company.com"
                  value={formData.email} onChange={handleChange}
                  className="form-input has-icon"
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
                <input name="password" type={showPwd ? 'text' : 'password'} placeholder="Min. 6 characters"
                  value={formData.password} onChange={handleChange}
                  className="form-input has-icon" style={{ paddingRight: 44 }}
                />
                <button type="button" onClick={() => setShowPwd(p => !p)}
                  style={{ position: 'absolute', right: 14, top: '50%', transform: 'translateY(-50%)', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)', padding: 0, display: 'flex' }}>
                  {showPwd ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
              {formData.password.length > 0 && (
                <div style={{ marginTop: 6 }}>
                  <div style={{ display: 'flex', gap: 4, marginBottom: 4 }}>
                    {[1, 2, 3].map(i => (
                      <div key={i} style={{
                        flex: 1, height: 3, borderRadius: 2,
                        background: i <= strength ? strengthColor : 'var(--bg-muted)',
                        transition: 'background 0.3s'
                      }} />
                    ))}
                  </div>
                  <span style={{ fontSize: 11, color: strengthColor, fontWeight: 600 }}>{strengthLabel}</span>
                </div>
              )}
              {errors.password && <div style={{ fontSize: 12, color: 'var(--red-400)', marginTop: 4 }}>{errors.password}</div>}
            </div>

            {/* Confirm Password */}
            <div>
              <label className="form-label">Confirm Password</label>
              <div style={{ position: 'relative' }}>
                <Lock size={16} style={{ position: 'absolute', left: 14, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                <input name="confirmPassword" type={showConfirm ? 'text' : 'password'} placeholder="Re-enter password"
                  value={formData.confirmPassword} onChange={handleChange}
                  className="form-input has-icon" style={{ paddingRight: 44, ...(errors.confirmPassword ? { borderColor: 'rgba(248,113,113,0.6)' } : {}) }}
                />
                <button type="button" onClick={() => setShowConfirm(p => !p)}
                  style={{ position: 'absolute', right: 14, top: '50%', transform: 'translateY(-50%)', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)', padding: 0, display: 'flex' }}>
                  {showConfirm ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
              {formData.confirmPassword && formData.password === formData.confirmPassword && (
                <div style={{ fontSize: 12, color: 'var(--emerald-400)', marginTop: 5, display: 'flex', alignItems: 'center', gap: 4 }}>
                  <CheckCircle size={12} /> Passwords match
                </div>
              )}
              {errors.confirmPassword && <div style={{ fontSize: 12, color: 'var(--red-400)', marginTop: 5 }}>{errors.confirmPassword}</div>}
            </div>

            <button type="submit" className="btn btn-primary btn-lg" disabled={submitting} style={{ marginTop: 8, width: '100%' }}>
              {submitting ? 'Creating account...' : 'Create Free Account'}
            </button>
          </form>

          <div style={{ textAlign: 'center', marginTop: 24, paddingTop: 20, borderTop: '1px solid var(--border-subtle)' }}>
            <span style={{ fontSize: 14, color: 'var(--text-secondary)' }}>
              Already have an account?{' '}
              <Link to="/login" style={{ color: 'var(--brand-400)', fontWeight: 600, textDecoration: 'none' }}>
                Sign in
              </Link>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
