package com.example.android.campusconnect;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPwdActivity extends AppCompatActivity {

    EditText et1;

    FirebaseAuth fa;


    RelativeLayout r2;

    LinearLayout l2;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_pwd);


        l2 = findViewById(R.id.inProcessLayout3);

        r2 = findViewById(R.id.resetRelLayout);

        r2.setVisibility(View.VISIBLE);

        l2.setVisibility(View.INVISIBLE);


        et1 = findViewById(R.id.resetEmailEditText);

        fa = FirebaseAuth.getInstance();

    }


    public void hideSoftKeyboard(View view)

    {

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }


    public void resetPwd(View v) {

        hideSoftKeyboard(v);


        String s1 = et1.getText().toString();

        if (s1.isEmpty()) {

            Toast.makeText(this, "Enter your E-mail Id", Toast.LENGTH_SHORT).show();

        } else {

            r2.setVisibility(View.INVISIBLE);

            l2.setVisibility(View.VISIBLE);


            fa.sendPasswordResetEmail(s1).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override

                public void onComplete(@NonNull Task<Void> task) {


                    if (task.isSuccessful()) {

                        Toast.makeText(ResetPwdActivity.this, "Check your E-mail inbox", Toast.LENGTH_SHORT).show();

                        Toast.makeText(ResetPwdActivity.this, "You must have got a reset password link", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(ResetPwdActivity.this, "Check your E-mail id and try again", Toast.LENGTH_SHORT).show();

                        Toast.makeText(ResetPwdActivity.this, "Also, check your Internet Connection", Toast.LENGTH_SHORT).show();

                    }

                    r2.setVisibility(View.VISIBLE);

                    l2.setVisibility(View.INVISIBLE);

                }

            });

        }

    }

}
