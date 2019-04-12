package com.example.android.campusconnect;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText et1, et2;

    FirebaseAuth fa;

    RelativeLayout loginLayout;

    LinearLayout inProcessLayout;

    String s1 = "";

    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnectedOrConnecting();
    }

    public void hideSoftKeyboard(View view)

    {

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void goToSignUpActivity(View v) {

        hideSoftKeyboard(v);

        Intent intn = new Intent(LoginActivity.this, SignupActivity.class);

        startActivity(intn);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inProcessLayout = findViewById(R.id.inProcessLayout);

        loginLayout = findViewById(R.id.loginRelLayout);

        loginLayout.setVisibility(View.VISIBLE);

        inProcessLayout.setVisibility(View.INVISIBLE);


        et1 = findViewById(R.id.loginEmailEditText);

        et2 = findViewById(R.id.loginPwdEditText);

        fa = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fa.getCurrentUser();
        if (currentUser != null) {

            if (!isNetworkConnected(getApplicationContext())) {
                Toast.makeText(LoginActivity.this,
                        "Provide internet connection and try again",
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                FireBaseDBUtils.getInstance().getUser(currentUser.getEmail());

                loginLayout.setVisibility(View.INVISIBLE);

                inProcessLayout.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something below after 5000ms
                        //sgdsfds
                        Intent intn = new Intent(LoginActivity.this, HomeNavActivity.class);
                        startActivity(intn);
                        finish();
                        Toast.makeText(LoginActivity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                    }
                }, 8000);
            }
        }
    }

    public void login(View v) {
        if (!isNetworkConnected(getApplicationContext())) {
            Toast.makeText(LoginActivity.this,
                    "Provide internet connection and try again",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        hideSoftKeyboard(v);

        s1 = et1.getText().toString();

        String s2 = et2.getText().toString();

        if (s1.isEmpty() || s2.isEmpty()) {

            Toast.makeText(this, "Fill all the Fields", Toast.LENGTH_SHORT).show();

        } else {

            loginLayout.setVisibility(View.INVISIBLE);

            inProcessLayout.setVisibility(View.VISIBLE);

            fa.signInWithEmailAndPassword(s1, s2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override

                public void onComplete(Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        FireBaseDBUtils.getInstance().getUser(s1);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something below after 5000ms
                                //sgdsfds
                                Intent intn = new Intent(LoginActivity.this, HomeNavActivity.class);
                                startActivity(intn);
                                finish();
                                Toast.makeText(LoginActivity.this, "Welcome to JSS ATE, NOIDA", Toast.LENGTH_SHORT).show();
                            }
                        }, 10000);

                    } else {

                        Toast.makeText(LoginActivity.this, "Login Failed! Check your E-mail id and password", Toast.LENGTH_SHORT).show();

                        loginLayout.setVisibility(View.VISIBLE);

                        inProcessLayout.setVisibility(View.INVISIBLE);

                    }
                }

            });
        }
    }

    public void resetPwd(View v) {

        hideSoftKeyboard(v);
        Intent intn = new Intent(LoginActivity.this, ResetPwdActivity.class);
        startActivity(intn);
    }
}
