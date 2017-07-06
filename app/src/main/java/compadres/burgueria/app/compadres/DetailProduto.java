package compadres.burgueria.app.compadres;

import android.app.Activity;
import android.content.Intent;
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

import java.util.Date;

public class DetailProduto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produto);

        Intent intent = getIntent();
        final Produto objeto = (Produto) intent.getSerializableExtra("objeto");

        TextView nome = (TextView) findViewById(R.id.detailNome);
        TextView descricao = (TextView) findViewById(R.id.detailDescricao);
        TextView preco = (TextView) findViewById(R.id.detailPreco);
        ImageView imagem = (ImageView) findViewById(R.id.detailImagem);
        final TextView quantidade = (TextView) findViewById(R.id.quantidade);
        final Button btnAddCarrinho = (Button) findViewById(R.id.btnAddCarrinho);
        ImageButton qtdAdd = (ImageButton) findViewById(R.id.qtd_add);
        ImageButton qtdRemove = (ImageButton) findViewById(R.id.qtd_remove);

        nome.setText(objeto.getNome());
        descricao.setText(objeto.getDescricao());
        preco.setText(""+objeto.getPreco());
        imagem.setImageResource(objeto.getImagem());

        btnAddCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.pedido == null){
                    MainActivity.pedido = new Pedido(new Date());
                }
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                if(mUser != null){
                    addProdutoCarrinho(objeto,Integer.parseInt(quantidade.getText().toString()));
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
        MainActivity.pedido.addCartItem(objeto, quantidade);
        Toast.makeText(getApplicationContext(),
                objeto.getNome()+" adicionado ao Carrinho", Toast.LENGTH_SHORT).show();
        Log.d("Compadres",MainActivity.pedido.getValor());
    }


}

