package com.example.meetup.Listner;


import com.example.meetup.Model.Users;


public interface UserListner {

    void initiateVideoMeeting(Users user);

    void initiateAudioMeeting(Users user);
}
