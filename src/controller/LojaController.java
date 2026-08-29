package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.similarity.LevenshteinDistance;

import exceptions.ArquivoException;
import exceptions.EstoqueInsuficienteException;
import exceptions.ProdutoDuplicadoException;
import exceptions.ProdutoNaoEncontradoException;
import interfaces.Pesquisavel;
import model.Carrinho;
import model.Categoria;
import model.Consumidor;
import model.ItemCarrinho;
import model.Produto;
import util.ArquivoCompra;
import util.ArquivoProduto;

public class LojaController implements Pesquisavel {

    private Map<Integer, Produto> produtos;

    public LojaController() {
        produtos = new HashMap<>();
    }

    public void cadastrarProduto(Produto produto)
            throws ProdutoDuplicadoException {

        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }

        if (produtos.containsKey(produto.getCodigo())) {
            throw new ProdutoDuplicadoException(
                    "Já existe um produto cadastrado com o código "
                            + produto.getCodigo() + ".");
        }

        produtos.put(produto.getCodigo(), produto);
    }

    public void excluirProduto(int codigo)
            throws ProdutoNaoEncontradoException {

        buscarPorCodigo(codigo);
        produtos.remove(codigo);
    }

    public Produto buscarPorCodigo(int codigo)
            throws ProdutoNaoEncontradoException {

        Produto produto = produtos.get(codigo);

        if (produto == null) {
            throw new ProdutoNaoEncontradoException(
                    "Produto com código " + codigo + " não encontrado.");
        }

        return produto;
    }

    public void alterarNome(int codigo, String novoNome)
            throws ProdutoNaoEncontradoException {

        buscarPorCodigo(codigo).setNome(novoNome);
    }

    public void alterarPreco(int codigo, double novoValor)
            throws ProdutoNaoEncontradoException {

        buscarPorCodigo(codigo).setValor(novoValor);
    }

    public void alterarEstoque(int codigo, int novaQuantidade)
            throws ProdutoNaoEncontradoException {

        buscarPorCodigo(codigo).setQuantidadeEstoque(novaQuantidade);
    }

    @Override
    public List<Produto> pesquisar(String termo) {

        List<Produto> resultado = new ArrayList<>();

        if (termo == null || termo.isBlank()) {
            return resultado;
        }

        String busca = termo.toLowerCase().trim();
        LevenshteinDistance distancia = new LevenshteinDistance(2);

        for (Produto produto : produtos.values()) {

            String nome = produto.getNome().toLowerCase();

            if (nome.contains(busca)) {
                resultado.add(produto);
                continue;
            }

            String[] palavras = nome.split("\\s+");

            for (String palavra : palavras) {
                if (distancia.apply(busca, palavra) != -1) {
                    resultado.add(produto);
                    break;
                }
            }
        }

        return resultado;
    }

    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos.values());
    }

    public List<Produto> listarPorCategoria(Categoria categoria) {

        List<Produto> resultado = new ArrayList<>();

        for (Produto produto : produtos.values()) {
            if (produto.getCategoria() == categoria) {
                resultado.add(produto);
            }
        }

        return resultado;
    }

    public void realizarCompra(Consumidor consumidor, Carrinho carrinho)
            throws EstoqueInsuficienteException, ArquivoException {

        if (carrinho == null || carrinho.estaVazio()) {
            return;
        }

        for (ItemCarrinho item : carrinho.listarItens()) {

            if (!item.getProduto()
                    .possuiEstoqueSuficiente(item.getQuantidade())) {

                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para \""
                                + item.getProduto().getNome() + "\".");
            }
        }

        StringBuilder detalhes = new StringBuilder();

        for (ItemCarrinho item : carrinho.listarItens()) {
            detalhes.append(String.format(
                    " - %s (x%d) - R$ %.2f%n",
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getSubtotal()
            ));
        }

        double total = carrinho.calcularTotal();

        ArquivoCompra.registrarCompra(
                consumidor.getNome(),
                detalhes.toString(),
                total
        );

        for (ItemCarrinho item : carrinho.listarItens()) {
            item.getProduto().reduzirEstoque(item.getQuantidade());
        }

        salvarProdutos();
        carrinho.limparCarrinho();
    }

    public void salvarProdutos() throws ArquivoException {
        ArquivoProduto.salvar(produtos);
    }

    public void carregarProdutos() throws ArquivoException {
        produtos = ArquivoProduto.carregar();
    }
}