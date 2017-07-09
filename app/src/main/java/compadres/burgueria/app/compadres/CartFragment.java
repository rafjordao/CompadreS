package compadres.burgueria.app.compadres;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rafjo on 09/07/2017.
 */

public class CartFragment extends Fragment {

    ListView lista;
    Pedido pedido;
    SharedPreferences pedidoPref;
    Editor prefEditor;
    PedidoAdapter adapter;


    public CartFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_produtos,container,false);

        lista = (ListView) view.findViewById(R.id.lvListaProduto);
        pedidoPref = ((MainActivity)getActivity()).getSharedPreferences("Pedido",MODE_PRIVATE);
        prefEditor = pedidoPref.edit();
        Gson gson = new Gson();
        if(!pedidoPref.getString("Pedido","").isEmpty()){
            String json = pedidoPref.getString("Pedido","");
            pedido = gson.fromJson(json,Pedido.class);
        }

        adapter = new PedidoAdapter(getActivity(),pedido);
        lista.setAdapter(adapter);
        /*lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailProduto.class);
                intent.putExtra("objeto", hamburguers.get(position));
                startActivity(intent);*/

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
