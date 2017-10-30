package com.fusiotec.warehousing.warehousing.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
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

public class RegisterBranchActivity extends BaseActivity{

    EditText et_branch_name,et_branch_prefix,et_address,et_mobile_number,et_description;
    Button btn_next,btn_cancel;

    boolean isUpdate = false;
    final public static int REQUEST_REGISTER_BRANCH = 301;
    final public static int REQUEST_UPDATE_BRANCH = 302;
    final public static int REQUEST_DELETE_BRANCH = 303;
    final public static String BRANCH_ID = "branch_id";

    Stations branch;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_branches);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int branch_id = getIntent().getIntExtra(BRANCH_ID,0);
        isUpdate = branch_id != 0;
        if(!isUpdate){
            branch = new Stations();
        }else{
            branch = realm.copyFromRealm(realm.where(Stations.class).equalTo("id",branch_id).findFirst());
        }

        initUI();
        if(isUpdate){
            setValues();
        }
    }

    public void setReceiver(String response,int process,int status){
        showProgress(false);
        switch (process){
            case REQUEST_REGISTER_BRANCH:
                if(setStation(response)) {
                    Toast.makeText(RegisterBranchActivity.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                break;
            case REQUEST_UPDATE_BRANCH:
                if(setStation(response)){
                    Toast.makeText(RegisterBranchActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                break;
            case REQUEST_DELETE_BRANCH:
                if(setStation(response)){
                    Toast.makeText(RegisterBranchActivity.this, "Branch Deleted!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                break;
        }
    }
    public void initUI(){
        et_branch_name = (EditText) findViewById(R.id.et_branch_name);
        et_branch_prefix = (EditText) findViewById(R.id.et_branch_prefix);
        et_address = (EditText) findViewById(R.id.et_address);
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        et_description = (EditText) findViewById(R.id.et_description);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                boolean success = save();
                if(success){
                    if(isUpdate){
                        update_station(branch);
                    }else{
                        create_station(branch);
                    }
                }else{
                    showProgress(false);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void setValues(){
        btn_next.setText("Update");
        et_branch_name.setText(branch.getStation_name());
        et_branch_prefix.setText(branch.getStation_prefix());
        et_address.setText(branch.getStation_address());
        et_mobile_number.setText(branch.getStation_number());
        et_description.setText(branch.getStation_description());
    }
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    public boolean save(){
        View error_edit_text = null;
        final String branch_name = et_branch_name.getText().toString();
        final String branch_prefix = et_branch_prefix.getText().toString();
        final String address = et_address.getText().toString();
        final String mobile_number = et_mobile_number.getText().toString();
        final String description = et_description.getText().toString();
        boolean cancel = false;

        if(branch_prefix.length() > 2){
            Toast.makeText(this, "prefix should have only two capital letters", Toast.LENGTH_SHORT).show();
            cancel = true;
            error_edit_text = et_branch_name;
        }

        if (TextUtils.isEmpty(branch_name)){
            et_branch_name.setError(getString(R.string.error_field_required));
            cancel = true;
            error_edit_text = et_branch_name;
        }
        if (TextUtils.isEmpty(branch_prefix)){
            et_branch_prefix.setError(getString(R.string.error_field_required));
            cancel = true;
            error_edit_text = et_branch_prefix;
        }
        if (TextUtils.isEmpty(address)){
            et_address.setError(getString(R.string.error_field_required));
            cancel = true;
            error_edit_text = et_address;
        }
        if (TextUtils.isEmpty(mobile_number)){
            et_mobile_number.setError(getString(R.string.error_field_required));
            cancel = true;
            error_edit_text = et_mobile_number;
        }
        if (TextUtils.isEmpty(description)){
            et_description.setError(getString(R.string.error_field_required));
            cancel = true;
            error_edit_text = et_description;
        }

        if(cancel){
            if(error_edit_text != null){
                error_edit_text.requestFocus();
            }
            return false;
        }

        branch.setStation_name(branch_name);
        branch.setStation_prefix(branch_prefix);
        branch.setStation_address(address);
        branch.setStation_number(mobile_number);
        branch.setStation_description(description);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Are sure you want to delete this Branch?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                branch.setIs_deleted(1);
                                delete_stations(branch);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        if(isUpdate){
            getMenuInflater().inflate(R.menu.action_delete, menu);
        }
        return true;
    }

    public void create_station(Stations station){
        requestManager.setRequestAsync(requestManager.getApiService().create_station(station.getStation_name(),station.getStation_prefix(),station.getStation_address(),station.getStation_number(),station.getStation_description()),REQUEST_REGISTER_BRANCH);
    }
    public void update_station(Stations station){
        requestManager.setRequestAsync(requestManager.getApiService().update_station(station.getId(),station.getStation_name(),station.getStation_prefix(),station.getStation_address(),station.getStation_number(),station.getStation_description()),REQUEST_UPDATE_BRANCH);
    }
    public void delete_stations(Stations station){
        requestManager.setRequestAsync(requestManager.getApiService().delete_stations(station.getId()),REQUEST_DELETE_BRANCH);
    }
    public boolean setStation(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt(RetrofitRequestManager.SUCCESS) == 1){
                JSONArray jsonArray = jsonObject.getJSONArray(Stations.TABLE_NAME);
                final ArrayList<Stations> stations = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                        .fromJson(jsonArray.toString(), new TypeToken<List<Stations>>(){}.getType());
                if(!stations.isEmpty()){
                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                            realm.copyToRealmOrUpdate(stations.get(0));
                        }
                    });
                }else{
                    errorMessage("Station does not exist");
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
