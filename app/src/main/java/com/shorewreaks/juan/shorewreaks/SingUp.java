package com.shorewreaks.juan.shorewreaks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingUp extends AppCompatActivity {
    private Button mCancelButton;
    private Button mSingupButton;
    private ImageView singUpGoogle;

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final int RC_SIGN_IN = 777;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static final String TAG = "GoogleActivity";


    private EditText etUsername, etEmail, etPassword, etNombre, etApellido;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApliClient;
    private FirebaseAuth mAuth;
    private Context c;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        c = this;

        etUsername = findViewById(R.id.etUserName);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);

        mCancelButton = (Button)findViewById(R.id.btnCancel);
        mSingupButton = (Button)findViewById(R.id.btnSingUp);
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        mDatabase = FirebaseDatabase.getInstance().getReference();




        //Sign Up with username, password and email
        mSingupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, email, name, lastname;

                username = etUsername.getText().toString();
                name = etNombre.getText().toString();
                lastname = etApellido.getText().toString();
                password = etPassword.getText().toString();
                email = etEmail.getText().toString();

                createAccount(email, password, name, lastname, username);
            }
        });


//        cancel button
        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }
    private void createAccount(final String email, String password, final String name, final String lastname, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            etUsername.setText("");
                            etPassword.setText("");
                            etEmail.setText("");
                            Toast.makeText(c, "Usuario Creado",
                                    Toast.LENGTH_SHORT).show();
                            Users nuevoUser = new Users(username,email,name,lastname);
                            mDatabase.child("users").child(user.getUid()).setValue(nuevoUser);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(c, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


 /*   public void logout(View view){
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}