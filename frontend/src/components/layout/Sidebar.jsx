import React from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, CreditCard, Award, ShieldCheck, Zap } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const Sidebar = () => {
  const { user } = useAuth();

  const navItems = [
    { name: 'Dashboard', path: '/', icon: LayoutDashboard },
    { name: 'Billing & Plans', path: '/payment', icon: CreditCard },
  ];

  return (
    <aside className="sidebar">
      {/* Navigation */}
      <div style={{ marginBottom: 8 }}>
        <div style={{
          fontSize: 11, fontWeight: 700, color: 'var(--text-muted)',
          letterSpacing: '0.10em', textTransform: 'uppercase',
          padding: '0 14px', marginBottom: 8
        }}>
          Menu
        </div>
        {navItems.map((item) => (
          <NavLink
            key={item.name}
            to={item.path}
            end={item.path === '/'}
            className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
          >
            <item.icon size={16} />
            {item.name}
          </NavLink>
        ))}
      </div>

      {/* Pro Tip Box */}
      <div style={{
        margin: '16px 0',
        padding: 16,
        background: 'rgba(99,102,241,0.06)',
        border: '1px solid rgba(99,102,241,0.15)',
        borderRadius: 12
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
          <Zap size={14} color="var(--brand-400)" />
          <span style={{ fontSize: 11, fontWeight: 700, color: 'var(--brand-400)', letterSpacing: '0.08em', textTransform: 'uppercase' }}>
            Pro Tip
          </span>
        </div>
        <p style={{ fontSize: 12, color: 'var(--text-secondary)', lineHeight: 1.6, margin: 0 }}>
          Use the <strong style={{ color: 'var(--text-primary)' }}>STAR method</strong> — Situation, Task, Action, Result — for higher readiness scores.
        </p>
      </div>

      {/* Account Status */}
      <div style={{ marginTop: 'auto' }}>
        <div style={{
          padding: 14,
          background: 'var(--bg-elevated)',
          border: '1px solid var(--border-subtle)',
          borderRadius: 12,
          display: 'flex',
          alignItems: 'center',
          gap: 12
        }}>
          <div style={{
            width: 36, height: 36, borderRadius: 9,
            background: 'rgba(99,102,241,0.12)',
            display: 'flex', alignItems: 'center', justifyContent: 'center'
          }}>
            <ShieldCheck size={16} color="var(--brand-400)" />
          </div>
          <div>
            <div style={{ fontSize: 13, fontWeight: 600, color: 'var(--text-primary)' }}>
              {user?.isPremium ? 'Premium' : 'Free Plan'}
            </div>
            <div style={{ fontSize: 11, color: 'var(--text-muted)', marginTop: 1 }}>
              {user?.isPremium ? 'Unlimited access' : '20 msg / session'}
            </div>
          </div>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
