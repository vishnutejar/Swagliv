package com.app.common.network;

import com.app.common.utils.Utility;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is a singleton class where we Create retrofit client and configure it to call web apis using retrofit library.
 */
public class RetrofitClient {

    private static RetrofitClient mInstance;
    private static String mToken;
    private Retrofit retrofit;
    //        public static final String BASE_URL = "https://prasko.co.in/json/";
    public static final String BASE_URL = "http://swagliv-env.eba-mkepnmq2.ap-south-1.elasticbeanstalk.com/";


    /**
     * Create retrofit instance.
     */
    private RetrofitClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(40, TimeUnit.SECONDS);
        httpClient.connectTimeout(40, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", mToken != null ? mToken : "")
                        .build();

                Utility.printLogs("BEARER", "" + mToken);
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public static String getToken() {
        return mToken;
    }

    public static void setToken(String mToken) {
        RetrofitClient.mToken = mToken;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
