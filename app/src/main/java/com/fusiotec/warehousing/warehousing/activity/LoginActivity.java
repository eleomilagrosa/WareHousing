package com.fusiotec.warehousing.warehousing.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.manager.LocalStorage;
import com.fusiotec.warehousing.warehousing.models.db_classes.Accounts;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
import com.fusiotec.warehousing.warehousing.network.RetrofitRequestManager;
import com.fusiotec.warehousing.warehousing.utilities.Constants;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class LoginActivity extends BaseActivity{


    private final static int LOGIN = 2;
    final public static int REQUEST_GET_STATIONS = 3;

    private final int REQUEST_CODE_ASK_PERMISSIONS_READ_EXTERNAL_STORAGE = 1000;

    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private Button mConnect;
    private Button registration;

    String mUsername,mPassword;

    String accounts = "{\"accounts\":[{\"id\":1,\"first_name\":\"Eleojasmil\",\"last_name\":\"Milagrosa\",\"username\":\"eleo\",\"password\":\"ed2b1f468c5f915f3f1cf75d7068baae\",\"email\":\"eleomilagrosa2@yahoo.com\",\"phone_no\":null,\"image\":null,\"account_type_id\":1,\"station_id\":1,\"is_main_branch\":0,\"approved_by\":null,\"date_approved\":\"2017-08-06 23:46:41\",\"date_created\":\"2017-08-06 21:30:57\",\"date_modified\":\"2017-08-13 20:42:23\",\"station\":{\"id\":1,\"station_name\":\"Indian Palace\",\"station_prefix\":\"IN\",\"station_address\":\"davao\",\"station_number\":\"321213213\",\"station_description\":\"kanto\",\"station_image\":null,\"date_created\":\"2017-08-12 14:41:57\",\"date_modified\":\"2017-08-12 14:41:57\"}}],\"success\":1,\"message\":\"Success\"}";

    public AppCompatActivity getActivity(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ls.saveBooleanOnLocalStorage(LocalStorage.IS_STILL_UPLOADING,false);
        Constants.webservice_address = ls.getString(LocalStorage.WEBSERVICE, Constants.webservice_address);

        int account_id = ls.getInt(LocalStorage.ACCOUNT_ID,0);
        if(account_id != 0){
            if(!ls.getBoolean(LocalStorage.IS_STILL_LOADING,false)){
                Intent in = new Intent(this,Dashboard.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.bottom_in, R.anim.freeze);
            }
        }

        permissionRequester(REQUEST_CODE_ASK_PERMISSIONS_READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA);

        initUI();

        getStations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        System.out.println("MENUS");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                askWebservice();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initUI(){
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConnect = (Button) findViewById(R.id.login_connect);
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setLogin(accounts);
                attemptLogin();
            }
        });
        registration = (Button) findViewById(R.id.registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.bottom_in, R.anim.freeze);
            }
        });
        mProgressView = findViewById(R.id.login_progress);
    }
    private void attemptLogin(){
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        mUsername = mUsernameView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mUsername)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        if (cancel){
            focusView.requestFocus();
        } else {
            showProgress(true);
            loginProcess(mUsername, mPassword);
        }
    }

    public void loginProcess(String username,String password){
        requestManager = new RetrofitRequestManager(this,callBackListener);
        requestManager.setRequestAsync(requestManager.getApiService().login(username,password),LOGIN);
    }

    public void setLogin(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt(RetrofitRequestManager.SUCCESS) == 1){
                JSONArray jsonArray = jsonObject.getJSONArray(Accounts.TABLE_NAME);
                final ArrayList<Accounts> accounts = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                        .fromJson(jsonArray.toString(), new TypeToken<List<Accounts>>(){}.getType());
                if(!accounts.isEmpty()){
                    final Accounts temp_account = accounts.get(0);
                    if(temp_account.getDate_approved() != null){
                        realm.executeTransaction(new Realm.Transaction(){
                            @Override
                            public void execute(Realm realm){
                                realm.insertOrUpdate(temp_account);
                            }
                        });
                        ls.saveIntegerOnLocalStorage(LocalStorage.ACCOUNT_ID, temp_account.getId());
                        ls.saveStringOnLocalStorage(LocalStorage.ACCOUNT_PASSWORD, mPassword);
                        goToSplashScreen();
                    }else{
                        errorMessage("Account did not approved yet");
                    }
                }else{
                    errorMessage("Account does not exist");
                }
            }else{
                errorMessage(jsonObject.getString(RetrofitRequestManager.MESSAGE));
            }
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        showProgress(false);
    }

    public void goToSplashScreen(){
        Intent in = new Intent(LoginActivity.this,SplashScreenActivity.class);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.bottom_in, R.anim.freeze);
    }
    @Override
    public void showProgress(final boolean show){
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mConnect.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        mConnect.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mConnect.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            }
        });
        registration.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        registration.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registration.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }



    public void permissionRequester(int code,String... permissions){
        ArrayList<String> permission_list = new ArrayList<>();
        for(String temp:permissions){
            int checker = ActivityCompat.checkSelfPermission(this,temp);
            if(checker != PackageManager.PERMISSION_GRANTED){
                permission_list.add(temp);
            }
        }
        if(!permission_list.isEmpty()){
            ActivityCompat.requestPermissions(this,permission_list.toArray(new String[permission_list.size()]),
                    code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS_READ_EXTERNAL_STORAGE:
                for (int results:grantResults){
                    if(results != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
                break;
        }
    }

    public void setReceiver(String response,int process,int status){
        switch (process){
            case LOGIN : setLogin(response);
                break;
            case REQUEST_GET_STATIONS : setStations(response);
                break;
        }
    }
    public void getStations(){
        requestManager.setRequestAsync(requestManager.getApiService().get_stations(""),REQUEST_GET_STATIONS);
    }
    public boolean setStations(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(Stations.TABLE_NAME);
            final ArrayList<Stations> stations = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                    .fromJson(jsonArray.toString(), new TypeToken<List<Stations>>(){}.getType());
            if(!stations.isEmpty()){
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm){
                        realm.copyToRealmOrUpdate(stations);
                    }
                });
                realm.close();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void askWebservice(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_webservice);
        final EditText device_name = (EditText) dialog.findViewById(R.id.device_name);
        device_name.setText(ls.getString(LocalStorage.WEBSERVICE,Constants.webservice_address));
        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device_name.setError(null);
                String name = device_name.getText().toString().trim();
                if( name.isEmpty() ){
                    device_name.setError("Invalid input!");
                }else{
                    Constants.webservice_address = name;
                    ls.saveStringOnLocalStorage(LocalStorage.WEBSERVICE,name);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}
