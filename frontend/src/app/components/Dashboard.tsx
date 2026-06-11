import { useState, useEffect } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { TrendingUp, TrendingDown, DollarSign, AlertCircle, Loader2 } from "lucide-react";
import { dashboardApi, DashboardResponse } from "../utils/api";
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from "recharts";
import { toast } from "sonner";

export function Dashboard() {
  const [dados, setDados] = useState<DashboardResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dashboardApi.resumo()
      .then(setDados)
      .catch((err) => toast.error(err.message || "Erro ao carregar dashboard"))
      .finally(() => setLoading(false));
  }, []);

  const COLORS = ["#10b981", "#3b82f6", "#f59e0b", "#ef4444", "#8b5cf6", "#ec4899", "#14b8a6", "#6366f1"];

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(value);

  if (loading) {
    return (
      <div className="p-8 flex items-center justify-center h-64">
        <Loader2 className="size-8 animate-spin text-green-600" />
      </div>
    );
  }

  const totalReceitas = dados?.totalReceitas ?? 0;
  const totalDespesas = dados?.totalDespesas ?? 0;
  const saldo = dados?.saldo ?? 0;
  const despesasPorCategoria = (dados?.despesasPorCategoria ?? []).map(d => ({
    name: d.categoria,
    value: d.valor,
  }));

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-3xl font-semibold text-gray-900 dark:text-gray-100 mb-2">Dashboard Financeiro</h1>
        <p className="text-gray-600 dark:text-gray-400">Visão geral das suas finanças</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Total Receitas</CardTitle>
            <TrendingUp className="size-5 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-semibold text-green-600">{formatCurrency(totalReceitas)}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Total Despesas</CardTitle>
            <TrendingDown className="size-5 text-red-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-semibold text-red-600">{formatCurrency(totalDespesas)}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Saldo</CardTitle>
            <DollarSign className={`size-5 ${saldo >= 0 ? "text-blue-600" : "text-orange-600"}`} />
          </CardHeader>
          <CardContent>
            <div className={`text-2xl font-semibold ${saldo >= 0 ? "text-blue-600" : "text-orange-600"}`}>
              {formatCurrency(saldo)}
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Despesas por Categoria</CardTitle>
          </CardHeader>
          <CardContent>
            {despesasPorCategoria.length > 0 ? (
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={despesasPorCategoria}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                    outerRadius={80}
                    dataKey="value"
                  >
                    {despesasPorCategoria.map((_, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip formatter={(value: number) => formatCurrency(value)} />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <div className="h-[300px] flex items-center justify-center text-gray-500">
                Nenhuma despesa registrada
              </div>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Dicas Financeiras</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex gap-3 p-4 bg-blue-50 rounded-lg">
              <AlertCircle className="size-5 text-blue-600 flex-shrink-0 mt-0.5" />
              <div>
                <h3 className="font-medium text-blue-900 mb-1">Regra 50-30-20</h3>
                <p className="text-sm text-blue-800">50% necessidades, 30% desejos, 20% poupança</p>
              </div>
            </div>
            <div className="flex gap-3 p-4 bg-green-50 rounded-lg">
              <AlertCircle className="size-5 text-green-600 flex-shrink-0 mt-0.5" />
              <div>
                <h3 className="font-medium text-green-900 mb-1">Reserve mensalmente</h3>
                <p className="text-sm text-green-800">Tente guardar pelo menos 10% da sua renda</p>
              </div>
            </div>
            <div className="flex gap-3 p-4 bg-orange-50 rounded-lg">
              <AlertCircle className="size-5 text-orange-600 flex-shrink-0 mt-0.5" />
              <div>
                <h3 className="font-medium text-orange-900 mb-1">Controle seus gastos</h3>
                <p className="text-sm text-orange-800">Registre todas as despesas para ter consciência financeira</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
