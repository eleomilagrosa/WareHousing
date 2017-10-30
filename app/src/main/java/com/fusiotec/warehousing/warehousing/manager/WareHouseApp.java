package com.fusiotec.warehousing.warehousing.manager;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.fusiotec.warehousing.warehousing.BuildConfig;
import com.fusiotec.warehousing.warehousing.utilities.Fonts;
import com.fusiotec.warehousing.warehousing.utilities.FontsOverride;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;

/**
 * This is The Base Application
 * @author eleom
 * @author Eleojasmil Milagrosa
 * @version %I% %G%
 * @since 1.0
 */

public class WareHouseApp extends Application {
    public static final String TAG = WareHouseApp.class.getSimpleName();
    private static WareHouseApp sInstance;
    @Override
    public void onCreate(){
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", Fonts.getTypeFaceDir(Fonts.ROBOTO_REGULAR));
        FontsOverride.setDefaultFont(this, "SANS_SERIF", Fonts.getTypeFaceDir(Fonts.RECEIPT_FONT));
        sInstance = this;
        Realm.init(this);
        if(BuildConfig.DEBUG){
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                    .build());
        }
    }
    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public static WareHouseApp getInstance() {
        return sInstance;
    }
    private RequestQueue mRequestQueue;
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
}
