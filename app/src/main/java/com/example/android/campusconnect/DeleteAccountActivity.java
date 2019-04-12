package com.example.android.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class DeleteAccountActivity extends AppCompatActivity {

    EditText deleteAccountET;
    String deletingEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        TextView deleteAccountTV = findViewById(R.id.resettextView);
        deleteAccountTV.setText("Want to delete the Account?");
        Button deleteAccountButton = findViewById(R.id.resetPwdButton);
        deleteAccountButton.setText("Yes, delete this account");
        deleteAccountET = findViewById(R.id.resetEmailEditText);

        if (FireBaseDBUtils.user.getUserType().equalsIgnoreCase("Student")) {
            deleteAccountET.setText(FireBaseDBUtils.user.getEmail());
            deleteAccountET.setEnabled(false);
        }

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginActivity.isNetworkConnected(getApplicationContext())) {
                    Toast.makeText(DeleteAccountActivity.this,
                            "Check your Internet Connection",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                deleteAccountET.setEnabled(true);
                deletingEmail = deleteAccountET.getText().toString();
                deleteAccountET.setEnabled(false);
                if (deletingEmail.isEmpty() || !deletingEmail.contains("@")) {
                    Toast.makeText(DeleteAccountActivity.this,
                            "Enter an E-mail id to delete an account",
                            Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference userRef =
                            FireBaseDBUtils.getInstance().getUserToBeDeleted(deletingEmail);
                    userRef.removeValue();

                    if (FireBaseDBUtils.user.getUserType().equalsIgnoreCase("Student") ||
                            (FireBaseDBUtils.user.getUserType().equalsIgnoreCase("Admin")
                                    && deletingEmail.equalsIgnoreCase(FireBaseDBUtils.user.getEmail()))) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("DeleteAccountActivity", "User account deleted.");
                                        }
                                    }
                                });
                        FirebaseAuth fa = FirebaseAuth.getInstance();
                        fa.signOut();
                        Toast.makeText(DeleteAccountActivity.this, "Your account has been deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DeleteAccountActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DeleteAccountActivity.this, "This Account will be deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DeleteAccountActivity.this, HomeNavActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
