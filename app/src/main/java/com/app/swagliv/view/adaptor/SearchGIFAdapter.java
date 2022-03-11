package com.app.swagliv.view.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.SearchGifItemBinding;

import java.util.ArrayList;

public class SearchGIFAdapter extends RecyclerView.Adapter<SearchGIFAdapter.ViewHolder> {

    private ArrayList<Integer> gifsList = new ArrayList<>();


    public SearchGIFAdapter(ArrayList<Integer> gifsList) {
        this.gifsList = gifsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_gif_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        int gif = gifsList.get(position);
        holder.binding.gifImageView.setImageResource(gif);

    }

    @Override
    public int getItemCount() {
        return gifsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SearchGifItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SearchGifItemBinding.bind(itemView);
        }
    }

}
