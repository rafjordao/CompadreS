package compadres.burgueria.app.compadres;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by rafjo on 09/07/2017.
 */

public class FinishDialog extends DialogFragment {

    Button gpsButton;
    EditText rua;
    EditText bairro;
    EditText cidade;
    EditText numero;
    EditText cep;
    Spinner metodoDePagamento;
    Button btnFinishOk;
    Button btnFinishCancel;

    Geocoder geocoder;
    List<Address> addresses;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.finish_dialog,container);

        geocoder = new Geocoder(((ListaProdutos)getActivity()), Locale.getDefault());

        gpsButton = (Button) view.findViewById(R.id.gpsButton);
        rua = (EditText) view.findViewById(R.id.rua);
        bairro = (EditText) view.findViewById(R.id.bairro);
        cidade = (EditText) view.findViewById(R.id.cidade);
        numero = (EditText) view.findViewById(R.id.numero);
        cep = (EditText) view.findViewById(R.id.cep);
        metodoDePagamento = (Spinner) view.findViewById(R.id.metodoDePagamento);
        btnFinishOk = (Button) view.findViewById(R.id.btnFinishOk);
        btnFinishCancel = (Button) view.findViewById(R.id.btnFinishCancel);

        String[] metodos = new String[] {"Cartão de Crédito","Dinheiro"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((ListaProdutos)getActivity()),
                android.R.layout.simple_spinner_item, metodos);

        metodoDePagamento.setAdapter(adapter);

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListaProdutos)getActivity()).getLocation();
                Log.d("CompadreS","Lat:"+((ListaProdutos)getActivity()).latitude+" Long:"+((ListaProdutos)getActivity()).longitude);
                try {
                    addresses = geocoder.getFromLocation(((ListaProdutos)getActivity()).latitude,((ListaProdutos)getActivity()).longitude,1);
                    /*rua.setText(addresses.get(0).getAddressLine(0));
                    bairro.setText(addresses.get(0).getSubLocality());
                    cidade.setText(addresses.get(0).getLocality());
                    estado.setText(addresses.get(0).getAdminArea());
                    cep.setText(addresses.get(0).getPostalCode());*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnFinishCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListaProdutos)getActivity()).turnOffFinishDialog("");
            }
        });

        btnFinishOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateAdress()){
                    return;
                } else {
                    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference finishCart = FirebaseDatabase.getInstance().getReference().child("user").child(mUser.getUid());
                    ((ListaProdutos) getActivity()).pedido.setData_pedido(new Date());
                    ((ListaProdutos) getActivity()).pedido.setStatus("Pedido Feito");
                    finishCart.child("pedidos").child(((ListaProdutos) getActivity()).pedido.getData_pedido()).setValue(((ListaProdutos) getActivity()).pedido);
                    finishCart.child("pedidos").child(((ListaProdutos) getActivity()).pedido.getData_pedido()).child("endereço").child("rua").setValue(rua.getText().toString());
                    finishCart.child("pedidos").child(((ListaProdutos) getActivity()).pedido.getData_pedido()).child("endereço").child("bairro").setValue(bairro.getText().toString());
                    finishCart.child("pedidos").child(((ListaProdutos) getActivity()).pedido.getData_pedido()).child("endereço").child("cidade").setValue(cidade.getText().toString());
                    finishCart.child("pedidos").child(((ListaProdutos) getActivity()).pedido.getData_pedido()).child("endereço").child("numero").setValue(numero.getText().toString());
                    finishCart.child("pedidos").child(((ListaProdutos) getActivity()).pedido.getData_pedido()).child("endereço").child("cep").setValue(cep.getText().toString());
                    finishCart.child("pedidos").child(((ListaProdutos) getActivity()).pedido.getData_pedido()).child("modoPagamento").setValue(metodoDePagamento.getSelectedItem().toString());
                    finishCart.child("pedido_pendente").removeValue();
                    ((ListaProdutos) getActivity()).prefEditor.putString("Pedido", "").commit();
                    ((ListaProdutos) getActivity()).pedido=null;
                    Toast.makeText(((ListaProdutos)getActivity()),"Pedido Feito com Sucesso!",Toast.LENGTH_LONG).show();
                    ((ListaProdutos)getActivity()).turnOffFinishDialog("success");
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().setLayout((width*9)/10, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private boolean validateAdress() {
        boolean valid = true;

        String casaRua = rua.getText().toString();
        if (TextUtils.isEmpty(casaRua)) {
            rua.setError("Required.");
            valid = false;
        } else {
            rua.setError(null);
        }

        String casaBairro = bairro.getText().toString();
        if (TextUtils.isEmpty(casaBairro)) {
            bairro.setError("Required.");
            valid = false;
        } else {
            bairro.setError(null);
        }

        String casaCidade = cidade.getText().toString();
        if (TextUtils.isEmpty(casaCidade)) {
            cidade.setError("Required.");
            valid = false;
        } else {
            cidade.setError(null);
        }

        String casaNumero = numero.getText().toString();
        if (TextUtils.isEmpty(casaNumero)) {
            numero.setError("Required.");
            valid = false;
        } else {
            numero.setError(null);
        }

        String casaCep = cep.getText().toString();
        if (TextUtils.isEmpty(casaCep)) {
            cep.setError("Required.");
            valid = false;
        } else {
            cep.setError(null);
        }

        return valid;
    }


}
