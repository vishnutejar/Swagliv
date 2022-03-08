package com.app.swagliv.view.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ConnectionListItemBinding;

import java.sql.Connection;
import java.util.ArrayList;

public class ManageConnectionAdapter extends RecyclerView.Adapter<ManageConnectionAdapter.ViewHolder> {

    private ArrayList<String> connectionsList = new ArrayList<>();


    public ManageConnectionAdapter(ArrayList<String> connectionsList) {
        this.connectionsList = connectionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connection_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String category = connectionsList.get(position);
        holder.binding.name.setText(category);

    }

    @Override
    public int getItemCount() {
        return connectionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConnectionListItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ConnectionListItemBinding.bind(itemView);
        }
    }

}
