package com.fusiotec.warehousing.warehousing.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.adapters.ImageViewPagerAdapter;
import com.fusiotec.warehousing.warehousing.customviews.CirclePageIndicator;
import com.fusiotec.warehousing.warehousing.customviews.HackyViewPager;
import com.fusiotec.warehousing.warehousing.models.db_classes.Images;
import com.fusiotec.warehousing.warehousing.network.RetrofitRequestManager;
import com.fusiotec.warehousing.warehousing.utilities.Utils;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Owner on 10/19/2017.
 */

public class ImageViewerActivity extends BaseActivity{

    public final static String META_DATA = "meta_data";
    public final static String META_DATA_ID = "meta_data_id";
    public final static int UPLOAD_PHOTO = 1;

    String meta_data;
    int meta_data_id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent in = getIntent();
        meta_data = in.getExtras().getString(META_DATA,"");
        meta_data_id = in.getExtras().getInt(META_DATA_ID,0);
        initUI();
    }
    public void initUI(){
        RealmResults<Images> images = realm.where(Images.class)
                .equalTo("meta_data_id",meta_data_id)
                .equalTo("meta_data",meta_data)
                .findAll();

        ImageViewPagerAdapter menuViewPagerAdapter = new ImageViewPagerAdapter(this,images,true);
        HackyViewPager mPager = (HackyViewPager) findViewById(R.id.pager);
        mPager.setAdapter(menuViewPagerAdapter);

        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        if(images.size() > 1 ){
            mIndicator.setViewPager(mPager);
        }else{
            mIndicator.setVisibility(View.INVISIBLE);
        }
    }

    public void upload_image(String image){
        showProgress(true);
        File file = new File(image);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        RequestBody rb_meta_data = Utils.convertToRequestBody("text/plain",meta_data);
        RequestBody rb_meta_data_id = Utils.convertToRequestBody("text/plain",meta_data_id+"");
        RequestBody rb_label = Utils.convertToRequestBody("text/plain","");

        requestManager.setRequestAsync(requestManager.getApiService().upload_image(body,rb_label,rb_meta_data,rb_meta_data_id),UPLOAD_PHOTO);
    }

    @Override
    void setReceiver(String response, int process, int status) {
        showProgress(false);
        switch (process){
            case UPLOAD_PHOTO:
                setImageUpload(response);
                break;
        }
    }
    public boolean setImageUpload(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt(RetrofitRequestManager.SUCCESS) == 1){
                JSONArray jsonArray = jsonObject.getJSONArray(Images.TABLE_NAME);
                final ArrayList<Images> images = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                        .fromJson(jsonArray.toString(), new TypeToken<List<Images>>(){}.getType());
                if(!images.isEmpty()){
                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                            realm.copyToRealm(images);
                        }
                    });
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch (Exception e){
            Log.e("upload_error",e.getMessage()+"");
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.action_camera, menu);
        return true;
    }

    Uri outputFileUri;
    Uri fileUri;
    String image_cont;

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_camera:
                outputFileUri = Utils.openImageIntent(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Utils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE){
                Log.e("CAMERA_CAPTURE_IMAGE",""+requestCode);
                try {
                    final boolean isCamera;
                    if (data == null) {
                        isCamera = true;
                    } else {
                        final String action = data.getAction();
                        if (action == null) {
                            isCamera = false;
                        } else {
                            isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }
                    if (isCamera) {
                        fileUri = outputFileUri;
                        image_cont = fileUri.getPath();
                        Utils.getResizeImage(image_cont);
                        upload_image(image_cont);
                    } else {
                        fileUri = data == null ? null : data.getData();
                        if(fileUri != null) {
                            image_cont = Utils.getPath(fileUri, this);
                            Utils.getResizeImage(image_cont);
                            upload_image(image_cont);
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(ImageViewerActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
