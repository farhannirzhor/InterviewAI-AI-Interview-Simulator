import React from 'react';
import { useAuth } from '../../context/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import { Sparkles, Crown, LogOut, CreditCard } from 'lucide-react';

const Header = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="header">
      {/* Logo */}
      <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', gap: 12 }}>
        <div style={{
          width: 36, height: 36, borderRadius: 10,
          background: 'linear-gradient(135deg, #4f46e5, #7c3aed)',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          boxShadow: '0 4px 14px rgba(99,102,241,0.4)'
        }}>
          <Sparkles size={18} color="#fff" />
        </div>
        <div>
          <span style={{ fontWeight: 800, fontSize: 17, color: '#f0f2ff', letterSpacing: '-0.02em' }}>
            InterviewAI
          </span>
          <span style={{ fontSize: 11, color: 'var(--text-muted)', display: 'block', lineHeight: 1, marginTop: 1 }}>
            AI-Powered Prep
          </span>
        </div>
      </Link>

      {/* Right Side */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
        {isAuthenticated && user ? (
          <>
            {/* Premium / Free Badge */}
            <Link to="/payment" style={{ textDecoration: 'none' }}>
              {user.isPremium ? (
                <div style={{
                  display: 'flex', alignItems: 'center', gap: 6,
                  padding: '5px 12px', borderRadius: 8,
                  background: 'rgba(251,191,36,0.10)',
                  border: '1px solid rgba(251,191,36,0.25)',
                  color: '#fbbf24', fontSize: 12, fontWeight: 700,
                  letterSpacing: '0.04em'
                }}>
                  <Crown size={13} />
                  PREMIUM
                </div>
              ) : (
                <div style={{
                  display: 'flex', alignItems: 'center', gap: 6,
                  padding: '5px 12px', borderRadius: 8,
                  background: 'var(--bg-elevated)',
                  border: '1px solid var(--border-default)',
                  color: 'var(--text-secondary)', fontSize: 12, fontWeight: 600,
                  transition: 'all 0.2s'
                }}>
                  <CreditCard size={13} />
                  Upgrade
                </div>
              )}
            </Link>

            {/* User Info */}
            <div style={{ textAlign: 'right' }} className="sm-hide">
              <div style={{ fontSize: 14, fontWeight: 600, color: 'var(--text-primary)', lineHeight: 1.2 }}>
                {user.name}
              </div>
              <div style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 2 }}>
                {user.email}
              </div>
            </div>

            {/* Avatar */}
            <div className="avatar">
              {user.name ? user.name.charAt(0).toUpperCase() : 'U'}
            </div>

            {/* Logout */}
            <button
              onClick={handleLogout}
              className="btn btn-secondary btn-sm"
              style={{ gap: 6 }}
            >
              <LogOut size={14} />
              <span className="sm-hide">Logout</span>
            </button>
          </>
        ) : (
          <div style={{ display: 'flex', gap: 8 }}>
            <Link to="/login" className="btn btn-ghost btn-sm">Sign In</Link>
            <Link to="/register" className="btn btn-primary btn-sm">Get Started</Link>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;
