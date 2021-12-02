package com.juanmaltadill.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class NuevaListaActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.NoteApp.MESSAGE";
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private String name;
    private String save;
    private ArrayList<Categoria> categories = new ArrayList<Categoria>();
    private Categoria cat;
    private Gson gson;
    private int LAUNCH_ICON_ACTIVITY = 2;
    private ImageView img;
    private String result;
    private boolean flag = true;
    private EditText et1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_lista);
        auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();
        dbRef = FirebaseDatabase.getInstance("https://noteapp-16399-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        gson = new Gson();
        cat = new Categoria();
        et1 = findViewById(R.id.nombreListaEditText);
        dbRef.child("users").child(userId).child("categorias").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue()!=null){
                        save = task.getResult().getValue().toString();
                        System.out.println("El resultado de save en nuevalista es "+save);
                        if(save.startsWith("{")){
                            categories.add(gson.fromJson(save, Categoria.class));
                        }else{
                            categories.addAll(gson.fromJson(save, new TypeToken<ArrayList<Categoria>>(){}.getType()));
                        }
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
                        initSelectIcon();
                        break;
                    case R.id.saveBtn:
                        flag = true;
                        System.out.println("El tamaño de categories es "+categories);
                        for(int i=0; i<categories.size();i++){
                            if(categories.get(i).nombre.equals(et1.getText().toString())){
                                flag=false;
                                System.out.println("El estado de flag es "+flag);
                            }
                        }
                        if(result==null){
                            Toast.makeText(context(), "Debes asignar un icono a la lista",
                                    Toast.LENGTH_SHORT).show();
                        }else if(!flag){
                            Toast.makeText(context(), "Ya existe una categoría con ese nombre",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            name = et1.getText().toString();
                            cat.setNombre(name);
                            cat.setIcon(result);
                            String json = gson.toJson(cat);
                            System.out.println("la variable enviar es"+json);
                            initHome(json);
                            finish();
                            break;
                        }

                }
            }
        };
        img = findViewById(R.id.imageView);

        Button selectIconBtn = findViewById(R.id.selectIconBtn);
        selectIconBtn.setOnClickListener(onClickListener);
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(onClickListener);
    }
    private Context context(){
        return this;
    }
    private void initSelectIcon(){
        Intent intent = new Intent(this, SelectIconActivity.class);
        startActivityForResult(intent, LAUNCH_ICON_ACTIVITY);
    }
    private void initHome(String cat){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("categoria", cat);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ICON_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                result = data.getStringExtra("result");
                System.out.println("El extra que se recibe en nuevalista es "+result);

                switch(result){
                    case "avion":
                        img.setImageResource(R.drawable.avion);
                        break;
                    case "compras":
                        img.setImageResource(R.drawable.compras);
                        break;
                    case "engranajes":
                        img.setImageResource(R.drawable.engranajes);
                        break;
                    case "estudios":
                        img.setImageResource(R.drawable.estudios);
                        break;
                    case "gimnasio":
                        img.setImageResource(R.drawable.gimnasio);
                        break;
                    case "hotel":
                        img.setImageResource(R.drawable.hotel);
                        break;
                    case "juego":
                        img.setImageResource(R.drawable.juegos);
                        break;
                    case "libros":
                        img.setImageResource(R.drawable.libros);
                        break;
                    case "reunion":
                        img.setImageResource(R.drawable.reuniones);
                        break;
                    case "llamada":
                        img.setImageResource(R.drawable.llamadas);
                        break;
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}