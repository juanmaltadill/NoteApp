package com.iesmurgi.noteapp;

import static com.iesmurgi.noteapp.SeleccionarFechaHoraActivity.PUBLIC_STATIC_STRING_IDENTIFIER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditarNotaActivity extends AppCompatActivity {
    private final int STATIC_INTEGER_VALUE = 1;
    private String titulo;
    private String contenido;
    private boolean vence;
    private String id;
    private String fecha;
    private String cat;
    private Gson gson = new Gson();
    private String enviar;
    private Nota nota;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId;
    private String save;
    private ArrayList<Categoria> categorias = new ArrayList<Categoria>();
    private ArrayList<String> listaCategorias = new ArrayList<String>();
    private ArrayList<Nota> copiaNotas = new ArrayList<Nota>();
    private TextView tv2;
    private EditText et1;
    private EditText et2;
    private Button btn1;
    private TextView tv1;
    private Button button;
    private Switch check;
    private Spinner categoria;
    private String copiaCategoria;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        this.setTitle("Editar nota");
        et1 = findViewById(R.id.editarTituloNotaEt);
        et2 = findViewById(R.id.editarContenidoNotaEt);
        btn1 = findViewById(R.id.editarFechaBtn);
        tv1 = findViewById(R.id.editarFechaNotaTv);
        tv2 = findViewById(R.id.editarfechaHoraTv);
        button = findViewById(R.id.editarNotaBtn);
        check = findViewById(R.id.editarVenceNotaSwitch);
        categoria = findViewById(R.id.editarCategoriaNotaSpinner);
        String extra = getIntent().getExtras().getString("nota");
        copiaCategoria = getIntent().getExtras().getString("categoria");
        userId = auth.getUid();
        nota = gson.fromJson(extra, Nota.class);
        System.out.println(extra);
        et1.setText(nota.getTitulo());
        System.out.println("El título de la nota a editar es: "+nota.getTitulo());
        et2.setText(nota.getContenido());
        check.setChecked(nota.vence);
        if(nota.vence){
            btn1.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
        }else{
            tv1.setVisibility(View.INVISIBLE);
            btn1.setVisibility(View.INVISIBLE);
        }
        if(nota.vence){
            tv1.setText(nota.getFechaVencimiento().toString());
        }


        dbRef.child("users").child(userId).child("categorias").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue()!=null){
                        save = task.getResult().getValue().toString();
                        categorias.addAll(gson.fromJson(save, new TypeToken<ArrayList<Categoria>>(){}.getType()));
                        for(int i=0; i<categorias.size(); i++){
                            listaCategorias.add(categorias.get(i).getNombre());
                        }
                        adaptarSpinner(categoria);
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        dbRef.child("users").child(userId).child("notas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue()!=null){
                        save = task.getResult().getValue().toString();
                        copiaNotas.addAll(gson.fromJson(save, new TypeToken<ArrayList<Nota>>(){}.getType()));
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.editarFechaBtn:
                        initSeleccionarFecha();
                        break;
                }

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                if(et1.getText().toString().equals(null) || et1.getText().toString() == "" || et1.getText().toString().length()<1){
                    flag = false;
                    Toast toast=Toast.makeText(getApplicationContext(),"El título de la nota está vacío",Toast.LENGTH_LONG);
                    toast.show();
                }
                titulo = et1.getText().toString();
                contenido = et2.getText().toString();
                vence = check.isChecked();
                if(vence){
                    fecha = tv1.getText().toString();
                }else{
                    fecha = null;
                }
                if (flag){
                    cat = categoria.getSelectedItem().toString();
                    Nota comparar = gson.fromJson(extra, Nota.class);
                    for(int i=0; i<copiaNotas.size(); i++){
                        System.out.println("CopiaNotas editnote titulo "+copiaNotas.get(i).getTitulo());
                        if(copiaNotas.get(i).getTitulo().equals(comparar.getTitulo())){
                            System.out.println("CopiaNotas editnote titulo "+copiaNotas.get(i).getTitulo());
                            copiaNotas.get(i).setTitulo(titulo);
                            copiaNotas.get(i).setContenido(contenido) ;
                            copiaNotas.get(i).setVence(vence);
                            if(vence){
                                copiaNotas.get(i).setFechaVencimiento(fecha);
                            }else{
                                copiaNotas.get(i).setFechaVencimiento(null);
                            }
                            copiaNotas.get(i).setCategoria(cat);
                            System.out.println("CopiaNotas editnote nuevo titulo "+copiaNotas.get(i).getTitulo());
                        }
                    }

                    String json = gson.toJson(copiaNotas);
                    dbRef.child("users").child(userId).child("notas").setValue(json);
                    initNotesList(json);
                    finish();
                }



            }
        });
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    btn1.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);
                }else{
                    tv1.setVisibility(View.INVISIBLE);
                    btn1.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void adaptarSpinner(Spinner spinner){
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaCategorias);
        spinner.setAdapter(adaptador);
    }

    public void initNotesList(String notasEdit){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", notasEdit);
        returnIntent.putExtra("categoria", cat);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    public void initSeleccionarFecha(){
        Intent intent = new Intent(this, SeleccionarFechaHoraActivity.class);
        startActivityForResult(intent, STATIC_INTEGER_VALUE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (STATIC_INTEGER_VALUE) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    tv1.setText(newText);
                } break;
            }
        }
    }

}