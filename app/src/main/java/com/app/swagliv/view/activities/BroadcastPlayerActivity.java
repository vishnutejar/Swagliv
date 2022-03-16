package com.app.swagliv.view.activities;

import static com.app.common.constant.AppCommonConstants.API_REQUEST.REQUEST_ID_1021;
import static com.app.common.constant.AppCommonConstants.API_REQUEST.REQUEST_ID_1024;
import static com.app.common.constant.AppCommonConstants.API_REQUEST.REQUEST_ID_1025;
import static com.app.common.network.RetrofitClient.BASE_URL;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.RequestDialogBinding;
import com.app.swagliv.model.livestream.pojo.AllLiveStreamCommentsResp;
import com.app.swagliv.model.livestream.pojo.GetListOfActiveViewers;
import com.app.swagliv.model.livestream.pojo.StartLiveStreamResp;
import com.app.swagliv.view.adaptor.ChatCommentsAdapter;
import com.app.swagliv.viewmodel.livestream.LiveStreamViewModel;
import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BroadcastPlayerActivity extends AppCompatActivity implements APIResponseHandler {
    private static final String APPLICATION_ID = "pZLjLsZY8QPXUPsrTr56pA";
    private static final String API_KEY = "AymJuqtonAkJfAMQg5YKKm";
    private ChatCommentsAdapter adapter;
    private ArrayList<AllLiveStreamCommentsResp.Datum> connectionsList;
    AlertDialog dialog;
    boolean isFirstGrid = false;
    int totalConnectedUser = 0;
    final OkHttpClient mOkHttpClient = new OkHttpClient();
    BroadcastPlayer mBroadcastPlayer;
    MediaController mMediaController = null;
    SurfaceViewWithAutoAR mVideoSurface;
    View mPlayerContentView;
    Display mDefaultDisplay;
    RecyclerView commentRecycleView;
    ImageView img_addConnections, heart, coin, imgBack, img_host;
    EditText et_comment;
    LinearLayout cloneContainer;
    LiveStreamViewModel liveStreamViewModel;
    AllLiveStreamCommentsResp allLiveStreamCommentsResp;
    GetListOfActiveViewers getListOfActiveViewers;

    TextView signals, gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_player);
        liveStreamViewModel = new ViewModelProvider(this).get(LiveStreamViewModel.class);
        liveStreamViewModel.responseMutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });
        mDefaultDisplay = getWindowManager().getDefaultDisplay();
        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        commentRecycleView = findViewById(R.id.commentRecycleView);
        img_addConnections = findViewById(R.id.img_addConnections);
        signals = findViewById(R.id.signals);
        heart = findViewById(R.id.heart);
        coin = findViewById(R.id.coin);
        imgBack = findViewById(R.id.img_back);
        et_comment = findViewById(R.id.et_comment);
        img_host = findViewById(R.id.img_host);
        cloneContainer = findViewById(R.id.cloneContainer);
        gif = findViewById(R.id.gif);

        img_addConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoTOListConnection = new Intent(BroadcastPlayerActivity.this, ManageConnectionActivity.class);
                startActivity(gotoTOListConnection);
            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartOnClick();
            }
        });

