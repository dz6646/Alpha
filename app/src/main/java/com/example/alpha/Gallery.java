package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class Gallery extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 19;
    ImageView img_gallery;
    Uri imageUri;
    Uri loadUri;
    FirebaseStorage storage;
    StorageReference storageRef;
    String random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        img_gallery = findViewById(R.id.img_glry);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add("Auth");
        menu.add("multiLine");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString())
        {
            case "Auth":
                Intent Auth = new Intent(this, Auth.class);
                startActivity(Auth);
                finish();
                break;
            case "multiText":
                Intent multi = new Intent(this, multiLine.class);
                startActivity(multi);
                finish();
                break;
        }
        return true;
    }

    public void upload_picture(View view) {
        Intent iGallery = new Intent(Intent.ACTION_PICK); // Intent to pick something
        iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // External content
        startActivityForResult(iGallery, GALLERY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image...");

        if(resultCode == RESULT_OK)
        {
            if(requestCode == GALLERY_REQ_CODE && data != null && data.getData() != null)
            {
                pd.show();
                imageUri = data.getData();
                random = UUID.randomUUID().toString();
                StorageReference imageRef = storageRef.child("images/" + random);
                imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(this, "Image Failed to upload", Toast.LENGTH_SHORT).show();

                }).addOnProgressListener(snapshot -> {
                    double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + progress + "%");
                });
            }
        }
    }

    public void show_picture(View view) {
        if(random != null) {
            String path = "images/" + random;
            StorageReference getImage = storageRef.child(path);
            getImage.getBytes(1024 * 1024).addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                img_gallery.setImageBitmap(bitmap);

            }).addOnFailureListener(e -> {

                Toast.makeText(this, "Image download failed", Toast.LENGTH_SHORT).show();
            });
        }
    }

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