package com.juanmaltadill.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
    private ArrayList<Categoria> copiaCategorias;
    private ArrayList<Note> copiaNotas = new ArrayList<Note>();
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setTitle("Categorias");
        listview = (ListView) findViewById(R.id.categoriesListView);
        listview.setClickable(true);

        userId = auth.getUid();
        System.out.println("El id del usuario es "+userId);
        gson = new Gson();
        copiaCategorias = new ArrayList<Categoria>();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("users").child(userId).child("categorias").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                while(!task.isComplete()){
                    waitingDialog();
                    task.isComplete();
                }
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String json = getIntent().getExtras().getString("categoria");
                    copia = String.valueOf(task.getResult().getValue());
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
                }
                Collections.reverse(copiaCategorias);
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
                TextView et1 = findViewById(R.id.nombreCategoria);
                String categoria = et1.getText().toString();
                initNotesList(categoria);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l) {
                TextView et1 = findViewById(R.id.nombreCategoria);
                String categoria = et1.getText().toString();
                createDialog(categoria);
                return true;
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
            case R.id.action_settings1:
                initNewList();
                return true;
            case R.id.action_settings2:
                initNewNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initNotesList(String categoria){
        Intent intent = new Intent(this, NotesListActivity.class);
        intent.putExtra("categoria", categoria);
        startActivity(intent);
    }
    private void initNewList(){
        Intent intent = new Intent(this, NuevaListaActivity.class);
        startActivity(intent);
    }
    private void initNewNote(){
        Intent intent = new Intent(this, NuevaNotaActivity.class);
        startActivity(intent);
    }

    private void createDialog(String categoria){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Si borras la categoría borrarás todas las notas que contiene. ¿Estás seguro?");
        builder.setCancelable(true);

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                    for(int i=0; i<copiaNotas.size(); i++){
//                        if(copiaNotas.get(i).categoria == categoria){
//                            copiaNotas.remove(i);
//                        }
//                    }
//                    enviar = gson.toJson(copiaNotas);
//                    dbRef.child("users").child(userId).child("notas").setValue(enviar);
//                    System.out.println("Tamaño copianotas" + copiaNotas.size());
//
//                    createView(copiaCategorias);

                    for(int i=0; i<copiaCategorias.size(); i++){
                        if(copiaCategorias.get(i).nombre == categoria){
                            copiaCategorias.remove(i);
                        }
                    }
                    enviar = gson.toJson(copiaCategorias);
                    dbRef.child("users").child(userId).child("categorias").setValue(enviar);
                    System.out.println("Tamaño copiaCategorias" + copiaCategorias.size());

                    createView(copiaCategorias);
                    dialog.cancel();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    public void restartActivity(){
        finish();
        startActivity(getIntent());
    }
    public void waitingDialog(){
        ProgressDialog MyDialog = ProgressDialog.show(this, ""," Loading. Please wait ... ", true);
    }
}