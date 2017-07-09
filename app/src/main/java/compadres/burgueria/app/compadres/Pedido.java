package compadres.burgueria.app.compadres;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by rafjo on 01/07/2017.
 */

public class Pedido {

    Date data_pedido;
    HashMap<String,Integer> produtos;
    HashMap<String,Float> valores;
    double valor;
    String status;

    Pedido(){}

    Pedido(Date data_pedido){
        this.data_pedido=data_pedido;
        this.produtos=new HashMap<String,Integer>();
        this.valores=new HashMap<String,Float>();
        valor=0;
        status = "Escolhendo Produtos";
    };

    public void addCartItem(Produto produto,int qt_item){
        if(!produtos.containsKey(produto.getNome())){
            produtos.put(produto.getNome(),qt_item);
            valores.put(produto.getNome(),produto.getPreco());
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
            valores.remove(produto.getNome());
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

    public HashMap<String,Integer> getProdutos(){
        return produtos;
    }

    public String getData_pedido() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(data_pedido);
    }

    public void setProdutos(HashMap<String,Integer> produtos){
        this.produtos=produtos;
    }

    public void setData_pedido(Date data_pedido){
        this.data_pedido=data_pedido;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public void setValor(Double valor){
        this.valor=valor;
    }

    public String getValor() {
        return valor+"";
    }

    public void setValores(HashMap<String,Float> valores) { this.valores=valores; }

    public HashMap<String,Float> getValores() { return valores; }

}
