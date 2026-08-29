package model;


public abstract class Midia extends Produto {

    private static final long serialVersionUID = 1L;

    private String artista;
    private String gravadora;
    private int anoLancamento;

    public Midia(int codigo, String nome, double valor, int quantidadeEstoque,
                 String artista, String gravadora, int anoLancamento) {
        super(codigo, nome, valor, quantidadeEstoque);
        this.artista = artista;
        this.gravadora = gravadora;
        this.anoLancamento = anoLancamento;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getGravadora() {
        return gravadora;
    }

    public void setGravadora(String gravadora) {
        this.gravadora = gravadora;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Artista: %s | Gravadora: %s | Ano: %d",
                artista, gravadora, anoLancamento);
    }
}
