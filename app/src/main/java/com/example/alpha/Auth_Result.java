package com.example.alpha;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Auth_Result extends AppCompatActivity {

    Intent gi;

    TextView welcomeMsg;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_result);
        gi = getIntent();
        welcomeMsg = findViewById(R.id.textView);
        welcomeMsg.setText("Welcome, " + gi.getStringExtra("name") + "!"); // welcome the user
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void signout(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    @Override
    protected void onDestroy () {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        finish();
        super.onBackPressed();
    }
}