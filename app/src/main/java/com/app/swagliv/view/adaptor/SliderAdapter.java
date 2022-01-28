package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.swagliv.R;
import com.app.swagliv.model.IntroModel;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<IntroModel> models;

    public SliderAdapter(ArrayList<IntroModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        IntroModel model = models.get(position);
//        holder.image.setAnimation(model.getIcon());
        switch (position) {
            case 0:
                holder.image.setAnimation(R.raw.make_your_profile);
                break;
            case 1:
                holder.image.setAnimation(R.raw.meetfriends);
                break;
            case 2:
                holder.image.setAnimation(R.raw.meet_perfect_match);
                break;
            case 3:
                holder.image.setAnimation(R.raw.celebrate_friends);
                break;
        }
        holder.title.setText(model.getTitle());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView image;
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.animation);
            title = itemView.findViewById(R.id.into_title);
        }
    }
}
