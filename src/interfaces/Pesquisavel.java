package interfaces;

import java.util.List;
import model.Produto;


public interface Pesquisavel {


    List<Produto> pesquisar(String termo);
}
