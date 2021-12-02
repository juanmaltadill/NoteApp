package com.juanmaltadill.noteapp;

import static com.juanmaltadill.noteapp.SelectDateAndHourActivity.PUBLIC_STATIC_STRING_IDENTIFIER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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

import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class NuevaNotaActivity extends AppCompatActivity {
    private final int STATIC_INTEGER_VALUE = 1;
    private String titulo;
    private String contenido;
    private boolean vence;
    private String id;
    private String fecha;
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
    private ArrayList<Note> copiaNotas = new ArrayList<Note>();
    private TextView tv2;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("");
    boolean flag;
    private TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_nota);
        this.setTitle("Nueva nota");
        EditText et1 = findViewById(R.id.tituloNuevaNotaEt);
        EditText et2 = findViewById(R.id.contenidoNuevaNotaEt);
        Button btn1 = findViewById(R.id.seleccionarFechaBtn);
        tv1 = findViewById(R.id.fechaNuevaNotaTv);
        Button button = findViewById(R.id.crearNuevaNotaBtn);
        Switch check = findViewById(R.id.venceNuevaNotaSwitch);
        Spinner categoria = findViewById(R.id.categoriaNuevaNotaSpinner);
        userId = auth.getUid();
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 if(b){
                    btn1.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);
                 }if(!b){
                     tv1.setVisibility(View.INVISIBLE);
                     btn1.setVisibility(View.INVISIBLE);
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
                        copiaNotas.addAll(gson.fromJson(save, new TypeToken<ArrayList<Note>>(){}.getType()));
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.seleccionarFechaBtn:
                        initSeleccionarFecha();
                        break;
                }

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                for(int i=0; i<copiaNotas.size(); i++){
                    if(copiaNotas.get(i).getTitulo().equals(et1.getText().toString())&&copiaNotas.get(i).getCategoria().equals(categoria.getSelectedItem().toString())){
                        flag = false;
                        Toast toast=Toast.makeText(getApplicationContext(),"Ya existe una nota con ese título en esta categoría",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                if(et1.getText().toString().equals(null) || et1.getText().toString() == "" || et1.getText().toString().length()<1){
                    flag = false;
                    Toast toast=Toast.makeText(getApplicationContext(),"El título de la nota está vacío",Toast.LENGTH_LONG);
                    toast.show();
                }
                if(listaCategorias.size()<1){
                    flag = false;
                    Toast toast=Toast.makeText(getApplicationContext(),"No existe ninguna categoría a la que asignar la nota",Toast.LENGTH_LONG);
                    toast.show();
                }
                if(flag){
                    titulo = et1.getText().toString();
                    contenido = et2.getText().toString();
                    vence = check.isChecked();
                    if(vence){
                        fecha = tv1.getText().toString();
                    }else{
                        fecha = null;
                    }
                    cat = categoria.getSelectedItem().toString();
                    nota = new Note(titulo, contenido, vence, fecha, cat);
                    String json = gson.toJson(nota);
                    initNotesList(json, cat);
                    finish();
                }

            }
        });
    }

    public void adaptarSpinner(Spinner spinner){
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaCategorias);
        spinner.setAdapter(adaptador);
    }

    public void initNotesList(String json, String categoria){
        Intent intent = new Intent(this, NotesListActivity.class);
        intent.putExtra("nota", json);
        intent.putExtra("categoria", categoria);
        startActivity(intent);
    }
    public void initSeleccionarFecha(){
        Intent intent = new Intent(this, SelectDateAndHourActivity.class);
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

//    public void crearAlarma(){
//        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 02);
//        calendar.set(Calendar.MINUTE, 00);
//
//        alarmMgr.set
//        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis()/1000,
//                AlarmManager.INTERVAL_DAY, alarmIntent);
//    }
}