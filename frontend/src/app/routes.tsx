import { createBrowserRouter } from "react-router";
import { Root } from "./components/Root";
import { Dashboard } from "./components/Dashboard";
import { Receitas } from "./components/Receitas";
import { Despesas } from "./components/Despesas";
import { Relatorios } from "./components/Relatorios";
import { Educacao } from "./components/Educacao";
import { Login } from "./components/Login";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: Root,
    children: [
      { index: true, Component: Dashboard },
      { path: "receitas", Component: Receitas },
      { path: "despesas", Component: Despesas },
      { path: "relatorios", Component: Relatorios },
      { path: "educacao", Component: Educacao },
    ],
  },
  {
    path: "/login",
    Component: Login,
  },
]);
