package view;

import java.util.List;
import java.util.Scanner;

import controller.LojaController;
import exceptions.ArquivoException;
import exceptions.ProdutoDuplicadoException;
import exceptions.ProdutoNaoEncontradoException;
import model.Acessorio;
import model.CD;
import model.Categoria;
import model.DVD;
import model.Instrumento;
import model.Produto;
import model.Roupa;
import model.Vinil;

public class MainADM {

    private static final LojaController controller = new LojaController();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println(
                "=== LOJA DE ARTIGOS MUSICAIS - PAINEL DO ADMINISTRADOR ===");

        try {
            controller.carregarProdutos();
            System.out.println("Produtos carregados com sucesso.");
        } catch (ArquivoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> alterarInformacoes();
                case 3 -> alterarPreco();
                case 4 -> alterarEstoque();
                case 5 -> excluirProduto();
                case 6 -> pesquisarProdutos();
                case 7 -> listarTodos();
                case 8 -> listarPorCategoria();
                case 9 -> salvar();
                case 10 -> carregar();
                case 0 -> System.out.println(
                        "Encerrando painel administrativo...");
                default -> System.out.println(
                        "Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("MENU ADMINISTRADOR");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Alterar informações do produto");
        System.out.println("3 - Alterar preço");
        System.out.println("4 - Alterar estoque");
        System.out.println("5 - Excluir produto");
        System.out.println("6 - Pesquisar produtos");
        System.out.println("7 - Listar todos os produtos");
        System.out.println("8 - Listar produtos por categoria");
        System.out.println("9 - Salvar alterações");
        System.out.println("10 - Carregar produtos");
        System.out.println("0 - Sair");
    }

    private static void cadastrarProduto() {

        System.out.println();
        System.out.println("-- Cadastro de Produto --");

        Categoria categoria = lerCategoria();

        if (categoria == null) {
            return;
        }

        try {

            int codigo = lerInteiro("Código: ");

            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            double valor = lerDouble("Valor: ");
            int estoque = lerInteiro("Quantidade em estoque: ");

            Produto produto = criarProdutoPorCategoria(
                    categoria,
                    codigo,
                    nome,
                    valor,
                    estoque
            );

            controller.cadastrarProduto(produto);
            controller.salvarProdutos();

            System.out.println("Produto cadastrado com sucesso!");

        } catch (ProdutoDuplicadoException
                 | ArquivoException
                 | IllegalArgumentException e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static Produto criarProdutoPorCategoria(
            Categoria categoria,
            int codigo,
            String nome,
            double valor,
            int estoque) {

        switch (categoria) {

            case DVD, VINIL, CD -> {

                System.out.print("Artista: ");
                String artista = scanner.nextLine();

                System.out.print("Gravadora: ");
                String gravadora = scanner.nextLine();

                int ano = lerInteiro("Ano de lançamento: ");

                return switch (categoria) {
                    case DVD -> new DVD(
                            codigo, nome, valor, estoque,
                            artista, gravadora, ano);

                    case VINIL -> new Vinil(
                            codigo, nome, valor, estoque,
                            artista, gravadora, ano);

                    default -> new CD(
                            codigo, nome, valor, estoque,
                            artista, gravadora, ano);
                };
            }

            case INSTRUMENTO -> {

                System.out.print("Marca: ");
                String marca = scanner.nextLine();

                System.out.print("Modelo: ");
                String modelo = scanner.nextLine();

                System.out.print("Material: ");
                String material = scanner.nextLine();

                return new Instrumento(
                        codigo,
                        nome,
                        valor,
                        estoque,
                        marca,
                        modelo,
                        material
                );
            }

            case ROUPA -> {

                System.out.print("Banda: ");
                String banda = scanner.nextLine();

                System.out.print("Tamanho: ");
                String tamanho = scanner.nextLine();

                System.out.print("Material: ");
                String material = scanner.nextLine();

                return new Roupa(
                        codigo,
                        nome,
                        valor,
                        estoque,
                        banda,
                        tamanho,
                        material
                );
            }

            default -> {

                System.out.print("Tipo: ");
                String tipo = scanner.nextLine();

                System.out.print("Marca: ");
                String marca = scanner.nextLine();

                return new Acessorio(
                        codigo,
                        nome,
                        valor,
                        estoque,
                        tipo,
                        marca
                );
            }
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

    private static void alterarInformacoes() {

        int codigo = lerInteiro("Código do produto: ");

        try {

            Produto produto = controller.buscarPorCodigo(codigo);

            System.out.println("Produto atual: " + produto);

            System.out.print(
                    "Novo nome (deixe em branco para manter o atual): ");

            String nome = scanner.nextLine();

            if (!nome.isBlank()) {
                controller.alterarNome(codigo, nome);
                controller.salvarProdutos();
                System.out.println("Produto atualizado com sucesso!");
            }

        } catch (ProdutoNaoEncontradoException
                 | ArquivoException
                 | IllegalArgumentException e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void alterarPreco() {

        int codigo = lerInteiro("Código do produto: ");
        double novoValor = lerDouble("Novo valor: ");

        try {

            controller.alterarPreco(codigo, novoValor);
            controller.salvarProdutos();

            System.out.println("Preço atualizado com sucesso!");

        } catch (ProdutoNaoEncontradoException
                 | ArquivoException
                 | IllegalArgumentException e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void alterarEstoque() {

        int codigo = lerInteiro("Código do produto: ");
        int novaQuantidade =
                lerInteiro("Nova quantidade em estoque: ");

        try {

            controller.alterarEstoque(codigo, novaQuantidade);
            controller.salvarProdutos();

            System.out.println("Estoque atualizado com sucesso!");

        } catch (ProdutoNaoEncontradoException
                 | ArquivoException
                 | IllegalArgumentException e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void excluirProduto() {

        int codigo = lerInteiro("Código do produto a excluir: ");

        try {

            controller.excluirProduto(codigo);
            controller.salvarProdutos();

            System.out.println("Produto excluído com sucesso!");

        } catch (ProdutoNaoEncontradoException
                 | ArquivoException e) {

            System.out.println("Erro: " + e.getMessage());
        }
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

    private static void salvar() {

        try {

            controller.salvarProdutos();
            System.out.println("Alterações salvas com sucesso!");

        } catch (ArquivoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void carregar() {

        try {

            controller.carregarProdutos();
            System.out.println("Produtos carregados com sucesso!");

        } catch (ArquivoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static int lerInteiro(String mensagem) {

        while (true) {

            System.out.print(mensagem);
            String linha = scanner.nextLine();

            try {
                return Integer.parseInt(linha.trim());

            } catch (NumberFormatException e) {
                System.out.println(
                        "Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(String mensagem) {

        while (true) {

            System.out.print(mensagem);
            String linha = scanner.nextLine();

            try {
                return Double.parseDouble(
                        linha.trim().replace(",", "."));

            } catch (NumberFormatException e) {
                System.out.println(
                        "Digite um número válido.");
            }
        }
    }
}