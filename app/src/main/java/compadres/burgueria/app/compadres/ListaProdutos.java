package compadres.burgueria.app.compadres;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaProdutos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        ListView lista = (ListView) findViewById(R.id.lvListaProduto);
        /*ArrayList<Produto> produtos;
        ArrayAdapter<Produto> adapter = new ProdutoAdapter(this,produtos);
        lista.setAdapter(adapter);*/

        //Chamar o adapter

    }
}