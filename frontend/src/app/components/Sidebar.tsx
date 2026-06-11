import { NavLink, useNavigate } from "react-router";
import { LayoutDashboard, TrendingUp, TrendingDown, BarChart3, BookOpen, LogOut } from "lucide-react";
import { logout, getUsuarioLogado } from "../utils/storage";

const navItems = [
  { to: "/", label: "Dashboard", icon: LayoutDashboard, end: true },
  { to: "/receitas", label: "Receitas", icon: TrendingUp },
  { to: "/despesas", label: "Despesas", icon: TrendingDown },
  { to: "/relatorios", label: "Relatórios", icon: BarChart3 },
  { to: "/educacao", label: "Educação", icon: BookOpen },
];

export function Sidebar() {
  const navigate = useNavigate();
  const usuario = getUsuarioLogado();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <aside className="w-64 bg-white dark:bg-gray-900 border-r border-gray-200 dark:border-gray-800 flex flex-col">
      <div className="p-6 border-b border-gray-200 dark:border-gray-800">
        <div className="flex items-center gap-3">
          <div className="bg-green-600 p-2 rounded-lg">
            <BookOpen className="size-5 text-white" />
          </div>
          <div>
            <h1 className="font-semibold text-sm text-gray-900 dark:text-gray-100">Finanças</h1>
            <p className="text-xs text-gray-500">da Comunidade</p>
          </div>
        </div>
      </div>

      <nav className="flex-1 p-4 space-y-1">
        {navItems.map(({ to, label, icon: Icon, end }) => (
          <NavLink
            key={to}
            to={to}
            end={end}
            className={({ isActive }) =>
              `flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                isActive
                  ? "bg-green-50 text-green-700 dark:bg-green-900/20 dark:text-green-400"
                  : "text-gray-600 hover:bg-gray-100 dark:text-gray-400 dark:hover:bg-gray-800"
              }`
            }
          >
            <Icon className="size-4" />
            {label}
          </NavLink>
        ))}
      </nav>

      <div className="p-4 border-t border-gray-200 dark:border-gray-800">
        {usuario && (
          <div className="mb-3 px-3">
            <p className="text-xs font-medium text-gray-900 dark:text-gray-100 truncate">{usuario.nome}</p>
            <p className="text-xs text-gray-500 truncate">{usuario.email}</p>
          </div>
        )}
        <button
          onClick={handleLogout}
          className="flex items-center gap-3 px-3 py-2 w-full rounded-lg text-sm font-medium text-red-600 hover:bg-red-50 transition-colors"
        >
          <LogOut className="size-4" />
          Sair
        </button>
      </div>
    </aside>
  );
}
