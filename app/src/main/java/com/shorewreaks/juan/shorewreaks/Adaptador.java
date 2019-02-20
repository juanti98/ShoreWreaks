package com.shorewreaks.juan.shorewreaks;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    Context context;
    ArrayList<RankingPlayas> lista_rankings;

    public Adaptador(Context context, ArrayList<RankingPlayas> lista_rankings) {
        this.context = context;
        this.lista_rankings = lista_rankings;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
