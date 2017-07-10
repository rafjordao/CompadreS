package compadres.burgueria.app.compadres;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.firebase.client.core.Tag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = "CompadreS";
    NavigationView navigationView;
    FloatingActionButton fab;
    View headerView;
    public User user;
    protected FirebaseAuth mAuth;
    static Pedido pedido;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected ProgressDialog mProgressDialog;
    protected DatabaseReference mDatabase;
    protected FirebaseDatabase database;
    //Add YOUR Firebase Reference URL instead of the following URL
    Firebase mRef;

    //FaceBook callbackManager
    protected CallbackManager callbackManager;
    //

    SharedPreferences sharedPref;
    SharedPreferences pedidoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //shared
        sharedPref = getSharedPreferences("UserName", Context.MODE_PRIVATE);
        pedidoPref = getSharedPreferences("Pedido",MODE_PRIVATE);
        Editor prefEditor = pedidoPref.edit();
        Gson gson = new Gson();
        if(!pedidoPref.getString("Pedido","").isEmpty()){
            String json = pedidoPref.getString("Pedido","");
            pedido = gson.fromJson(json,Pedido.class);
        }

        //Previous versions of Firebase
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mRef=new Firebase("https://compadres-26673.firebaseio.com/");
        mRef.keepSynced(true);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        fab = (FloatingActionButton) findViewById(R.id.car);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                if(!pedidoPref.getString("Pedido","").isEmpty()){
                    String json = pedidoPref.getString("Pedido","");
                    pedido = gson.fromJson(json,Pedido.class);
                }
                if(pedido!=null) {
                    Intent intent = new Intent(getApplicationContext(), ListaProdutos.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Sem nenhum item no carrinho!",Toast.LENGTH_LONG).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                    navigationView.getMenu().findItem(R.id.nav_order).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_sair_item).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_carte).setVisible(true);
                    fab.findViewById(R.id.car).setVisibility(View.VISIBLE);
                    headerView.findViewById(R.id.login_logout).setVisibility(View.INVISIBLE);
                    setUserInfos(mUser.getUid());
                    if(pedidoPref.getString("Pedido","").isEmpty()){
                        Log.d(TAG,pedidoPref.getString("Pedido",""));
                        Query pendingCart = mDatabase.child("users").child(mUser.getUid()).child("pedido_pendente");
                        pendingCart.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    pedido = dataSnapshot.getValue(Pedido.class);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Gson gson = new Gson();
                        String json = pedidoPref.getString("Pedido","");
                        pedido = gson.fromJson(json,Pedido.class);
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    navigationView.getMenu().findItem(R.id.nav_order).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_sair_item).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_carte).setVisible(false);
                    fab.findViewById(R.id.car).setVisibility(View.INVISIBLE);
                    headerView.findViewById(R.id.login_logout).setVisibility(View.VISIBLE);
                    TextView displayName = (TextView) headerView.findViewById(R.id.userDisplayName);
                    displayName.setText(R.string.userName);
                }

            }
        };

        //FaceBook
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        callbackManager = CallbackManager.Factory.create();
        //

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_burguer) {
            // Handle the camera action
            List<Fragment> listaFragmento = getSupportFragmentManager().getFragments();

            if(listaFragmento!=null) {
                for (Fragment frag : listaFragmento) {
                    if (frag == null) {
                        continue;
                    }
                    getSupportFragmentManager().beginTransaction().remove(frag).commit();
                }
            }
        } else if (id == R.id.nav_contact) {
            //LayoutInflater é utilizado para inflar nosso layout em uma view.
            //-pegamos nossa instancia da classe
            LayoutInflater li = getLayoutInflater();

            //inflamos o layout alerta.xml na view
            View view = li.inflate(R.layout.alert_contato, null);
            AlertDialog.Builder contato = new AlertDialog.Builder(MainActivity.this);
            contato.setTitle("Contato");
            contato.setView(view);
            AlertDialog alerta = contato.create();
            alerta.show();

        } else if (id == R.id.nav_carte) {
            CardapioFragment cardapio = new CardapioFragment();
            new LoadInternetDependentFragment().execute(cardapio);

        } else if (id == R.id.nav_order) {
            Toast.makeText(getApplicationContext(),"Em Construção!",Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_sair) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onLabelClick(View v){
        if(v.getId()==R.id.login_logout){
            LoginDialog lg = new LoginDialog();
            lg.show(getSupportFragmentManager(),"loginDialog");
        }
    }

    public void turnOffLoginDialog(){
        LoginDialog lg = (LoginDialog) getSupportFragmentManager().findFragmentByTag("loginDialog");
        if(lg!=null){
            lg.dismiss();
            getSupportFragmentManager().beginTransaction().remove(lg);
        }
    }

    public void signUpDialog(){
        SignUpDialog lg = new SignUpDialog();
        lg.show(getSupportFragmentManager(),"signUpDialog");
    }

    public void turnOffSignUpDialog(){
        SignUpDialog sud = (SignUpDialog) getSupportFragmentManager().findFragmentByTag("signUpDialog");
        if(sud!=null){
            sud.dismiss();
            getSupportFragmentManager().beginTransaction().remove(sud);
        }
    }

    protected void signOut() {
        mAuth.signOut();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //

    public void setUserInfos(String uid){

        TextView displayName = (TextView) headerView.findViewById(R.id.userDisplayName);
        String name;
        if (mAuthListener != null){
            name = mAuth.getCurrentUser().getDisplayName();
            SharedPreferences.Editor ed = sharedPref.edit();
            ed.putString("userName",name);
        } else {
            name = sharedPref.getString("userName","Olá Visitante!");
        }
        displayName.setText(name);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }

    class LoadInternetDependentFragment extends AsyncTask<Fragment, Void , Boolean> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(Fragment... fragments) {
            Boolean haveInternet = hasActiveInternetConnection();
            if(haveInternet) {
                getSupportFragmentManager().beginTransaction().replace(R.id.conteudo_fragment, fragments[0]).commit();
            }
            return haveInternet;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            hideProgressDialog();
            if(!aBoolean){
                Toast.makeText(getApplicationContext(),
                        "Você precisa de conexão com a internet para acessar o Cardápio", Toast.LENGTH_LONG).show();
            }
        }

    }

}
