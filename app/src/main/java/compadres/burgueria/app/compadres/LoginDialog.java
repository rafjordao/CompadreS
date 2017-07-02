package compadres.burgueria.app.compadres;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Created by rafjo on 01/07/2017.
 */

public class LoginDialog extends DialogFragment {

    TextView signUpLink;

    Button cancelBtn;
    Button loginBtn;

    EditText email;
    EditText password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Script","onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.login_dialog,container);

        loginBtn = (Button) view.findViewById(R.id.btnLogin);
        cancelBtn = (Button) view.findViewById(R.id.btnCancel);

        signUpLink = (TextView) view.findViewById(R.id.link_signup);

        email = (EditText) view.findViewById(R.id.txtUsername);
        password = (EditText) view.findViewById(R.id.txtPassword);

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
                ((MainActivity)getActivity()).signUpDialog();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).turnOffLoginDialog();
            }
        });

        return view;
    }

    protected void setUpUser() {
        ((MainActivity)getActivity()).user = new User();
        ((MainActivity)getActivity()).user.setEmail(email.getText().toString());
        ((MainActivity)getActivity()).user.setPassword(password.getText().toString());
    }

    private void signIn(String email, String password) {
        Log.d(((MainActivity)getActivity()).TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        ((MainActivity)getActivity()).showProgressDialog();

        ((MainActivity)getActivity()).mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((MainActivity)getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(((MainActivity)getActivity()).TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(((MainActivity)getActivity()).TAG, "signInWithEmail", task.getException());
                            Toast.makeText(((MainActivity)getActivity()).getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ((MainActivity)getActivity()).turnOffLoginDialog();
                        }
                        ((MainActivity)getActivity()).hideProgressDialog();
                    }
                });
        //
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

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(500, 350);
        window.setGravity(Gravity.CENTER);
    }
}
