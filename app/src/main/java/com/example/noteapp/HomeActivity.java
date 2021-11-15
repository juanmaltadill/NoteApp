package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
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
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private String email;
    private String name;
    private String pass;
    private String phone;
    private ListView listview;
    private ArrayList<Categoria> categoryArray = new ArrayList<Categoria>();
    private Gson gson;
    private String copia;
    private Categoria[] copiaCategoria;
    private ArrayList<Categoria> copiaCategorias;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance("https://noteapp-16399-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        listview = (ListView) findViewById(R.id.categoriesListView);
        String json = getIntent().getExtras().getString("categoria");
        gson = new Gson();

        dbRef.child("users").child("categorias").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() != null) {
                        copia = task.getResult().getValue().toString();
                        System.out.println("aaaaaaaaaaaaaaaa"+copia);
                        copiaCategoria = gson.fromJson(copia, new TypeToken<ArrayList<Categoria>>(){}.getType());
                      //  copiaCategorias.add(copiaCategoria);
                        System.out.println("La copia de la base de datos en HomeActivity es: " + copiaCategoria.length);
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        if(json!=null){
            System.out.println(json);
            copiaCategorias = gson.fromJson(json, new TypeToken<ArrayList<Categoria>>(){}.getType());
            //categoryArray.add(copiaCategorias[0]);
            for(Categoria categoria:categoryArray){
                System.out.println(categoria.nombre);
            }
            CategoryAdapter adapter = new CategoryAdapter(this, R.layout.categoryadapter_layout, categoryArray);
            listview.setAdapter(adapter);
        }
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