package compadres.burgueria.app.compadres;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by rafjo on 01/07/2017.
 */

public class Pedido {

    Date data_pedido;
    HashMap<String,Integer> produtos;
    double valor;

    Pedido(Date data_pedido){
        this.data_pedido=data_pedido;
        this.produtos=new HashMap<String,Integer>();
        valor=0;
    };

    public void addCartItem(Produto produto,int qt_item){
        if(!produtos.containsKey(produto.getNome())){
            produtos.put(produto.getNome(),qt_item);
        } else{
            produtos.put(produto.getNome(),produtos.get(produto.getNome())+qt_item);
        }
        addValue(produto.getPreco(),qt_item);
    }

    public void removeCartItem(Produto produto,int qt_item){
        if(!produtos.containsKey(produto.getNome())){

        } else if(produtos.get(produto.getNome())-qt_item>0){
            produtos.put(produto.getNome(),produtos.get(produto.getNome())-qt_item);
        } else{
            qt_item=produtos.get(produto.getNome());
            produtos.remove(produto.getNome());
        }
        removeValue(produto.getPreco(),qt_item);
    }

    private void removeValue(double price, int qt_item){
        if(valor-(price*qt_item)>0){
            valor-=price*qt_item;
        } else {
            valor=0;
        }
    }

    private void addValue(double price, int qt_item){
        valor+=price*qt_item;
    }

    private HashMap<String,Integer> getProdutos(){
        return produtos;
    }

    public String getData_pedido() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(data_pedido);
    }

    public double getValor() {
        return valor;
    }

}
