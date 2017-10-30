package com.fusiotec.warehousing.warehousing.network;

import android.content.Context;
import android.util.Log;

import com.fusiotec.warehousing.warehousing.utilities.Utils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Owner on 3/23/2017.
 */

public class RetrofitRequestManager {
    ApiService apiService;

    public static final int NETWORK_CONNECTION_FAILED = 100;
    public static final int SERVER_CONNECTION_FAILED = 101;

    public static final int HTTP_BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int REQUEST_SUCCESS = 200;

    public static final int OK = 1;
    public static final int FAILED = 0;

    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";

    private Context context;
    private RestClient restClient;
    public RetrofitRequestManager(Context context,callBackListener listener){
        restClient = new RestClient(context);
        apiService = restClient.getApiService();
        this.listener = listener;
        this.context = context;
    }

    public ApiService getApiService(){
        return apiService;
    }

    public void setRequestSync(Call<GenericReceiver> callBack,final int process){
        try {
            final Response<GenericReceiver> response = callBack.execute();
            if(!response.isSuccessful()){
                listener.requestReceiver(response.errorBody().string(),HTTP_BAD_REQUEST,process,response.code(), response.errorBody().string());
                try {
                    Utils.saveToErrorLogs("onResponse:"+process+"\n"+ response.toString() +"\n"+ response.errorBody().string());
                }catch (IOException ex){
                    Log.e("IOException"," "+ex.getMessage());
                }
            }else{
                listener.requestReceiver(
                        response.body() == null ? "" : response.body().getData(),
                        process,
                        response.body() == null ? 0 : response.body().getSuccess(),
                        response.code(),
                        response.body() == null ? "" : response.body().getMessage());
            }
        }catch (IOException ex){
            Utils.saveToErrorLogs("onResponse:"+process+"\n"+ ex.getMessage());
        }
    }
    public void setRequestAsync(Call<GenericReceiver> callBack,final int process){
        callBack.enqueue(new Callback<GenericReceiver>(){
            @Override
            public void onResponse(Call<GenericReceiver> call, Response<GenericReceiver> response){
                Log.e("response.code()"," "+response.code());
                listener.requestReceiver(
                        response.body() == null ? "" : response.body().getData(),
                        process,
                        response.body() == null ? 0 : response.body().getSuccess(),
                        response.code(),
                        response.body() == null ? "" : response.body().getMessage());
                if(!response.isSuccessful()){
                    Log.e("isSuccessful","ewwas");
                    try {
                        Utils.saveToErrorLogs("onResponse:"+process+"\n"+ response.toString() +"\n"+ response.errorBody().string());
                    }catch (IOException ex){
                        Log.e("IOException"," "+ex.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<GenericReceiver> call, Throwable t){
                Log.e("onFailure",t.getMessage()+" ");
                listener.requestReceiver(t.getMessage(),HTTP_BAD_REQUEST,process,HTTP_BAD_REQUEST, "Bad Request");
                Utils.saveToErrorLogs("onFailure:"+process+"\n"+t.getMessage()+"\n"+call.toString());
            }
        });
    }

    private callBackListener listener;
    public interface callBackListener{
        void requestReceiver(String response, int process, int status, int response_code, String message);
    }
}
