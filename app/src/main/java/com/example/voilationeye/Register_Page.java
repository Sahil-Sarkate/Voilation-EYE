package com.example.voilationeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register_Page extends AppCompatActivity {

    EditText username, password;
    Button register;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        username=findViewById(R.id.registerUsername);
        password=findViewById(R.id.registerPassword);

        register = findViewById(R.id.registerBtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = username.getText().toString();
                String psw = password.getText().toString();
                registerUser(em, psw);
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success
                            FirebaseUser user = mAuth.getCurrentUser();
                            // You can do additional actions here, like sending a verification email
                        } else {
                            // Registration failed
                            Toast.makeText(Register_Page.this, "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }
}