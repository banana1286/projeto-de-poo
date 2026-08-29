package util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import exceptions.ArquivoException;
import model.Produto;


public class ArquivoProduto {

    private static final String CAMINHO_ARQUIVO = "produtos.dat";

    private ArquivoProduto() {
        // classe utilitária: não deve ser instanciada
    }


    @SuppressWarnings("unchecked")
    public static Map<Integer, Produto> carregar() throws ArquivoException {
        File arquivo = new File(CAMINHO_ARQUIVO);
        Map<Integer, Produto> produtos = new HashMap<>();

        if (!arquivo.exists()) {
            return produtos;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object objeto = ois.readObject();
            if (objeto instanceof Map<?, ?> mapaLido) {
                produtos = (Map<Integer, Produto>) mapaLido;
            }
        } catch (EOFException e) {
            // arquivo vazio, mantém o mapa vazio
        } catch (IOException | ClassNotFoundException e) {
            throw new ArquivoException("Erro ao carregar o arquivo de produtos: " + e.getMessage());
        }

        return produtos;
    }


    public static void salvar(Map<Integer, Produto> produtos) throws ArquivoException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CAMINHO_ARQUIVO))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            throw new ArquivoException("Erro ao salvar o arquivo de produtos: " + e.getMessage());
        }
    }
}
