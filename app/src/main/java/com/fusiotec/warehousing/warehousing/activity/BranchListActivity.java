package com.fusiotec.warehousing.warehousing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.adapters.BranchListAdapter;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owner on 9/3/2017.
 */

public class BranchListActivity extends BaseActivity {

    BranchListAdapter branchListAdapter;
    RealmResults<Stations> station_list;
    EditText et_search;
    SwipeRefreshLayout swipeContainer;
    LinearLayoutManager linearlayout;
    RecyclerView recyclerView;

    public final static int REQUEST_GET_STATIONS = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initList();
        initSearch();
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
        switch (process){
            case REQUEST_GET_STATIONS:
                showProgress(false);
                setStations(response);
                break;
        }
    }
    public void initList(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearlayout = new LinearLayoutManager(this);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                getStations(et_search.getText().toString());
            }
        });
        station_list = realm.where(Stations.class).equalTo("is_deleted",0).findAll();

        branchListAdapter = new BranchListAdapter(this,station_list,accounts);
        recyclerView.setAdapter(branchListAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(accounts.getAccount_type_id() > 1 ? View.VISIBLE : View.GONE);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addNewBranch();
            }
        });
    }



    public void initSearch(){
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    search(et_search.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }
    public void addNewBranch(){
        Intent in = new Intent(this,RegisterBranchActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public void search(String s){
        showProgress(true);
        station_list = realm.where(Stations.class)
                .equalTo("is_deleted",0)
                .contains("station_name",s)
                .findAll();
        setList();
        getStations(s);
    }
    public void setList(){
        branchListAdapter.setData(station_list);
        branchListAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    public void getStations(String search){
        requestManager.setRequestAsync(requestManager.getApiService().get_stations(search),REQUEST_GET_STATIONS);
    }
    public boolean setStations(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(Stations.TABLE_NAME);
            final ArrayList<Stations> stations = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                    .fromJson(jsonArray.toString(), new TypeToken<List<Stations>>(){}.getType());
            if(!stations.isEmpty()){
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm){
                        realm.copyToRealmOrUpdate(stations);
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
