package com.example.android.campusconnect;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    EditText et1, et2, et3, et4;

    FirebaseAuth fa;

    LinearLayout l;

    RelativeLayout r;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);


        l = findViewById(R.id.inProcessLayout2);

        r = findViewById(R.id.signupRelLayout);


        et1 = findViewById(R.id.signupEmailEditText);

        et2 = findViewById(R.id.signupPwdEditText);

        et3 = findViewById(R.id.signupNameEditText);

        et4 = findViewById(R.id.signupRollNoEditText);

        fa = FirebaseAuth.getInstance();

    }


    public void hideSoftKeyboard(View view)

    {

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }


    public void register(View view) {

        hideSoftKeyboard(view);

        final String s1 = et1.getText().toString();

        String s2 = et2.getText().toString();

        final String s3 = et3.getText().toString();

        final String s4 = et4.getText().toString();

        final String s5 = getString(R.string.user_type_default);

        if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty() || s4.isEmpty()) {

            Toast.makeText(this, "Fill all the Fields", Toast.LENGTH_SHORT).show();

        } else {

            if (s2.length() < 6)

                //et2.setError("Password should atleast have 6 characters");

                Toast.makeText(this, "Password should atleast have 6 characters", Toast.LENGTH_LONG).show();

            else {

                r.setVisibility(View.INVISIBLE);

                l.setVisibility(View.VISIBLE);

                fa.createUserWithEmailAndPassword(s1, s2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        r.setVisibility(View.VISIBLE);

                        l.setVisibility(View.INVISIBLE);

                        if (task.isSuccessful()) {

                            FireBaseDBUtils.getInstance().addUser(new User(s3, s4, s1, s5));
                            Toast.makeText(SignupActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();

                            Toast.makeText(SignupActivity.this, "Welcome to ABC college", Toast.LENGTH_SHORT).show();

                            Intent intn = new Intent(SignupActivity.this, MainActivity.class);

                            intn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intn);
                            finish();

                        } else {

                            Toast.makeText(SignupActivity.this, "Unable to create new user!", Toast.LENGTH_SHORT).show();

                            Toast.makeText(SignupActivity.this, "Check your internet, E-mail Id and try again", Toast.LENGTH_SHORT).show();

                        }

                    }

                });

            }

        }

    }

}
