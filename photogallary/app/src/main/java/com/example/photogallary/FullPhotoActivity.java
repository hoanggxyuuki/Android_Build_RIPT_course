package com.example.photogallary;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FullPhotoActivity extends AppCompatActivity {

    private ImageView fullImageView;
    private ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        hideSystemUI();

        fullImageView = findViewById(R.id.fullImageView);
        closeButton = findViewById(R.id.closeButton);

        String photoUriString = getIntent().getStringExtra("PHOTO_URI");
        if (photoUriString != null) {
            Uri photoUri = Uri.parse(photoUriString);
            fullImageView.setImageURI(photoUri);
        } else {
            Toast.makeText(this, "Không thể tải ảnh", Toast.LENGTH_SHORT).show();
            finish();
        }

        closeButton.setOnClickListener(v -> finish());

        fullImageView.setOnClickListener(v -> toggleSystemUI());
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void toggleSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        if ((uiOptions | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == uiOptions) {
            showSystemUI();
        } else {
            hideSystemUI();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}
