package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.progressbar.LoadingProgressBarDialog;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivityLoginBinding;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.viewmodel.login.LoginViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, APIResponseHandler {

    //constant
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 234;
    private static final String EMAIL = "email";

    // variables
    private ActivityLoginBinding mBinding;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private String mEmail, mName, mProfile, mUserID;

    // widgets
    private LoginButton mFacebookLoginBtn;
    private AlertDialog mProgressDialog;
    private LoginViewModel loginViewModel;
    private int mUserType;
    private AlertDialog mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //--------------------
        mBinding.googleSigninBtn.setOnClickListener(this);
        mBinding.facebookSigninBtn.setOnClickListener(this);
        mBinding.loginSignin.setOnClickListener(this);
        mBinding.loginSignup.setOnClickListener(this);

        mProgressDialog = new LoadingProgressBarDialog.Builder()
                .setContext(this)
                .setMessage(getString(R.string.please_wait))
                .build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mFacebookLoginBtn = mBinding.loginButton;

        // initialize login with facebook callback manager
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginBtn.setPermissions(Arrays.asList(EMAIL));

        // Callback registration
        mFacebookLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Utility.printLogs("status: ", "success");
                Utility.showToast(LoginActivity.this, "Login success");

                //----------
                getUserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                Utility.showToast(LoginActivity.this, "failure");
            }
        });

        loginViewModel.responseMutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });


        mProgressbar = new LoadingProgressBarDialog.Builder()
                .setContext(this)
                .setMessage(getString(R.string.please_wait))
                .build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_signin_btn:
                signIn();
                break;
            case R.id.facebook_signin_btn:
                mFacebookLoginBtn.performClick();
                break;
            case R.id.login_signin:
                Intent intent = new Intent(LoginActivity.this, EmailSignInActivity.class);
                startActivity(intent);
                break;
            case R.id.login_signup:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * On Google Sign in button click
     */
    private void signIn() {
        //getting the google sign in intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        mProgressDialog.dismiss();
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            try {
                mUserType = AppCommonConstants.LOGIN_TYPE.GMAIL;
                mUserID = account.getId();
                mName = account.getDisplayName();
                mEmail = account.getEmail();
                mProfile = account.getPhotoUrl().toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                Utility.showToast(this, "Login Success, Welcome" + account.getDisplayName());
                callDashboardActivity();
            }
        } catch (ApiException e) {
            Utility.showToast(LoginActivity.this, "Authentication failed." + e.getStatusCode());
        }
    }


    /**
     * getUser Profile data by access token
     *
     * @param currentAccessToken as AccessToken
     */
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                (object, response) -> {
                    Utility.printLogs("LOGIN", "GraphRequest Response ==> " + response.toString());
                    try {
                        mUserID = object.getString("id");
                        mName = object.getString("name");
                        mProfile = object.getString("picture");
                        mEmail = object.getString("email") != null ? object.getString("email") : "";
                        mUserType = AppCommonConstants.LOGIN_TYPE.FACEBOOK;
                        Utility.showToast(LoginActivity.this, "Welcome " + mName + " email: " + mEmail);
                        callDashboardActivity();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.showToast(LoginActivity.this, "Welcome " + mName);
                        callDashboardActivity();
                    }

                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callDashboardActivity() {
        User user = new User();
        user.setName(mName);
        user.setEmail(mEmail);

        if (mProfile != null) {
            user.setProfileImages(mProfile);
        }
        user.setId(mUserID);

        if (Utility.isNetworkAvailable(this)) {
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            loginViewModel.doLoginWithEmail(mEmail, null, mUserType, mUserID, AppCommonConstants.API_REQUEST.REQUEST_ID_1001, deviceId);
        }
    }


    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mProgressbar.show();
                break;
            case SUCCESS:
                mProgressbar.dismiss();
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        if (apiResponse.data instanceof String) {
                            String message = (String) apiResponse.data;
                            Utility.showToast(this, message);
                        } else {
                            LoginResponseBaseModel registrationResponse = (LoginResponseBaseModel) apiResponse.data;
                            AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_PROFILE_STATUS, AppConstant.PROFILE_STATUS.SIGN_UP, this);
                            AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_ID, registrationResponse.getUser().getId(), this);
                            AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.APP_TOKEN, registrationResponse.getUser().getToken(), this);
                            AppInstance.getAppInstance().setAppUserInstance(registrationResponse.getUser(), this);
                            //Utility.showToast(this, registrationResponse.getMessage());
                            //--------
                            Intent intent = new Intent(this, PhoneActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        break;
                }
                break;
            case ERROR:
                mProgressbar.dismiss();
                Utility.showToast(this, getString(R.string.api_failure_error_msg));
                break;
        }
    }
}