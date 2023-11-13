package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class multiLine extends AppCompatActivity {

    EditText input_text;
    TextView output;
    FirebaseDatabase db;
    DatabaseReference myRef;
    String saveText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_line);
        input_text = findViewById(R.id.input_text);
        output = findViewById(R.id.view_text);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("text");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    saveText = snapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add("Auth");
        menu.add("Gallery");
        menu.add("Camera");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString())
        {
            case "Auth":
                Intent Auth = new Intent(this, com.example.alpha.Auth.class);
                startActivity(Auth);
                finish();
                break;
            case "Gallery":
                Intent Gallery = new Intent(this, com.example.alpha.Gallery.class);
                startActivity(Gallery);
                finish();
                break;
            case "Camera":
                Intent camera = new Intent(this, Camera_TF.class);
                startActivity(camera);
                finish();
                break;
        }
        return true;
    }

    public void save(View view) {
        String text = input_text.getText().toString();
        myRef.setValue(text, (error, ref) -> {
            if(error != null)
            {
                Toast.makeText(this, "Failed : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getText(View view) {
        output.setText(saveText);
    }
}