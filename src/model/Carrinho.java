package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exceptions.EstoqueInsuficienteException;

public class Carrinho implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<ItemCarrinho> itens = new ArrayList<>();

    public void adicionarProduto(Produto produto, int quantidade)
            throws EstoqueInsuficienteException {

        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        for (ItemCarrinho item : itens) {
            if (item.getProduto().getCodigo() == produto.getCodigo()) {

                int novaQuantidade = item.getQuantidade() + quantidade;

                if (!produto.possuiEstoqueSuficiente(novaQuantidade)) {
                    throw new EstoqueInsuficienteException(
                            "Estoque insuficiente para adicionar mais unidades de \""
                                    + produto.getNome() + "\".");
                }

                item.setQuantidade(novaQuantidade);
                return;
            }
        }

        if (!produto.possuiEstoqueSuficiente(quantidade)) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para adicionar \""
                            + produto.getNome() + "\" ao carrinho.");
        }

        itens.add(new ItemCarrinho(produto, quantidade));
    }

    public boolean removerProduto(int codigo) {
        return itens.removeIf(
                item -> item.getProduto().getCodigo() == codigo);
    }

    public List<ItemCarrinho> listarItens() {
        return new ArrayList<>(itens);
    }

    public double calcularTotal() {
        double total = 0;

        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }

        return total;
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public void limparCarrinho() {
        itens.clear();
    }
}