package compadres.burgueria.app.compadres;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class ListaProdutos extends AppCompatActivity {

    Pedido pedido;
    SharedPreferences pedidoPref;
    SharedPreferences.Editor prefEditor;
    PedidoAdapter adapter;
    TextView valorTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        ListView lista = (ListView) findViewById(R.id.lvListaProduto);
        valorTotal = (TextView) findViewById(R.id.total_pedido);
        Button finishCartButton = (Button) findViewById(R.id.finalizarCarrinho);

        pedidoPref = getSharedPreferences("Pedido",MODE_PRIVATE);
        prefEditor = pedidoPref.edit();
        Gson gson = new Gson();
        if(!pedidoPref.getString("Pedido","").isEmpty()){
            String json = pedidoPref.getString("Pedido","");
            pedido = gson.fromJson(json,Pedido.class);
        }


        valorTotal.setText(pedido.getValor());

        adapter = new PedidoAdapter(this,pedido);
        lista.setAdapter(adapter);

        finishCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFinishDialog();
            }
        });

    }

    public void updateTotalValue(){
        Gson gson = new Gson();
        if(!pedidoPref.getString("Pedido","").isEmpty()){
            String json = pedidoPref.getString("Pedido","");
            pedido = gson.fromJson(json,Pedido.class);
        }
        valorTotal.setText(pedido.getValor());

    }

    public void showFinishDialog(){
        FinishDialog fd = new FinishDialog();
        fd.show(getSupportFragmentManager(),"finishDialog");
    }

    public void turnOffFinishDialog() {
        FinishDialog fd = (FinishDialog) getSupportFragmentManager().findFragmentByTag("finishDialog");
        if(fd!=null){
            fd.dismiss();
            getSupportFragmentManager().beginTransaction().remove(fd);
        }
    }
}
