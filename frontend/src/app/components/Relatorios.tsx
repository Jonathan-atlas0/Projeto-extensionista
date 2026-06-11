import { useState, useEffect } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { receitasApi, despesasApi } from "../utils/api";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";
import { Calendar, TrendingUp, TrendingDown, DollarSign, Loader2 } from "lucide-react";
import { toast } from "sonner";

export function Relatorios() {
  const [mes, setMes] = useState("2026-06");
  const [dadosMensais, setDadosMensais] = useState<any[]>([]);
  const [resumoMes, setResumoMes] = useState({ receitas: 0, despesas: 0, saldo: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const carregar = async () => {
      setLoading(true);
      try {
        const [receitas, despesas] = await Promise.all([
          receitasApi.listar(),
          despesasApi.listar(),
        ]);

        const receitasMes = receitas.filter((r) => r.data.startsWith(mes));
        const despesasMes = despesas.filter((d) => d.data.startsWith(mes));

        const totalReceitas = receitasMes.reduce((acc, r) => acc + r.valor, 0);
        const totalDespesas = despesasMes.reduce((acc, d) => acc + d.valor, 0);

        setResumoMes({
          receitas: totalReceitas,
          despesas: totalDespesas,
          saldo: totalReceitas - totalDespesas,
        });

        const categorias = new Set([
          ...receitasMes.map((r) => r.categoria),
          ...despesasMes.map((d) => d.categoria),
        ]);

        const dadosComparacao = Array.from(categorias).map((categoria) => ({
          categoria,
          receitas: receitasMes.filter((r) => r.categoria === categoria).reduce((acc, r) => acc + r.valor, 0),
          despesas: despesasMes.filter((d) => d.categoria === categoria).reduce((acc, d) => acc + d.valor, 0),
        }));

        setDadosMensais(dadosComparacao);
      } catch (err: any) {
        toast.error(err.message || "Erro ao carregar relatórios");
      } finally {
        setLoading(false);
      }
    };

    carregar();
  }, [mes]);

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(value);

  const mesesDisponiveis = [
    { value: "2026-06", label: "Junho 2026" },
    { value: "2026-05", label: "Maio 2026" },
    { value: "2026-04", label: "Abril 2026" },
    { value: "2026-03", label: "Março 2026" },
    { value: "2026-02", label: "Fevereiro 2026" },
    { value: "2026-01", label: "Janeiro 2026" },
  ];

  if (loading) {
    return (
      <div className="p-8 flex items-center justify-center h-64">
        <Loader2 className="size-8 animate-spin text-green-600" />
      </div>
    );
  }

  return (
    <div className="p-8">
      <div className="mb-8 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-semibold text-gray-900 dark:text-gray-100 mb-2">Relatórios</h1>
          <p className="text-gray-600 dark:text-gray-400">Análise detalhada das suas finanças</p>
        </div>
        <div className="flex items-center gap-3">
          <Calendar className="size-5 text-gray-600 dark:text-gray-400" />
          <Select value={mes} onValueChange={setMes}>
            <SelectTrigger className="w-48">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {mesesDisponiveis.map((m) => (
                <SelectItem key={m.value} value={m.value}>{m.label}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Receitas do Mês</CardTitle>
            <TrendingUp className="size-5 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-semibold text-green-600">{formatCurrency(resumoMes.receitas)}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Despesas do Mês</CardTitle>
            <TrendingDown className="size-5 text-red-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-semibold text-red-600">{formatCurrency(resumoMes.despesas)}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Saldo do Mês</CardTitle>
            <DollarSign className={`size-5 ${resumoMes.saldo >= 0 ? "text-blue-600" : "text-orange-600"}`} />
          </CardHeader>
          <CardContent>
            <div className={`text-2xl font-semibold ${resumoMes.saldo >= 0 ? "text-blue-600" : "text-orange-600"}`}>
              {formatCurrency(resumoMes.saldo)}
            </div>
          </CardContent>
        </Card>
      </div>

      <Card className="mb-8">
        <CardHeader>
          <CardTitle>Comparação: Receitas x Despesas por Categoria</CardTitle>
        </CardHeader>
        <CardContent>
          {dadosMensais.length > 0 ? (
            <ResponsiveContainer width="100%" height={400}>
              <BarChart data={dadosMensais}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="categoria" />
                <YAxis />
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
                <Legend />
                <Bar dataKey="receitas" fill="#10b981" name="Receitas" />
                <Bar dataKey="despesas" fill="#ef4444" name="Despesas" />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <div className="h-[400px] flex items-center justify-center text-gray-500">
              Nenhum dado disponível para o mês selecionado
            </div>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Análise do Período</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {resumoMes.saldo >= 0 ? (
              <div className="p-4 bg-green-50 rounded-lg border border-green-200">
                <h3 className="font-medium text-green-900 mb-2">✓ Saldo Positivo</h3>
                <p className="text-sm text-green-800">
                  Parabéns! Suas receitas superaram suas despesas em {formatCurrency(resumoMes.saldo)}.
                  Continue mantendo o controle financeiro!
                </p>
              </div>
            ) : (
              <div className="p-4 bg-orange-50 rounded-lg border border-orange-200">
                <h3 className="font-medium text-orange-900 mb-2">⚠ Atenção</h3>
                <p className="text-sm text-orange-800">
                  Suas despesas superaram as receitas em {formatCurrency(Math.abs(resumoMes.saldo))}.
                  Revise seus gastos e busque reduzir despesas desnecessárias.
                </p>
              </div>
            )}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="p-4 bg-blue-50 rounded-lg">
                <h4 className="font-medium text-blue-900 mb-2">Dica de Economia</h4>
                <p className="text-sm text-blue-800">
                  Avalie as categorias com maiores gastos e identifique onde é possível economizar.
                </p>
              </div>
              <div className="p-4 bg-purple-50 rounded-lg">
                <h4 className="font-medium text-purple-900 mb-2">Meta de Poupança</h4>
                <p className="text-sm text-purple-800">
                  Tente poupar pelo menos 20% das suas receitas mensais para objetivos futuros.
                </p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
