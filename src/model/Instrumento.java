package model;


public class Instrumento extends Produto {

    private static final long serialVersionUID = 1L;

    private String marca;
    private String modelo;
    private String material;

    public Instrumento(int codigo, String nome, double valor, int quantidadeEstoque,
                        String marca, String modelo, String material) {
        super(codigo, nome, valor, quantidadeEstoque);
        this.marca = marca;
        this.modelo = modelo;
        this.material = material;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.INSTRUMENTO;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Marca: %s | Modelo: %s | Material: %s",
                marca, modelo, material);
    }
}
