package com.example.meetup.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetup.Listner.UserListner;
import com.example.meetup.Model.Users;
import com.example.meetup.R;



import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    ArrayList<Users> list;
    UserListner userListner;

    public UserAdapter(ArrayList<Users> list, UserListner userListner) {
        this.list = list;
        this.userListner = userListner;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_container,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final Users users = list.get(position);

        holder.textemail.setText(users.getEmail());
        holder.textfullname.setText(users.getFullname());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userListner.initiateAudioMeeting(users);
            }
        });
        holder.videocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userListner.initiateVideoMeeting(users);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView userprofile,call,videocall;
        TextView textfullname,textemail;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            userprofile = itemView.findViewById(R.id.profile_image);
            call = itemView.findViewById(R.id.call);
            videocall = itemView.findViewById(R.id.video);
            textfullname = itemView.findViewById(R.id.fullname);
            textemail = itemView.findViewById(R.id.textemail);
        }
    }
}
