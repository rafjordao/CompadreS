package compadres.burgueria.app.compadres;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


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
                        ArrayList<Hamburguer> hamburguers = addHamburguer();
                        adapter = new HamburguerAdapter(getActivity(),hamburguers);
                        lista.setAdapter(adapter);
                        return true;
                    case R.id.navigation_bebida:
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Bebidas", Toast.LENGTH_SHORT).show();
                        //adapter = ArrayAdapter.createFromResource(getActivity(), R.array.bebidas, android.R.layout.simple_list_item_1);
                        //lista.setAdapter(adapter);
                        ArrayList<Bebida> bebidas = addBebida();
                        adapter = new BebidaAdapter(getActivity(),bebidas);
                        lista.setAdapter(adapter);
                        return true;
                }
                return false;
            }

        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //setAdapterHamburger (o codigo que ta no switch case acima - isolar em um metodo)
        ArrayList<Hamburguer> hamburguers = addHamburguer();
        adapter = new HamburguerAdapter(getActivity(),hamburguers);
        lista.setAdapter(adapter);

        return view;
    }


    private ArrayList<Hamburguer> addHamburguer(){
        ArrayList<Hamburguer> burguers = new ArrayList<Hamburguer>();
        Hamburguer h = new Hamburguer("Compadre'S","pão, carne ,queijo, cebola caramelizada e molho do Compadre'S",7.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("Comadre","pão, carne, queijo, alface, tomate e molho do Compadre'S",7.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("1% Vagabundo","pão, frango, queijo, ovo, alface, tomate e molho opcional( bechamel ou Compadre'S)",8.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("Gordelícia","pão, carne, queijo, bacon, alface, tomate e molho opcional( barbecue ou Compadre'S)",9.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("Gota Serena","pão, carne, queijo, bacon, ovo, cebola caramelizada, alface, tomate e molho opcional( barbecue ou picante)",10.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("Mái Meninu","pão, carne, queijo, calabresa, alface, tomate, cebola e molho do opcional( barbecue ou Compadre'S)",9.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("Danadin","pão, hambúrguer de toscana mista, queijo, bacon, alface, tomate e molho opcional( maionese de limão ou Compadre'S)",10.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("Arretado","pão, carne, queijo coalho, charque desfiada, cebola e molho opcional( maionese de alho ou Compadre'S)",12.00f,R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("Coisa pra Cinema","pão, 2 carnes, queijo, bacon, cebola e molho opcional cheddar",13.00f,R.drawable.burguer_image);
        burguers.add(h);
        return burguers;
    }

    private ArrayList<Bebida> addBebida(){
        ArrayList<Bebida> bebidas = new ArrayList<Bebida>();
        Bebida b = new Bebida("Coca-Cola","350 ml",4.00f,R.drawable.bebida_image);
        bebidas.add(b);
        b = new Bebida("Soda","350 ml",4.00f,R.drawable.bebida_image);
        bebidas.add(b);
        return bebidas;
    }
}
