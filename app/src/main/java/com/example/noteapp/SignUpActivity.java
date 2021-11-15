package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.NoteApp.MESSAGE";
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private String name;
    private String email;
    private String pass;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.registerBtn:
                        EditText nameEt = findViewById(R.id.nameEditText);
                        name = nameEt.getText().toString();
                        EditText mailEt = findViewById(R.id.emailSUEditText);
                        email = mailEt.getText().toString();
                        EditText passEt = findViewById(R.id.passEditText);
                        pass = passEt.getText().toString();
                        EditText phoneEt = findViewById(R.id.nameEditText);
                        name = nameEt.getText().toString();
                        createAccount(email, pass);
                        break;
                }
            }
        };
        Button signUpButton = findViewById(R.id.registerBtn);
        signUpButton.setOnClickListener(onClickListener);
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance("https://noteapp-16399-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        email = auth.getCurrentUser().getEmail();
    }
    private void createAccount(String email, String password){
        System.out.println(email + password);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            writeNewUser(auth.getCurrentUser().getUid().toString(), name, email);
                            initHome(name, email, password, phone);
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("createUserWithEmail:failure");
                            if(password.length()<10){
                                Toast.makeText(SignUpActivity.this, "La contraseña debe tener al menos 10 caracteres",
                                        Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(SignUpActivity.this, "El registro ha fallado",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }
    private void initHome(String name, String email, String pass, String phone){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(EXTRA_MESSAGE, name);
        intent.putExtra(EXTRA_MESSAGE, email);
        intent.putExtra(EXTRA_MESSAGE, pass);
        intent.putExtra(EXTRA_MESSAGE, phone);
        startActivity(intent);
    }
    public void writeNewUser(String userId, String name, String email) {
        System.out.println(auth.getCurrentUser().getEmail().toString());
        User user = new User(name, email);
        dbRef.child("users").child(userId).setValue(user);
        dbRef.child("users").child("email").setValue(email);
        dbRef.child("users").child("name").setValue(name);
    }
}