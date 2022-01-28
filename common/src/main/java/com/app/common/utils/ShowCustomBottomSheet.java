package com.app.common.utils;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.common.R;
import com.app.common.databinding.BottomsheetCustomMessageboxBinding;
import com.app.common.interfaces.CustomDialogConfirmationInterfaces;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * show given message into bottomsheet.
 */
public class ShowCustomBottomSheet extends BottomSheetDialogFragment {

    //Constants
    private static final String TAG = ShowCustomBottomSheet.class.getSimpleName();

    //Variables
    private BottomsheetCustomMessageboxBinding mViewBinding;
    private CustomDialogConfirmationInterfaces mBottomsheetResult;
    private String mTitle, mMessage, mTextPositiveBtn, mTextNegativeBtn;
    private Drawable mDrawable;
    private boolean mIsNegativeButtonRequired;
    private boolean mIsCancelable, mIsDismiss;

    /**
     *
     * @param mBottomsheetResult callback user action
     * @param mDrawable drawable to show on top of bottomsheet.
     * @param mTitle title to show on top of bottomsheet.
     * @param mMessage message to show.
     * @param mTextPositiveBtn text to show on positive button.
     * @param mTextNegativeBtn text to show on negative button.
     * @param mIsNegativeButtonRequired this is a boolean value to decide is negative button shon on Ui or not.
     * @param mIsCancelable this is a boolean value to decide is bottomsheet cancelable.
     * @param mIsDismiss this is a boolean value to decide is bottomsheet dismiss after click positive or negative button.
     */
    public ShowCustomBottomSheet(CustomDialogConfirmationInterfaces mBottomsheetResult, Drawable mDrawable, String mTitle, String mMessage, String mTextPositiveBtn, String mTextNegativeBtn, boolean mIsNegativeButtonRequired, boolean mIsCancelable, boolean mIsDismiss) {
        this.mBottomsheetResult = mBottomsheetResult;
        this.mDrawable = mDrawable;
        this.mTitle = mTitle;
        this.mMessage = mMessage;
        this.mTextPositiveBtn = mTextPositiveBtn;
        this.mTextNegativeBtn = mTextNegativeBtn;
        this.mIsNegativeButtonRequired = mIsNegativeButtonRequired;
        this.mIsCancelable = mIsCancelable;
        this.mIsDismiss = mIsDismiss;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.bottomsheet_custom_messagebox, null, false);
        setCancelable(mIsCancelable);

        showDetail();
        onPositiveButtonClick();
        mViewBinding.mainLayoutHeight.setMinimumHeight(getResources().getDisplayMetrics().heightPixels / 3);
        return mViewBinding.getRoot();
    }

    /**
     * Handel positive button click event.
     */
    private void onPositiveButtonClick() {
        mViewBinding.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mIsDismiss)
                    dismiss();
                if (mBottomsheetResult != null)
                    mBottomsheetResult.callConfirmationDialogPositive();
            }
        });
    }

    /**
     * configure bottom sheet as per
     */
    private void showDetail() {

        if (mDrawable != null){
            mViewBinding.messageDrawable.setImageDrawable(mDrawable);
        }

        mViewBinding.dialogButtonOKLeftSpaceView.setVisibility(View.VISIBLE);
        mViewBinding.dialogButtonOKRightSpaceView.setVisibility(View.VISIBLE);
        if (mTitle != null) {
            mViewBinding.messageTitle.setText(mTitle);
        }
        if (mMessage != null) {
            mViewBinding.dialogMessageText.setText(mMessage);
        }
        if (mTextPositiveBtn != null) {
            mViewBinding.btnPositive.setText(mTextPositiveBtn);
        }

        if (mIsNegativeButtonRequired) {
            mViewBinding.dialogButtonOKLeftSpaceView.setVisibility(View.GONE);
            mViewBinding.dialogButtonOKRightSpaceView.setVisibility(View.GONE);
            mViewBinding.dialogButtonCancel.setVisibility(View.VISIBLE);

            if (mTextNegativeBtn != null) {
                mViewBinding.btnNegative.setText(mTextNegativeBtn);
            }
            onNegativeButtonClick();
        }
    }

    /**
     * Handel negative button click event.
     */
    private void onNegativeButtonClick() {
        mViewBinding.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mBottomsheetResult != null)
                    mBottomsheetResult.callConfirmationDialogNegative();
            }
        });
    }
}
