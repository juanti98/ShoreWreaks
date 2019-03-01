package com.shorewreaks.juan.shorewreaks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PerfilUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context c;
    private EditText et_name_user, et_nombre, et_apellido;
    private TextView tv_mail_user;
    private ImageView img_user;
    private TextView tvNombreUser, tvEmail;
    private DatabaseReference mDatabase;
    private Button btnGuardar;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        cambioVistaUser();
        cargarDatos();

        c = this;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        if (user != null) {
            final String userID = user.getUid();
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(userID)){
                            et_name_user.setText(dataSnapshot.child(userID).child("username").getValue().toString());
                            et_nombre.setText(dataSnapshot.child(userID).child("name").getValue().toString());
                            et_apellido.setText(dataSnapshot.child(userID).child("lastname").getValue().toString());
                            tv_mail_user.setText(dataSnapshot.child(userID).child("email").getValue().toString());
                        } else {
                            et_name_user.setText("");
                            et_nombre.setText("");
                            et_apellido.setText("");
                            tv_mail_user.setText(email);
                            if (tv_mail_user.getText().toString().isEmpty()){
                                tv_mail_user.setText("N/A");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        } else {
            goLoginScreen();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatos();
            }
        });

    }

    private void updateDatos() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email2 = tv_mail_user.getText().toString();
        if (email2.isEmpty()){
            email2 = "N/A";
        }
        final String email = email2, username = et_name_user.getText().toString(), name = et_nombre.getText().toString(), lastname = et_apellido.getText().toString();

        if (user != null) {
            final String userID = user.getUid();
            mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(userID)){

                        Users userUpdated = new Users(username,email,name,lastname);
                        Map<String, Object> update = userUpdated.toMap();
                        mDatabase.child("users").child(userID).updateChildren(update);
                        Intent intent = new Intent(c, MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Users nuevoUser = new Users(username,email,name,lastname);
                        mDatabase.child("users").child(userID).setValue(nuevoUser);
                        Intent intent = new Intent(c, MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        } else {
            goLoginScreen();
        }
    }

    private void setNombreApellido(String name) {
        int count = 0, count2;
        String nombre = "", apellido = "";
        /*
        while(count < name.length()){
            if (!(name.substring(count, count + 1).equals(" "))){
                nombre += name.substring(count, count + 1);
            } else if(name.substring(count, count + 1).equals(" ")){
                count2 = count + 1;
                while(count2 < name.length()){
                    apellido += name.substring(count2, count2 + 1);
                    count2++;
                }
                count = name.length();
            }
            count++;
        }
        */
        et_nombre.setText(nombre);
        et_apellido.setText(apellido);

    }

    private void cargarDatos() {
        et_name_user = findViewById(R.id.et_name_user);
        tv_mail_user = findViewById(R.id.tv_mail_user);
        et_nombre = findViewById(R.id.et_name);
        et_apellido = findViewById(R.id.et_last_name);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    private void cambioVistaUser() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvNombreUser = (TextView) headerView.findViewById(R.id.tv_nombreUser);
        tvEmail = (TextView) headerView.findViewById(R.id.tv_email_header);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(c, MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(c, PerfilUser.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_playas) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.logOut) {
            mAuth.signOut();
            Intent intent = new Intent(PerfilUser.this, LoginScreen.class);
            startActivity(intent);
        }

        //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //  drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goLoginScreen() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }
}