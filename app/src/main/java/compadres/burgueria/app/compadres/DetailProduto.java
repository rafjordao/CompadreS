package compadres.burgueria.app.compadres;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Date;



public class DetailProduto extends AppCompatActivity {

    SharedPreferences pedidoPref;
    Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produto);

        pedidoPref = getSharedPreferences("Pedido",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pedidoPref.edit();
        Gson gson = new Gson();
        if(!pedidoPref.getString("Pedido","").isEmpty()){
            String json = pedidoPref.getString("Pedido","");
            pedido = gson.fromJson(json,Pedido.class);
        }

        Intent intent = getIntent();
        final Produto objeto = (Produto) intent.getSerializableExtra("objeto");

        TextView nome = (TextView) findViewById(R.id.detailNome);
        TextView descricao = (TextView) findViewById(R.id.detailDescricao);
        final TextView preco = (TextView) findViewById(R.id.detailPreco);
        ImageView imagem = (ImageView) findViewById(R.id.detailImagem);
        final TextView quantidade = (TextView) findViewById(R.id.quantidade);
        final Button btnAddCarrinho = (Button) findViewById(R.id.btnAddCarrinho);
        ImageButton qtdAdd = (ImageButton) findViewById(R.id.qtd_add);
        ImageButton qtdRemove = (ImageButton) findViewById(R.id.qtd_remove);

        nome.setText(objeto.getNome());
        descricao.setText(objeto.getDescricao());
        preco.setText(""+objeto.getPreco());
        imagem.setImageResource(objeto.getImagem());

        qtdAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qtd = Integer.parseInt(quantidade.getText().toString());
                quantidade.setText(qtd+1+"");
                preco.setText((objeto.getPreco()*(qtd+1))+"");
            }
        });

        qtdRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qtd = Integer.parseInt(quantidade.getText().toString());
                if(qtd>1){
                    quantidade.setText(qtd-1+"");
                    preco.setText((objeto.getPreco()*(qtd-1))+"");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "A quantidade mínima possível é 1", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnAddCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                if(mUser != null){
                    if(pedido == null){
                        pedido = new Pedido(new Date());
                        Log.d("CompadreS","ESSA PORRA TÁ NULA CARALHO!");
                    }
                    addProdutoCarrinho(objeto,Integer.parseInt(quantidade.getText().toString()));
                    DatabaseReference pendingCart = FirebaseDatabase.getInstance().getReference().child("user").child(mUser.getUid());
                    pendingCart.child("pedido_pendente").setValue(MainActivity.pedido);
                    Gson gson = new Gson();
                    String json = gson.toJson(pedido);
                    SharedPreferences.Editor ed = pedidoPref.edit();
                    ed.putString("Pedido",json).commit();
                    Log.d("CompadreS",json);
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Você precisa está logado para adicionar um item ao seu Carrinho", Toast.LENGTH_LONG).show();
                }
                finish();
            }
         });


    }

    private void addProdutoCarrinho(Produto objeto, int quantidade){
        pedido.addCartItem(objeto, quantidade);
        Toast.makeText(getApplicationContext(),
                objeto.getNome()+" adicionado ao Carrinho", Toast.LENGTH_SHORT).show();
        Log.d("CompadreS",pedido.getValor());
    }


}

