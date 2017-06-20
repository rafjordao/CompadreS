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
        navigation.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // o contexto é obtido de forma diferente
                BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                        = new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_hamburgueres:
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Hamburgueres", Toast.LENGTH_SHORT).show();
                                lista = (ListView) view.findViewById(R.id.lvCardapio);
                                ArrayList<Hamburguer> hamburguers = addHamburguer();
                                adapter = new HamburguerAdapter(getActivity(),hamburguers);
                                lista.setAdapter(adapter);
                                return true;
                            case R.id.navigation_bebida:
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Bebidas", Toast.LENGTH_SHORT).show();
                                lista = (ListView) view.findViewById(R.id.lvCardapio);
                                adapter = ArrayAdapter.createFromResource(getActivity(), R.array.bebidas, android.R.layout.simple_list_item_1);
                                lista.setAdapter(adapter);
                                return true;
                        }
                        return false;
                    }

                };

                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            }
        });

        return view;
    }


    private ArrayList<Hamburguer> addHamburguer(){
        ArrayList<Hamburguer> burguers = new ArrayList<Hamburguer>();
        Hamburguer h = new Hamburguer("Compadre","pão,hambuguer,queijo,cebola caramelizada e molho",R.drawable.burguer_image);
        burguers.add(h);
        h = new Hamburguer("Comadre","pão,hambuguer,queijo,tomate e molho",R.drawable.burguer_image);
        burguers.add(h);
        return burguers;
    }
}
