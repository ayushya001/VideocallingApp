package com.example.meetup.Firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.meetup.Activity.Incominginvitation;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService{
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("ayush", "onMessageReceived: "+"kaam kr rha hai");




        if (message.getData().size() > 0) {
            Log.d("ayush", "onMessageReceived: "+message.getData().toString());
        }
            String type = message.getData().get("type");
            String meetingType = message.getData().get("meetingType");
            String name = message.getData().get("name");
            String email =message.getData().get("email");
            String inviterToken = message.getData().get("inviterToken");

        if (type!=null){
//            Toast.makeText(this, "ayush", Toast.LENGTH_SHORT).show();
            if (type.equals("invitation")){
                Intent intent = new Intent(getApplicationContext(), Incominginvitation.class);
                intent.putExtra("meetingType",meetingType);
                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("inviterToken",inviterToken);
                intent.putExtra("meetingRoom",message.getData().get("meetingRoom"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (type.equals("invitationResponse")) {
                Intent intent2  = new Intent("invitationResponse");
                intent2.putExtra("invitationResponse",message.getData().get("invitationResponse"));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);

            }
        }


        }




//



//         Also if you intend on generating your own notifications as a result of a received FCM
//         message, here is where that should be initiated. See sendNotification method below.


}




