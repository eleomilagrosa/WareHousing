package com.fusiotec.warehousing.warehousing.models.db_classes;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Owner on 10/15/2017.
 */

public class Images extends RealmObject {

    public final static String TABLE_NAME = "images";

    @PrimaryKey
    private int id;
    private String meta_data;
    private int meta_data_id;
    private String image;
    private String label;
    private Date date_created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(String meta_data) {
        this.meta_data = meta_data;
    }

    public int getMeta_data_id() {
        return meta_data_id;
    }

    public void setMeta_data_id(int meta_data_id) {
        this.meta_data_id = meta_data_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }
}
