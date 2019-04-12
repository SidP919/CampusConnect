package com.example.android.campusconnect;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.util.Formatter;

public class FireBaseDBUtils {
    public static User user;// = new User();
    public static Notice notice = new Notice();
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

    String hashEmailId;

    public void addUser(User user) {
        Log.d(TAG, "addUser: ");
        DatabaseReference userRef = database.getReference("User");

        if (!TextUtils.isEmpty(user.getEmail())) {
            hashEmailId = generateHashkey(user.getEmail());
            Log.d(TAG, "addUser: EmailId" + user.getUserName() + " --- " + hashEmailId);
            userRef.child(hashEmailId).setValue(user);
        } else {
            Log.d(TAG, "addUser: empty emailId");
        }
        userRef.child(hashEmailId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "User created--" + hashEmailId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Unable to create user--" + databaseError);
            }
        });
    }

    public DatabaseReference getUser(String email) {
        DatabaseReference userRef = database.getReference("User").child(generateHashkey(email));
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return userRef;
    }

    public DatabaseReference getUserToBeDeleted(String email) {
        DatabaseReference dbRef = database.getReference("User").child(generateHashkey(email));
        return dbRef;
    }

    public void addNotice(Notice notice) {
        Log.d(TAG, "addNotice: ");
        DatabaseReference userRef = database.getReference("Notice");
        final String noticeTime = notice.getNoticeTime();
        userRef.child(noticeTime).setValue(notice);
        userRef.child(noticeTime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Notice created--" + noticeTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Unable to create notice--" + databaseError);
            }
        });
    }

    public DatabaseReference getNotice(String noticeTime) {
        DatabaseReference noticeRef = database.getReference("Notice").child(noticeTime);
        noticeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notice = dataSnapshot.getValue(Notice.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return noticeRef;
    }

    public DatabaseReference getNoticeToBeDeleted(String noticeTime) {
        DatabaseReference noticeRef = database.getReference("Notice").child(noticeTime);
        return noticeRef;
    }

    private String generateHashkey(String email) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(email.getBytes("UTF-8"));
            return byteToHex(hash); // make it printable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public String byteToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String hex = formatter.toString();
        return hex;
    }
}
