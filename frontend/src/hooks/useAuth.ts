import { useState, useEffect, useCallback } from 'react';
import { request } from '../api/request';

interface AuthUser {
  id: number;
  email: string;
  nickname: string;
}

export function useAuth() {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);

  const checkAuth = useCallback(async () => {
    try {
      const res = await request.get<AuthUser>('/api/auth/me');
      setUser(res);
    } catch {
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    checkAuth();
  }, [checkAuth]);

  const login = async (email: string, password: string) => {
    const res = await request.post<AuthUser>('/api/auth/login', { email, password });
    setUser(res);
    return res;
  };

  const register = async (email: string, password: string, nickname?: string) => {
    const res = await request.post<AuthUser>('/api/auth/register', { email, password, nickname });
    setUser(res);
    return res;
  };

  const logout = async () => {
    await request.post('/api/auth/logout');
    setUser(null);
  };

  return { user, loading, login, register, logout, checkAuth };
}
