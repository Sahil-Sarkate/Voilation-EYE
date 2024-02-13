package com.example.voilationeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class login extends AppCompatActivity {
    Button login;
    String id;
    private String username, password, confirmPassword, phoneNumber;
    public static String email;
    static boolean loginFlag = false;
    private FirebaseAuth firebaseAuth;
    TextView forgotPassword;
    LinearLayout google;
    GoogleSignInClient googleSignInClient;
    private String emailk, passwordk;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        google = findViewById(R.id.google);

        if(loginFlag == true)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.email);
                email = editText.getText().toString();
                editText = (EditText) findViewById(R.id.password);
                password = editText.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                    Toast.makeText(getApplicationContext(),
                            "Incorrect credentials :(",
                            Toast.LENGTH_SHORT).show();
                else
                    loginUser();
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions googleSignInOptions =
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken("268566876511-l41nb4mjfq48hc6h6388f7la9euefm1q.apps.googleusercontent.com")
                                .requestEmail()
                                .build();

                // Initialize sign in client
                googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);
                Intent intent = googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent, 100);

            }
        });
    }
    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);

        // write the email using which you registered
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(getApplicationContext(),"Email sent!",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginUser()
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        Toast.makeText(getApplicationContext(),
                                "Login successful :)",
                                Toast.LENGTH_LONG).show();
                        loginFlag = true;
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
    }
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                Log.d("sign-in-using-google","called");
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check condition
                                        if (task.isSuccessful()) {
                                            // When task is successful redirect to profile activity display Toast
                                            displayToast("Firebase authentication successful");
                                            loginFlag = true;
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        } else {
                                            // When task is `unsuccessful` display Toast
                                            displayToast("Authentication Failed :"
                                                    + task.getException().getMessage());
                                        }

                                        // Initialize firebase auth
                                        firebaseAuth = FirebaseAuth.getInstance();

                                        // Initialize firebase user
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                        // Check condition
                                        if (firebaseUser != null) {
                                            // set name on text view
                                            username = firebaseUser.getDisplayName();
                                            email = firebaseUser.getEmail();
                                            phoneNumber = "";
                                            id = firebaseUser.getUid();
                                            //saveUserInformation();
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            else {
                displayToast("unsuccessful");
                Log.d("message is ", String.valueOf(signInAccountTask.getException()));
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private boolean validateData() {
        if(email.length() == 0 || password.length() == 0 || phoneNumber.length() == 0 ||
                !confirmPassword.equals(password))
        {
            displayToast("Incorrect credentials");
            return true;
        }
        // firebase authentication needs password of at least 6 characters
        else if(password.length() < 6)
        {
            displayToast("Password too short!");
            return true;
        }

        return false;
    }

}