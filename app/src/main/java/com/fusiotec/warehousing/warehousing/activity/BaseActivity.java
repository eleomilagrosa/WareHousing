package com.fusiotec.warehousing.warehousing.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.manager.LocalStorage;
import com.fusiotec.warehousing.warehousing.models.db_classes.Accounts;
import com.fusiotec.warehousing.warehousing.network.RetrofitRequestManager;

import io.realm.Realm;

/**
 * Created by Owner on 8/5/2017.
 */

public abstract class BaseActivity extends AppCompatActivity{

    Realm realm;
    LocalStorage ls;
    Accounts accounts;
    RetrofitRequestManager requestManager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        ls = new LocalStorage(this);
        accounts = realm.where(Accounts.class).equalTo("id",ls.getInt(LocalStorage.ACCOUNT_ID,0)).findFirst();
        requestManager = new RetrofitRequestManager(this,callBackListener);
    }

    RetrofitRequestManager.callBackListener callBackListener = new RetrofitRequestManager.callBackListener(){
        @Override
        public void requestReceiver(String response, int process, int status, int response_code,String message){
            if(response_code == RetrofitRequestManager.REQUEST_SUCCESS){
                switch (process){
                    case RetrofitRequestManager.HTTP_BAD_REQUEST:
                    case RetrofitRequestManager.NETWORK_CONNECTION_FAILED:
                    case RetrofitRequestManager.SERVER_CONNECTION_FAILED:
                        showProgress(false);
                        Toast.makeText(BaseActivity.this, "BadRequest Error!" + response, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        setReceiver(response,process,status);
                        break;
                }
            }else{
                showProgress(false);
                Toast.makeText(BaseActivity.this, "BadRequest Error!" + response, Toast.LENGTH_SHORT).show();
            }
        }
    };

    abstract void setReceiver(String response,int process,int status);

    ProgressDialog progress;
    public void showProgress(boolean show){
        if(show){
            if(progress != null){
                if(progress.isShowing()) progress.dismiss();
            }
            progress = new ProgressDialog(this);
            progress.setMessage("Please Wait");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }else{
            if(progress != null){
                if(progress.isShowing())progress.dismiss();
            }
        }
    }


    public void errorMessage(final String message){
        runOnUiThread(new Runnable() {

            @Override
            public void run()
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
                alertDialog.setTitle("Error");
                alertDialog.setMessage(message);
                alertDialog.setIcon(android.R.drawable.stat_notify_error);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }

        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}
