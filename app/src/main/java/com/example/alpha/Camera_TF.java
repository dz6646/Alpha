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
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

public class Camera_TF extends AppCompatActivity {

    ImageView camera;
    FirebaseStorage store;
    StorageReference sRef;
    TextView out;
    Bitmap bitmap;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_tf);
        camera = findViewById(R.id.show_captured);
        out = findViewById(R.id.analysis);
        bitmap = null;
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
                bitmap = (Bitmap) data.getExtras().get("data");
                camera.setImageBitmap(bitmap);
                String random = UUID.randomUUID().toString();

                StorageReference img_ref = sRef.child("camera/" + random);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data_bytes = baos.toByteArray();

                img_ref.putBytes(data_bytes).addOnSuccessListener(taskSnapshot ->
                                Toast.makeText(Camera_TF.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }
    }
    public void Analyze(View view) {
        if(bitmap == null)
        {
            return;
        }
        int brightness = calculateBrightness(bitmap, 1); // the brightness is 0 for completely black and 255 to white
        out.setText("Brightness : " + brightness);
    }

    public int calculateBrightness(Bitmap bitmap, int pixelSpacing)
    {
        int R = 0; int G = 0; int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i += pixelSpacing) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return (R + B + G) / (n * 3);
    }
}