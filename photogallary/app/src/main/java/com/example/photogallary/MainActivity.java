package com.example.photogallary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private FloatingActionButton fabAddPhoto;
    private ActivityResultLauncher<Intent> photoPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupRecyclerView();
        setupPhotoPickerLauncher();
        setupFabClickListener();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAddPhoto = findViewById(R.id.fabAddPhoto);
    }

    private void setupRecyclerView() {
        photoAdapter = new PhotoAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(photoAdapter);

        photoAdapter.setOnPhotoClickListener((photoUri, position) -> {
            showPhotoDialog(photoUri, position);
        });
    }

    private void setupPhotoPickerLauncher() {
        photoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            List<Uri> uris = new ArrayList<>();
                            for (int i = 0; i < count; i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                takePersistableUriPermission(uri);
                                uris.add(uri);
                            }
                            photoAdapter.addPhotos(uris);
                            Toast.makeText(this, count + " ảnh đã đc thêm", Toast.LENGTH_SHORT).show();
                        } else if (data.getData() != null) {
                            Uri uri = data.getData();
                            takePersistableUriPermission(uri);
                            photoAdapter.addPhoto(uri);
                            Toast.makeText(this, "ảnh đã đc thêm", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void takePersistableUriPermission(Uri uri) {
        try {
            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void setupFabClickListener() {
        fabAddPhoto.setOnClickListener(v -> openPhotoPicker());
    }

    private void openPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        photoPickerLauncher.launch(intent);
    }

    private void showPhotoDialog(Uri photoUri, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Photo Options")
                .setMessage("What would you like to do with this photo?")
                .setPositiveButton("View Full", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, FullPhotoActivity.class);
                    intent.putExtra("PHOTO_URI", photoUri.toString());
                    startActivity(intent);
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    photoAdapter.removePhoto(position);
                    Toast.makeText(this, "Photo deleted", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
}