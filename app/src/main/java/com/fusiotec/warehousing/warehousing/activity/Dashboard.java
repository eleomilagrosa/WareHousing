package com.fusiotec.warehousing.warehousing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.manager.ImageManager;
import com.fusiotec.warehousing.warehousing.manager.LocalStorage;
import com.fusiotec.warehousing.warehousing.models.db_classes.Accounts;
import com.fusiotec.warehousing.warehousing.utilities.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.realm.Realm;

/**
 * Created by Owner on 10/14/2017.
 */

public class Dashboard extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,View.OnClickListener{

    public final static int GET_ORIGINAL_DATE = 1;
    NavigationView navigationView;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        handlerExit = new Handler();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(accounts.getAccount_type_id() >= Accounts.ADMIN){
            navigationView.getMenu().findItem(R.id.setups).setVisible(true);
            navigationView.getMenu().findItem(R.id.accounts).setVisible(true);
            navigationView.getMenu().findItem(R.id.approved_accounts).setVisible(true);
            navigationView.getMenu().findItem(R.id.branches).setVisible(true);
            if(accounts.getAccount_type_id() == Accounts.SUPER_ADMIN){
                navigationView.getMenu().findItem(R.id.admins).setVisible(true);
            }else{
                navigationView.getMenu().findItem(R.id.admins).setVisible(false);
            }
        }else{
            navigationView.getMenu().findItem(R.id.setups).setVisible(false);
            navigationView.getMenu().findItem(R.id.accounts).setVisible(false);
            navigationView.getMenu().findItem(R.id.approved_accounts).setVisible(false);
            navigationView.getMenu().findItem(R.id.admins).setVisible(false);
            navigationView.getMenu().findItem(R.id.branches).setVisible(false);
        }

        findViewById(R.id.rl_new_job_order).setOnClickListener(this);
        getOriginalDate();
    }
    public void setNavHeader(){
        View header = navigationView.getHeaderView(0);
        ImageView iv_profile = header.findViewById(R.id.iv_profile);
        TextView tv_name = header.findViewById(R.id.tv_name);
        TextView tv_email = header.findViewById(R.id.tv_email);
        tv_name.setText(accounts.getLast_name()+", "+accounts.getFirst_name());
        tv_email.setText(accounts.getEmail());
        ImageManager.PicassoLoadThumbnail(this, Constants.webservice_address,accounts.getImage(),iv_profile,R.drawable.profile_unknown);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setNavHeader();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        clickMenu(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onClick(View view){
        int id = view.getId();
        clickMenu(id);
    }
    public void clickMenu(int id) {
        switch (id){
//            case R.id.rl_upload:
//                Toast.makeText(this, "Uploading "+upload_images.size()+" images" , Toast.LENGTH_SHORT).show();
//                Utils.syncImages(this);
//                break;
            case R.id.rl_new_job_order:
            case R.id.new_job_orders:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent accountActivity = new Intent(Dashboard.this,ReceivingListActivity.class);
                        accountActivity.putExtra(ReceivingListActivity.SHOW_TYPE,ReceivingListActivity.OPEN_RECEIVINGS);
                        startActivity(accountActivity);
                        overridePendingTransition(R.anim.top_in, R.anim.freeze);
                    }
                },300);
                break;
            case R.id.accounts:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent accountActivity = new Intent(Dashboard.this,AccountListActivity.class);
                        accountActivity.putExtra(AccountListActivity.APPROVED,true);
                        startActivity(accountActivity);
                        overridePendingTransition(R.anim.top_in, R.anim.freeze);
                    }
                },300);
                break;
            case R.id.approved_accounts:
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent accountActivity2 = new Intent(Dashboard.this,AccountListActivity.class);
                        accountActivity2.putExtra(AccountListActivity.APPROVED,false);
                        startActivity(accountActivity2);
                        overridePendingTransition(R.anim.top_in, R.anim.freeze);
                    }
                },300);
                break;
            case R.id.branches:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent in = new Intent(Dashboard.this,BranchListActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.top_in, R.anim.freeze);
                    }
                },300);
                break;
            case R.id.admins:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent in = new Intent(Dashboard.this,AdminListActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.top_in, R.anim.freeze);
                    }
                },300);
                break;
            case R.id.profile:
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent in = new Intent(Dashboard.this,RegistrationActivity.class);
                        in.putExtra(RegistrationActivity.ACCOUNT_ID,accounts.getId());
                        in.putExtra(RegistrationActivity.IS_PROFILE,true);
                        startActivity(in);
                        overridePendingTransition(R.anim.top_in, R.anim.freeze);
                    }
                },300);
                break;
            case R.id.settings:
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent in = new Intent(Dashboard.this,ChangePasswordActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.top_in, R.anim.freeze);
                    }
                },300);
                break;
            case R.id.log_out:
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm){
                        realm.delete(Accounts.class);
                    }
                });
                ls.saveIntegerOnLocalStorage(LocalStorage.ACCOUNT_ID,0);
                Intent in = new Intent(Dashboard.this,LoginActivity.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.top_in, R.anim.freeze);

                break;
        }
    }

    boolean is_exiting = false;
    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(is_exiting){
                super.onBackPressed();
            }else{
                Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
                is_exiting = true;
                doubleTap();
            }
        }
    }
    Handler handlerExit;
    public void doubleTap(){
        handlerExit.postDelayed(new Runnable(){
            @Override
            public void run(){
                is_exiting = false;
            }
        }, 2500);
    }

    public void getOriginalDate(){
        requestManager.setRequestAsync(requestManager.getApiService().get_original_date(),GET_ORIGINAL_DATE);
    }
    public void setReceiver(String response,int process,int status){
        Log.e(process + "response" + status, response);
        switch(process){
            case GET_ORIGINAL_DATE:
                setTimeDifference(response);
                break;
        }
    }
    public void setTimeDifference(String response){
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        String date = jsonObject.get("original_date").getAsString();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime server_datetime = fmt.parseDateTime(date);
        DateTime device_dateTime = new DateTime();
        Seconds sec = Seconds.secondsBetween(device_dateTime,server_datetime);
        ls.saveIntegerOnLocalStorage(LocalStorage.TIME_DIFFERENCE_IN_SECONDS,sec.getSeconds());
    }
}
