package com.fusiotec.warehousing.warehousing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.manager.LocalStorage;
import com.fusiotec.warehousing.warehousing.models.db_classes.Category;
import com.fusiotec.warehousing.warehousing.models.db_classes.Items;
import com.fusiotec.warehousing.warehousing.models.db_classes.Locations;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
import com.fusiotec.warehousing.warehousing.models.serialize.CategorySerialize;
import com.fusiotec.warehousing.warehousing.network.RetrofitRequestManager;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Owner on 10/18/2017.
 */

public class SplashScreenActivity extends BaseActivity {

    public final static int GET_ITEMS = 1;
    public final static int GET_LOCATIONS = 2;
    public final static int GET_CATEGORIES = 3;
    public final static int REQUEST_GET_STATIONS = 4;

    ProgressBar progressBar;
    TextView textView;

    int current_loading = 0;
    int total_loads = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        progressBar = (ProgressBar) findViewById(R.id.splashscreen_progressbar);
        textView = (TextView) findViewById(R.id.splashscreen_text);
        ls.saveBooleanOnLocalStorage(LocalStorage.IS_STILL_LOADING,true);
        startLoading();
    }

    public void startLoading(){
        current_loading++;
        progressBar.setProgress((current_loading * 100) / total_loads);
        if(current_loading <= total_loads){
            switch (current_loading){
                case GET_ITEMS:
                    textView.setText("Getting Items...");
                    getItems();
                    break;
                case GET_LOCATIONS:
                    textView.setText("Getting Locations...");
                    getLocations();
                    break;
                case GET_CATEGORIES:
                    textView.setText("Getting Categories...");
                    getCategories();
                    break;
                case REQUEST_GET_STATIONS:
                    textView.setText("Getting Branches...");
                    getStations();
                    break;
            }
        }else{
            ls.saveBooleanOnLocalStorage(LocalStorage.IS_STILL_LOADING,false);
            Intent in = new Intent(this,Dashboard.class);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.bottom_in, R.anim.freeze);
        }
    }

    public void getItems(){
        requestManager.setRequestAsync(requestManager.getApiService().get_all_items(),GET_ITEMS);
    }
    public boolean setItems(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt(RetrofitRequestManager.SUCCESS) == 1){
                JSONArray jsonArray = jsonObject.getJSONArray(Items.TABLE_NAME);
                final ArrayList<Items> items = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                        .fromJson(jsonArray.toString(), new TypeToken<List<Items>>(){}.getType());
                if(!items.isEmpty()){
                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                            realm.copyToRealmOrUpdate(items);
                        }
                    });
                    startLoading();
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
    public void getLocations(){
        requestManager.setRequestAsync(requestManager.getApiService().get_all_locations(),GET_LOCATIONS);
    }
    public boolean setLocations(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt(RetrofitRequestManager.SUCCESS) == 1){
                JSONArray jsonArray = jsonObject.getJSONArray(Locations.TABLE_NAME);
                final ArrayList<Locations> locations = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                        .fromJson(jsonArray.toString(), new TypeToken<List<Locations>>(){}.getType());
                if(!locations.isEmpty()){
                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                            realm.copyToRealmOrUpdate(locations);
                        }
                    });
                    startLoading();
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
    public void getCategories(){
        requestManager.setRequestAsync(requestManager.getApiService().get_categories(),GET_CATEGORIES);
    }
    public boolean setCategories(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt(RetrofitRequestManager.SUCCESS) == 1){
                JSONArray jsonArray = jsonObject.getJSONArray(Category.TABLE_NAME);
                final ArrayList<Category> categories = new GsonBuilder()
                        .registerTypeAdapter(Category.class,new CategorySerialize())
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                        .fromJson(jsonArray.toString(), new TypeToken<List<Category>>(){}.getType());
                if(!categories.isEmpty()){
                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                            realm.copyToRealmOrUpdate(categories);
                        }
                    });
                    startLoading();
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
    public void getStations(){
        requestManager.setRequestAsync(requestManager.getApiService().get_stations(""),REQUEST_GET_STATIONS);
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
                startLoading();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    void setReceiver(String response, int process, int status){
        switch (process){
            case GET_ITEMS:
                setItems(response);
                break;
            case GET_LOCATIONS:
                setLocations(response);
                break;
            case GET_CATEGORIES:
                setCategories(response);
                break;
            case REQUEST_GET_STATIONS:
                setStations(response);
                break;
        }
    }
}
