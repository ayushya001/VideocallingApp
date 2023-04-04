package com.example.meetup.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.meetup.MainActivity;
import com.example.meetup.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {
    TextView signuppage;
    EditText email, password;
    MaterialButton signin;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        pd = new ProgressDialog(this);
        pd.setTitle("Signing in");
        pd.setCanceledOnTouchOutside(false);

        signuppage = findViewById(R.id.signuppage);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signuppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigingin();
            }

            private void sigingin() {
                if (email.getText().toString().trim().isEmpty()) {
                    email.setError("Enter your email Id");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    Toast.makeText(SigninActivity.this, "Email id is not valid", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Password must not be Blank");
                } else {

                    pd.show();
                    String Email = email.getText().toString();
                    String Password = password.getText().toString();
                    pd.show();
                    auth.signInWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            pd.dismiss();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            Toast.makeText(SigninActivity.this, "Email and password cannot match", Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }

}