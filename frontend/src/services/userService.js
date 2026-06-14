import api from './api';

export const userService = {
  getCurrentUser: async () => {
    const response = await api.get('/api/users/me');
    return response.data; // Wraps { success: true, message: "...", data: UserResponse }
  },
};

export default userService;
