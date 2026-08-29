package model;


public class Roupa extends Produto {

    private static final long serialVersionUID = 1L;

    private String banda;
    private String tamanho;
    private String material;

    public Roupa(int codigo, String nome, double valor, int quantidadeEstoque,
                 String banda, String tamanho, String material) {
        super(codigo, nome, valor, quantidadeEstoque);
        this.banda = banda;
        this.tamanho = tamanho;
        this.material = material;
    }

    public String getBanda() {
        return banda;
    }

    public void setBanda(String banda) {
        this.banda = banda;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.ROUPA;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Banda: %s | Tamanho: %s | Material: %s",
                banda, tamanho, material);
    }
}
