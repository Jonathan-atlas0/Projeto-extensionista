// Camada de integração com o back-end Spring Boot
// Com o proxy do Vite, basta usar /api — sem precisar de CORS em dev.
// Em produção, configure a variável VITE_API_BASE na build.
const API_BASE = import.meta.env.VITE_API_BASE ?? '/api';

// ─── Tipos espelhados do back-end ────────────────────────────────────────────

export interface AuthResponse {
  token: string;
  nome: string;
  email: string;
  id: number;
}

export interface ReceitaResponse {
  id: number;
  descricao: string;
  valor: number;
  data: string;
  categoria: string;
  usuarioId: number;
}

export interface DespesaResponse {
  id: number;
  descricao: string;
  valor: number;
  data: string;
  categoria: string;
  usuarioId: number;
}

export interface DashboardResponse {
  totalReceitas: number;
  totalDespesas: number;
  saldo: number;
  despesasPorCategoria: { categoria: string; valor: number }[];
}

// ─── Helper central de fetch ─────────────────────────────────────────────────

function getToken(): string | null {
  return localStorage.getItem('token');
}

async function apiFetch<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = getToken();
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>),
  };
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });

  if (!res.ok) {
    let msg = `Erro ${res.status}`;
    try {
      const err = await res.json();
      msg = err.mensagem || JSON.stringify(err);
    } catch {}
    throw new Error(msg);
  }

  if (res.status === 204) return undefined as unknown as T;
  return res.json();
}

// ─── Auth ─────────────────────────────────────────────────────────────────────

export const authApi = {
  async registro(nome: string, email: string, senha: string): Promise<AuthResponse> {
    return apiFetch('/auth/registro', {
      method: 'POST',
      body: JSON.stringify({ nome, email, senha }),
    });
  },

  async login(email: string, senha: string): Promise<AuthResponse> {
    return apiFetch('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, senha }),
    });
  },
};

// ─── Receitas ────────────────────────────────────────────────────────────────

export const receitasApi = {
  async listar(): Promise<ReceitaResponse[]> {
    return apiFetch('/receitas');
  },

  async criar(data: { descricao: string; valor: number; data: string; categoria: string }): Promise<ReceitaResponse> {
    return apiFetch('/receitas', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async deletar(id: number): Promise<void> {
    return apiFetch(`/receitas/${id}`, { method: 'DELETE' });
  },
};

// ─── Despesas ────────────────────────────────────────────────────────────────

export const despesasApi = {
  async listar(): Promise<DespesaResponse[]> {
    return apiFetch('/despesas');
  },

  async criar(data: { descricao: string; valor: number; data: string; categoria: string }): Promise<DespesaResponse> {
    return apiFetch('/despesas', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async deletar(id: number): Promise<void> {
    return apiFetch(`/despesas/${id}`, { method: 'DELETE' });
  },
};

// ─── Dashboard ───────────────────────────────────────────────────────────────

export const dashboardApi = {
  async resumo(): Promise<DashboardResponse> {
    return apiFetch('/dashboard');
  },
};
