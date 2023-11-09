package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Camera_TF extends AppCompatActivity {

    ImageView camera;
    FirebaseStorage store;
    StorageReference sRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_tf);
        camera = findViewById(R.id.show_captured);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.CAMERA},
                    100);
        }
        store = FirebaseStorage.getInstance();
        sRef = store.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add("Auth");
        menu.add("Gallery");
        menu.add("multiLine");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch(item.getTitle().toString())
        {
            case "Auth":
                intent = new Intent(this, Auth.class);
                break;

            case "Gallery":
                intent = new Intent(this, Gallery.class);
                break;

            case "multiLine":
                intent = new Intent(this, multiLine.class);
                break;
        }
        startActivity(intent);
        finish();
        return true;
    }

    public void capture(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 19);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19)
        {
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null)
            {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                camera.setImageBitmap(bitmap);
                String random = UUID.randomUUID().toString();

                StorageReference img_ref = sRef.child("camera/" + random);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data_bytes = baos.toByteArray();

                img_ref.putBytes(data_bytes).addOnSuccessListener(taskSnapshot ->
                        Toast.makeText(Camera_TF.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }

    public void Analyze(View view) {

    }
}