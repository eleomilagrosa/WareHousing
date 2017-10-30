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
import com.fusiotec.warehousing.warehousing.adapters.AccountListAdapter;
import com.fusiotec.warehousing.warehousing.models.db_classes.Accounts;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
import com.fusiotec.warehousing.warehousing.network.RetrofitRequestManager;
import com.fusiotec.warehousing.warehousing.utilities.Constants;
import com.fusiotec.warehousing.warehousing.utilities.Utils;
import com.github.ybq.endless.Endless;
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

public class AccountListActivity extends BaseActivity{
    AccountListAdapter accountListAdapter;
    RealmResults<Accounts> account_list;
    EditText et_search;
    SwipeRefreshLayout swipeContainer;
    boolean show_approved = true;

    final public static int REQUEST_GET_ACCOUNTS = 301;

    final public static String APPROVED = "approved";

    RecyclerView recyclerView;
    Endless endless;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        show_approved = getIntent().getBooleanExtra(APPROVED,true);

        initList();
        initSearch();

        Accounts temp_account;
        if(show_approved){
            temp_account = realm.where(Accounts.class).equalTo("is_deleted",0).notEqualTo("id",accounts.getId()).isNotNull("date_approved").equalTo("account_type_id",Accounts.EMPLOYEE).findFirst();
        }else{
            temp_account = realm.where(Accounts.class).equalTo("is_deleted",0).notEqualTo("id",accounts.getId()).isNull("date_approved").equalTo("account_type_id",Accounts.EMPLOYEE).findFirst();
        }
        if(temp_account == null){
            getAccounts("", Constants.FIRST_LOAD);
        }
    }
    public void setReceiver(String response,int process,int status){
        if(endless != null) endless.loadMoreComplete();
        switch (process){
            case REQUEST_GET_ACCOUNTS:
                showProgress(false);
                setAccount(response);
                if(account_list.size() < 7){
                    endless.setLoadMoreAvailable(false);
                }
                break;
        }
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

    public void initList(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                getAccounts(et_search.getText().toString(),Constants.SWIPE_DOWN);
            }
        });
        if(show_approved){
            account_list = realm.where(Accounts.class).equalTo("is_deleted",0).notEqualTo("id",accounts.getId()).isNotNull("date_approved").equalTo("account_type_id",Accounts.EMPLOYEE).findAllSorted("date_modified",  Sort.DESCENDING);
        }else{
            account_list = realm.where(Accounts.class).equalTo("is_deleted",0).notEqualTo("id",accounts.getId()).isNull("date_approved").equalTo("account_type_id",Accounts.EMPLOYEE).findAllSorted("date_modified",  Sort.DESCENDING);
        }
        accountListAdapter = new AccountListAdapter(this,account_list);
        recyclerView.setAdapter(accountListAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(show_approved ? View.GONE : View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addNewCustomer();
            }
        });
        setLoadMoreInit();
    }
    public void setLoadMoreInit(){
        View loadingView = View.inflate(this, R.layout.layout_loading, null);
        endless = Endless.applyTo(recyclerView, loadingView);
        endless.setLoadMoreListener(new Endless.LoadMoreListener(){
            @Override
            public void onLoadMore(int page){
                getAccounts(et_search.getText().toString(), Constants.SWIPE_UP);
            }
        });
    }
    public void initSearch(){
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    search(et_search.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }
    public void addNewCustomer(){
        Intent in = new Intent(this,RegistrationActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
    public void search(String s){
        showProgress(true);
        if(show_approved){
            account_list = realm.where(Accounts.class)
                    .equalTo("is_deleted",0)
                    .notEqualTo("id",accounts.getId())
                    .isNotNull("date_approved")
                    .equalTo("account_type_id",Accounts.EMPLOYEE)
                    .beginGroup()
                        .contains("last_name",s, Case.INSENSITIVE)
                        .or()
                        .contains("first_name",s, Case.INSENSITIVE)
                    .endGroup()
                    .findAllSorted("date_modified",  Sort.DESCENDING);
        }else{
            account_list = realm.where(Accounts.class)
                    .equalTo("is_deleted",0)
                    .notEqualTo("id",accounts.getId())
                    .equalTo("account_type_id",Accounts.EMPLOYEE)
                        .beginGroup()
                        .contains("last_name",s, Case.INSENSITIVE)
                        .or()
                        .contains("first_name",s, Case.INSENSITIVE)
                        .endGroup()
                    .isNull("date_approved").findAllSorted("date_modified",  Sort.DESCENDING);
        }
        setList();
        getAccounts(s,Constants.FIRST_LOAD);
    }
    public void setList(){
        accountListAdapter.setData(account_list);
        accountListAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    public void getAccounts(String search,int direction){
        requestManager.setRequestAsync(requestManager.getApiService().get_accounts(show_approved ? 1 : 2 , search, getEndOrStartTime(direction == Constants.SWIPE_DOWN), account_list.isEmpty() ? Constants.FIRST_LOAD : direction ),REQUEST_GET_ACCOUNTS);
    }

    public String getEndOrStartTime(boolean swipe_down){
        if(!account_list.isEmpty()){
            return Utils.dateToString(account_list.where().findAllSorted("date_modified",swipe_down ? Sort.DESCENDING : Sort.ASCENDING).get(0).getDate_modified(),"yyyy-MM-dd HH:mm:ss");
        }
        return "";
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
                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                        realm.copyToRealmOrUpdate(accounts);
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
