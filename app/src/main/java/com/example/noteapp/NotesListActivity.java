package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

public class NotesListActivity extends AppCompatActivity {

    private ListView listview;
    private String userId;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Gson gson;
    private DatabaseReference dbRef;
    private String categoria;
    private String copia;
    private String nota;
    private ArrayList<Note> copiaNotas = new ArrayList<Note>();
    private Note nuevaNota;
    private String enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        listview = (ListView) findViewById(R.id.notesListView);
        categoria = getIntent().getExtras().getString("categoria");
        nota = getIntent().getExtras().getString("nota");
        userId = auth.getUid();
        System.out.println("El id del usuario es "+userId);
        gson = new Gson();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("users").child(userId).child("notas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    copia = String.valueOf(task.getResult().getValue());
                    System.out.println("Tamaño copiacategorias " + copiaNotas.size());
                    System.out.println(copia.length());
                    if(copia.length()<73){
                        copiaNotas.add(gson.fromJson(copia, Note.class));
                    }else{
                        copiaNotas.addAll(gson.fromJson(copia, new TypeToken<ArrayList<Note>>(){}.getType()));
                    }

                    if(nota != null){
                        nuevaNota = gson.fromJson(copia, Note.class);
                        copiaNotas.add(nuevaNota);
                    }
                    enviar = gson.toJson(copiaNotas);
                    dbRef.child("users").child(userId).child("notas").setValue(enviar);
                    System.out.println("Tamaño copiacategorias" + copiaNotas.size());
                }
                Collections.reverse(copiaNotas);
                createView(copiaNotas);
            }
        });

    }
    private void createView(ArrayList<Note> notas){
        NoteAdapter adapter = new NoteAdapter(NotesListActivity.this, R.layout.noteadapter_layout, copiaNotas);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}