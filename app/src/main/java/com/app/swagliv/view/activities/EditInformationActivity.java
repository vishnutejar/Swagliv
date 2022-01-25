package com.app.swagliv.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.common.interfaces.DatePickerDialogListener;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityEditInformationBinding;

import java.util.Date;

public class EditInformationActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialogListener {
    private ActivityEditInformationBinding mbinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_information);
        mbinding.datePickIc.setOnClickListener(this);
        mbinding.editBackIcon.setOnClickListener(this);
        mbinding.saveBtnTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_pick_ic:
                Utility utility = new Utility(this);
                utility.datePickerDialog(EditInformationActivity.this, this, new Date());
                break;
            case R.id.edit_back_icon:
                finish();
                break;
            case R.id.save_btn:
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void getSelectedDate(String selectedDate) {
        mbinding.birthDateText.setText(selectedDate);
    }
}