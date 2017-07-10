package compadres.burgueria.app.compadres;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pedro Neto on 06/07/2017.
 */

public class PedidoAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final Pedido elementos;
    SharedPreferences pedidoPref;

    public PedidoAdapter(Context context, Pedido elementos){
        super(context,R.layout.pedido_linha,R.id.item_nome, toSimpleArrayList(elementos));
        this.context = context;
        this.elementos = elementos;
        pedidoPref = context.getSharedPreferences("Pedido",Context.MODE_PRIVATE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pedido_linha, parent,false);


        final Gson gson = new Gson();
        //String json = gson.toJson(pedido);
        final SharedPreferences.Editor ed = pedidoPref.edit();

        final TextView nomeItem = (TextView) rowView.findViewById(R.id.item_nome);
        final TextView precoItem = (TextView) rowView.findViewById(R.id.item_preco);
        ImageView imagemItem = (ImageView) rowView.findViewById(R.id.item_imagem);
        final TextView quantidade = (TextView) rowView.findViewById(R.id.quantidade_item);
        ImageButton addItem = (ImageButton) rowView.findViewById(R.id.qtd_add_item);
        ImageButton removeItem = (ImageButton) rowView.findViewById(R.id.qtd_remove_item);

        final ArrayList<Produto> produtos = new ArrayList<Produto>();
        final ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        for(String key:elementos.getProdutos().keySet()){
            ArrayList<String> subList = new ArrayList<String>();
            subList.add(elementos.getProdutos().get(key)+"");
            subList.add(elementos.getValores().get(key)+"");
            subList.add(key);
            list.add(subList);
        }

        quantidade.setText(list.get(position).get(0));
        precoItem.setText(Double.parseDouble(list.get(position).get(1))*Integer.parseInt(list.get(position).get(0))+"");
        nomeItem.setText(list.get(position).get(2));

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qtd = Integer.parseInt(quantidade.getText().toString());
                quantidade.setText(qtd+1+"");
                precoItem.setText((Double.parseDouble(list.get(position).get(1))*(qtd+1))+"");
                elementos.addCartItem(new Produto(nomeItem.getText().toString(),null,null,Float.parseFloat(list.get(position).get(1)),1),1);
                String json = gson.toJson(elementos);
                ed.putString("Pedido",json).commit();
                ((ListaProdutos)context).updateTotalValue();
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qtd = Integer.parseInt(quantidade.getText().toString());
                if(qtd>1){
                    quantidade.setText(qtd-1+"");
                    precoItem.setText((Double.parseDouble(list.get(position).get(1))*(qtd-1))+"");
                    elementos.removeCartItem(new Produto(nomeItem.getText().toString(),null,null,Float.parseFloat(list.get(position).get(1)),1),1);
                    String json = gson.toJson(elementos);
                    ed.putString("Pedido",json).commit();
                    ((ListaProdutos)context).updateTotalValue();
                } else {
                    elementos.removeCartItem(new Produto(nomeItem.getText().toString(),null,null,Float.parseFloat(list.get(position).get(1)),1),1);
                    String json = gson.toJson(elementos);
                    ed.putString("Pedido",json).commit();
                    ((ListaProdutos)context).updateTotalValue();
                    ((ListaProdutos)context).updateLista();
                }
            }
        });

        //Pedido vem em um Hash

        /*nomeItem.setText(elementos.get(position).getNome());
        descricaoBurguer.setText(elementos.get(position).getDescricao());
        precoBurguer.setText("R$ " + elementos.get(position).getPreco());
        imagemBurguer.setImageResource(elementos.get(position).getImagem());*/

        return rowView;
    }

    public static ArrayList<String> toSimpleArrayList(Pedido elementos){
        ArrayList<String> keys = new ArrayList<String>();
        for(String subList:elementos.getProdutos().keySet()){
            keys.add(subList);
        }
        return keys;
    }

}
