package com.fusiotec.warehousing.warehousing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.adapters.ReceivingItemsListAdapter;
import com.fusiotec.warehousing.warehousing.models.db_classes.ReceivingSession;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
import com.fusiotec.warehousing.warehousing.network.RetrofitRequestManager;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owner on 10/15/2017.
 */

public class ReceivingActivity extends BaseActivity{
    ReceivingSession receivingSession;
    RealmResults<Stations> stations;
    ReceivingItemsListAdapter receivingItemsListAdapter;
    RelativeLayout rl_branch;

    int receiving_session_id;

    public static final int CREATE_RECEIVING_SESSION = 1;
    public static final int UPDATE_RECEIVING_SESSION = 2;
    public static final int GET_RECEIVING_SESSION = 3;

    public final static String ID = "id";
    TextView tv_branch,tv_receiving_no;
    String ref_no = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Ref #:");

        stations = realm.where(Stations.class).findAll();
        initView();

        Intent in = getIntent();
        receiving_session_id = in.getIntExtra(ID,0);
    }
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiving_session_id != 0){
            receivingSession = realm.where(ReceivingSession.class).equalTo("id",receiving_session_id).findFirst();
            get_receiving_session_by_id(receiving_session_id);
            setValues();
        }else{
            create_receiving_session(accounts.getId());
        }
    }

    public void initView(){
        tv_receiving_no = (TextView) findViewById(R.id.tv_receiving_no);
        tv_branch = (TextView) findViewById(R.id.tv_branch);
        rl_branch = (RelativeLayout) findViewById(R.id.rl_branch);
        rl_branch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(receivingSession != null){
                    String[] st = new String[stations.size()];
                    int selected_branch = 0;
                    for (int i = 0; i < stations.size(); i++){
                        st[i] = stations.get(i).getStation_name();
                        if(receivingSession.getBranch_id() == stations.get(i).getId()){
                            selected_branch = i;
                        }
                    }
                    new MaterialDialog.Builder(ReceivingActivity.this)
                            .title("Select Principal")
                            .items(st)
                            .itemsCallbackSingleChoice(selected_branch, new MaterialDialog.ListCallbackSingleChoice(){
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text){
                                    askForReferenceNumber(which);
                                    return true;
                                }
                            })
                            .positiveText("Choose")
                            .show();
                }
            }
        });
    }
    public void askForReferenceNumber(final int which){
        new MaterialDialog.Builder(ReceivingActivity.this)
                .title("Select Principal")
                .input("Ref #", receivingSession.getRef_no(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input){
                        ref_no = input.toString();
                        setBranch(which,ref_no);
                    }
                })
                .positiveText("Done")
                .show();
    }
    public void setValues(){
        receivingItemsListAdapter = new ReceivingItemsListAdapter(this,receivingSession.getReceiving_items());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(receivingItemsListAdapter);

        tv_branch.setText(receivingSession.getBranch_id() == 0 ? "" : stations.where().equalTo("id",receivingSession.getBranch_id()).findFirst().getStation_name());
        tv_receiving_no.setText(receivingSession.getReceive_no());
        setTitle("Ref #: "+ (receivingSession.getRef_no() == null ? "" : receivingSession.getRef_no()));
    }
    public void setBranch(final int position,String ref_no){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                receivingSession.setBranch_id(stations.get(position).getId());
            }
        });
        tv_branch.setText(receivingSession.getBranch_id() == 0 ? "": stations.where().equalTo("id",receivingSession.getBranch_id()).findFirst().getStation_name());
        update_receiving_session(receivingSession.getId(),receivingSession.getBranch_id(),ref_no);
    }
    public void create_receiving_session(int user_id){
        requestManager.setRequestAsync(requestManager.getApiService().create_receive_session(user_id),CREATE_RECEIVING_SESSION);
    }
    public void update_receiving_session(int id,int station_id,String ref_no){
        requestManager.setRequestAsync(requestManager.getApiService().update_receiving_session(id,station_id,ref_no),UPDATE_RECEIVING_SESSION);
    }
    public void get_receiving_session_by_id(int id){
        requestManager.setRequestAsync(requestManager.getApiService().get_receiving_session_by_id(id),GET_RECEIVING_SESSION);
    }

    @Override
    public void setReceiver(String response, int process, int status){
        showProgress(false);
        switch (process){
            case CREATE_RECEIVING_SESSION:
            case UPDATE_RECEIVING_SESSION:
            case GET_RECEIVING_SESSION:
                setReceiving(response);
                break;
        }
    }
    public boolean setReceiving(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt(RetrofitRequestManager.SUCCESS) == 1){
                JSONArray jsonArray = jsonObject.getJSONArray(ReceivingSession.TABLE_NAME);
                final ArrayList<ReceivingSession> receivingSessions = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                        .fromJson(jsonArray.toString(), new TypeToken<List<ReceivingSession>>(){}.getType());
                if(!receivingSessions.isEmpty()){
                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                           receivingSession = realm.copyToRealmOrUpdate(receivingSessions.get(0));
                           receiving_session_id = receivingSession.getId();
                        }
                    });
                    setValues();
                }
            }else{
                Toast.makeText(this, "No More Results", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.action_receiving_session, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_camera:
                if(receivingSession != null){
                    Intent intent = new Intent(ReceivingActivity.this, ImageViewerActivity.class);
                    intent.putExtra(ImageViewerActivity.META_DATA,ReceivingSession.TABLE_NAME);
                    intent.putExtra(ImageViewerActivity.META_DATA_ID,receivingSession.getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }else{
                    Toast.makeText(this, "No ID for Receiving Session", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_receiving:
                if(receivingSession != null){
                    if(receivingSession.getBranch_id() != 0){
                        Intent intent = new Intent(ReceivingActivity.this, ReceivingItemsActivity.class);
                        intent.putExtra(SearchItemActivity.PRINCIPAL_ID,receivingSession.getBranch_id());
                        intent.putExtra(ReceivingItemsActivity.ID,receivingSession.getId());
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    }else{
                        Toast.makeText(this, "Select Principal First", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "No ID for Receiving Session", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
