package compadres.burgueria.app.compadres;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.finish_dialog,container);

        gpsButton = (Button) view.findViewById(R.id.gpsButton);
        rua = (EditText) view.findViewById(R.id.rua);
        bairro = (EditText) view.findViewById(R.id.bairro);
        cidade = (EditText) view.findViewById(R.id.cidade);
        estado = (EditText) view.findViewById(R.id.estado);
        cep = (EditText) view.findViewById(R.id.cep);
        metodoDePagamento = (Spinner) view.findViewById(R.id.metodoDePagamento);
        btnFinishOk = (Button) view.findViewById(R.id.btnFinishOk);
        btnFinishCancel = (Button) view.findViewById(R.id.btnFinishCancel);

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
        getDialog().getWindow().setLayout((width*9)/10,(height*7)/10);
    }
}
