package com.example.meetup.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.meetup.Model.Users;
import com.example.meetup.Network.ApiClient;
import com.example.meetup.Network.Apiservice;
import com.example.meetup.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutGoingInvitation extends AppCompatActivity {

    private String inviterToken = null;
    CircleImageView reject;
    String fullname, Email;
    String meetingRoom=null;

    Users users;


//    Apiservice apiService = ApiClient.getClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_going_invitation);
        reject = findViewById(R.id.imageRejectInvitation);
        ImageView imagemeetingType = findViewById(R.id.imageMeetingType);
        String meetingtype = getIntent().getStringExtra("type");

         SharedPreferences sp = getSharedPreferences("token", MODE_PRIVATE);
        inviterToken = (sp.getString("fcmToken", ""));





        if (meetingtype != null) {
            if (meetingtype.equals("video")) {
                imagemeetingType.setImageResource(R.drawable.ic_baseline_videocam);
            }
        }

        TextView textname = findViewById(R.id.textname);
        TextView textemail = findViewById(R.id.textemail);
//        Users user = (Users) getIntent().getSerializableExtra("user");
        String userss = getIntent().getStringExtra("user");
        Log.d("ankur", "onCreate: " + userss);

        if (meetingtype !=null && userss!=null){
            FirebaseFirestore.getInstance().collection("VideoUsers").document(userss)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                     users = document.toObject(Users.class);
                                    int length = users.getFullname().trim().length();
                                    Log.d("length", "onDataChange: " + length);
                                    fullname = users.getFullname();
                                    Email = users.getEmail();
                                    textname.setText(users.getFullname().substring(0, 1));
                                    textemail.setText(users.getEmail());
                                    initiateMeeting(meetingtype,users.getFcm_token());
//
                                }

                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Outgoing", "onFailure: " + e);

                        }
                    });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelInvitationResponse(users.getFcm_token());
                }
            });





//            Data data = new Data("invitation",meetingtype,fullname,Email,inviterToken);
//            DataMessageRequest request = new DataMessageRequest(userss, data, "high");
//            Call<ResponseBody> call = apiService.sendDataMessage(request);
//
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    // Handle success response
//                    Log.w("body", "onResponse: "+response );
////
//
//                        Toast.makeText(OutGoingInvitation.this, "Invitation sent successfully", Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    // Handle failure response
//                    Log.e("ayush", "onFailure: "+t );
//                    Toast.makeText(OutGoingInvitation.this, "Invitation not sent successfully", Toast.LENGTH_SHORT).show();
//
//                }
//            });




        }




    }

    private void initiateMeeting(String meetingType, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("type","invitation");
            data.put("meetingType", meetingType);
            data.put("name", fullname);
            data.put("email", Email);
            data.put("inviterToken", inviterToken);
            body.put("data", data);
            body.put("registration_ids", tokens);


            meetingRoom = FirebaseAuth.getInstance().getUid() +"_"+ UUID.randomUUID()
                            .toString().substring(0,5);

            data.put("meetingRoom",meetingRoom);
            Log.d("initiate", "initiateMeeting: "+"kaam kar rha hai");

            sendRemoteMessage(body.toString(),"invitation");

        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {


        ApiClient.getClient().create(Apiservice.class).sendRemoteMessage(
                (HashMap<String, String>) getRemoteMessageHeaders(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("initiate", "initiateMeetings: "+"kaam kar rha hai");
                    Log.w("body", "onResponse: "+remoteMessageBody );

                    if (type.equals("invitation")) {

                        Toast.makeText(OutGoingInvitation.this, "Invitation sent successfully", Toast.LENGTH_SHORT).show();
                    } else if (type.equals("invitationResponse")) {
                        Toast.makeText(OutGoingInvitation.this, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {


                        Toast.makeText(OutGoingInvitation.this, response.message(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(OutGoingInvitation.this, t.getMessage(), Toast.LENGTH_SHORT).show();


                finish();

            }
        });
    }
    private void cancelInvitationResponse(String receiverToken){
        try{
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("type","invitationResponse");

            data.put("invitationResponse","cancelled");

            body.put("data", data);
            body.put("registration_ids", tokens);

            sendRemoteMessage(body.toString(),"invitationResponse");

        }catch (Exception exception){
            Toast.makeText(this,exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();

        }
    }

    private Object getRemoteMessageHeaders() {
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization","key=AAAAJ8I4H3I:APA91bELnOayC37YNkNhKuydLhIdw9FasD9dQFjW5cQp-__tolbj-OR8uFRkMFRFhMVzmUMWOMotoCCHt2UwqHHc4mQFKodq6Kf-iS703KJiSQnw3fxqr9WetpzKJKfF9kERoB7kp1k9");
        headers.put("Content-Type","application/json");
        return headers;
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("invitationResponse");
            if (type!=null){
                if (type.equals("accepted")){
                    Log.d("responseReceiver", "onReceive: "+"accepted");
                    startActivity(new Intent(getApplicationContext(),AgoraImplementation.class));
                    Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                } else if (type.equals("rejected")) {
                    Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).
                registerReceiver(invitationResponseReceiver,new IntentFilter("invitationResponse"));
    }
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).
                unregisterReceiver(invitationResponseReceiver);
    }

}

