package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.model.profile.pojo.PersonalImages;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class PicturesAttachmentAdapter extends RecyclerView.Adapter<PicturesAttachmentAdapter.ViewHolder> {


    private OnImageSelectedListener onImageSelectedListener;
    private Context context;
    private List<PersonalImages> photos;

    public PicturesAttachmentAdapter(Context context, List<PersonalImages> uriList, OnImageSelectedListener onImageSelectedListener) {
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
        PersonalImages personalImages = photos.get(position);

//      Picasso.get().load(uri).into(holder.imageView);
        if (personalImages.getImagesURI() != null)
            holder.imageView.setImageURI(personalImages.getImagesURI());
        else {
            Glide.with(context).load(personalImages.getUrl()).into(holder.imageView);
        }
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

    public void updateSelectedPhotosList(List<PersonalImages> uriList, String positionToRemoveItem) {
        Utility.printLogs("log1", "selectedImageToRemove");
        PersonalImages personalImages = null;
        if (positionToRemoveItem != null) {
            personalImages = photos.get(Integer.parseInt(positionToRemoveItem));
            photos.remove(Integer.parseInt(positionToRemoveItem));
        } else {
            this.photos = new ArrayList<>(uriList);
        }
        onImageSelectedListener.imageListUpdated(personalImages);
        notifyDataSetChanged();
    }

    public List<PersonalImages> getSelectedPhotosList() {
        return photos;
    }

    public interface OnImageSelectedListener {
        public void imageListUpdated(PersonalImages personalImages);
    }
}
