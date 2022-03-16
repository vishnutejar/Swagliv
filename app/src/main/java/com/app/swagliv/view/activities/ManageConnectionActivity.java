package com.app.swagliv.view.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityManageConnectionBinding;
import com.app.swagliv.databinding.RequestDialogBinding;
import com.app.swagliv.model.livestream.pojo.ConnectionsListResp;
import com.app.swagliv.view.adaptor.ManageConnectionAdapter;
import com.app.swagliv.viewmodel.livestream.LiveStreamViewModel;

import java.util.ArrayList;

public class ManageConnectionActivity extends AppCompatActivity implements View.OnClickListener, APIResponseHandler {
    private ActivityManageConnectionBinding mBinding;
    private ManageConnectionAdapter adapter;
    private ArrayList<ConnectionsListResp.Datum> connectionsList = new ArrayList<>();
    LiveStreamViewModel liveStreamViewModel;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liveStreamViewModel = new ViewModelProvider(this).get(LiveStreamViewModel.class);
        liveStreamViewModel.responseMutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_connection);
        mBinding.commonHeader.backBtn.setOnClickListener(this);
        mBinding.commonHeader.headerTitle.setText(R.string.manage_connection);

        liveStreamViewModel.getConnectionsList(AppCommonConstants.API_REQUEST.REQUEST_ID_1023);

        mBinding.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRequestDialog();
            }
        });

    }

    public void openRequestDialog() {
        RequestDialogBinding dialogBinding = RequestDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.getWindow().getAttributes().y = 90;
        dialog.show();

        dialogBinding.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialogBinding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Utility.showToast(ManageConnectionActivity.this, "multiple connection's not support in trail version");
            }
        });
    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                //------
                break;
            case SUCCESS:
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1023:
                        ConnectionsListResp connectionsListResp = (ConnectionsListResp) apiResponse.data;
                        mBinding.connectionRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                        connectionsList = (ArrayList<ConnectionsListResp.Datum>) connectionsListResp.getData();
                        adapter = new ManageConnectionAdapter(connectionsList);
                        mBinding.connectionRecycleView.setAdapter(adapter);
                        break;
                }
                break;
            case ERROR:
                //-------
                Utility.showToast(this, getString(R.string.api_failure_error_msg));
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
        }
    }
}