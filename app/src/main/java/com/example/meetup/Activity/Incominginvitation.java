package com.example.meetup.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.meetup.Network.ApiClient;
import com.example.meetup.Network.Apiservice;
import com.example.meetup.R;

//import org.jitsi.meet.sdk.JitsiMeetActivity;
//import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Incominginvitation extends AppCompatActivity {

    TextView textname,textemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incominginvitation);
        textname = findViewById(R.id.textname);
        textemail = findViewById(R.id.textemail);

        ImageView imageMeetingType = findViewById(R.id.imageMeetingType);
        String meetingType = getIntent().getStringExtra("meetingType");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        if (name!=null){
            textname.setText(name.substring(0, 1));
        }

        textemail.setText(email);

//
        if (meetingType!=null) {
            if (meetingType.equals("video")) {
                imageMeetingType.setImageResource(R.drawable.ic_baseline_videocam);
            }
        }
        CircleImageView invitationAccept = findViewById(R.id.imageAcceptInvitation);
        CircleImageView invitationReject = findViewById(R.id.imageRejectInvitation);

        invitationAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitationResponse("accepted",getIntent().getStringExtra("inviterToken"));
            }
        });
        invitationReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitationResponse("rejected",getIntent().getStringExtra("inviterToken"));
            }
        });

    }
    private void sendInvitationResponse(String type, String receiverToken){
        try{
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("type","invitationResponse");

            data.put("invitationResponse",type);

            body.put("data", data);
            body.put("registration_ids", tokens);

            sendRemoteMessage(body.toString(),type);

        }catch (Exception exception){
            Toast.makeText(this,exception.getMessage(), Toast.LENGTH_SHORT).show();
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
                    if (type.equals("accepted")){
                        startActivity(new Intent(getApplicationContext(),AgoraImplementation.class));
                       try{


//                           RtcEngineConfig config = new RtcEngineConfig();
//                           config.mContext = getBaseContext();

////
                       }catch (Exception exception){
                           Toast.makeText(Incominginvitation.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }else{
                        Toast.makeText(Incominginvitation.this, "invitation Rejected", Toast.LENGTH_SHORT).show();
                    }
                }
                finish();



            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(Incominginvitation.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                finish();

            }
        });
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
                if (type.equals("cancelled")){
                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
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