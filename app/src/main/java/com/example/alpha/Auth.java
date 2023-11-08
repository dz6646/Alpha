package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth extends AppCompatActivity {

    Intent si;
    EditText email;
    EditText password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        si = new Intent(this, Auth_Result.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add("Gallery");
        menu.add("multiLine");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getTitle().toString())
        {
            case "Gallery":
                Intent Gallery = new Intent(this, Gallery.class);
                startActivity(Gallery);
                finish();
                break;

            case "multiLine":
                Intent multi = new Intent(this, multiLine.class);
                startActivity(multi);
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // should be impossible
            Toast.makeText(this, "there is a user logged in", Toast.LENGTH_SHORT).show();
            startActivity(si);
        }
        super.onStart();
    }

    public void sign_in(View view) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                si.putExtra("name", email.getText().toString().substring(0, email.getText().toString().indexOf('@'))); // get the name of the user
                Toast.makeText(Auth.this, "Sign up Successful", Toast.LENGTH_SHORT).show();

                startActivity(si);
            }
            else {
                Toast.makeText(Auth.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void log_in(View view) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                si.putExtra("name", email.getText().toString().substring(0, email.getText().toString().indexOf('@'))); // get the name of the user
                Toast.makeText(Auth.this, "Log in Successful", Toast.LENGTH_SHORT).show();

                startActivity(si);
            }
            else {
                Toast.makeText(Auth.this, "log in Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}