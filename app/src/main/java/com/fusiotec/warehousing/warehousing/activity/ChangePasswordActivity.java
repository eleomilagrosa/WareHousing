package com.fusiotec.warehousing.warehousing.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.manager.LocalStorage;
import com.fusiotec.warehousing.warehousing.models.db_classes.Accounts;
import com.fusiotec.warehousing.warehousing.network.RetrofitRequestManager;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Owner on 9/10/2017.
 */

public class ChangePasswordActivity extends BaseActivity {
    EditText et_oldpassword, et_newpassword, et_cppassword;
    Button btn_next, btn_cancel;
    final public static int REQUEST_CHANGE_PASSWORD = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        accounts = realm.copyFromRealm(realm.where(Accounts.class).equalTo("id",ls.getInt(LocalStorage.ACCOUNT_ID,0)).findFirst());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Change Password");

        initUI();
    }

    public void setReceiver(String response,int process,int status) {
        switch (process){
            case REQUEST_CHANGE_PASSWORD:
                showProgress(false);
                setAccount(response);
                break;
        }
    }

    public void initUI() {
        et_oldpassword = (EditText) findViewById(R.id.et_oldpassword);
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_cppassword = (EditText) findViewById(R.id.et_cppassword);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                if(changePass()){
                    change_password(accounts);
                }else{
                    showProgress(false);
                }
            }
        });
    }

    public boolean changePass(){
        final String oldpassword = et_oldpassword.getText().toString();
        final String newpassword = et_newpassword.getText().toString();
        final String cppassword = et_cppassword.getText().toString();

        View error_edit_text = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(newpassword)){
            et_newpassword.setError(getString(R.string.error_field_required));
            cancel = true;
            error_edit_text = et_newpassword;
        }
        if (!ls.getString(LocalStorage.ACCOUNT_PASSWORD,"").equals(oldpassword)){
            et_oldpassword.setError("Should be your Old Password");
            cancel = true;
            error_edit_text = et_oldpassword;
        }
        if (!cppassword.equals(newpassword)){
            et_newpassword.setError("Password Mismatch");
            et_cppassword.setError("Password Mismatch");
            cancel = true;
            error_edit_text = et_newpassword;
        }
        if(cancel){
            if(error_edit_text != null){
                error_edit_text.requestFocus();
            }
            return false;
        }

        accounts.setPassword(newpassword);
        return true;
    }
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    public void change_password(Accounts accounts){
        requestManager.setRequestAsync(requestManager.getApiService().change_password(accounts.getId(),accounts.getPassword()),REQUEST_CHANGE_PASSWORD);
    }
    public boolean setAccount(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt(RetrofitRequestManager.SUCCESS) == 1){
                JSONArray jsonArray = jsonObject.getJSONArray(Accounts.TABLE_NAME);
                final ArrayList<Accounts> accounts = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                        .fromJson(jsonArray.toString(), new TypeToken<List<Accounts>>(){}.getType());
                if(!accounts.isEmpty()){
                    ls.saveStringOnLocalStorage(LocalStorage.ACCOUNT_PASSWORD,this.accounts.getPassword());
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(accounts.get(0));
                        }
                    });

                    Toast.makeText(ChangePasswordActivity.this, "Change Pass Success", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    errorMessage("Account does not exist");
                    return false;
                }
            }else{
                errorMessage(jsonObject.getString(RetrofitRequestManager.MESSAGE));
                return false;
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
