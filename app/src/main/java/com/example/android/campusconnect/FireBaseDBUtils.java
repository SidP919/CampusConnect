package com.example.android.campusconnect;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseDBUtils {
    public static User user;
    private static FireBaseDBUtils fireBaseDBUtils;
    private final String TAG = "FireBaseDBUtils";
    private FirebaseDatabase database;
//    private GeneralUtils utils;

    private FireBaseDBUtils() {
        this.database = FirebaseDatabase.getInstance();
//        utils= new GeneralUtils();
    }

    public static FireBaseDBUtils getInstance() {
        if (fireBaseDBUtils == null)
            fireBaseDBUtils = new FireBaseDBUtils();
        return fireBaseDBUtils;
    }

    public void addUser(User user) {
        Log.d(TAG, "addUser: ");
        DatabaseReference userRef = database.getReference("User");

        if (!TextUtils.isEmpty(user.getEmail())) {
//            String hashEmailId=generateHashkey(user.getEmailId());
            Log.d(TAG, "addUser: EmailId" + user.getUserName());
            userRef.child(user.getEmail()).setValue(user);
        } else {
            Log.d(TAG, "addUser: empty emailId");
        }
    }

    public DatabaseReference getUser(String email) {
        DatabaseReference userRef = database.getReference("User").child(email);
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                user = dataSnapshot.getValue(User.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        return userRef;
    }
}
