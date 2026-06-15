import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "./ui/accordion";
import { BookOpen, Target, TrendingUp, Shield, PiggyBank, CreditCard } from "lucide-react";

export function Educacao() {
  const conteudos = [
    {
      id: "orcamento",
      titulo: "Como fazer um orçamento doméstico",
      icon: PiggyBank,
      color: "text-blue-600",
      bgColor: "bg-blue-50",
      conteudo: (
        <div className="space-y-3 text-sm">
          <p>
            Um orçamento doméstico é essencial para o controle financeiro. Siga estes passos:
          </p>
          <ol className="list-decimal list-inside space-y-2 ml-2">
            <li><strong>Liste todas as receitas:</strong> Salário, renda extra, pensões, etc.</li>
            <li><strong>Registre todas as despesas:</strong> Fixas (aluguel, contas) e variáveis (alimentação, lazer).</li>
            <li><strong>Categorize os gastos:</strong> Organize por tipo para identificar padrões.</li>
            <li><strong>Compare receitas e despesas:</strong> Seu saldo deve ser positivo.</li>
            <li><strong>Ajuste quando necessário:</strong> Corte gastos supérfluos se estiver no vermelho.</li>
          </ol>
          <div className="mt-4 p-3 bg-blue-50 rounded">
            <p className="font-medium">💡 Dica Importante:</p>
            <p className="mt-1">Revise seu orçamento mensalmente e ajuste conforme mudanças na sua vida financeira.</p>
          </div>
        </div>
      ),
    },
    {
      id: "regra-50-30-20",
      titulo: "Regra 50-30-20 para organizar suas finanças",
      icon: Target,
      color: "text-green-600",
      bgColor: "bg-green-50",
      conteudo: (
        <div className="space-y-3 text-sm">
          <p>
            A regra 50-30-20 é uma forma simples de dividir sua renda mensal:
          </p>
          <div className="space-y-4 mt-4">
            <div className="p-3 bg-green-50 rounded border-l-4 border-green-600">
              <h4 className="font-medium text-green-900">50% - Necessidades</h4>
              <p className="mt-1 text-gray-700">
                Gastos essenciais como moradia, alimentação, transporte, saúde e contas básicas.
              </p>
            </div>
            <div className="p-3 bg-blue-50 rounded border-l-4 border-blue-600">
              <h4 className="font-medium text-blue-900">30% - Desejos</h4>
              <p className="mt-1 text-gray-700">
                Lazer, entretenimento, restaurantes, hobbies e outras coisas que você gosta mas não são essenciais.
              </p>
            </div>
            <div className="p-3 bg-purple-50 rounded border-l-4 border-purple-600">
              <h4 className="font-medium text-purple-900">20% - Poupança e Investimentos</h4>
              <p className="mt-1 text-gray-700">
                Reserve para emergências, objetivos futuros, aposentadoria e pagamento de dívidas.
              </p>
            </div>
          </div>
        </div>
      ),
    },
    {
      id: "economizar",
      titulo: "Dicas práticas para economizar no dia a dia",
      icon: TrendingUp,
      color: "text-orange-600",
      bgColor: "bg-orange-50",
      conteudo: (
        <div className="space-y-3 text-sm">
          <p>Pequenas mudanças nos hábitos podem gerar grande economia:</p>
          <ul className="space-y-2 ml-2">
            <li className="flex items-start gap-2">
              <span className="text-orange-600">•</span>
              <div>
                <strong>Faça lista de compras:</strong> Evite compras por impulso no supermercado.
              </div>
            </li>
            <li className="flex items-start gap-2">
              <span className="text-orange-600">•</span>
              <div>
                <strong>Cozinhe em casa:</strong> Comer fora é muito mais caro. Prepare marmitas para o trabalho.
              </div>
            </li>
            <li className="flex items-start gap-2">
              <span className="text-orange-600">•</span>
              <div>
                <strong>Compare preços:</strong> Use aplicativos e pesquise antes de comprar.
              </div>
            </li>
            <li className="flex items-start gap-2">
              <span className="text-orange-600">•</span>
              <div>
                <strong>Economize energia:</strong> Desligue aparelhos da tomada, use iluminação LED.
              </div>
            </li>
            <li className="flex items-start gap-2">
              <span className="text-orange-600">•</span>
              <div>
                <strong>Evite parcelamentos:</strong> Juros no cartão de crédito são altíssimos.
              </div>
            </li>
            <li className="flex items-start gap-2">
              <span className="text-orange-600">•</span>
              <div>
                <strong>Use transporte público:</strong> Economize com gasolina e estacionamento.
              </div>
            </li>
          </ul>
        </div>
      ),
    },
    {
      id: "dividas",
      titulo: "Como sair das dívidas",
      icon: CreditCard,
      color: "text-red-600",
      bgColor: "bg-red-50",
      conteudo: (
        <div className="space-y-3 text-sm">
          <p>Se você está endividado, siga este plano de ação:</p>
          <div className="space-y-3 mt-4">
            <div className="p-3 bg-gray-50 rounded">
              <h4 className="font-medium mb-1">1. Liste todas as dívidas</h4>
              <p>Anote valor, juros e data de vencimento de cada uma.</p>
            </div>
            <div className="p-3 bg-gray-50 rounded">
              <h4 className="font-medium mb-1">2. Priorize as dívidas</h4>
              <p>Pague primeiro as com maiores juros (cartão de crédito, cheque especial).</p>
            </div>
            <div className="p-3 bg-gray-50 rounded">
              <h4 className="font-medium mb-1">3. Negocie com credores</h4>
              <p>Muitas empresas oferecem descontos para pagamento à vista ou renegociação.</p>
            </div>
            <div className="p-3 bg-gray-50 rounded">
              <h4 className="font-medium mb-1">4. Corte gastos desnecessários</h4>
              <p>Temporariamente, elimine despesas supérfluas para quitar as dívidas.</p>
            </div>
            <div className="p-3 bg-gray-50 rounded">
              <h4 className="font-medium mb-1">5. Busque renda extra</h4>
              <p>Freelance, vendas, trabalhos temporários podem acelerar o pagamento.</p>
            </div>
          </div>
          <div className="mt-4 p-3 bg-red-50 rounded border border-red-200">
            <p className="font-medium text-red-900">⚠ Importante:</p>
            <p className="mt-1 text-red-800">Não faça novas dívidas enquanto estiver quitando as antigas!</p>
          </div>
        </div>
      ),
    },
    {
      id: "investimentos",
      titulo: "Primeiros passos em investimentos",
      icon: Shield,
      color: "text-purple-600",
      bgColor: "bg-purple-50",
      conteudo: (
        <div className="space-y-3 text-sm">
          <p>Investir é importante para fazer seu dinheiro crescer. Comece assim:</p>
          <div className="space-y-3 mt-4">
            <div className="p-3 bg-purple-50 rounded">
              <h4 className="font-medium mb-1">1. Crie uma reserva de emergência</h4>
              <p>Antes de investir, tenha 6 meses de despesas guardados em aplicações líquidas.</p>
            </div>
            <div className="p-3 bg-purple-50 rounded">
              <h4 className="font-medium mb-1">2. Conheça seu perfil de investidor</h4>
              <p>Conservador (baixo risco), moderado ou arrojado (alto risco).</p>
            </div>
            <div className="p-3 bg-purple-50 rounded">
              <h4 className="font-medium mb-1">3. Comece pelo básico</h4>
              <p>Tesouro Direto e CDB são opções seguras para iniciantes.</p>
            </div>
            <div className="p-3 bg-purple-50 rounded">
              <h4 className="font-medium mb-1">4. Diversifique</h4>
              <p>Não coloque todo o dinheiro em um único investimento.</p>
            </div>
            <div className="p-3 bg-purple-50 rounded">
              <h4 className="font-medium mb-1">5. Estude antes de investir</h4>
              <p>Nunca invista em algo que você não entende.</p>
            </div>
          </div>
        </div>
      ),
    },
    {
      id: "planejamento",
      titulo: "Planejamento financeiro de longo prazo",
      icon: BookOpen,
      color: "text-teal-600",
      bgColor: "bg-teal-50",
      conteudo: (
        <div className="space-y-3 text-sm">
          <p>Pense no futuro e estabeleça metas financeiras:</p>
          <div className="space-y-3 mt-4">
            <div className="p-3 bg-teal-50 rounded">
              <h4 className="font-medium mb-1">Defina objetivos claros</h4>
              <p>Comprar casa, carro, viajar, aposentadoria, estudos dos filhos, etc.</p>
            </div>
            <div className="p-3 bg-teal-50 rounded">
              <h4 className="font-medium mb-1">Estabeleça prazos</h4>
              <p>Curto prazo (até 1 ano), médio (1-5 anos) e longo prazo (acima de 5 anos).</p>
            </div>
            <div className="p-3 bg-teal-50 rounded">
              <h4 className="font-medium mb-1">Calcule quanto precisa guardar</h4>
              <p>Divida o valor da meta pelo número de meses até o prazo.</p>
            </div>
            <div className="p-3 bg-teal-50 rounded">
              <h4 className="font-medium mb-1">Revise periodicamente</h4>
              <p>Ajuste suas metas conforme sua realidade financeira muda.</p>
            </div>
          </div>
          <div className="mt-4 p-3 bg-teal-100 rounded">
            <p className="font-medium">🎯 Exemplo prático:</p>
            <p className="mt-1">
              Meta: Comprar um carro de R$ 30.000 em 3 anos (36 meses)<br />
              Quanto guardar por mês: R$ 30.000 ÷ 36 = R$ 833,33
            </p>
          </div>
        </div>
      ),
    },
  ];

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-3xl font-semibold text-gray-900 dark:text-gray-100 mb-2">Educação Financeira</h1>
        <p className="text-gray-600 dark:text-gray-400">Aprenda a organizar suas finanças e construir um futuro próspero</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        <Card className="bg-gradient-to-br from-green-50 to-green-100 border-green-200">
          <CardContent className="pt-6">
            <div className="flex items-center gap-3">
              <div className="bg-green-600 p-3 rounded-full">
                <BookOpen className="size-6 text-white" />
              </div>
              <div>
                <p className="text-2xl font-semibold text-green-900">6</p>
                <p className="text-sm text-green-700">Módulos educativos</p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="bg-gradient-to-br from-blue-50 to-blue-100 border-blue-200">
          <CardContent className="pt-6">
            <div className="flex items-center gap-3">
              <div className="bg-blue-600 p-3 rounded-full">
                <Target className="size-6 text-white" />
              </div>
              <div>
                <p className="text-2xl font-semibold text-blue-900">100%</p>
                <p className="text-sm text-blue-700">Conteúdo gratuito</p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="bg-gradient-to-br from-purple-50 to-purple-100 border-purple-200">
          <CardContent className="pt-6">
            <div className="flex items-center gap-3">
              <div className="bg-purple-600 p-3 rounded-full">
                <TrendingUp className="size-6 text-white" />
              </div>
              <div>
                <p className="text-2xl font-semibold text-purple-900">Prático</p>
                <p className="text-sm text-purple-700">Aplicável no dia a dia</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Conteúdos Educativos</CardTitle>
        </CardHeader>
        <CardContent>
          <Accordion type="single" collapsible className="w-full">
            {conteudos.map((item) => (
              <AccordionItem key={item.id} value={item.id}>
                <AccordionTrigger className="hover:no-underline">
                  <div className="flex items-center gap-3">
                    <div className={`${item.bgColor} p-2 rounded-lg`}>
                      <item.icon className={`size-5 ${item.color}`} />
                    </div>
                    <span className="text-left">{item.titulo}</span>
                  </div>
                </AccordionTrigger>
                <AccordionContent className="pt-4 pb-6 px-2">
                  {item.conteudo}
                </AccordionContent>
              </AccordionItem>
            ))}
          </Accordion>
        </CardContent>
      </Card>

      <Card className="mt-6 bg-gradient-to-r from-green-600 to-blue-600">
        <CardContent className="pt-6 text-white">
          <h3 className="text-xl font-semibold mb-2">💡 Lembre-se</h3>
          <p className="mb-4">
            Educação financeira é um processo contínuo. Quanto mais você aprende e pratica, melhores serão suas decisões financeiras!
          </p>
          <p className="text-sm opacity-90">
            Use as ferramentas desta plataforma para colocar em prática o que você aprendeu aqui.
          </p>
        </CardContent>
      </Card>
    </div>
  );
}