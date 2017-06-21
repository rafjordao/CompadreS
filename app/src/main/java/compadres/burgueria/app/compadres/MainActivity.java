package compadres.burgueria.app.compadres;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import java.security.KeyStore;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CompadreS";
    NavigationView navigationView;
    View headerView;
    public User user;
    private FirebaseAuth mAuth;
    boolean logged=false;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgressDialog;
    //Add YOUR Firebase Reference URL instead of the following URL
    Firebase mRef;

    //FaceBook callbackManager
    private CallbackManager callbackManager;
    //

    Button cancelBtn;
    Button loginBtn;
    LoginButton facebookLoginButton;
    TextView signUpLink;

    EditText email;
    EditText password;
    EditText signUpName;
    EditText signUpPassword;
    EditText signUpPhone;
    EditText signUpEmail;

    Dialog signup;
    Dialog login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Previous versions of Firebase
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        mRef=new Firebase("https://compadres-26673.firebaseio.com/");
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        /*/FaceBook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        /*/

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

        } else if (id == R.id.nav_sair) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(logged){

        } else {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onStart();
    }

    public void onLabelClick(View v){
        if(v.getId()==R.id.login_logout){
            /*logged=!logged;
            geraToast(""+logged);*/
            login = new Dialog(this);
            login.setContentView(R.layout.login_dialog);
            login.setTitle("Login");

            loginBtn = (Button) login.findViewById(R.id.btnLogin);
            cancelBtn = (Button) login.findViewById(R.id.btnCancel);

            signUpLink = (TextView) login.findViewById(R.id.link_signup);

            email = (EditText) login.findViewById(R.id.txtUsername);
            password = (EditText) login.findViewById(R.id.txtPassword);

            /*/Facebook login
            facebookLoginButton = (LoginButton) findViewById(R.id.button_facebook_login);
            facebookLoginButton.setReadPermissions("email", "public_profile");
            facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    signInWithFacebook(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                }
            });
            /**/

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    setUpUser();
                    signIn(email.getText().toString(), password.getText().toString());
                }
            });

            signUpLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signUpDialog();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login.dismiss();
                }
            });

            login.show();

        }
    }

    public void signUpDialog(){

        signup = new Dialog(this);
        signup.setContentView(R.layout.signup_dialog);

        signUpName = (EditText) signup.findViewById(R.id.signUpName);
        signUpPhone = (EditText) signup.findViewById(R.id.signUpPhone);
        signUpEmail = (EditText) signup.findViewById(R.id.signUpEmail);
        signUpPassword = (EditText) signup.findViewById(R.id.signUpPassword);

        Button signUpOk= (Button) signup.findViewById(R.id.btnSignUpOk);
        Button signUpCancel = (Button) signup.findViewById(R.id.btnSignUpCancel);

        signUpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                createNewAccount(signUpEmail.getText().toString(), signUpPassword.getText().toString());
                showProgressDialog();
            }
        });

        signUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                signup.dismiss();
            }
        });

        signup.show();
    }

    protected void setSignUpUser() {
        user = new User();
        user.setName(signUpName.getText().toString());
        user.setPhoneNumber(signUpPhone.getText().toString());
        user.setEmail(signUpEmail.getText().toString());
        user.setPassword(signUpPassword.getText().toString());
    }

    private void createNewAccount(String email, String password) {
        Log.d(TAG, "createNewAccount:" + email);
        if (!validateSignUpForm()) {
            return;
        }
        //This method sets up a new User by fetching the user entered details.
        setSignUpUser();
        //This method  method  takes in an email address and password, validates them and then creates a new user
        // with the createUserWithEmailAndPassword method.
        // If the new account was created, the user is also signed in, and the AuthStateListener runs the onAuthStateChanged callback.
        // In the callback, you can use the getCurrentUser method to get the user's account data.

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            onAuthenticationSucess(task.getResult().getUser());
                        }


                    }
                });

    }

    private void onAuthenticationSucess(FirebaseUser mUser) {
        // Write new user
        saveNewUser(mUser.getUid(), user.getName(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
        signOut();
        signup.dismiss();
        // Go to LoginActivity
        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        //finish();
    }

    private void saveNewUser(String userId, String name, String phone, String email, String password) {
        User user = new User(userId,name,phone,email,password);

        mRef.child("users").child(userId).setValue(user);
    }

    private void signOut() {
        mAuth.signOut();
        navigationView.getMenu().findItem(R.id.nav_carte).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_order).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_sair_item).setVisible(false);
        headerView.findViewById(R.id.login_logout).setVisibility(View.VISIBLE);
        TextView displayName = (TextView) headerView.findViewById(R.id.userDisplayName);
        displayName.setText(R.string.userName);
    }
    //This method, validates email address and password
    private boolean validateSignUpForm() {
        boolean valid = true;

        String userEmail = signUpEmail.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            signUpEmail.setError("Required.");
            valid = false;
        } else {
            signUpEmail.setError(null);
        }

        String userPassword = signUpPassword.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            signUpPassword.setError("Required.");
            valid = false;
        } else {
            signUpPassword.setError(null);
        }

        String userPhone = signUpPhone.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            signUpPhone.setError("Required.");
            valid = false;
        } else {
            signUpPhone.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

    private void signInWithFacebook(AccessToken token) {
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
                            logged=true;
                            startActivity(intent);
                            finish();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String userEmail = email.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String userPassword = password.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //

    protected void setUpUser() {
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            String uid = mAuth.getCurrentUser().getUid();
                            intent.putExtra("user_id", uid);
                            navigationView.getMenu().findItem(R.id.nav_carte).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_order).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_sair_item).setVisible(true);
                            login.dismiss();
                            setUserInfos(uid);
                            //startActivity(intent);
                            //finish();
                        }
                        hideProgressDialog();
                    }
                });
        //
    }

    public void setUserInfos(String uid){


        TextView displayName = (TextView) navigationView.findViewById(R.id.userDisplayName);
        displayName.setText(mAuth.getCurrentUser().getEmail().toString());

        /*mRef.child("users").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/

        navigationView.findViewById(R.id.login_logout).setVisibility(View.INVISIBLE);
    }

}
