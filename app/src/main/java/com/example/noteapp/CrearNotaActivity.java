package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Date;

public class CrearNotaActivity extends AppCompatActivity {
    private Note nota;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private int id;
    private String titulo;
    private String contenido;
    private Date fechaVencimiento;
    private Icon icon;
    private String categoria;
    private Gson gson;
    private Boolean vence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);
        dbRef = db.getReference();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.selectIconBtn:

                        break;
                    case R.id.saveBtn:
                        EditText et1 = findViewById(R.id.nombreNotaEt);
                        EditText et2 = findViewById(R.id.contenidoNotaEt);
                        EditText et3 = findViewById(R.id.fechaNotaEt);
                        EditText et4 = findViewById(R.id.categoriaNotaEt);
                        Chip check = findViewById(R.id.venceCheck);
                        titulo = et1.getText().toString();
                        contenido = et2.getText().toString();
                        fechaVencimiento = (Date) et3.getText();
                        System.out.println("La fecha de vencimiento es: "+fechaVencimiento);
                        vence = check.isChecked();
                        nota = new Note();
                        nota.setTitulo(titulo);
                        nota.setContenido(contenido);
                        nota.setVence(vence);
                        nota.setFechaVencimiento(fechaVencimiento);
                        nota.setCategoria(categoria);
                        String json = gson.toJson(nota);
                        System.out.println("la variable enviar es"+json);
                        initNotes(json, nota);
                        finish();
                        break;
                }
            }
        };
        Button createNoteBtn = findViewById(R.id.guardarNotaBtn);
        createNoteBtn.setOnClickListener(onClickListener);
    }
    private void initNotes(String json,Note note){
        Intent intent = new Intent(this, NotesListActivity.class);
        intent.putExtra("categoria", note.categoria);
        startActivity(intent);
    }
}