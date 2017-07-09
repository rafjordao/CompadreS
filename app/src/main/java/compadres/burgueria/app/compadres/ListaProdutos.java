package compadres.burgueria.app.compadres;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ListaProdutos extends AppCompatActivity {

    ListView lista;
    Pedido pedido;
    SharedPreferences pedidoPref;
    SharedPreferences.Editor prefEditor;
    PedidoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        ListView lista = (ListView) findViewById(R.id.lvListaProduto);

        pedidoPref = getSharedPreferences("Pedido",MODE_PRIVATE);
        prefEditor = pedidoPref.edit();
        Gson gson = new Gson();
        if(!pedidoPref.getString("Pedido","").isEmpty()){
            String json = pedidoPref.getString("Pedido","");
            pedido = gson.fromJson(json,Pedido.class);
        }
        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
        pedidos.add(pedido);

        adapter = new PedidoAdapter(this,pedidos);
        lista.setAdapter(adapter);
        /*ArrayList<Produto> produtos;
        ArrayAdapter<Produto> adapter = new ProdutoAdapter(this,produtos);
        lista.setAdapter(adapter);*/

        //Chamar o adapter

    }
}
