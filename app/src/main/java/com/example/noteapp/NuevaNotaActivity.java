package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;

public class NuevaNotaActivity extends AppCompatActivity {
    private String titulo;
    private String contenido;
    private boolean vence;
    private String id;
    private Date fecha;
    private String cat;
    private Gson gson = new Gson();
    private String enviar;
    private Note nota;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId;
    private String save;
    private ArrayList<Categoria> categorias = new ArrayList<Categoria>();
    private ArrayList<String> listaCategorias = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_nota);
        EditText et1 = findViewById(R.id.tituloNuevaNotaEt);
        EditText et2 = findViewById(R.id.contenidoNuevaNotaEt);
        EditText et3 = findViewById(R.id.fechaNuevaNotaEt);
        TextView tv1 = findViewById(R.id.fechaNuevaNotaTv);
        Button button = findViewById(R.id.crearNuevaNotaBtn);
        Switch check = findViewById(R.id.venceNuevaNotaSwitch);
        Spinner categoria = findViewById(R.id.categoriaNuevaNotaSpinner);
        userId = auth.getUid();
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 if(b){
                    et3.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);
                 }
             }
         });
        dbRef.child("users").child(userId).child("categorias").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue()!=null){
                        save = task.getResult().getValue().toString();
                        System.out.println("La longitud de save es "+save.length());
                        if(save.length()<48){
                            categorias.add(gson.fromJson(save, Categoria.class));
                        }else{
                            categorias.addAll(gson.fromJson(save, new TypeToken<ArrayList<Categoria>>(){}.getType()));
                        }

                        for(int i=0; i<categorias.size(); i++){
                            listaCategorias.add(categorias.get(i).getNombre());
                        }
                        adaptarSpinner(categoria);
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titulo = et1.getText().toString();
                contenido = et2.getText().toString();
                vence = check.isChecked();
                if(vence){
                    fecha = (Date) et3.getText();
                }else{
                    fecha = null;
                }
                cat = categoria.getSelectedItem().toString();
                nota = new Note(titulo, contenido, vence, fecha, cat);

            }
        });
    }

    public void adaptarSpinner(Spinner spinner){
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaCategorias);
        spinner.setAdapter(adaptador);
    }

    public void initNotesList(String json){
        Intent intent = new Intent(this, NotesListActivity.class);
        intent.putExtra("nota", json);
        startActivity(intent);
    }
}