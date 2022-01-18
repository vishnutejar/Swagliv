package com.app.progressbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;

/**
 * Created by Maxim Dybarsky | maxim.dybarskyy@gmail.com
 * on 13.01.15 at 14:22
 */
public class LoadingProgressBarDialog extends AlertDialog {

    public static class Builder {

        private Context context;
        private String message;
        private int messageId;
        private int themeId;
        private boolean cancelable = false; // default dialog behaviour
        private boolean isShowCancelableButton = false;
        private OnCancelListener cancelListener;
        private OnCancelButtonListener cancelButtonListener;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setTheme(@StyleRes int themeId) {
            this.themeId = themeId;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setProgressBarCancelListener(OnCancelListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public Builder isShowCancelButton(boolean isShowCancelableButton) {
            this.isShowCancelableButton = isShowCancelableButton;
            return this;
        }

        public Builder setCancelButtonListener(OnCancelButtonListener cancelListener) {
            this.cancelButtonListener = cancelListener;
            return this;
        }

        public AlertDialog build() {
            return new LoadingProgressBarDialog(
                    context,
                    messageId != 0 ? context.getString(messageId) : message,
                    themeId != 0 ? themeId : R.style.ProgressBarDefault,
                    cancelable,
                    cancelListener,
                    isShowCancelableButton,
                    cancelButtonListener
            );
        }
    }

    private static final int DELAY = 150;
    private static final int DURATION = 1500;

    private int size;
    private ProgressBarAnimatedView[] spots;
    private ProgressBarAnimatorPlayer animator;
    private CharSequence message;
    private boolean isShowCancelableButton;
    private Button cancelableButton;
    private ImageView imageView;
    private OnCancelListener cancelListener;
    private OnCancelButtonListener cancelButtonListener;

    private LoadingProgressBarDialog(Context context, String message, int theme, boolean cancelable, OnCancelListener cancelListener, boolean isShowCancelableButton, OnCancelButtonListener cancelButtonListener) {
        super(context, theme);
        this.message = message;
        this.isShowCancelableButton = isShowCancelableButton;
        this.cancelListener = cancelListener;
        this.cancelButtonListener = cancelButtonListener;

        setCancelable(cancelable);
        if (cancelListener != null) setOnCancelListener(cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progressbar_dialog_main_ui);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initMessage();
        setCancelButtonVisibility();
        initProgress();

    }

    @Override
    protected void onStart() {
        super.onStart();

        for (ProgressBarAnimatedView view : spots) view.setVisibility(View.VISIBLE);

        animator = new ProgressBarAnimatorPlayer(createAnimations());
        animator.play();


        //   progressCradleBallBounceLoading.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        animator.stop();
        // progressCradleBallBounceLoading.stop();
    }

    @Override
    public void setMessage(CharSequence message) {
        this.message = message;
        if (isShowing()) initMessage();
    }

    //~

    private void initMessage() {
        if (message != null && message.length() > 0) {
            ((TextView) findViewById(R.id.dmax_spots_title)).setText(message);
        }
    }

    private void setCancelButtonVisibility() {
        int visible = isShowCancelableButton ? View.VISIBLE : View.GONE;
        cancelableButton = findViewById(R.id.cancelProgressBar);
        imageView = findViewById(R.id.image);

        // load gif using glide
        Glide.with(getContext()).asGif().load(R.raw.heart_progress).into(imageView);

        View spaceView = findViewById(R.id.spaceView);
        spaceView.setVisibility(visible);
        cancelableButton.setVisibility(visible);
        cancelableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelButtonListener != null) {
                    cancelButtonListener.onCancelButtonListener();
                }
            }
        });
    }

    private void initProgress() {
        ProgressBarLayout progress = findViewById(R.id.dmax_spots_progress);
        progress.setBackgroundColor(Color.TRANSPARENT);
        size = progress.getSpotsCount();

        spots = new ProgressBarAnimatedView[size];
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.progress_bar_size);
        int progressWidth = getContext().getResources().getDimensionPixelSize(R.dimen.progress_width);
        for (int i = 0; i < spots.length; i++) {
            ProgressBarAnimatedView v = new ProgressBarAnimatedView(getContext());
            v.setBackgroundResource(R.drawable.dmax_progress_bar_spot);
            v.setTarget(progressWidth);
            v.setXFactor(-1f);
            v.setVisibility(View.INVISIBLE);
            progress.addView(v, size, size);
            spots[i] = v;
        }
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[size];
        for (int i = 0; i < spots.length; i++) {
            final ProgressBarAnimatedView animatedView = spots[i];
            Animator move = ObjectAnimator.ofFloat(animatedView, "xFactor", 0, 1);
            move.setDuration(DURATION);
            move.setInterpolator(new ProgressBarHesitateInterpolator());
            move.setStartDelay(DELAY * i);
            move.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animatedView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    animatedView.setVisibility(View.VISIBLE);
                }
            });
            animators[i] = move;
        }
        return animators;
    }

    public interface OnCancelButtonListener {
        void onCancelButtonListener();
    }
}
