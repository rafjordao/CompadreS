package compadres.burgueria.app.compadres;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
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
    EditText estado;
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
        estado = (EditText) view.findViewById(R.id.estado);
        cep = (EditText) view.findViewById(R.id.cep);
        metodoDePagamento = (Spinner) view.findViewById(R.id.metodoDePagamento);
        btnFinishOk = (Button) view.findViewById(R.id.btnFinishOk);
        btnFinishCancel = (Button) view.findViewById(R.id.btnFinishCancel);

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
                ((ListaProdutos)getActivity()).turnOffFinishDialog();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout((width*9)/10, LinearLayout.LayoutParams.WRAP_CONTENT);
    }



}
