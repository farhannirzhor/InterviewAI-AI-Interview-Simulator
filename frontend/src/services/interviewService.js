import api from './api';

export const interviewService = {
  createInterview: async (jobRole, jobDescription) => {
    const response = await api.post('/api/interviews', { jobRole, jobDescription });
    return response.data;
  },

  getMyInterviews: async () => {
    const response = await api.get('/api/interviews/my');
    return response.data;
  },

  getInterviewById: async (id) => {
    const response = await api.get(`/api/interviews/${id}`);
    return response.data;
  },

  getMessageCount: async (id) => {
    const response = await api.get(`/api/interviews/${id}/message-count`);
    return response.data;
  },

  deleteInterview: async (id) => {
    const response = await api.delete(`/api/interviews/${id}`);
    return response.data;
  },

  finishInterview: async (id) => {
    const response = await api.post(`/api/interviews/${id}/finish`);
    return response.data;
  },
};

export default interviewService;
