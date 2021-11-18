package com.juanmaltadill.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private int resourceLayout;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public NoteAdapter(Context context, int resource, List<Note> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Note p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView tt2 = (TextView) v.findViewById(R.id.tituloNota);

            /*if (tt1 != null) {
                tt1.setText(p.getIcon());
            }*/

            if (tt2 != null) {
                tt2.setText(p.getTitulo().toString());
            }
        }

        return v;
    }

}