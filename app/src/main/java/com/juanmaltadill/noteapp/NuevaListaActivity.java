package com.juanmaltadill.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NuevaListaActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.NoteApp.MESSAGE";
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private String name;
    private Icon icon;
    private String save;
    private ArrayList<Categoria> categories;
    private Categoria cat;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_lista);
        categories= new ArrayList<Categoria>();
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance("https://noteapp-16399-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        gson = new Gson();
        dbRef.child("users").child("categorias").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue()!=null){
                        save = task.getResult().getValue().toString();
                        System.out.println("La copia de la base de datos es: "+save);
                        System.out.println("categorias "+save);
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.selectIconBtn:

                        break;
                    case R.id.saveBtn:
                        EditText et1 = findViewById(R.id.nombreListaEditText);
                        name = et1.getText().toString();
                        cat = new Categoria();
                        cat.setNombre(name);
                        String json = gson.toJson(cat);
                        System.out.println("la variable enviar es"+json);
                        initHome(json);
                        finish();
                        break;
                }
            }
        };
        Button selectIconBtn = findViewById(R.id.selectIconBtn);
        selectIconBtn.setOnClickListener(onClickListener);
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(onClickListener);
    }
    private void initHome(String cat){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("categoria", cat);
        startActivity(intent);
    }
}