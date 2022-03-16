package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ConnectionListItemBinding;
import com.app.swagliv.model.livestream.pojo.ConnectionsListResp;
import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.util.ArrayList;

public class ManageConnectionAdapter extends RecyclerView.Adapter<ManageConnectionAdapter.ViewHolder> {

    private ArrayList<ConnectionsListResp.Datum> connectionsList = new ArrayList<>();

    Context context;

    public ManageConnectionAdapter(ArrayList<ConnectionsListResp.Datum> connectionsList) {
        this.connectionsList = connectionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connection_list_item, parent, false);
        context = parent.getContext();
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ConnectionsListResp.Datum datum = connectionsList.get(position);
        holder.binding.name.setText(datum.getName());
        Glide.with(context).load(datum.getImages()).
                into(holder.binding.imgConnectionprofile).onLoadFailed(context.getDrawable(R.drawable.nav_drawer_pofile));
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
