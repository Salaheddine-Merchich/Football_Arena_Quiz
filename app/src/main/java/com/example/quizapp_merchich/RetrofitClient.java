package com.example.quizapp_merchich;

import android.util.Log;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RETROFIT_DEBUG";
    private static final String BASE_URL = "http://10.0.2.2:8083/";
    private static Retrofit retrofit = null;

    static {
        Log.e(TAG, "!!! RETROFIT CLIENT CLASS LOADED !!!");
    }

    public static ApiService getApiService() {
        if (retrofit == null) {
            Log.e(TAG, "Initializing Retrofit instance for: " + BASE_URL);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {
                Log.i("QUIZ_API_LOG", message);
            });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Log.d(TAG, "OUTGOING REQUEST: " + chain.request().url());
                        return chain.proceed(chain.request());
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            
            Log.e(TAG, "Retrofit instance created successfully.");
        }
        return retrofit.create(ApiService.class);
    }
}
