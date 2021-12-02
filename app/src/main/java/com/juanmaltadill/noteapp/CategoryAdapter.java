package com.juanmaltadill.noteapp;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Categoria> {

    private int resourceLayout;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public CategoryAdapter(Context context, int resource, List<Categoria> items) {
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

        Categoria p = getItem(position);

        if (p != null) {
            ImageView img = (ImageView) v.findViewById(R.id.icon);
            TextView tt2 = (TextView) v.findViewById(R.id.nombreCategoria);

            if (img != null && p.getIcon() != null) {
                switch(p.getIcon().toString()){
                    case "avion":
                        img.setImageResource(R.drawable.avion);
                        break;
                    case "compras":
                        img.setImageResource(R.drawable.compras);
                        break;
                    case "engranajes":
                        img.setImageResource(R.drawable.engranajes);
                        break;
                    case "estudios":
                        img.setImageResource(R.drawable.estudios);
                        break;
                    case "gimnasio":
                        img.setImageResource(R.drawable.gimnasio);
                        break;
                    case "hotel":
                        img.setImageResource(R.drawable.hotel);
                        break;
                    case "juego":
                        img.setImageResource(R.drawable.juegos);
                        break;
                    case "libros":
                        img.setImageResource(R.drawable.libros);
                        break;
                    case "reunion":
                        img.setImageResource(R.drawable.reuniones);
                        break;
                    case "llamada":
                        img.setImageResource(R.drawable.llamadas);
                        break;
                }
                System.out.println("Categoryadapter p.icon "+p.getIcon().toString());
            }
            if (tt2 != null) {
                tt2.setText(p.getNombre().toString());
                System.out.println("El nombre en el adaptador es+ "+p.getNombre().toString());
            }
        }
        return v;
    }

}