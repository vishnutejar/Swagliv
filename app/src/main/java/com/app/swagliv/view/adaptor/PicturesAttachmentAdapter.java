package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;

import java.util.ArrayList;
import java.util.List;


public class PicturesAttachmentAdapter extends RecyclerView.Adapter<PicturesAttachmentAdapter.ViewHolder> {


    private OnImageSelectedListener onImageSelectedListener;
    private Context context;
    private List<Uri> photos;

    public PicturesAttachmentAdapter(Context context, List<Uri> uriList, OnImageSelectedListener onImageSelectedListener) {
        this.context = context;
        this.onImageSelectedListener = onImageSelectedListener;
        this.photos = new ArrayList<>(uriList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = photos.get(position);
//      Picasso.get().load(uri).into(holder.imageView);
        holder.imageView.setImageURI(uri);
        holder.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelectedPhotosList(null, "" + position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, removeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pictures);
            removeImage = itemView.findViewById(R.id.remove_ic);
        }
    }

    public void updateSelectedPhotosList(List<Uri> uriList, String positionToRemoveItem) {
        if (positionToRemoveItem != null) {
            photos.remove(Integer.parseInt(positionToRemoveItem));
        } else {
            this.photos = new ArrayList<>(uriList);
        }
        onImageSelectedListener.imageListUpdated();
        notifyDataSetChanged();
    }

    public List<Uri> getSelectedPhotosList() {
        return photos;
    }

    public interface OnImageSelectedListener {
        public void imageListUpdated();
    }
}
