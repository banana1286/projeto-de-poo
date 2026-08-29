package view;

import java.util.List;
import java.util.Scanner;

import controller.LojaController;
import exceptions.ArquivoException;
import exceptions.EstoqueInsuficienteException;
import exceptions.ProdutoNaoEncontradoException;
import model.Categoria;
import model.Consumidor;
import model.ItemCarrinho;
import model.Produto;


public class MainConsumidor {

    private static final LojaController controller = new LojaController();
    private static final Scanner scanner = new Scanner(System.in);
    private static Consumidor consumidor;

    public static void main(String[] args) {
        System.out.println("=== LOJA DE ARTIGOS MUSICAIS - ÁREA DO CONSUMIDOR ===");
        try {
            controller.carregarProdutos();
        } catch (ArquivoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        identificarConsumidor();

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> pesquisarProdutos();
                case 2 -> listarTodos();
                case 3 -> listarPorCategoria();
                case 4 -> visualizarDetalhes();
                case 5 -> adicionarAoCarrinho();
                case 6 -> removerDoCarrinho();
                case 7 -> visualizarCarrinho();
                case 8 -> calcularTotalCarrinho();
                case 9 -> finalizarCompra();
                case 0 -> System.out.println("Obrigado pela visita, " + consumidor.getNome() + "!");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void identificarConsumidor() {
        System.out.println("Antes de começar, informe os seus dados:");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        consumidor = new Consumidor(nome, email, cpf);
        System.out.println("Bem-vindo(a), " + nome + "!");
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("           MENU DO CONSUMIDOR ");
        System.out.println("1 - Pesquisar produtos");
        System.out.println("2 - Listar todos os produtos disponíveis");
        System.out.println("3 - Listar produtos por categoria");
        System.out.println("4 - Visualizar detalhes de um produto");
        System.out.println("5 - Adicionar produto ao carrinho");
        System.out.println("6 - Remover produto do carrinho");
        System.out.println("7 - Visualizar carrinho");
        System.out.println("8 - Calcular valor total da compra");
        System.out.println("9 - Finalizar compra");
        System.out.println("0 - Sair");

    }

    private static void pesquisarProdutos() {
        System.out.print("Digite o termo de pesquisa: ");
        String termo = scanner.nextLine();
        exibirLista(controller.pesquisar(termo));
    }

    private static void listarTodos() {
        exibirLista(controller.listarProdutos());
    }

    private static void listarPorCategoria() {
        Categoria categoria = lerCategoria();
        if (categoria == null) {
            return;
        }
        exibirLista(controller.listarPorCategoria(categoria));
    }

    private static void visualizarDetalhes() {
        int codigo = lerInteiro("Código do produto: ");
        try {
            Produto produto = controller.buscarPorCodigo(codigo);
            System.out.println(produto);
        } catch (ProdutoNaoEncontradoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void adicionarAoCarrinho() {
        int codigo = lerInteiro("Código do produto: ");
        int quantidade = lerInteiro("Quantidade: ");
        try {
            Produto produto = controller.buscarPorCodigo(codigo);
            consumidor.getCarrinho().adicionarProduto(produto, quantidade);
            System.out.println("Produto adicionado ao carrinho com sucesso!");
        } catch (ProdutoNaoEncontradoException | EstoqueInsuficienteException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void removerDoCarrinho() {
        int codigo = lerInteiro("Código do produto a remover do carrinho: ");
        boolean removido = consumidor.getCarrinho().removerProduto(codigo);
        System.out.println(removido
                ? "Produto removido do carrinho."
                : "Produto não encontrado no carrinho.");
    }

    private static void visualizarCarrinho() {
        List<ItemCarrinho> itens = consumidor.getCarrinho().listarItens();
        if (itens.isEmpty()) {
            System.out.println("Seu carrinho está vazio.");
            return;
        }
        System.out.println();
        System.out.println("-- Itens do carrinho --");
        for (ItemCarrinho item : itens) {
            System.out.println(item);
        }
    }

    private static void calcularTotalCarrinho() {
        System.out.printf("Valor total do carrinho: R$ %.2f%n", consumidor.getCarrinho().calcularTotal());
    }

    private static void finalizarCompra() {
        if (consumidor.getCarrinho().estaVazio()) {
            System.out.println("Seu carrinho está vazio. Adicione produtos antes de finalizar a compra.");
            return;
        }
        try {
            controller.realizarCompra(consumidor, consumidor.getCarrinho());
            System.out.println("Compra finalizada com sucesso! Obrigado, " + consumidor.getNome() + "!");
        } catch (EstoqueInsuficienteException | ArquivoException e) {
            System.out.println("Erro ao finalizar compra: " + e.getMessage());
        }
    }

    private static Categoria lerCategoria() {
        System.out.println("Categorias disponíveis:");
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + " - " + categorias[i]);
        }
        int opcao = lerInteiro("Escolha a categoria: ");
        if (opcao < 1 || opcao > categorias.length) {
            System.out.println("Categoria inválida.");
            return null;
        }
        return categorias[opcao - 1];
    }

    private static void exibirLista(List<Produto> produtos) {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }
        System.out.println();
        for (Produto produto : produtos) {
            System.out.println(produto);
        }
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine();
            try {
                return Integer.parseInt(linha.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }
}
