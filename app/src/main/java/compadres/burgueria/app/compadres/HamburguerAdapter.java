package compadres.burgueria.app.compadres;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PEDRONETO on 20/06/2017.
 */

public class HamburguerAdapter extends ArrayAdapter<Hamburguer> {
    private final Context context;
    private final ArrayList<Hamburguer> elementos;

    public HamburguerAdapter(Context context, ArrayList<Hamburguer> elementos){
        super(context, R.layout.cardapio_linha, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cardapio_linha, parent,false);

        TextView nomeBurguer = (TextView) rowView.findViewById(R.id.burguer_nome);
        TextView descricaoBurguer = (TextView) rowView.findViewById(R.id.burguer_descricao);
        TextView precoBurguer = (TextView) rowView.findViewById(R.id.burguer_preco);
        ImageView imagemBurguer = (ImageView) rowView.findViewById(R.id.burguer_imagem);

        nomeBurguer.setText(elementos.get(position).getNome());
        descricaoBurguer.setText(elementos.get(position).getDescricao());
        precoBurguer.setText("R$ " + elementos.get(position).getPreco());
        imagemBurguer.setImageResource(elementos.get(position).getImagem());

        return rowView;
    }
}
