package com.app.swagliv.view.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.SearchGifItemBinding;
import com.app.swagliv.databinding.SelectHostItemBinding;

import java.util.ArrayList;

public class SelectHostAdapter extends RecyclerView.Adapter<SelectHostAdapter.ViewHolder> {

    private ArrayList<String> hostsList = new ArrayList<>();


    public SelectHostAdapter(ArrayList<String> hostsList) {
        this.hostsList = hostsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_host_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String gif = hostsList.get(position);

    }

    @Override
    public int getItemCount() {
        return hostsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SelectHostItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SelectHostItemBinding.bind(itemView);
        }
    }

}
