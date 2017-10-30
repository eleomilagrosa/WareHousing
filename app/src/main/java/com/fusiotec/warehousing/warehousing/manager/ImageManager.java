package com.fusiotec.warehousing.warehousing.manager;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Eleojasmil Milagrosa on 10/20/2015.
 */
public class ImageManager {
    public static void PicassoLoadThumbnail(Context mContext,String webService,String media_path,ImageView image_menu, int placeholder){
        loadImage(mContext,webService,media_path,image_menu,placeholder,null);
    }
    public static void PicassoLoadThumbnail(Context mContext,String webService,String media_path,ImageView image_menu,int placeholder,Callback listener){
        loadImage(mContext,webService,media_path,image_menu,placeholder,listener);
    }
    private static void loadImage(Context mContext,String webService,String media_path,ImageView image_menu,int placeholder, Callback listener){
        if(media_path != null){
            String[] filePath = media_path.split("/");
            if(filePath[0].equals("images")){
                Picasso.with(mContext).load(webService+media_path).fit().centerInside().placeholder(placeholder).error(placeholder).into(image_menu,listener);
            }else{
                File path = new File(media_path);
                Picasso.with(mContext).load(path).fit().centerInside().placeholder(placeholder).error(placeholder).into(image_menu,listener);
            }
        }else{
            Picasso.with(mContext).load(placeholder).fit().centerInside().placeholder(placeholder).error(placeholder).into(image_menu,listener);
        }
    }
}
