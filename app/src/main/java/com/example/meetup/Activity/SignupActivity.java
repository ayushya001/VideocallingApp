package com.example.meetup.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetup.MainActivity;
import com.example.meetup.Model.Users;
import com.example.meetup.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupActivity extends AppCompatActivity {
    EditText name,fullname,email,password,cpassword;
    Button signupbtn;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseFirestore db= FirebaseFirestore.getInstance();
//    FirebaseDatabase db = FirebaseDatabase.getInstance();
//    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        TextView signinpage = findViewById(R.id.signinpage);
        name = findViewById(R.id.name);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        signupbtn = findViewById(R.id.signin);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isvalid();
            }
        });
        signinpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void isvalid() {

        if (name.getText().toString().trim().isEmpty()) {
            name.setError("name must not be blank");
        } else if (fullname.getText().toString().trim().isEmpty()) {
            fullname.setError("Full name must not be empty");
        } else if (email.getText().toString().isEmpty()) {
            email.setError("gmail must not be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Gmail is invalid");

        } else if (password.getText().toString().trim().isEmpty()) {
            password.setError("password must not be empty");
        } else if (password.getText().toString().length() < 6) {
            password.setError("Password should be at least 6 characters ");
        } else if (cpassword.getText().toString().trim().isEmpty()) {
            cpassword.setError("Confirm your password");
        } else if (!cpassword.getText().toString().trim().equals(password.getText().toString().trim())) {
            cpassword.setError("password and confirm password must be same");
        } else {
            ProgressDialog d = new ProgressDialog(this);
            d.setTitle("Signingup");
            d.setCanceledOnTouchOutside(false);
            d.show();
            String Password = password.getText().toString();
            String Email = email.getText().toString();
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Users user = new Users();
                    user.setUserid(mAuth.getUid());
                    user.setName(name.getText().toString());
                    user.setFullname(fullname.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(password.getText().toString());
                    Log.d("check", "isvalid: " + mAuth.getUid());
                    db.collection("VideoUsers").document(mAuth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            SharedPreferences sp = getSharedPreferences("saved", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("firstname", name.getText().toString());
                            editor.putString("fullname", fullname.getText().toString());
                            editor.putString("email", email.getText().toString());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            d.dismiss();
                            Toast.makeText(SignupActivity.this, "Signing up successfully", Toast.LENGTH_SHORT).show();
                            editor.commit();

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Signup", "onFailure: " + e.toString());
                    Toast.makeText(SignupActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
}