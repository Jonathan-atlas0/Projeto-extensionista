import { useState, useEffect, FormEvent } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { Plus, Trash2, TrendingUp, Loader2 } from "lucide-react";
import { categoriasApi, CategoriaResponse, receitasApi, ReceitaResponse } from "../utils/api";
import { toast } from "sonner";

export function Receitas() {
  const [receitas, setReceitas] = useState<ReceitaResponse[]>([]);
  const [categorias, setCategorias] = useState<CategoriaResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [salvando, setSalvando] = useState(false);
  const [descricao, setDescricao] = useState("");
  const [valor, setValor] = useState("");
  const [data, setData] = useState("");
  const [categoriaId, setCategoriaId] = useState("");

  const carregar = async () => {
    try {
      const [dados, categoriasReceita] = await Promise.all([
        receitasApi.listar(),
        categoriasApi.listarPorTipo("RECEITA"),
      ]);
      setReceitas(dados);
      setCategorias(categoriasReceita);
    } catch (err: any) {
      toast.error(err.message || "Erro ao carregar receitas");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { carregar(); }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!descricao || !valor || !data || !categoriaId) return;
    setSalvando(true);
    try {
      const nova = await receitasApi.criar({
        descricao,
        valor: parseFloat(valor),
        data,
        categoriaId: Number(categoriaId),
      });
      setReceitas(prev => [nova, ...prev]);
      setDescricao(""); setValor(""); setData(""); setCategoriaId("");
      toast.success("Receita adicionada com sucesso!");
    } catch (err: any) {
      toast.error(err.message || "Erro ao adicionar receita");
    } finally {
      setSalvando(false);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await receitasApi.deletar(id);
      setReceitas(prev => prev.filter(r => r.id !== id));
      toast.success("Receita removida!");
    } catch (err: any) {
      toast.error(err.message || "Erro ao remover receita");
    }
  };

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(value);

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr + "T00:00:00");
    return date.toLocaleDateString("pt-BR");
  };

  const totalReceitas = receitas.reduce((acc, r) => acc + r.valor, 0);

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-3xl font-semibold text-gray-900 dark:text-gray-100 mb-2">Receitas</h1>
        <p className="text-gray-600 dark:text-gray-400">Gerencie suas entradas financeiras</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="lg:col-span-1">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Plus className="size-5" /> Nova Receita
            </CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="descricao">Descrição</Label>
                <Input id="descricao" type="text" placeholder="Ex: Salário, Freelance..." value={descricao}
                  onChange={(e) => setDescricao(e.target.value)} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="valor">Valor (R$)</Label>
                <Input id="valor" type="number" step="0.01" placeholder="0,00" value={valor}
                  onChange={(e) => setValor(e.target.value)} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="data">Data</Label>
                <Input id="data" type="date" value={data} onChange={(e) => setData(e.target.value)} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="categoria">Categoria</Label>
                <Select value={categoriaId} onValueChange={setCategoriaId} required>
                  <SelectTrigger><SelectValue placeholder="Selecione uma categoria" /></SelectTrigger>
                  <SelectContent>
                    {categorias.map((cat) => (
                      <SelectItem key={cat.id} value={String(cat.id)}>{cat.nome}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <Button type="submit" className="w-full bg-green-600 hover:bg-green-700" disabled={salvando || categorias.length === 0}>
                {salvando ? <Loader2 className="size-4 mr-2 animate-spin" /> : <Plus className="size-4 mr-2" />}
                Adicionar Receita
              </Button>
            </form>
          </CardContent>
        </Card>

        <Card className="lg:col-span-2">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle>Histórico de Receitas</CardTitle>
              <div className="flex items-center gap-2 text-green-600">
                <TrendingUp className="size-5" />
                <span className="font-semibold">{formatCurrency(totalReceitas)}</span>
              </div>
            </div>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="flex items-center justify-center h-40">
                <Loader2 className="size-6 animate-spin text-green-600" />
              </div>
            ) : receitas.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                <TrendingUp className="size-12 mx-auto mb-4 opacity-20" />
                <p>Nenhuma receita registrada ainda.</p>
                <p className="text-sm">Adicione sua primeira receita ao lado!</p>
              </div>
            ) : (
              <div className="space-y-3">
                {[...receitas]
                  .sort((a, b) => new Date(b.data).getTime() - new Date(a.data).getTime())
                  .map((receita) => (
                    <div key={receita.id}
                      className="flex items-center justify-between p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-1">
                          <h3 className="font-medium text-gray-900">{receita.descricao}</h3>
                          <span className="text-xs px-2 py-1 bg-green-100 text-green-700 rounded-full">
                            {receita.categoria}
                          </span>
                        </div>
                        <p className="text-sm text-gray-600">{formatDate(receita.data)}</p>
                      </div>
                      <div className="flex items-center gap-4">
                        <span className="font-semibold text-green-600">{formatCurrency(receita.valor)}</span>
                        <Button variant="ghost" size="sm" onClick={() => handleDelete(receita.id)}
                          className="text-red-600 hover:text-red-700 hover:bg-red-50">
                          <Trash2 className="size-4" />
                        </Button>
                      </div>
                    </div>
                  ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
