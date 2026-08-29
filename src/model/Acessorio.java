package model;


public class Acessorio extends Produto {

    private static final long serialVersionUID = 1L;

    private String tipo;
    private String marca;

    public Acessorio(int codigo, String nome, double valor, int quantidadeEstoque,
                      String tipo, String marca) {
        super(codigo, nome, valor, quantidadeEstoque);
        this.tipo = tipo;
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.ACESSORIO;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Tipo: %s | Marca: %s", tipo, marca);
    }
}
