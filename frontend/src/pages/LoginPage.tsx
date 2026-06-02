import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { Sparkles } from 'lucide-react';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await login(email, password);
      navigate('/', { replace: true });
    } catch (err) {
      setError(err instanceof Error ? err.message : '登录失败');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-gradient-to-br from-orange-50 via-amber-50 to-yellow-50">
      {/* Decorative background shapes */}
      <div className="pointer-events-none absolute inset-0">
        <div className="absolute -left-40 -top-40 h-96 w-96 rounded-full bg-primary-200/30 blur-3xl" />
        <div className="absolute -bottom-32 -right-32 h-80 w-80 rounded-full bg-amber-200/40 blur-3xl" />
        <div className="absolute left-1/4 top-1/4 h-64 w-64 rounded-full bg-orange-100/50 blur-2xl" />
        {/* Grid pattern */}
        <svg className="absolute inset-0 h-full w-full opacity-[0.03]" xmlns="http://www.w3.org/2000/svg">
          <defs>
            <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
              <path d="M 40 0 L 0 0 0 40" fill="none" stroke="currentColor" strokeWidth="1" />
            </pattern>
          </defs>
          <rect width="100%" height="100%" fill="url(#grid)" />
        </svg>
        {/* Floating dots */}
        <svg className="absolute right-12 top-20 text-primary-300/40" width="120" height="120" viewBox="0 0 120 120">
          <circle cx="20" cy="20" r="4" fill="currentColor" />
          <circle cx="60" cy="20" r="4" fill="currentColor" />
          <circle cx="100" cy="20" r="4" fill="currentColor" />
          <circle cx="20" cy="60" r="4" fill="currentColor" />
          <circle cx="60" cy="60" r="4" fill="currentColor" />
          <circle cx="100" cy="60" r="4" fill="currentColor" />
          <circle cx="20" cy="100" r="4" fill="currentColor" />
          <circle cx="60" cy="100" r="4" fill="currentColor" />
          <circle cx="100" cy="100" r="4" fill="currentColor" />
        </svg>
        {/* Decorative lines */}
        <svg className="absolute bottom-16 left-16 text-amber-300/30" width="80" height="80" viewBox="0 0 80 80">
          <line x1="0" y1="0" x2="80" y2="80" stroke="currentColor" strokeWidth="2" />
          <line x1="0" y1="20" x2="60" y2="80" stroke="currentColor" strokeWidth="2" />
          <line x1="0" y1="40" x2="40" y2="80" stroke="currentColor" strokeWidth="2" />
        </svg>
      </div>

      {/* Login card */}
      <div className="relative z-10 w-full max-w-md px-4">
        {/* Logo */}
        <div className="mb-8 flex flex-col items-center">
          <div className="mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-primary-500 to-amber-500 shadow-lg shadow-primary-500/30">
            <Sparkles className="h-7 w-7 text-white" />
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-800">AI Interview</h1>
          <p className="mt-1 text-sm text-slate-500">智能面试助手</p>
        </div>

        {/* Form card */}
        <div className="rounded-2xl border border-white/60 bg-white/80 p-8 shadow-xl shadow-slate-200/50 backdrop-blur-sm">
          <h2 className="mb-6 text-center text-lg font-semibold text-slate-700">欢迎回来</h2>
          {error && (
            <div className="mb-4 rounded-lg border border-red-100 bg-red-50 px-4 py-2.5 text-sm text-red-600">
              {error}
            </div>
          )}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="mb-1.5 block text-sm font-medium text-slate-600">邮箱</label>
              <input
                type="email"
                value={email}
                onChange={e => setEmail(e.target.value)}
                required
                placeholder="your@email.com"
                className="w-full rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm transition-all placeholder:text-slate-400 focus:border-primary-400 focus:outline-none focus:ring-2 focus:ring-primary-500/20"
              />
            </div>
            <div>
              <label className="mb-1.5 block text-sm font-medium text-slate-600">密码</label>
              <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
                placeholder="输入密码"
                className="w-full rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm transition-all placeholder:text-slate-400 focus:border-primary-400 focus:outline-none focus:ring-2 focus:ring-primary-500/20"
              />
            </div>
            <button
              type="submit"
              disabled={submitting}
              className="w-full rounded-xl bg-gradient-to-r from-primary-600 to-amber-500 py-3 text-sm font-semibold text-white shadow-lg shadow-primary-500/25 transition-all hover:from-primary-700 hover:to-amber-600 hover:shadow-xl hover:shadow-primary-500/30 disabled:opacity-50"
            >
              {submitting ? (
                <span className="flex items-center justify-center gap-2">
                  <svg className="h-4 w-4 animate-spin" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                  </svg>
                  登录中...
                </span>
              ) : '登录'}
            </button>
          </form>
          <p className="mt-5 text-center text-sm text-slate-500">
            还没有账号？{' '}
            <Link to="/register" className="font-medium text-primary-600 transition-colors hover:text-primary-700">
              注册
            </Link>
          </p>
        </div>

        {/* Footer */}
        <p className="mt-6 text-center text-xs text-slate-400">
          Powered by AI Interview Platform
        </p>
      </div>
    </div>
  );
}
