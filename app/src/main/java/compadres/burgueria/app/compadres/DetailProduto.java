package compadres.burgueria.app.compadres;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView quantidade = (TextView) findViewById(R.id.quantidade);
        Button btnAddCarrinho = (Button) findViewById(R.id.btnAddCarrinho);
        ImageButton qtdAdd = (ImageButton) findViewById(R.id.qtd_add);
        ImageButton qtdRemove = (ImageButton) findViewById(R.id.qtd_remove);

        nome.setText(objeto.getNome());
        descricao.setText(objeto.getDescricao());
        preco.setText(""+objeto.getPreco());
        imagem.setImageResource(objeto.getImagem());

        btnAddCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProduto.this, ListaProdutos.class);
                intent.putExtra("item", objeto);
                startActivity(intent);
            }
        });


    }
}

