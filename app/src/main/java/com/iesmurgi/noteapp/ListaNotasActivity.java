package com.iesmurgi.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iesmurgi.noteapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class ListaNotasActivity extends AppCompatActivity {

    private ListView listview;
    private String userId;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Gson gson;
    private DatabaseReference dbRef;
    private String categoria;
    private String copia;
    private String nota;
    private ArrayList<Nota> copiaNotas = new ArrayList<Nota>();
    private ArrayList<Nota> viewNotas = new ArrayList<Nota>();
    private Nota nuevaNota;
    private String enviar;
    private int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        this.setTitle(R.string.notas_tv);
        listview = (ListView) findViewById(R.id.notesListView);
        nota = getIntent().getExtras().getString("nota");
        System.out.println("La nota extra es "+nota);
        userId = auth.getUid();
        gson = new Gson();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("users").child(userId).child("notas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    categoria = getIntent().getExtras().getString("categoria");
                    System.out.println("La categoria extra es "+categoria);
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    copia = String.valueOf(task.getResult().getValue());
                    System.out.println("La variable copia es "+copia);
                    if(copia.startsWith("{")){
                        copiaNotas.add(gson.fromJson(copia, Nota.class));
                    }else{
                        copiaNotas.addAll(gson.fromJson(copia, new TypeToken<ArrayList<Nota>>(){}.getType()));
                    }
                    if(nota != null){
                        System.out.println("La nota creada es "+nota);
                        nuevaNota = gson.fromJson(nota, Nota.class);
                        copiaNotas.add(nuevaNota);
                    }
                    for(int i=0; i<copiaNotas.size(); i++){
                        System.out.println("Indice "+i+" de CopiaNotas = "+copiaNotas.get(i));
                    }

                    System.out.println("Tamaño CopiaNotas "+copiaNotas.size());
                    enviar = gson.toJson(copiaNotas);
                    System.out.println("El string de notas a enviar es "+ enviar);
                    dbRef.child("users").child(userId).child("notas").setValue(enviar);
                }
                for(int i=0; i<copiaNotas.size(); i++){
                    boolean comprobar = copiaNotas.get(i).categoria.equals(categoria);
                    System.out.println("Categoria copianotas "+copiaNotas.get(i).categoria + " Categoria extra "+categoria);
                    System.out.println("Comprobacion " + comprobar);
                    if(copiaNotas.get(i).categoria.equals(categoria)){
                        viewNotas.add(copiaNotas.get(i));
                    }

                }
                createView(viewNotas);
            }
        });

    }
    private void createView(ArrayList<Nota> notas){
        Collections.reverse(viewNotas);
        NoteAdapter adapter = new NoteAdapter(ListaNotasActivity.this, R.layout.noteadapter_layout, viewNotas);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Nota nota = viewNotas.get(i);
                editNote(nota);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l) {
                String nota = viewNotas.get(i).getTitulo();
                createDialog(nota);
                return true;
            }
        });
    }
    private void editNote(Nota nota){
        Intent intent = new Intent(this, EditarNotaActivity.class);
        Collections.reverse(viewNotas);
        for(int i=0; i<viewNotas.size(); i++){
            if(viewNotas.get(i) == nota){
                this.nota = gson.toJson(viewNotas.get(i));
                System.out.println("el extra que se va a enviar desde noteslist es "+ this.nota);
                intent.putExtra("nota", this.nota);
                intent.putExtra("categoria", viewNotas.get(i).getCategoria());
            }
        }
        Collections.reverse(viewNotas);
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }
    private void createDialog(String nota){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que deseas borrar la nota?");
        builder.setCancelable(true);

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for(int i=0; i<copiaNotas.size(); i++){
                    if(copiaNotas.get(i).titulo == nota){
                        copiaNotas.remove(i);
                    }
                }
                enviar = gson.toJson(copiaNotas);
                dbRef.child("users").child(userId).child("notas").setValue(enviar);
                Collections.reverse(viewNotas);
                for(int i=0; i<viewNotas.size(); i++){
                    if(viewNotas.get(i).titulo == nota){
                        viewNotas.remove(i);
                    }
                }
                for(int i=0; i<viewNotas.size(); i++){
                    System.out.println("Contenido viewNotas: "+i+" "+viewNotas.get(i).getTitulo());
                }
                createView(viewNotas);
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
            case R.id.logout:
                auth.signOut();
                initAuth();
                finish();
                return true;
            case R.id.action_settings1:
                initNewList();
                finish();
                return true;
            case R.id.action_settings2:
                initNewNote();
                finish();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                String categ = data.getStringExtra("categoria");
                copiaNotas = gson.fromJson(result, new TypeToken<ArrayList<Nota>>(){}.getType());
                viewNotas = new ArrayList<Nota>();
                for(int i=0; i<copiaNotas.size(); i++){
                    if(copiaNotas.get(i).categoria.equals(categ)){
                        viewNotas.add(copiaNotas.get(i));
                    }
                }
                createView(viewNotas);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
    private void initAuth(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

}