package model;


public class DVD extends Midia {

    private static final long serialVersionUID = 1L;

    public DVD(int codigo, String nome, double valor, int quantidadeEstoque,
               String artista, String gravadora, int anoLancamento) {
        super(codigo, nome, valor, quantidadeEstoque, artista, gravadora, anoLancamento);
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.DVD;
    }
}
