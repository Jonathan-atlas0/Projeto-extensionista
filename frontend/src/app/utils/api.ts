// Camada de integração com o back-end Spring Boot.
// Com o proxy do Vite, basta usar /api em desenvolvimento.
const API_BASE = import.meta.env.VITE_API_BASE ?? '/api';

type TipoCategoria = 'RECEITA' | 'DESPESA';

export interface LoginResponse {
  token: string;
  tipo: string;
  refreshToken: string;
  nome: string;
  email: string;
  role: string;
}

export interface UsuarioResponse {
  id: number;
  nome: string;
  email: string;
  role: string;
  criadoEm: string;
}

export interface CategoriaResponse {
  id: number;
  nome: string;
  tipo: TipoCategoria;
  padrao: boolean;
}

interface ReceitaBackendResponse {
  id: number;
  descricao: string;
  valor: number | string;
  data: string;
  categoriaId: number;
  categoriaNome: string;
  usuarioId: number;
  criadoEm?: string;
  atualizadoEm?: string;
}

export interface ReceitaResponse {
  id: number;
  descricao: string;
  valor: number;
  data: string;
  categoriaId: number;
  categoria: string;
  usuarioId: number;
}

interface DespesaBackendResponse {
  id: number;
  descricao: string;
  valor: number | string;
  data: string;
  categoriaId: number;
  categoriaNome: string;
  usuarioId: number;
  criadoEm?: string;
  atualizadoEm?: string;
}

export interface DespesaResponse {
  id: number;
  descricao: string;
  valor: number;
  data: string;
  categoriaId: number;
  categoria: string;
  usuarioId: number;
}

interface DashboardBackendResponse {
  mes: number;
  ano: number;
  totalReceitas: number | string;
  totalDespesas: number | string;
  saldo: number | string;
  maiorCategoriaGasto: string | null;
  gastosPorCategoria: { categoria: string; total: number | string; percentual: number }[];
}

export interface DashboardResponse {
  mes: number;
  ano: number;
  totalReceitas: number;
  totalDespesas: number;
  saldo: number;
  maiorCategoriaGasto: string | null;
  despesasPorCategoria: { categoria: string; valor: number }[];
}

type ApiFetchOptions = RequestInit & {
  auth?: boolean;
};

function getToken(): string | null {
  return localStorage.getItem('token');
}

async function apiFetch<T>(path: string, options: ApiFetchOptions = {}): Promise<T> {
  const { auth = true, headers: customHeaders, ...fetchOptions } = options;
  const token = getToken();
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(customHeaders as Record<string, string>),
  };
  if (auth && token) headers['Authorization'] = `Bearer ${token}`;

  const res = await fetch(`${API_BASE}${path}`, { ...fetchOptions, headers });

  if (!res.ok) {
    let msg = `Erro ${res.status}`;
    try {
      const err = await res.json();
      if (err.campos) {
        msg = Object.values(err.campos).join(', ');
      } else {
        msg = err.mensagem || err.erro || JSON.stringify(err);
      }
    } catch {}
    throw new Error(msg);
  }

  if (res.status === 204) return undefined as unknown as T;
  return res.json();
}

function toNumber(value: number | string): number {
  return typeof value === 'number' ? value : Number(value);
}

function normalizeReceita(receita: ReceitaBackendResponse): ReceitaResponse {
  return {
    id: receita.id,
    descricao: receita.descricao,
    valor: toNumber(receita.valor),
    data: receita.data,
    categoriaId: receita.categoriaId,
    categoria: receita.categoriaNome,
    usuarioId: receita.usuarioId,
  };
}

function normalizeDespesa(despesa: DespesaBackendResponse): DespesaResponse {
  return {
    id: despesa.id,
    descricao: despesa.descricao,
    valor: toNumber(despesa.valor),
    data: despesa.data,
    categoriaId: despesa.categoriaId,
    categoria: despesa.categoriaNome,
    usuarioId: despesa.usuarioId,
  };
}

function normalizeDashboard(dashboard: DashboardBackendResponse): DashboardResponse {
  return {
    mes: dashboard.mes,
    ano: dashboard.ano,
    totalReceitas: toNumber(dashboard.totalReceitas),
    totalDespesas: toNumber(dashboard.totalDespesas),
    saldo: toNumber(dashboard.saldo),
    maiorCategoriaGasto: dashboard.maiorCategoriaGasto,
    despesasPorCategoria: (dashboard.gastosPorCategoria ?? []).map((gasto) => ({
      categoria: gasto.categoria,
      valor: toNumber(gasto.total),
    })),
  };
}

export const authApi = {
  async registro(nome: string, email: string, senha: string): Promise<UsuarioResponse> {
    return apiFetch('/auth/registro', {
      auth: false,
      method: 'POST',
      body: JSON.stringify({ nome, email, senha }),
    });
  },

  async login(email: string, senha: string): Promise<LoginResponse> {
    return apiFetch('/auth/login', {
      auth: false,
      method: 'POST',
      body: JSON.stringify({ email, senha }),
    });
  },
};

export const categoriasApi = {
  async listarPorTipo(tipo: TipoCategoria): Promise<CategoriaResponse[]> {
    return apiFetch(`/categorias/tipo?tipo=${tipo}`);
  },
};

export const usuariosApi = {
  async perfil(): Promise<UsuarioResponse> {
    return apiFetch('/usuarios/perfil');
  },
};

export const receitasApi = {
  async listar(): Promise<ReceitaResponse[]> {
    const receitas = await apiFetch<ReceitaBackendResponse[]>('/receitas');
    return receitas.map(normalizeReceita);
  },

  async criar(data: { descricao: string; valor: number; data: string; categoriaId: number }): Promise<ReceitaResponse> {
    const receita = await apiFetch<ReceitaBackendResponse>('/receitas', {
      method: 'POST',
      body: JSON.stringify(data),
    });
    return normalizeReceita(receita);
  },

  async deletar(id: number): Promise<void> {
    return apiFetch(`/receitas/${id}`, { method: 'DELETE' });
  },
};

export const despesasApi = {
  async listar(): Promise<DespesaResponse[]> {
    const despesas = await apiFetch<DespesaBackendResponse[]>('/despesas');
    return despesas.map(normalizeDespesa);
  },

  async criar(data: { descricao: string; valor: number; data: string; categoriaId: number }): Promise<DespesaResponse> {
    const despesa = await apiFetch<DespesaBackendResponse>('/despesas', {
      method: 'POST',
      body: JSON.stringify(data),
    });
    return normalizeDespesa(despesa);
  },

  async deletar(id: number): Promise<void> {
    return apiFetch(`/despesas/${id}`, { method: 'DELETE' });
  },
};

export const dashboardApi = {
  async resumo(): Promise<DashboardResponse> {
    const dashboard = await apiFetch<DashboardBackendResponse>('/dashboard');
    return normalizeDashboard(dashboard);
  },
};
