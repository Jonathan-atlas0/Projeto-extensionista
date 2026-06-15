import { useState, FormEvent } from "react";
import { useNavigate } from "react-router";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import { setUsuarioLogado, setToken } from "../utils/storage";
import { authApi } from "../utils/api";
import { BookOpen, Loader2 } from "lucide-react";
import { toast } from "sonner";

export function Login() {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Estado do formulário de login
  const [loginEmail, setLoginEmail] = useState("");
  const [loginSenha, setLoginSenha] = useState("");

  // Estado do formulário de registro
  const [regNome, setRegNome] = useState("");
  const [regEmail, setRegEmail] = useState("");
  const [regSenha, setRegSenha] = useState("");

  const handleLogin = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await authApi.login(loginEmail, loginSenha);
      setToken(res.token);
      setUsuarioLogado({ id: String(res.id), nome: res.nome, email: res.email });
      navigate("/");
    } catch (err: any) {
      toast.error(err.message || "Erro ao fazer login");
    } finally {
      setLoading(false);
    }
  };

  const handleRegistro = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await authApi.registro(regNome, regEmail, regSenha);
      setToken(res.token);
      setUsuarioLogado({ id: String(res.id), nome: res.nome, email: res.email });
      toast.success("Conta criada com sucesso!");
      navigate("/");
    } catch (err: any) {
      toast.error(err.message || "Erro ao criar conta");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 to-blue-50 flex items-center justify-center p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="text-center space-y-2">
          <div className="flex justify-center mb-4">
            <div className="bg-green-600 p-3 rounded-full">
              <BookOpen className="size-8 text-white" />
            </div>
          </div>
          <CardTitle className="text-2xl">Finanças da Comunidade</CardTitle>
          <CardDescription>
            Plataforma de educação financeira e organização pessoal
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Tabs defaultValue="login">
            <TabsList className="w-full mb-4">
              <TabsTrigger value="login" className="flex-1">Entrar</TabsTrigger>
              <TabsTrigger value="registro" className="flex-1">Criar conta</TabsTrigger>
            </TabsList>

            {/* LOGIN */}
            <TabsContent value="login">
              <form onSubmit={handleLogin} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="email">E-mail</Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="seu@email.com"
                    value={loginEmail}
                    onChange={(e) => setLoginEmail(e.target.value)}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="senha">Senha</Label>
                  <Input
                    id="senha"
                    type="password"
                    placeholder="••••••"
                    value={loginSenha}
                    onChange={(e) => setLoginSenha(e.target.value)}
                    required
                  />
                </div>
                <Button type="submit" className="w-full bg-green-600 hover:bg-green-700" disabled={loading}>
                  {loading ? <Loader2 className="size-4 mr-2 animate-spin" /> : null}
                  Entrar na Plataforma
                </Button>
              </form>
            </TabsContent>

            {/* REGISTRO */}
            <TabsContent value="registro">
              <form onSubmit={handleRegistro} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="reg-nome">Nome completo</Label>
                  <Input
                    id="reg-nome"
                    type="text"
                    placeholder="Seu nome"
                    value={regNome}
                    onChange={(e) => setRegNome(e.target.value)}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="reg-email">E-mail</Label>
                  <Input
                    id="reg-email"
                    type="email"
                    placeholder="seu@email.com"
                    value={regEmail}
                    onChange={(e) => setRegEmail(e.target.value)}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="reg-senha">Senha</Label>
                  <Input
                    id="reg-senha"
                    type="password"
                    placeholder="Mínimo 6 caracteres"
                    value={regSenha}
                    onChange={(e) => setRegSenha(e.target.value)}
                    minLength={6}
                    required
                  />
                </div>
                <Button type="submit" className="w-full bg-green-600 hover:bg-green-700" disabled={loading}>
                  {loading ? <Loader2 className="size-4 mr-2 animate-spin" /> : null}
                  Criar conta
                </Button>
              </form>
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>
    </div>
  );
}
