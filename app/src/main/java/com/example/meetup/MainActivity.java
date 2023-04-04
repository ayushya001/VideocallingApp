package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetup.Activity.OutGoingInvitation;
import com.example.meetup.Activity.SigninActivity;
import com.example.meetup.Adapter.UserAdapter;
import com.example.meetup.Listner.UserListner;
import com.example.meetup.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements UserListner {

    TextView name, signout;
    String fcmToken;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Users> list;
    UserAdapter userAdapter;
    RecyclerView rv;

    SwipeRefreshLayout swipeRefreshLayout;

    private static final int TIME_INTERVAL = 2000;
    private long backPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.firstname);
        signout = findViewById(R.id.signout);
        rv = findViewById(R.id.rv);
        swipeRefreshLayout = findViewById(R.id.swipe);


//        preferenceManager.getString("firstname");
//        firstname.setText((CharSequence) firstname);
        list = new ArrayList<>();
        userAdapter = new UserAdapter(list, this);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(userAdapter);
        getusers();
//        swipeRefreshLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getusers();
//
//            }
//        });


        db.collection("VideoUsers").document(auth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Users user = document.toObject(Users.class);
                                name.setText(user.getFullname());
                            }
                        }
                    }
                });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                preferenceManager.clearPreferences();

                SharedPreferences sh = getSharedPreferences("token", MODE_PRIVATE);
                SharedPreferences.Editor editor = sh.edit();
                editor.clear();
                editor.commit();

                db.collection("VideoUsers").document(auth.getUid()).update("fcm_token", FieldValue.delete());

//                db.getReference().child("Users").child(auth.getUid()).child("fcm_token").removeValue();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SigninActivity.class));
                finish();

            }
        });


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("fc", "onSuccess: " + s);

                SharedPreferences sp = getSharedPreferences("token", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("fcmToken", s);
                ed.commit();
                db.collection("VideoUsers").document(auth.getUid())
                        .update("fcm_token", s);
//                ed.apply();

//                db.getReference().child("Users").child(auth.getUid()).child("fcm_token").setValue(s);


            }
        });
        Log.d("ftoken", "onCreate: " + fcmToken);


    }


    private void getusers() {
//        swipeRefreshLayout.setRefreshing(true);

        db.collection("VideoUsers").whereNotEqualTo("userid", auth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                list.add(dc.getDocument().toObject(Users.class));
                            }
                        }
                        userAdapter.notifyDataSetChanged();


                    }
                });
    }

    @Override
    public void initiateVideoMeeting(Users user) {

        if (user.getFcm_token() == null || user.getFcm_token().trim().isEmpty()) {
            Toast.makeText(this, user.getName() + " is not available for video call", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), OutGoingInvitation.class);
            intent.putExtra("user", user.getUserid());
            intent.putExtra("type", "video");
            startActivity(intent);

        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();


        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
//            finish();

            return;
        } else {
            Toast.makeText(this, "Press again to exit the app", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();


    }

    @Override
    public void initiateAudioMeeting(Users user) {

        if (user.getFcm_token() == null || user.getFcm_token().trim().isEmpty()) {
            Toast.makeText(this, user.getName() + "is not available for Audio call", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Audio call with" + user.getFullname(), Toast.LENGTH_SHORT).show();
        }


    }


}
