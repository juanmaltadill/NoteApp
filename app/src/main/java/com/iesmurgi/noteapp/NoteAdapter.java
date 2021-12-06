package com.iesmurgi.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iesmurgi.noteapp.R;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Nota> {

    private int resourceLayout;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public NoteAdapter(Context context, int resource, List<Nota> items) {
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

        Nota p = getItem(position);

        if (p != null) {
            TextView tt2 = (TextView) v.findViewById(R.id.tituloNota);
            TextView tt3 = (TextView) v.findViewById(R.id.contenidoNota);
            TextView tt4 = (TextView) v.findViewById(R.id.fechaVencimiento);
            String contenido = "";
            if (tt2 != null) {
                tt2.setText(p.getTitulo().toString());
            }
            if (tt3 != null) {
                for(int i=0; i<15; i++){
                    if(p.contenido.length()>0){
                        contenido = contenido + p.contenido.charAt(i);
                        if(i==p.contenido.length()-1){
                            break;
                        }
                    }

                }
                contenido = contenido + "...";
                tt3.setText(contenido);
            }
            if (tt4 != null) {
                if(p.getFechaVencimiento()!=null){
                    tt4.setText(p.getFechaVencimiento().toString());
                }else{
                    tt4.setText(R.string.no_vence);
                }

            }
        }

        return v;
    }

}