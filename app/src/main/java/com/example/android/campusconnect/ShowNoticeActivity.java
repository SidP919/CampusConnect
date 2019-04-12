package com.example.android.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class ShowNoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);

        TextView tv3 = findViewById(R.id.textView3);
        TextView tv4 = findViewById(R.id.textView4);

        Intent intent = getIntent();
        final String title = FireBaseDBUtils.notice.getTitle();//intent.getStringExtra("NoticeTitle");
        String body = FireBaseDBUtils.notice.getBody();//intent.getStringExtra("NoticeBody");

        tv3.setText(title);
        tv4.setText(body);

        Button deleteBtn = findViewById(R.id.show_notice_deleteNoticeButton);
        if (FireBaseDBUtils.user.getUserType().equalsIgnoreCase("Student")) {
            deleteBtn.setVisibility(View.INVISIBLE);
        } else {
            deleteBtn.setVisibility(View.VISIBLE);
        }
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference noticeRef = FireBaseDBUtils.getInstance().getNoticeToBeDeleted(FireBaseDBUtils.notice.getNoticeTime());
                noticeRef.removeValue();
                Intent in = new Intent(ShowNoticeActivity.this, HomeNavActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }
        });
    }
}
