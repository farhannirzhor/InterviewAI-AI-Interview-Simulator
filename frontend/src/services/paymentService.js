import api from './api';

export const paymentService = {
  getPlans: async () => {
    const response = await api.get('/api/payments/plans');
    return response.data;
  },

  getPremiumStatus: async () => {
    const response = await api.get('/api/payments/premium-status');
    return response.data;
  },

  initiatePayment: async (paymentDetails) => {
    // paymentDetails: { plan, cardHolderName, cardNumber, expiryDate, cvv, amount }
    const response = await api.post('/api/payments/initiate', paymentDetails);
    return response.data;
  },

  confirmPayment: async (paymentId) => {
    const response = await api.post(`/api/payments/confirm/${paymentId}`);
    return response.data;
  },

  cancelPayment: async (paymentId) => {
    const response = await api.post(`/api/payments/cancel/${paymentId}`);
    return response.data;
  },

  getPaymentStatus: async (paymentId) => {
    const response = await api.get(`/api/payments/status/${paymentId}`);
    return response.data;
  },

  getMyPayments: async () => {
    const response = await api.get('/api/payments/my');
    return response.data;
  },
};

export default paymentService;
