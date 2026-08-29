package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import exceptions.ArquivoException;


public class ArquivoCompra {

    private static final String CAMINHO_ARQUIVO = "compras.txt";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private ArquivoCompra() {
        // classe utilitária: não deve ser instanciada
    }


    public static void registrarCompra(String nomeConsumidor, String detalhesProdutos, double valorTotal)
            throws ArquivoException {
        String data = LocalDateTime.now().format(FORMATO_DATA);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {
            bw.write("COMPRA REALIZADA");
            bw.newLine();
            bw.write("Data: " + data);
            bw.newLine();
            bw.write("Consumidor: " + nomeConsumidor);
            bw.newLine();
            bw.write("Produtos:");
            bw.newLine();
            bw.write(detalhesProdutos);
            bw.write(String.format("Valor total: R$ %.2f", valorTotal));
            bw.newLine();

            bw.newLine();
            bw.newLine();
        } catch (IOException e) {
            throw new ArquivoException("Erro ao registrar a compra no arquivo: " + e.getMessage());
        }
    }
}
