package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.chiprecycleview.MultiChipsRecyclerView;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.model.home.pojo.Passions;
import com.app.swagliv.view.activities.PassionSelectionActivity;

import java.util.ArrayList;


/**
 * Created by Aditya on 22/12/2021.
 */

public class PassionAdapter extends MultiChipsRecyclerView.Adapter<PassionAdapter.InterestViewHolder> {


    private Context mContext;
    private ArrayList<Passions> passionsArrayList;
    private int selectedPosition = -1;

    public PassionAdapter(Context context, ArrayList<Passions> listGuestUserData) {
        this.passionsArrayList = listGuestUserData;
        this.mContext = context;
    }


    @Override
    public InterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data, parent, false);
        return new InterestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InterestViewHolder holder, final int position) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //---------
        if (position % 2 == 0)
            params.setMargins(15, 5, 10, 5);
        else
            params.setMargins(10, 5, 15, 5);
        //---------
        Passions passions = passionsArrayList.get(position);
//        holder.itemDataBinding.setPassion(passions);
        holder.tv_name.setLayoutParams(params);
        holder.tv_name.setText(passions.getLabel());
        handleMultiChoiceSelection(holder, position, holder.tv_name);
    }

    @Override
    public int getItemCount() {
        return passionsArrayList.size();
    }


    class InterestViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;

        InterestViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    private void handleMultiChoiceSelection(RecyclerView.ViewHolder holder, final int position, TextView textView) {
        if (passionsArrayList.get(position).isSelected()) {
            textView.setBackgroundResource(R.drawable.user_data_bg_selected);
        } else {
            textView.setBackgroundResource(R.drawable.user_data_bg_unselected);
        }
        textView.setText(passionsArrayList.get(position).getLabel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check max selected count
                int selectedCount = 0;
                for (Passions passion : passionsArrayList) {
                    if (passion.isSelected()) {
                        selectedCount++;
                    }
                }

                if (selectedCount < 5) {
                    if (passionsArrayList.get(position).isSelected()) {
                        passionsArrayList.get(position).setSelected(false);
                    } else {
                        passionsArrayList.get(position).setSelected(true);
                    }
                    // list is filtered with selected attribute
                    //TODO perform operation to get selected items
                } else {
                    if (passionsArrayList.get(position).isSelected()) {
                        passionsArrayList.get(position).setSelected(false);
                    } else
                        Utility.showToast(mContext, "You can select max 5 choices");
                }
                //------------
                if (mContext instanceof PassionSelectionActivity) {
                    ((PassionSelectionActivity) mContext).selectGuestUserListData(passionsArrayList);
                }
                notifyDataSetChanged();
            }
        });
    }

    public void updateData(ArrayList<Passions> passions) {
        passionsArrayList = passions;
        notifyDataSetChanged();
    }
}
