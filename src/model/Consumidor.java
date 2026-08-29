package model;


public class Consumidor extends Usuario {

    private static final long serialVersionUID = 1L;

    private final Carrinho carrinho;

    public Consumidor(String nome, String email, String cpf) {
        super(nome, email, cpf);
        this.carrinho = new Carrinho();
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }
}
