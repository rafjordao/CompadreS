package compadres.burgueria.app.compadres;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.annotations.NotNull;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardapioFragment extends Fragment {

    ListView lista;
    ArrayAdapter adapter;

    public CardapioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cardapio, container, false);
        // linkar com o componente da view
        final BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        lista = (ListView) view.findViewById(R.id.lvCardapio);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_hamburgueres:
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Hamburgueres", Toast.LENGTH_SHORT).show();
                        final ArrayList<Produto> hamburguers = addHamburguer();
                        adapter = new ProdutoAdapter(getActivity(),hamburguers);
                        lista.setAdapter(adapter);
                        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), DetailProduto.class);
                                intent.putExtra("objeto", hamburguers.get(position));
                                startActivity(intent);
                            }
                        });
                        return true;
                    case R.id.navigation_bebida:
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Bebidas", Toast.LENGTH_SHORT).show();
                        //adapter = ArrayAdapter.createFromResource(getActivity(), R.array.bebidas, android.R.layout.simple_list_item_1);
                        //lista.setAdapter(adapter);
                        final ArrayList<Produto> bebidas = addBebida();
                        adapter = new ProdutoAdapter(getActivity(),bebidas);
                        lista.setAdapter(adapter);
                        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), DetailProduto.class);
                                intent.putExtra("objeto", bebidas.get(position));
                                startActivity(intent);
                            }
                        });
                        return true;
                }
                return false;
            }

        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //setAdapterHamburger (o codigo que ta no switch case acima - isolar em um metodo)
        final ArrayList<Produto> hamburguers = addHamburguer();
        adapter = new ProdutoAdapter(getActivity(),hamburguers);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailProduto.class);
                intent.putExtra("objeto", hamburguers.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    /*private ArrayList<Produto> addProduct(@NotNull final Callback callback, String type){

        final ArrayList<Produto> produtos = new ArrayList<Produto>();
        Query productQuery = ((MainActivity)getActivity()).mDatabase.getRef().child("produtos").child(type);

        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){

                    produtos.add(data.getValue(Produto.class));
                    Log.d(((MainActivity)getActivity()).TAG,data.toString());
                }

                for(Produto p:produtos){
                    Log.d(((MainActivity)getActivity()).TAG,p.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((MainActivity)getActivity()).showProgressDialog();
        while(produtos.size()==0){}
        ((MainActivity)getActivity()).hideProgressDialog();
        return produtos;
    }*/

    private ArrayList<Produto> addHamburguer(){
        ArrayList<Produto> burguers = new ArrayList<Produto>();
        Produto h = new Produto("Compadre'S","Hamburguer","pão, carne ,queijo, cebola caramelizada e molho do Compadre'S",7.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Produto("Comadre","Hamburguer","pão, carne, queijo, alface, tomate e molho do Compadre'S",7.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Produto("1% Vagabundo","Hamburguer","pão, frango, queijo, ovo, alface, tomate e molho opcional( bechamel ou Compadre'S)",8.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Produto("Gordelícia","Hamburguer","pão, carne, queijo, bacon, alface, tomate e molho opcional( barbecue ou Compadre'S)",9.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Produto("Gota Serena","Hamburguer","pão, carne, queijo, bacon, ovo, cebola caramelizada, alface, tomate e molho opcional( barbecue ou picante)",10.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Produto("Mái Meninu","Hamburguer","pão, carne, queijo, calabresa, alface, tomate, cebola e molho do opcional( barbecue ou Compadre'S)",9.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Produto("Danadin","Hamburguer","pão, hambúrguer de toscana mista, queijo, bacon, alface, tomate e molho opcional( maionese de limão ou Compadre'S)",10.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Produto("Arretado","Hamburguer","pão, carne, queijo coalho, charque desfiada, cebola e molho opcional( maionese de alho ou Compadre'S)",12.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Produto("Coisa pra Cinema","Hamburguer","pão, 2 carnes, queijo, bacon, cebola e molho opcional cheddar",13.00f,R.drawable.burguer_image);
        burguers.add(h);
        //return addProduct("Hamburguer");
        return burguers;
    }

    private ArrayList<Produto> addBebida(){
        ArrayList<Produto> bebidas = new ArrayList<Produto>();
        Produto b = new Produto("Coca-Cola","Bebida","350 ml",4.00f,R.drawable.bebida_image);
        bebidas.add(b);
        b = new Produto("Soda","Bebida","350 ml",4.00f,R.drawable.bebida_image);
        bebidas.add(b);
        //return addProduct("Bebida");
        return bebidas;
    }

}
