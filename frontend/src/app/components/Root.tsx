import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router";
import { Sidebar } from "./Sidebar";
import { getUsuarioLogado } from "../utils/storage";

export function Root() {
  const navigate = useNavigate();

  useEffect(() => {
    const usuario = getUsuarioLogado();
    if (!usuario) {
      navigate("/login");
    }
  }, [navigate]);

  const usuario = getUsuarioLogado();
  if (!usuario) return null;

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-950">
      <Sidebar />
      <main className="flex-1 overflow-y-auto">
        <Outlet />
      </main>
    </div>
  );
}