import api from './api';

export const authService = {
  login: async (email, password) => {
    const response = await api.post('/api/auth/login', { email, password });
    return response.data; // Wraps { success: true, message: "...", data: AuthResponse }
  },

  register: async (name, email, password) => {
    const response = await api.post('/api/auth/register', { name, email, password });
    return response.data; // Wraps { success: true, message: "...", data: AuthResponse }
  },
};

export default authService;
