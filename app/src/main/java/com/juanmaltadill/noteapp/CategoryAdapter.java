package com.juanmaltadill.noteapp;

import android.content.Context;
import android.graphics.drawable.Icon;
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
            ImageView icon = (ImageView) v.findViewById(R.id.icon);
            TextView tt2 = (TextView) v.findViewById(R.id.nombreCategoria);

            if (icon != null) {
                icon.setImageIcon(p.getIcon());
            }

            if (tt2 != null) {
                tt2.setText(p.getNombre().toString());
            }
        }

        return v;
    }

}