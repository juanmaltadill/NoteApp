package com.iesmurgi.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.iesmurgi.noteapp.R;

import java.util.ArrayList;

public class SeleccionarIconoActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<ImageView> img = new ArrayList<ImageView>();
    private int id;
    private String imageId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_icon);
        this.setTitle("Elegir icono");
        ImageView avion = (ImageView) findViewById(R.id.iconAvion);
        img.add(avion);
        ImageView compras = (ImageView) findViewById(R.id.iconCompras);
        img.add(compras);
        ImageView engranajes = (ImageView) findViewById(R.id.iconEngranajes);
        img.add(engranajes);
        ImageView estudios = (ImageView) findViewById(R.id.iconEstudios);
        img.add(estudios);
        ImageView gimnasio = (ImageView) findViewById(R.id.iconGimnasio);
        img.add(gimnasio);
        ImageView hotel = (ImageView) findViewById(R.id.iconHotel);
        img.add(hotel);
        ImageView juegos = (ImageView) findViewById(R.id.iconJuego);
        img.add(juegos);
        ImageView libros = (ImageView) findViewById(R.id.iconLibros);
        img.add(libros);
        ImageView llamadas = (ImageView) findViewById(R.id.iconLlamada);
        img.add(llamadas);
        ImageView reuniones = (ImageView) findViewById(R.id.iconReunion);
        img.add(reuniones);
        Button button = (Button) findViewById(R.id.iconBtn);
        avion.setOnClickListener(this);
        compras.setOnClickListener(this);
        engranajes.setOnClickListener(this);
        estudios.setOnClickListener(this);
        gimnasio.setOnClickListener(this);
        hotel.setOnClickListener(this);
        juegos.setOnClickListener(this);
        libros.setOnClickListener(this);
        llamadas.setOnClickListener(this);
        reuniones.setOnClickListener(this);
        button.setOnClickListener(this);


    }
    public void initNotesList(String tag){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", tag);
        System.out.println("El extra que se envia desde selecticon es "+tag);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iconBtn){
            initNotesList(imageId);
        }else{
            for(int i=0; i<img.size(); i++){
                if(view.getId() != img.get(i).getId()){
                    img.get(i).setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }else{
                    view.setBackgroundColor(Color.parseColor("#7C64BFFB"));
                }
            }
            imageId = view.getTag().toString();
        }
    }
}