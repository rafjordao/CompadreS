package compadres.burgueria.app.compadres;

import android.*;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class ListaProdutos extends AppCompatActivity implements LocationListener{

    Pedido pedido;
    SharedPreferences pedidoPref;
    SharedPreferences.Editor prefEditor;
    PedidoAdapter adapter;
    TextView valorTotal;

    LocationManager lm;
    Location location;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        ListView lista = (ListView) findViewById(R.id.lvListaProduto);
        valorTotal = (TextView) findViewById(R.id.total_pedido);
        Button finishCartButton = (Button) findViewById(R.id.finalizarCarrinho);

        pedidoPref = getSharedPreferences("Pedido", MODE_PRIVATE);
        prefEditor = pedidoPref.edit();
        Gson gson = new Gson();
        if (!pedidoPref.getString("Pedido", "").isEmpty()) {
            String json = pedidoPref.getString("Pedido", "");
            pedido = gson.fromJson(json, Pedido.class);
        }


        valorTotal.setText(pedido.getValor());

        adapter = new PedidoAdapter(this, pedido);
        lista.setAdapter(adapter);

        finishCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFinishDialog();
            }
        });

    }

    public void updateTotalValue() {
        Gson gson = new Gson();
        if (!pedidoPref.getString("Pedido", "").isEmpty()) {
            String json = pedidoPref.getString("Pedido", "");
            pedido = gson.fromJson(json, Pedido.class);
        }
        valorTotal.setText(pedido.getValor());

    }

    public void showFinishDialog() {
        FinishDialog fd = new FinishDialog();
        fd.show(getSupportFragmentManager(), "finishDialog");
    }

    public void turnOffFinishDialog() {
        FinishDialog fd = (FinishDialog) getSupportFragmentManager().findFragmentByTag("finishDialog");
        if (fd != null) {
            fd.dismiss();
            getSupportFragmentManager().beginTransaction().remove(fd);
        }
    }

    void getLocation() {
        try {
            lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
            Criteria locationCritera = new Criteria();
            String providerName = lm.getBestProvider(locationCritera,
                    true);
            if(providerName!=null)
                location = lm.getLastKnownLocation(providerName);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            this.location=location;
        }
        latitude = this.location.getLatitude();
        longitude = this.location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}
