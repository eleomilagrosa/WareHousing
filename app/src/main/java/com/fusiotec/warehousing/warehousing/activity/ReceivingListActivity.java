package com.fusiotec.warehousing.warehousing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.adapters.ReceivingSessionListAdapter;
import com.fusiotec.warehousing.warehousing.models.db_classes.ReceivingSession;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
import com.fusiotec.warehousing.warehousing.network.RetrofitRequestManager;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Owner on 9/3/2017.
 */

public class ReceivingListActivity extends BaseActivity {

    ReceivingSessionListAdapter receivingSessionListAdapter;
    RealmResults<ReceivingSession> receivingSessions;
    SwipeRefreshLayout swipeContainer;

    public final static int REQUEST_GET_RECEIVING = 301;
    public final static String SHOW_TYPE = "show_type";

    public final static int OPEN_RECEIVINGS = 1;
    public final static int ALL_RECEIVING = 2;

    int current_show_type = OPEN_RECEIVINGS;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent in = getIntent();
        current_show_type = in.getIntExtra(SHOW_TYPE,OPEN_RECEIVINGS);

        initList();

        getReceivingList();
    }
    @Override
    public void showProgress(boolean show){
        if(show){
            if(swipeContainer != null){
                if(!swipeContainer.isRefreshing()){
                    swipeContainer.setRefreshing(true);
                }
            }
        }else{
            if(swipeContainer != null){
                if(swipeContainer.isRefreshing()){
                    swipeContainer.setRefreshing(false);
                }
            }
        }
    }
    public void setReceiver(String response,int process,int status){
        showProgress(false);
        switch (process){
            case REQUEST_GET_RECEIVING:
                setReceiving(response);
                break;
        }
    }
    public void initList(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                getReceivingList();
            }
        });

        switch (current_show_type){
            case OPEN_RECEIVINGS:
                receivingSessions = realm.where(ReceivingSession.class).isNull("date_closed").findAll();
                break;
            case ALL_RECEIVING:
                receivingSessions = realm.where(ReceivingSession.class).findAll();
                break;
        }

        receivingSessionListAdapter = new ReceivingSessionListAdapter(this,receivingSessions,realm.where(Stations.class).findAll());
        recyclerView.setAdapter(receivingSessionListAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivingListActivity.this, ReceivingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void getReceivingList(){
        requestManager.setRequestAsync(requestManager.getApiService().get_receiving_session(current_show_type),REQUEST_GET_RECEIVING);
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
                        realm.copyToRealmOrUpdate(receivingSessions);
                        }
                    });
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
}
