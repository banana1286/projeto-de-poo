//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package controller;

import exceptions.ArquivoException;
import exceptions.EstoqueInsuficienteException;
import exceptions.ProdutoDuplicadoException;
import exceptions.ProdutoNaoEncontradoException;
import interfaces.Pesquisavel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Carrinho;
import model.Categoria;
import model.Consumidor;
import model.ItemCarrinho;
import model.Produto;
import util.ArquivoCompra;
import util.ArquivoProduto;

public class LojaController implements Pesquisavel {
    private Map<Integer, Produto> produtos = new HashMap();

    public LojaController() {
    }

    public void cadastrarProduto(Produto produto) throws ProdutoDuplicadoException {
        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        } else if (this.produtos.containsKey(produto.getCodigo())) {
            throw new ProdutoDuplicadoException("Já existe um produto cadastrado com o código " + produto.getCodigo() + ".");
        } else {
            this.produtos.put(produto.getCodigo(), produto);
        }
    }

    public void excluirProduto(int codigo) throws ProdutoNaoEncontradoException {
        this.buscarPorCodigo(codigo);
        this.produtos.remove(codigo);
    }

    public Produto buscarPorCodigo(int codigo) throws ProdutoNaoEncontradoException {
        Produto produto = (Produto)this.produtos.get(codigo);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto com código " + codigo + " não encontrado.");
        } else {
            return produto;
        }
    }

    public void alterarNome(int codigo, String novoNome) throws ProdutoNaoEncontradoException {
        this.buscarPorCodigo(codigo).setNome(novoNome);
    }

    public void alterarPreco(int codigo, double novoValor) throws ProdutoNaoEncontradoException {
        this.buscarPorCodigo(codigo).setValor(novoValor);
    }

    public void alterarEstoque(int codigo, int novaQuantidade) throws ProdutoNaoEncontradoException {
        this.buscarPorCodigo(codigo).setQuantidadeEstoque(novaQuantidade);
    }

    public List<Produto> pesquisar(String termo) {
        List<Produto> resultado = new ArrayList();
        if (termo != null && !termo.isBlank()) {
            String busca = termo.toLowerCase().trim();

            for(Produto produto : this.produtos.values()) {
                String nome = produto.getNome().toLowerCase();
                if (nome.contains(busca)) {
                    resultado.add(produto);
                }
            }

            return resultado;
        } else {
            return resultado;
        }
    }

    public List<Produto> listarProdutos() {
        return new ArrayList(this.produtos.values());
    }

    public List<Produto> listarPorCategoria(Categoria categoria) {
        List<Produto> resultado = new ArrayList();

        for(Produto produto : this.produtos.values()) {
            if (produto.getCategoria() == categoria) {
                resultado.add(produto);
            }
        }

        return resultado;
    }

    public void realizarCompra(Consumidor consumidor, Carrinho carrinho) throws EstoqueInsuficienteException, ArquivoException {
        if (carrinho != null && !carrinho.estaVazio()) {
            for(ItemCarrinho item : carrinho.listarItens()) {
                if (!item.getProduto().possuiEstoqueSuficiente(item.getQuantidade())) {
                    throw new EstoqueInsuficienteException("Estoque insuficiente para \"" + item.getProduto().getNome() + "\".");
                }
            }

            StringBuilder detalhes = new StringBuilder();

            for(ItemCarrinho item : carrinho.listarItens()) {
                detalhes.append(String.format(" - %s (x%d) - R$ %.2f%n", item.getProduto().getNome(), item.getQuantidade(), item.getSubtotal()));
            }

            double total = carrinho.calcularTotal();
            ArquivoCompra.registrarCompra(consumidor.getNome(), detalhes.toString(), total);

            for(ItemCarrinho item : carrinho.listarItens()) {
                item.getProduto().reduzirEstoque(item.getQuantidade());
            }

            this.salvarProdutos();
            carrinho.limparCarrinho();
        }
    }

    public void salvarProdutos() throws ArquivoException {
        ArquivoProduto.salvar(this.produtos);
    }

    public void carregarProdutos() throws ArquivoException {
        this.produtos = ArquivoProduto.carregar();
    }
}

}
