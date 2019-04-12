package com.example.android.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoticeActivity extends AppCompatActivity {

    String title;
    String body;
    EditText titleET;
    EditText bodyET;

    public String getCurrentTime() {
        return ("" + System.currentTimeMillis());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        titleET = findViewById(R.id.add_notice_titleET);
        bodyET = findViewById(R.id.add_notice_bodyET);
        Button addNotice = findViewById(R.id.add_notice_addNoticeButton);
        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleET.getText().toString();
                body = bodyET.getText().toString();
                if (title.isEmpty() || body.isEmpty()) {
                    Toast.makeText(AddNoticeActivity.this, "Fill both fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (!LoginActivity.isNetworkConnected(getApplicationContext())) {
                        Toast.makeText(AddNoticeActivity.this,
                                "Provide Internet Connection to Add Notice",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Notice notice = new Notice(title, body, getCurrentTime());
                        FireBaseDBUtils.getInstance().addNotice(notice);
                        Toast.makeText(AddNoticeActivity.this, "Notice has been added", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(AddNoticeActivity.this, HomeNavActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();
                    }
                }
            }
        });
    }
}
