// Gerencia sessão do usuário no localStorage (token JWT + dados básicos)
// A persistência de receitas e despesas agora é feita pelo back-end.

export interface Usuario {
  id: string;
  nome: string;
  email: string;
}

export const CATEGORIAS_RECEITA = [
  "Salário",
  "Freelance",
  "Investimentos",
  "Vendas",
  "Outros",
];

export const CATEGORIAS_DESPESA = [
  "Alimentação",
  "Transporte",
  "Moradia",
  "Saúde",
  "Educação",
  "Lazer",
  "Contas",
  "Outros",
];

export const getUsuarioLogado = (): Usuario | null => {
  const user = localStorage.getItem("usuarioLogado");
  return user ? JSON.parse(user) : null;
};

export const setUsuarioLogado = (usuario: Usuario) => {
  localStorage.setItem("usuarioLogado", JSON.stringify(usuario));
};

export const setToken = (token: string) => {
  localStorage.setItem("token", token);
};

export const getToken = (): string | null => {
  return localStorage.getItem("token");
};

export const logout = () => {
  localStorage.removeItem("usuarioLogado");
  localStorage.removeItem("token");
};
