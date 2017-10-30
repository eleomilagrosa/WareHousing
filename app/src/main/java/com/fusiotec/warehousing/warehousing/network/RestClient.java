package com.fusiotec.warehousing.warehousing.network;

import android.content.Context;

import com.fusiotec.warehousing.warehousing.BuildConfig;
import com.fusiotec.warehousing.warehousing.utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Owner on 3/22/2017.
 */

public class RestClient{
    private ApiService apiService;
    public RestClient(Context context){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(interceptor);
        }

        httpClient.addInterceptor(new Interceptor(){
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "custom/json")
                        .addHeader("id", Constants.client_id)
                        .addHeader("secret", Constants.client_secret)
                        .build();
                return chain.proceed(request);
            }
        });

        httpClient.connectTimeout(30, TimeUnit.SECONDS); // connect timeout
        httpClient.readTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.webservice_address)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
    }
    public ApiService getApiService(){
        return apiService;
    }
}