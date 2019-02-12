package com.example.juan.shorewreaks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class SingUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button mCancelButton;
    private Button mSingupButton;
    private ImageView singUpGoogle;

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final int RC_SIGN_IN = 777;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static final String TAG = "GoogleActivity";


    private EditText etUsername, etEmail, etPassword;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApliClient;
    private FirebaseAuth mAuth;
    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("45561172240-20h1qogc0tkdjegb3usq3e80n6r39suo.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApliClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();mGoogleApliClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        c = this;



        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);

        mCancelButton = (Button)findViewById(R.id.btnCancel);
        mSingupButton = (Button)findViewById(R.id.btnSingUp);
        singUpGoogle = (ImageView) findViewById(R.id.imgGoogle);
        SignInButton signInButton = findViewById(R.id.sign_in_button);


        //Sign Up with username, password and email
        mSingupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, email;

                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                email = etEmail.getText().toString();

                createAccount(email, password);
            }
        });

        //Sign Up with Google
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApliClient);
                Intent intent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
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
    private void createAccount(String email, String password) {
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            //GoogleSignIn.getSignedInAccountFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
           // handleSignInResult(task);
        }


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goLoginScreen();
                        }
                        else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.sign_in_button), "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /* private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
         try {
             GoogleSignInAccount account = completedTask.getResult(ApiException.class);
             goLoginScreen();

         } catch (ApiException e) {
             Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
         }
     }
 */
    private void goLoginScreen() {
        Intent intent = new Intent(this, MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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