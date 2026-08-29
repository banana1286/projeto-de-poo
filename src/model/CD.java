package model;

public class CD extends Midia {

    private static final long serialVersionUID = 1L;

    public CD(int codigo, String nome, double valor, int quantidadeEstoque,
              String artista, String gravadora, int anoLancamento) {
        super(codigo, nome, valor, quantidadeEstoque, artista, gravadora, anoLancamento);
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.CD;
    }
}
