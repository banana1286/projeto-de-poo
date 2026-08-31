//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package util;

import exceptions.ArquivoException;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import model.Produto;

public class ArquivoProduto {
    private static final String CAMINHO_ARQUIVO = "produtos.dat";

    private ArquivoProduto() {
    }

    public static Map<Integer, Produto> carregar() throws ArquivoException {
        File arquivo = new File("produtos.dat");
        Map<Integer, Produto> produtos = new HashMap();
        if (!arquivo.exists()) {
            return produtos;
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                Object objeto = ois.readObject();
                if (objeto instanceof Map) {
                    Map<?, ?> mapaLido = (Map)objeto;
                    produtos = mapaLido;
                }
            } catch (EOFException var7) {
            } catch (ClassNotFoundException | IOException e) {
                throw new ArquivoException("Erro ao carregar o arquivo de produtos: " + ((Exception)e).getMessage());
            }

            return produtos;
        }
    }

    public static void salvar(Map<Integer, Produto> produtos) throws ArquivoException {
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("produtos.dat"))) {
                oos.writeObject(produtos);
            }

        } catch (IOException e) {
            throw new ArquivoException("Erro ao salvar o arquivo de produtos: " + e.getMessage());
        }
    }
}
