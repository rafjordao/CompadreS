package compadres.burgueria.app.compadres;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pedro Neto on 06/07/2017.
 */

public class PedidoAdapter extends ArrayAdapter<Pedido> {
    private final Context context;
    private final ArrayList<Pedido> elementos;

    public PedidoAdapter(Context context, ArrayList<Pedido> elementos){
        super(context,R.layout.pedido_linha, (ArrayList)elementos.get(0).getProdutos().keySet());
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pedido_linha, parent,false);

        TextView nomeItem = (TextView) rowView.findViewById(R.id.item_nome);
        TextView precoItem = (TextView) rowView.findViewById(R.id.item_preco);
        ImageView imagemItem = (ImageView) rowView.findViewById(R.id.item_imagem);
        TextView quantidade = (TextView) rowView.findViewById(R.id.quantidade_item);
        ImageButton addItem = (ImageButton) rowView.findViewById(R.id.qtd_add_item);
        ImageButton removeItem = (ImageButton) rowView.findViewById(R.id.qtd_remove_item);

        nomeItem.setText(elementos.get(0).getProdutos().keySet().toArray()[position].toString());
        precoItem.setText(elementos.get(0).getValores().values().toArray()[position].toString());
        quantidade.setText(elementos.get(0).getProdutos().values().toArray()[position].toString());


        //Pedido vem em um Hash

        /*nomeItem.setText(elementos.get(position).getNome());
        descricaoBurguer.setText(elementos.get(position).getDescricao());
        precoBurguer.setText("R$ " + elementos.get(position).getPreco());
        imagemBurguer.setImageResource(elementos.get(position).getImagem());*/

        return rowView;
    }
}
