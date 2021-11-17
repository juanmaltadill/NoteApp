package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference dbRef;
    private String userId;
    private String email;
    private String name;
    private String pass;
    private String phone;
    private ListView listview;
    private ArrayList<Categoria> categoryArray = new ArrayList<Categoria>();
    private Gson gson;
    private String copia;
    private String enviar;
    private Categoria flag;
    private Categoria nuevaCategoria;
    private ArrayList<Categoria> copiaCategorias = new ArrayList<Categoria>();
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listview = (ListView) findViewById(R.id.categoriesListView);
        String json = getIntent().getExtras().getString("categoria");
        userId = auth.getUid();
        System.out.println("El id del usuario es "+userId);
        gson = new Gson();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("users").child(userId).child("categorias").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    copia = String.valueOf(task.getResult().getValue());
                    System.out.println("Tamaño copiacategorias " + copiaCategorias.size());
                    System.out.println(copia.length());
                    if(copia.length()<27){
                        copiaCategorias.add(gson.fromJson(copia, Categoria.class));
                    }else{
                        copiaCategorias.addAll(gson.fromJson(copia, new TypeToken<ArrayList<Categoria>>(){}.getType()));
                    }

                    if(json != null){
                        nuevaCategoria = gson.fromJson(json, Categoria.class);
                        copiaCategorias.add(nuevaCategoria);
                    }
                    enviar = gson.toJson(copiaCategorias);
                    dbRef.child("users").child(userId).child("categorias").setValue(enviar);
                    System.out.println("Tamaño copiacategorias" + copiaCategorias.size());
                }
                createView(copiaCategorias);
            }
        });


    }

    private void createView(ArrayList<Categoria> categorias){
        CategoryAdapter adapter = new CategoryAdapter(HomeActivity.this, R.layout.categoryadapter_layout, copiaCategorias);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_settings1:
                initNewList();
                return true;
            case R.id.action_settings2:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initNewList(){
        Intent intent = new Intent(this, NuevaListaActivity.class);
        startActivity(intent);
    }
    private void initNewNote(){
        Intent intent = new Intent(this, NuevaNotaActivity.class);
        startActivity(intent);
    }

}