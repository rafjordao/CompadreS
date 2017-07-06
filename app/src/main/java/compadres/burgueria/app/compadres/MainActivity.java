package compadres.burgueria.app.compadres;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = "CompadreS";
    NavigationView navigationView;
    FloatingActionButton fab;
    View headerView;
    public User user;
    protected FirebaseAuth mAuth;
    boolean logged=false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //shared
        sharedPref = getSharedPreferences("UserName", Context.MODE_PRIVATE);

        //Previous versions of Firebase
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        mRef=new Firebase("https://compadres-26673.firebaseio.com/");

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        fab = (FloatingActionButton) findViewById(R.id.car);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                    fab.findViewById(R.id.car).setVisibility(View.VISIBLE);
                    headerView.findViewById(R.id.login_logout).setVisibility(View.INVISIBLE);
                    setUserInfos(mUser.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    navigationView.getMenu().findItem(R.id.nav_order).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_sair_item).setVisible(false);
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
            logged=true;
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

        } else if (id == R.id.nav_carte) {
            CardapioFragment cardapio = new CardapioFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.conteudo_fragment, cardapio).commit();
        } else if (id == R.id.nav_order) {
            Pedido pedido = new Pedido(new Date());
            pedido.addCartItem(new Produto("Coisa pra Cinema","Hamburguer","pão, 2 carnes, queijo, bacon, cebola e molho opcional cheddar",13.00f,R.drawable.burguer_image),5);
            pedido.addCartItem(new Produto("Soda","Bebida","350 ml",4.00f,R.drawable.bebida_image),5);
            mDatabase.child("user").child(mAuth.getCurrentUser().getUid()).child("pedidos").child(pedido.getData_pedido()).setValue(pedido);
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
            lg.show(getSupportFragmentManager().beginTransaction(),"loginDialog");
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
        lg.show(getSupportFragmentManager().beginTransaction(),"signUpDialog");
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

    /*private void signInWithFacebook(AccessToken token) {
        Log.d(TAG, "signInWithFacebook:" + token);

        showProgressDialog();


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            String uid=task.getResult().getUser().getUid();
                            String name=task.getResult().getUser().getDisplayName();
                            String email=task.getResult().getUser().getEmail();
                            String image=task.getResult().getUser().getPhotoUrl().toString();

                            //Create a new User and Save it in Firebase database
                            User user = new User(uid,name,null,email,null);

                            mRef.child(uid).setValue(user);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user_id",uid);
                            intent.putExtra("profile_picture",image);
                            navigationView.getMenu().findItem(R.id.nav_carte).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_order).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_sair_item).setVisible(true);
                            //login.dismiss();
                            setUserInfos(uid);
                            //startActivity(intent);
                            //finish();
                        }

                        hideProgressDialog();
                    }
                });
    }*/

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

}
