package model;

import java.io.Serializable;
import java.util.Objects;

import exceptions.EstoqueInsuficienteException;

public abstract class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int codigo;
    private String nome;
    private double valor;
    private int quantidadeEstoque;

    public Produto(int codigo, String nome, double valor, int quantidadeEstoque) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("O código deve ser maior que zero.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto não pode estar vazio.");
        }

        if (valor < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }

        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.valor = valor;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome não pode estar vazio.");
        }

        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }

        this.valor = valor;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }

        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void aumentarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        quantidadeEstoque += quantidade;
    }

    public void reduzirEstoque(int quantidade) throws EstoqueInsuficienteException {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (!possuiEstoqueSuficiente(quantidade)) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para o produto \"" + nome
                            + "\". Disponível: " + quantidadeEstoque
                            + ", solicitado: " + quantidade);
        }

        quantidadeEstoque -= quantidade;
    }

    public boolean possuiEstoqueSuficiente(int quantidade) {
        return quantidade > 0 && quantidadeEstoque >= quantidade;
    }

    public abstract Categoria getCategoria();

    @Override
    public String toString() {
        return String.format(
                "Código: %d | Nome: %s | Valor: R$ %.2f | Estoque: %d | Categoria: %s",
                codigo, nome, valor, quantidadeEstoque, getCategoria());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Produto produto = (Produto) o;
        return codigo == produto.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}