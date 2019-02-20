package com.example.juan.shorewreaks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class PerfilUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context c;
    private EditText et_name_user, et_nombre, et_apellido;
    private TextView tv_mail_user;
    private ImageView img_user;
    private TextView tvNombreUser, tvEmail;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String username = "juanti9";
        Users nuevoUser = new Users(username,"juanti98@gmail.com","Juan","TorresI");
        mDatabase.child("users").child(username).setValue(nuevoUser);

        cambioVistaUser();
        cargarDatos();

        c = this;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();
            setNombreApellido(name);
            tvNombreUser.setText(name);
            et_name_user.setText(name);
            tvEmail.setText(email);
            tv_mail_user.setText(email);

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




    }

    private void setNombreApellido(String name) {
        int count = 0, count2;
        String nombre = "", apellido = "";
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

        et_nombre.setText(nombre);
        et_apellido.setText(apellido);

    }

    private void cargarDatos() {
        et_name_user = findViewById(R.id.et_name_user);
        tv_mail_user = findViewById(R.id.tv_mail_user);
        et_nombre = findViewById(R.id.et_name);
        et_apellido = findViewById(R.id.et_last_name);
    }

    private void cambioVistaUser() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvNombreUser = (TextView) headerView.findViewById(R.id.tv_nombreUser);
        tvEmail = (TextView)headerView.findViewById(R.id.tv_email_header);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(c, MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(c, PerfilUser.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_playas) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.logOut) {
            Intent intent = new Intent(c, LoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

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
