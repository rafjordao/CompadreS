package compadres.burgueria.app.compadres;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by rafjo on 01/07/2017.
 */

public class SignUpDialog extends DialogFragment {

    EditText signUpName;
    EditText signUpPassword;
    EditText signUpPhone;
    EditText signUpEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.signup_dialog,container);

        signUpName = (EditText) view.findViewById(R.id.signUpName);
        signUpPhone = (EditText) view.findViewById(R.id.signUpPhone);
        signUpEmail = (EditText) view.findViewById(R.id.signUpEmail);
        signUpPassword = (EditText) view.findViewById(R.id.signUpPassword);

        Button signUpOk= (Button) view.findViewById(R.id.btnSignUpOk);
        Button signUpCancel = (Button) view.findViewById(R.id.btnSignUpCancel);

        signUpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                createNewAccount(signUpEmail.getText().toString(), signUpPassword.getText().toString());
                ((MainActivity)getActivity()).showProgressDialog();
            }
        });

        signUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainActivity)getActivity()).turnOffSignUpDialog();
            }
        });

        return view;
    }

    private void createNewAccount(String email, String password) {
        Log.d(((MainActivity)getActivity()).TAG, "createNewAccount:" + email);
        if (!validateSignUpForm()) {
            return;
        }
        //This method sets up a new User by fetching the user entered details.
        setSignUpUser();
        //This method  method  takes in an email address and password, validates them and then creates a new user
        // with the createUserWithEmailAndPassword method.
        // If the new account was created, the user is also signed in, and the AuthStateListener runs the onAuthStateChanged callback.
        // In the callback, you can use the getCurrentUser method to get the user's account data.

        Log.d(((MainActivity)getActivity()).TAG, "passei pelo setSignUp, com email:"+email+" e senha "+password);

        ((MainActivity)getActivity()).mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(((MainActivity)getActivity()).TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        ((MainActivity)getActivity()).hideProgressDialog();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(((MainActivity)getActivity()).getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            onAuthenticationSucess(task.getResult().getUser());
                        }


                    }
                }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(((MainActivity)getActivity()).TAG,"Erro: "+e.getMessage());
            }
        });

    }

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

    protected void setSignUpUser() {
        ((MainActivity)getActivity()).user = new User();
        ((MainActivity)getActivity()).user.setDisplayName(signUpName.getText().toString());
        ((MainActivity)getActivity()).user.setPhoneNumber(signUpPhone.getText().toString());
        ((MainActivity)getActivity()).user.setEmail(signUpEmail.getText().toString());
        ((MainActivity)getActivity()).user.setPassword(signUpPassword.getText().toString());
    }

    private void onAuthenticationSucess(FirebaseUser mUser) {
        // Write new user
        User user = ((MainActivity)getActivity()).user;
        saveNewUser(mUser.getUid(), user.getDisplayName(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
        ((MainActivity)getActivity()).signOut();
        ((MainActivity)getActivity()).turnOffSignUpDialog();
        // Go to LoginActivity
        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        //finish();
    }

    private void saveNewUser(String userId, String name, String phone, String email, String password) {
        User user = new User(userId,name,phone,email,password);

        ((MainActivity)getActivity()).mRef.child("users").child(userId).setValue(user);
        //((MainActivity)getActivity()).mDatabase.child("users").child(userId).setValue(user);

        FirebaseUser mUser = ((MainActivity)getActivity()).mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        mUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(((MainActivity)getActivity()).TAG,"Usuário criado com sucesso!");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(500, 450);
        window.setGravity(Gravity.CENTER);
    }

}
