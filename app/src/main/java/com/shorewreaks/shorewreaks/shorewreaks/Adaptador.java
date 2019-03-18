package com.shorewreaks.shorewreaks.shorewreaks;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    Context context;
    ArrayList<RankingPlayas> lista_rankings;

    public Adaptador(Context context, ArrayList<RankingPlayas> lista_rankings) {
        this.context = context;
        this.lista_rankings = lista_rankings;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<RankingPlayas> getLista_rankings() {
        return lista_rankings;
    }

    public void setLista_rankings(ArrayList<RankingPlayas> lista_rankings) {
        this.lista_rankings = lista_rankings;
    }

    @Override
    public int getCount() {
        return lista_rankings.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflador = LayoutInflater.from(context);
        View vista = inflador.inflate(R.layout.lv_vista, null);

        TextView tvNombre, tvDireccion, tvNota, tvProvincia;
        ImageButton imgbtnMapa;
        RankingPlayas rankingPlayas = lista_rankings.get(position);

        tvNombre = vista.findViewById(R.id.tvNombre2);
        tvDireccion = vista.findViewById(R.id.tvDireccion2);
        tvNota = vista.findViewById(R.id.tvNota2);
        tvProvincia = vista.findViewById(R.id.tvProvincia2);
        imgbtnMapa = vista.findViewById(R.id.imgbtnMapa);

        tvNombre.setText(rankingPlayas.getNombre());
        tvDireccion.setText(rankingPlayas.getDireccion());
        tvNota.setText(Double.toString(rankingPlayas.getNota()));
        if (rankingPlayas.getNota() >= 0 && rankingPlayas.getNota() < 2.5) {
            tvNota.setTextColor(Color.BLACK);
        } else if (rankingPlayas.getNota() >= 2.5 && rankingPlayas.getNota() < 5) {
            tvNota.setTextColor(Color.RED);
        } else if (rankingPlayas.getNota() >= 5 && rankingPlayas.getNota() < 7.5) {
            tvNota.setTextColor(Color.YELLOW);
        } else if (rankingPlayas.getNota() >= 7.5) {
            tvNota.setTextColor(Color.GREEN);
        }
        tvProvincia.setText(rankingPlayas.getProvincia());

        return vista;
    }
}
