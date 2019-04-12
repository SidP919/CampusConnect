package com.example.android.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.android.campusconnect.FireBaseDBUtils.user;

public class HomeNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    User loggedInUser;
    String userEmail = "";
    ListView noticesListView;
    LinkedList<String> noticesStack;
    ArrayAdapter<String> noticeArrayAdapter;
    Map<String, String> noticeMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent homeIntent = getIntent();
//        userEmail = homeIntent.hasExtra("userEmail") && !homeIntent.equals(null)?
//                homeIntent.getStringExtra("userEmail"):"";

        if (!user.getEmail().isEmpty()) {
            loggedInUser = user;
            showAddNoticeOrNot(loggedInUser);
        }

        noticesListView = findViewById(R.id.app_bar_home_nav_ListView);
        noticesStack = new LinkedList<>();
        noticeMap = new HashMap<String, String>();

        noticeArrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_notice_listview_item, noticesStack);
        noticesListView.setAdapter(noticeArrayAdapter);

        readNotices();

        noticesListView.setOnItemClickListener(new MyListener());
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_nav, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_profile) {
            Log.d("HomeNavActivity------>", "MyProfile pressed");
            Intent intn = new Intent(this, MyProfileActivity.class);
            startActivity(intn);

        } else if (id == R.id.nav_change_pwd) {
            Log.d("HomeNavActivity------>", "ChangePwd pressed");
            Intent intn = new Intent(this, ResetPwdActivity.class);
            startActivity(intn);

        } else if (id == R.id.nav_log_out) {
            Log.d("HomeNavActivity------>", "Log out pressed");

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            FirebaseAuth fa = FirebaseAuth.getInstance();
            fa.signOut();
            finish();

//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showAddNoticeOrNot(User user) {

        TextView tv = findViewById(R.id.app_bar_home_nav_AddNoticeTextView);
        TextView tv2 = findViewById(R.id.app_bar_home_nav_DeleteUserTextView);
        if (user.getUserType().equalsIgnoreCase("Admin")) {
            tv.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(HomeNavActivity.this, AddNoticeActivity.class);
                    startActivity(in);
                }
            });
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(HomeNavActivity.this, DeleteAccountActivity.class);
                    startActivity(in);
                }
            });

        } else if (user.getUserType().equalsIgnoreCase("Student")) {
            tv.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
        }
    }

    private void readNotices() {

        final DatabaseReference noticeRef = FirebaseDatabase.getInstance().getReference("Notice");
        noticesStack.clear();

        ValueEventListener valEvtListnr = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noticeSnapshot : dataSnapshot.getChildren()) {

                    Notice n = noticeSnapshot.getValue(Notice.class);

                    String noticeTitle = (String) noticeSnapshot.child("title").getValue();
                    String time = n.getNoticeTime();
                    String userName = FireBaseDBUtils.user.getUserName();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    String currentTime = sdf.format(Long.parseLong(time));

                    noticesStack.addFirst(noticeTitle + "\nPosted By: " + userName + "\nTime: " + currentTime);
                    noticeMap.put(noticeTitle + "\nPosted By: " + userName + "\nTime: " + currentTime, time);
                    noticeArrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(HomeNavActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();

            }

        };

        noticeRef.addValueEventListener(valEvtListnr);
        //        deptRef2.addChildEventListener(childEventListener);
    }

    public class MyListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String noticeTitle = noticesStack.get(i);
            String noticeTime = noticeMap.get(noticeTitle);
            FireBaseDBUtils.getInstance().getNotice(noticeTime);
            Intent in = new Intent(HomeNavActivity.this, ShowNoticeActivity.class);
            startActivity(in);
        }
    }
}
