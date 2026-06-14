import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import paymentService from '../services/paymentService';
import { Crown, Check, CreditCard, AlertTriangle, History, ShieldCheck, X, Sparkles, Zap } from 'lucide-react';

const PLAN_FEATURES = {
  BASIC: { features: ['Up to 5 mock interviews', 'Standard AI responses', 'Basic score reports', 'Chat-based interface'], highlight: false },
  PRO:   { features: ['Up to 25 mock interviews', 'Advanced Gemma3 AI', 'Full skill breakdowns', 'STAR method evaluation', 'Priority support'], highlight: true },
  UNLIMITED: { features: ['Unlimited sessions', 'Custom job uploads', 'Full transcript history', 'Personalized prep guides', 'Instant evaluations'], highlight: false },
};

const PaymentPage = () => {
  const { user, refreshUserStatus } = useAuth();
  const [plans, setPlans] = useState([]);
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null); // null | 'checkout' | 'review' | 'success'
  const [selectedPlan, setSelectedPlan] = useState(null);
  const [card, setCard] = useState({ cardHolderName: '', cardNumber: '4111 1111 1111 1111', expiryDate: '12/28', cvv: '123' });
  const [cardErrors, setCardErrors] = useState({});
  const [initiated, setInitiated] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => { load(); }, []);

  const load = async () => {
    try {
      const [pRes, hRes] = await Promise.all([paymentService.getPlans(), paymentService.getMyPayments()]);
      if (pRes.success && pRes.data) setPlans(pRes.data);
      if (hRes.success && hRes.data) setPayments(hRes.data);
    } catch {} finally { setLoading(false); }
  };

  const openCheckout = (plan) => { setSelectedPlan(plan); setModal('checkout'); setInitiated(null); setCardErrors({}); };
  const closeModal = () => { setModal(null); setSelectedPlan(null); setInitiated(null); };

  const validateCard = () => {
    const e = {};
    if (!card.cardHolderName.trim()) e.cardHolderName = 'Required';
    if (!card.cardNumber.trim()) e.cardNumber = 'Required';
    if (!card.expiryDate.trim()) e.expiryDate = 'Required';
    if (!card.cvv.trim()) e.cvv = 'Required';
    setCardErrors(e);
    return !Object.keys(e).length;
  };

  const handleInitiate = async (e) => {
    e.preventDefault();
    if (!validateCard()) return;
    try {
      setSubmitting(true);
      const r = await paymentService.initiatePayment({ plan: selectedPlan.planName, ...card, amount: selectedPlan.price });
      if (r.success && r.data) { setInitiated(r.data); setModal('review'); }
    } catch (err) { alert(err.response?.data?.message || 'Failed to initiate payment.'); }
    finally { setSubmitting(false); }
  };

  const handleConfirm = async () => {
    try {
      setSubmitting(true);
      const r = await paymentService.confirmPayment(initiated.paymentId);
      if (r.success) {
        setModal('success');
        await refreshUserStatus();
        const h = await paymentService.getMyPayments();
        if (h.success && h.data) setPayments(h.data);
      }
    } catch (err) { alert(err.response?.data?.message || 'Confirmation failed.'); }
    finally { setSubmitting(false); }
  };

  const handleCancel = async () => {
    if (initiated) {
      try { await paymentService.cancelPayment(initiated.paymentId); } catch {}
    }
    closeModal();
  };

  if (loading) return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: 'calc(100vh - 65px)' }}>
      <div style={{ width: 44, height: 44, border: '3px solid var(--bg-elevated)', borderTopColor: 'var(--brand-500)', borderRadius: '50%', animation: 'spin 0.8s linear infinite' }} />
    </div>
  );

  return (
    <div style={{ maxWidth: 1000, margin: '0 auto', padding: '36px 24px' }}>

      {/* Header */}
      <div style={{ textAlign: 'center', marginBottom: 48 }}>
        {user?.isPremium ? (
          <div style={{
            display: 'inline-flex', alignItems: 'center', gap: 12, padding: '14px 28px',
            borderRadius: 16, background: 'rgba(251,191,36,0.08)', border: '1px solid rgba(251,191,36,0.25)',
            marginBottom: 20
          }}>
            <Crown size={24} color="#fbbf24" style={{ animation: 'bounce 2s infinite' }} />
            <div style={{ textAlign: 'left' }}>
              <div style={{ fontSize: 16, fontWeight: 800, color: 'var(--text-primary)' }}>Premium Active</div>
              <div style={{ fontSize: 13, color: '#fbbf24' }}>Unlimited interview access unlocked</div>
            </div>
          </div>
        ) : (
          <>
            <div style={{
              display: 'inline-flex', alignItems: 'center', gap: 7, fontSize: 12, fontWeight: 700,
              letterSpacing: '0.07em', color: 'var(--brand-400)', textTransform: 'uppercase',
              padding: '5px 14px', borderRadius: 8, background: 'rgba(99,102,241,0.10)',
              border: '1px solid rgba(99,102,241,0.20)', marginBottom: 16
            }}>
              <Crown size={13} /> Choose Your Plan
            </div>
            <h1 style={{ fontSize: 36, fontWeight: 900, color: 'var(--text-primary)', letterSpacing: '-0.03em', margin: '0 0 14px' }}>
              Unlock Full Access
            </h1>
            <p style={{ fontSize: 16, color: 'var(--text-secondary)', maxWidth: 500, margin: '0 auto', lineHeight: 1.7 }}>
              Free accounts are limited to 20 messages per session. Upgrade for unlimited practice and deeper AI insights.
            </p>
          </>
        )}
      </div>

      {/* Plans Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 20, marginBottom: 48 }}>
        {plans.map((plan) => {
          const cfg = PLAN_FEATURES[plan.planName] || { features: [], highlight: false };
          return (
            <div key={plan.planName} style={{
              background: cfg.highlight ? 'linear-gradient(145deg, #141935, #1c2345)' : 'var(--bg-surface)',
              border: cfg.highlight ? '2px solid rgba(99,102,241,0.40)' : '1px solid var(--border-subtle)',
              borderRadius: 20, padding: 28, display: 'flex', flexDirection: 'column',
              position: 'relative', overflow: 'hidden',
              boxShadow: cfg.highlight ? '0 0 40px rgba(99,102,241,0.12)' : 'none'
            }}>
              {cfg.highlight && (
                <div style={{
                  position: 'absolute', top: 0, left: '50%', transform: 'translate(-50%, -50%)',
                  background: 'linear-gradient(90deg, #4f46e5, #7c3aed)',
                  color: '#fff', fontSize: 11, fontWeight: 700, letterSpacing: '0.06em',
                  padding: '4px 16px', borderRadius: 999
                }}>
                  MOST POPULAR
                </div>
              )}

              <div style={{ marginBottom: 20 }}>
                <div style={{ fontSize: 13, fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.08em', marginBottom: 8 }}>
                  {plan.planName}
                </div>
                <div style={{ display: 'flex', alignItems: 'baseline', gap: 4 }}>
                  <span style={{ fontSize: 40, fontWeight: 900, color: 'var(--text-primary)', letterSpacing: '-0.03em', fontFamily: 'monospace' }}>
                    ${plan.price}
                  </span>
                  <span style={{ fontSize: 14, color: 'var(--text-muted)' }}>/mo</span>
                </div>
              </div>

              <div style={{ height: 1, background: 'var(--border-subtle)', margin: '0 0 20px' }} />

              <div style={{ flex: 1, marginBottom: 24 }}>
                {cfg.features.map((f, i) => (
                  <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 12 }}>
                    <div style={{
                      width: 20, height: 20, borderRadius: 6, flexShrink: 0,
                      background: cfg.highlight ? 'rgba(99,102,241,0.15)' : 'rgba(255,255,255,0.05)',
                      display: 'flex', alignItems: 'center', justifyContent: 'center'
                    }}>
                      <Check size={12} color={cfg.highlight ? 'var(--brand-400)' : 'var(--text-muted)'} />
                    </div>
                    <span style={{ fontSize: 13, color: 'var(--text-secondary)' }}>{f}</span>
                  </div>
                ))}
              </div>

              {user?.isPremium ? (
                <button className="btn btn-secondary" disabled style={{ justifyContent: 'center', width: '100%' }}>
                  {cfg.highlight ? 'Active Plan' : 'Available'}
                </button>
              ) : (
                <button
                  className={cfg.highlight ? 'btn btn-primary' : 'btn btn-outline'}
                  onClick={() => openCheckout(plan)}
                  style={{ justifyContent: 'center', width: '100%' }}
                >
                  {cfg.highlight && <Zap size={14} />}
                  Subscribe Now
                </button>
              )}
            </div>
          );
        })}
      </div>

      {/* Transaction History */}
      <div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 16 }}>
          <History size={17} color="var(--brand-400)" />
          <span style={{ fontSize: 16, fontWeight: 700, color: 'var(--text-primary)' }}>Billing History</span>
        </div>

        {payments.length === 0 ? (
          <div className="card" style={{ textAlign: 'center', padding: '36px 24px' }}>
            <CreditCard size={32} color="var(--text-muted)" style={{ margin: '0 auto 12px', display: 'block' }} />
            <p style={{ color: 'var(--text-muted)', fontSize: 14, margin: 0 }}>No transactions found.</p>
          </div>
        ) : (
          <div style={{ border: '1px solid var(--border-subtle)', borderRadius: 16, overflow: 'hidden' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ background: 'var(--bg-elevated)', borderBottom: '1px solid var(--border-subtle)' }}>
                  {['Transaction ID', 'Plan', 'Amount', 'Status', 'Date'].map(h => (
                    <th key={h} style={{ padding: '12px 16px', fontSize: 11, fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.07em', textAlign: 'left' }}>{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {payments.map((p, i) => (
                  <tr key={p.paymentId} style={{ borderBottom: i < payments.length - 1 ? '1px solid var(--border-subtle)' : 'none' }}>
                    <td style={{ padding: '13px 16px', fontSize: 12, fontFamily: 'monospace', color: 'var(--text-secondary)' }}>{p.transactionId || `PAY-${p.paymentId}`}</td>
                    <td style={{ padding: '13px 16px', fontSize: 13, fontWeight: 600, color: 'var(--text-primary)' }}>{p.plan}</td>
                    <td style={{ padding: '13px 16px', fontSize: 13, fontWeight: 700, fontFamily: 'monospace', color: 'var(--text-primary)' }}>${p.amount}</td>
                    <td style={{ padding: '13px 16px' }}>
                      <span style={{
                        fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 6,
                        background: ['SUCCESS', 'CONFIRMED'].includes(p.status) ? 'rgba(52,211,153,0.10)' : 'rgba(251,191,36,0.10)',
                        color: ['SUCCESS', 'CONFIRMED'].includes(p.status) ? 'var(--emerald-400)' : 'var(--yellow-400)',
                        border: `1px solid ${['SUCCESS', 'CONFIRMED'].includes(p.status) ? 'rgba(52,211,153,0.25)' : 'rgba(251,191,36,0.25)'}`
                      }}>
                        {p.status}
                      </span>
                    </td>
                    <td style={{ padding: '13px 16px', fontSize: 12, color: 'var(--text-muted)' }}>{p.createdAt ? p.createdAt.substring(0, 19) : 'N/A'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal Overlay */}
      {modal && (
        <div onClick={closeModal} style={{
          position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.75)',
          backdropFilter: 'blur(8px)', display: 'flex', alignItems: 'center', justifyContent: 'center',
          padding: 24, zIndex: 50, animation: 'fadeIn 0.2s ease'
        }}>
          <div onClick={e => e.stopPropagation()} style={{
            background: 'var(--bg-surface)', border: '1px solid var(--border-default)',
            borderRadius: 20, padding: 32, width: '100%', maxWidth: 420,
            position: 'relative', animation: 'slideUp 0.3s ease',
            boxShadow: '0 20px 60px rgba(0,0,0,0.5)'
          }}>
            <button onClick={closeModal} style={{
              position: 'absolute', top: 16, right: 16, background: 'none', border: 'none',
              cursor: 'pointer', color: 'var(--text-muted)', padding: 4, display: 'flex'
            }}>
              <X size={18} />
            </button>

            {/* Checkout Form */}
            {modal === 'checkout' && (
              <div>
                <div style={{ marginBottom: 20 }}>
                  <h3 style={{ fontSize: 20, fontWeight: 800, color: 'var(--text-primary)', margin: '0 0 6px' }}>
                    Subscribe to {selectedPlan?.planName}
                  </h3>
                  <p style={{ fontSize: 14, color: 'var(--text-secondary)', margin: 0 }}>
                    ${selectedPlan?.price}/month · Demo payment gateway
                  </p>
                </div>

                <div style={{
                  display: 'flex', alignItems: 'flex-start', gap: 10, padding: '12px 14px', borderRadius: 10, marginBottom: 20,
                  background: 'rgba(251,191,36,0.07)', border: '1px solid rgba(251,191,36,0.20)', fontSize: 12,
                  color: 'var(--yellow-400)', fontWeight: 500
                }}>
                  <AlertTriangle size={14} style={{ flexShrink: 0, marginTop: 1 }} />
                  Demo mode — use test card details. No real transactions processed.
                </div>

                <form onSubmit={handleInitiate} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
                  {[
                    { key: 'cardHolderName', label: 'Cardholder Name', placeholder: 'John Doe', half: false },
                    { key: 'cardNumber', label: 'Card Number', placeholder: '4111 1111 1111 1111', half: false },
                  ].map(({ key, label, placeholder, half }) => (
                    <div key={key}>
                      <label className="form-label">{label}</label>
                      <input name={key} value={card[key]} onChange={e => setCard(p => ({ ...p, [e.target.name]: e.target.value }))}
                        placeholder={placeholder} className="form-input"
                        style={cardErrors[key] ? { borderColor: 'rgba(248,113,113,0.6)' } : {}}
                      />
                      {cardErrors[key] && <div style={{ fontSize: 12, color: 'var(--red-400)', marginTop: 4 }}>{cardErrors[key]}</div>}
                    </div>
                  ))}
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                    {[
                      { key: 'expiryDate', label: 'Expiry (MM/YY)', placeholder: '12/28' },
                      { key: 'cvv', label: 'CVV', placeholder: '123' }
                    ].map(({ key, label, placeholder }) => (
                      <div key={key}>
                        <label className="form-label">{label}</label>
                        <input name={key} value={card[key]} onChange={e => setCard(p => ({ ...p, [e.target.name]: e.target.value }))}
                          placeholder={placeholder} className="form-input"
                          style={cardErrors[key] ? { borderColor: 'rgba(248,113,113,0.6)' } : {}}
                        />
                        {cardErrors[key] && <div style={{ fontSize: 12, color: 'var(--red-400)', marginTop: 4 }}>{cardErrors[key]}</div>}
                      </div>
                    ))}
                  </div>
                  <button type="submit" className="btn btn-primary btn-lg" disabled={submitting} style={{ width: '100%', justifyContent: 'center', marginTop: 4 }}>
                    <ShieldCheck size={16} /> {submitting ? 'Processing…' : 'Continue to Review'}
                  </button>
                </form>
              </div>
            )}

            {/* Review */}
            {modal === 'review' && initiated && (
              <div style={{ textAlign: 'center' }}>
                <div style={{ width: 56, height: 56, borderRadius: 16, margin: '0 auto 20px', background: 'rgba(99,102,241,0.12)', border: '1px solid rgba(99,102,241,0.25)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <ShieldCheck size={26} color="var(--brand-400)" style={{ animation: 'pulse 2s infinite' }} />
                </div>
                <h3 style={{ fontSize: 20, fontWeight: 800, color: 'var(--text-primary)', margin: '0 0 8px' }}>Review Transaction</h3>
                <p style={{ fontSize: 14, color: 'var(--text-secondary)', margin: '0 0 24px' }}>Please confirm this demo payment</p>

                <div style={{ background: 'var(--bg-elevated)', border: '1px solid var(--border-default)', borderRadius: 12, padding: 16, marginBottom: 20, textAlign: 'left' }}>
                  {[
                    ['Transaction ID', initiated.transactionId],
                    ['Plan', selectedPlan?.planName],
                    ['Amount', `$${initiated.amount}`],
                    ['Status', initiated.status],
                  ].map(([k, v]) => (
                    <div key={k} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--border-subtle)', fontSize: 13 }}>
                      <span style={{ color: 'var(--text-muted)' }}>{k}</span>
                      <span style={{ color: 'var(--text-primary)', fontWeight: 600, fontFamily: k === 'Amount' || k === 'Transaction ID' ? 'monospace' : 'inherit' }}>{v}</span>
                    </div>
                  ))}
                </div>

                <div style={{ display: 'flex', gap: 10 }}>
                  <button className="btn btn-secondary" style={{ flex: 1, justifyContent: 'center' }} onClick={handleCancel} disabled={submitting}>Cancel</button>
                  <button className="btn btn-primary" style={{ flex: 1, justifyContent: 'center', background: 'linear-gradient(135deg,#059669,#10b981)' }} onClick={handleConfirm} disabled={submitting}>
                    {submitting ? 'Confirming…' : 'Confirm Payment'}
                  </button>
                </div>
              </div>
            )}

            {/* Success */}
            {modal === 'success' && (
              <div style={{ textAlign: 'center', padding: '8px 0' }}>
                <div style={{ width: 72, height: 72, borderRadius: 20, margin: '0 auto 24px', background: 'rgba(251,191,36,0.12)', border: '1px solid rgba(251,191,36,0.30)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Crown size={34} color="#fbbf24" style={{ animation: 'bounce 1.5s infinite' }} />
                </div>
                <h3 style={{ fontSize: 24, fontWeight: 900, color: 'var(--text-primary)', margin: '0 0 10px', letterSpacing: '-0.02em' }}>You're Premium!</h3>
                <p style={{ fontSize: 15, color: 'var(--text-secondary)', margin: '0 0 28px', lineHeight: 1.7 }}>
                  Your account has been upgraded. Enjoy unlimited AI-powered mock interviews!
                </p>
                <button className="btn btn-primary btn-lg" style={{ width: '100%', justifyContent: 'center' }} onClick={closeModal}>
                  <Sparkles size={16} /> Start Practicing
                </button>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default PaymentPage;
