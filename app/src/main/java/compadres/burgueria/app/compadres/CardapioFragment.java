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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardapioFragment extends Fragment {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_hamburgueres:
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Hamburgueres", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_bebida:
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Bebidas", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }

    };

    public CardapioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardapio, container, false);
        // linkar com o componente da view
        final BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // o contexto é obtido de forma diferente
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            }
        });

        return view;
    }

}
