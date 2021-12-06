package com.iesmurgi.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.iesmurgi.noteapp.R;

public class AuthActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.NoteApp.MESSAGE";
    private FirebaseAuth mAuth;
    public EditText mail;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        mAuth = FirebaseAuth.getInstance();
        Button signUp = findViewById(R.id.signUpButton);
        mail = findViewById(R.id.emailEditText);
        pass = findViewById(R.id.passwordEditText);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.signUpButton:
                        initSignUp();
                        break;
                    case R.id.logInButton:
                        signIn(mail.getText().toString(), pass.getText().toString());
                        break;
                }
            }
        };
        signUp.setOnClickListener(onClickListener);
        Button logIn = findViewById(R.id.logInButton);
        logIn.setOnClickListener(onClickListener);
    }



    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            initHome(email);
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("createUserWithEmail:failure");
                            Toast.makeText(AuthActivity.this, "El usuario o la contraseña no son correctos",
                                        Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void initSignUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    private void initHome(String email){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(EXTRA_MESSAGE, email);
        startActivity(intent);
    }
}