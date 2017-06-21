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

public class BebidaAdapter extends ArrayAdapter<Bebida> {
    private final Context context;
    private final ArrayList<Bebida> elementos;

    public BebidaAdapter(Context context, ArrayList<Bebida> elementos){
        super(context, R.layout.bebida_linha, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.bebida_linha, parent,false);

        TextView nomeBebida = (TextView) rowView.findViewById(R.id.bebida_nome);
        TextView descricaoBebida = (TextView) rowView.findViewById(R.id.bebida_descricao);
        TextView precoBebida = (TextView) rowView.findViewById(R.id.bebida_preco);
        ImageView imagemBebida = (ImageView) rowView.findViewById(R.id.bebida_imagem);

        nomeBebida.setText(elementos.get(position).getNome());
        descricaoBebida.setText(elementos.get(position).getDescricao());
        precoBebida.setText("R$ " + elementos.get(position).getPreco());
        imagemBebida.setImageResource(elementos.get(position).getImagem());

        return rowView;
    }
}