/*
        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRequestDialog();
            }
        });
*/
        gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSearchGif = new Intent(BroadcastPlayerActivity.this, SearchGIFActivity.class);
                startActivity(gotoSearchGif);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String msg_comment = v.getText().toString();
                    if (!msg_comment.isEmpty()) {
                        postComment(msg_comment);
                        et_comment.getText().clear();
                    }
                    handled = true;
                }
                return handled;
            }

            private void postComment(String msg_comment) {
                if (Utility.isNetworkAvailable(BroadcastPlayerActivity.this)) {
                    liveStreamViewModel.postCommentOnLiveStream(AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.LiveStreamId, BroadcastPlayerActivity.this)
                            , msg_comment, REQUEST_ID_1021);
                }
            }
        });
        img_host.setOnClickListener(view -> {
            Intent gotoTOListConnection = new Intent(BroadcastPlayerActivity.this, SelectHostActivity.class);
            startActivity(gotoTOListConnection);
        });

        if (Utility.isNetworkAvailable(this))
            liveStreamViewModel.getAllLiveStreamCommentsResp(BASE_URL + AppConstant.API.GetAllLiveStreamComments + "/" + AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.LiveStreamId, BroadcastPlayerActivity.this)
                    , REQUEST_ID_1024);
        if (Utility.isNetworkAvailable(this))
            liveStreamViewModel.getListOfActiveViewers(AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.LiveStreamId, BroadcastPlayerActivity.this)
                    , REQUEST_ID_1025);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        mVideoSurface = null;
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;
        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = null;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        getLatestResourceUri();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_UP && mBroadcastPlayer != null && mMediaController != null) {
            PlayerState state = mBroadcastPlayer.getState();
            if (state == PlayerState.PLAYING ||
                    state == PlayerState.BUFFERING ||
                    state == PlayerState.PAUSED ||
                    state == PlayerState.COMPLETED) {
                if (mMediaController.isShowing())
                    mMediaController.hide();
                else
                    mMediaController.show();
            } else {
                mMediaController.hide();
            }
        }
        return false;
    }

    void getLatestResourceUri() {
        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    JSONObject latestBroadcast = results.optJSONObject(0);
                    resourceUri = latestBroadcast.optString("resourceUri");
                } catch (Exception ignored) {
                }
                final String uri = resourceUri;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPlayer(uri);
                    }
                });
            }
        });
    }

    void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();

    }

    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
                if (mMediaController == null && mBroadcastPlayer != null && !mBroadcastPlayer.isTypeLive()) {
                    mMediaController = new MediaController(BroadcastPlayerActivity.this);
                    mMediaController.setAnchorView(mPlayerContentView);
                    mMediaController.setMediaPlayer(mBroadcastPlayer);
                }
                if (mMediaController != null) {
                    mMediaController.setEnabled(true);
                    mMediaController.show();
                }
            } else if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
                if (mMediaController != null) {
                    mMediaController.setEnabled(false);
                    mMediaController.hide();
                }
                mMediaController = null;
            }
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
            Point size = getScreenSize();
            float screenAR = size.x / (float) size.y;
            float videoAR = width / (float) height;
            float arDiff = screenAR - videoAR;
            mVideoSurface.setCropToParent(Math.abs(arDiff) < 0.2);

        }
    };

    private Point getScreenSize() {
        if (mDefaultDisplay == null)
            mDefaultDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            // this is officially supported since SDK 17 and said to work down to SDK 14 through reflection,
            // so it might be everything we need.
            mDefaultDisplay.getClass().getMethod("getRealSize", Point.class).invoke(mDefaultDisplay, size);
        } catch (Exception e) {
            // fallback to approximate size.
            mDefaultDisplay.getSize(size);
        }
        return size;
    }


    public void openRequestDialog() {
        RequestDialogBinding dialogBinding = RequestDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(BroadcastPlayerActivity.this);
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
            }
        });
    }


    void heartOnClick() {

        // Disable clips on all parent generations
        disableAllParentsClip();

        // Create clone
        ImageView imageClone = cloneImage();

        // Animate
        animateFlying(imageClone);
        animateFading(imageClone);
    }

    private void disableAllParentsClip() {
        LinearLayout parent = cloneContainer;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            child.setEnabled(false);
        }
    }

    private ImageView cloneImage() {
        ImageView clone = new ImageView(BroadcastPlayerActivity.this);
        clone.setLayoutParams(heart.getLayoutParams());
        clone.setImageDrawable(heart.getDrawable());
        cloneContainer.addView(clone);
        return clone;
    }


    private void animateFlying(ImageView image) {
        ObjectAnimator.ofFloat(image, View.TRANSLATION_Y, 800, -290f).setDuration(1800).start();
    }

    private void animateFading(ImageView image) {
        image.animate()
                .alpha(0f)
                .setDuration(2000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cloneContainer.removeView(image);
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
                    case REQUEST_ID_1024:
                        allLiveStreamCommentsResp = (AllLiveStreamCommentsResp) apiResponse.data;
                        if (allLiveStreamCommentsResp != null) {
                            commentRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                            connectionsList = (ArrayList<AllLiveStreamCommentsResp.Datum>) allLiveStreamCommentsResp.getData();
                            adapter = new ChatCommentsAdapter(connectionsList);
                            commentRecycleView.setAdapter(adapter);
                        }
                        break;
                    case REQUEST_ID_1021:
                        if (Utility.isNetworkAvailable(this))
                            liveStreamViewModel.getAllLiveStreamCommentsResp(BASE_URL + AppConstant.API.GetAllLiveStreamComments + "/" + AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.LiveStreamId, BroadcastPlayerActivity.this)
                                    , REQUEST_ID_1024);
                        break;
                    case REQUEST_ID_1025:
                        getListOfActiveViewers = (GetListOfActiveViewers) apiResponse.data;
                        if (getListOfActiveViewers != null) {
                            int viewerCount = getListOfActiveViewers.getData().getActiveViewersData().size();
                            signals.setText(viewerCount + "");
                        }
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
}