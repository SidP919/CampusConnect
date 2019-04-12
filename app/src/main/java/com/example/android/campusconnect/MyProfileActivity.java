package com.example.android.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Button delAccountButton = findViewById(R.id.my_profile_deleteAccountButton);
        delAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MyProfileActivity.this, DeleteAccountActivity.class);
                startActivity(in);
            }
        });

        EditText nameET = findViewById(R.id.my_profile_nameET);
        nameET.setText(FireBaseDBUtils.user.getUserName());

        EditText rollnoET = findViewById(R.id.my_profile_rollnoET);
        rollnoET.setText(FireBaseDBUtils.user.getRollNo());

        EditText emailET = findViewById(R.id.my_profile_emailET);
        emailET.setText(FireBaseDBUtils.user.getEmail());

        EditText profileTypeET = findViewById(R.id.my_profile_profileTypeET);
        profileTypeET.setText(FireBaseDBUtils.user.getUserType());

    }
}
