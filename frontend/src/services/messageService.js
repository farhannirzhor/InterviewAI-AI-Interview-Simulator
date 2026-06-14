import api from './api';

export const messageService = {
  // Opening message
  startInterview: async (interviewId) => {
    const response = await api.post(`/api/messages/start/${interviewId}`);
    return response.data;
  },

  // Send message
  sendMessage: async (interviewId, content) => {
    const response = await api.post('/api/messages/send', { interviewId, content });
    return response.data;
  },

  // Complete interview
  finishInterview: async (interviewId) => {
    const response = await api.post(`/api/messages/finish/${interviewId}`);
    return response.data;
  },

  // Chat status
  getStatus: async (interviewId) => {
    const response = await api.get(`/api/messages/status/${interviewId}`);
    return response.data;
  },

  // Evaluation endpoints
  generateEvaluation: async (interviewId) => {
    const response = await api.post(`/api/evaluation/${interviewId}/generate`);
    return response.data;
  },

  getSavedEvaluation: async (interviewId) => {
    const response = await api.get(`/api/evaluation/${interviewId}`);
    return response.data;
  },

  getScoreSummary: async (interviewId) => {
    const response = await api.get(`/api/evaluation/${interviewId}/summary`);
    return response.data;
  },
};

export default messageService;
