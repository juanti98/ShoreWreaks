package com.shorewreaks.juan.shorewreaks;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class firebaseDatos {
    private DatabaseReference mDatabase;
    private ArrayList<RankingPlayas> listaPlayas;


    public firebaseDatos() {
        this.listaPlayas = new ArrayList<>();
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    public ArrayList<RankingPlayas> getListaPlayas() {
        return listaPlayas;
    }

    public void setListaPlayas(ArrayList<RankingPlayas> listaPlayas) {
        this.listaPlayas = listaPlayas;
    }

    public ArrayList<RankingPlayas> cogerPlayas() {
        ArrayList<RankingPlayas> playasCompletas = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RankingPlayas> playas = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.child("playas").getChildren()){
                    String lat, lon, nombre, direccion, localidad, provincia, nota;
                    lat = ds.child("lat").getValue().toString();
                    lon = ds.child("lon").getValue().toString();
                    nombre = ds.child("nombre").getValue().toString();
                    direccion = ds.child("direccion").getValue().toString();
                    localidad = ds.child("localidad").getValue().toString();
                    provincia = ds.child("provincia").getValue().toString();
                    nota = ds.child("nota").getValue().toString();
                    playas.add(new RankingPlayas(nombre, direccion, localidad, provincia, lat, lon, Double.parseDouble(nota)));
                }
                setListaPlayas(playas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        playasCompletas = getListaPlayas();
        return playasCompletas;
    }
}