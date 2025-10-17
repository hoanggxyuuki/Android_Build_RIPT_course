package com.example.photogallary;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private final List<Uri> photoUris;
    private OnPhotoClickListener listener;

    public interface OnPhotoClickListener {
        void onPhotoClick(Uri photoUri, int position);
    }

    public PhotoAdapter() {
        this.photoUris = new ArrayList<>();
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.listener = listener;
    }

    public void addPhoto(Uri uri) {
        photoUris.add(uri);
        notifyItemInserted(photoUris.size() - 1);
    }

    public void addPhotos(List<Uri> uris) {
        int startPosition = photoUris.size();
        photoUris.addAll(uris);
        notifyItemRangeInserted(startPosition, uris.size());
    }

    public void removePhoto(int position) {
        if (position >= 0 && position < photoUris.size()) {
            photoUris.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri photoUri = photoUris.get(position);
        holder.imageView.setImageURI(photoUri);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhotoClick(photoUri, position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            removePhoto(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return photoUris.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
