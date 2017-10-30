package com.fusiotec.warehousing.warehousing.models.db_classes;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Owner on 8/12/2017.
 */

public class Stations extends RealmObject{
    public final static String TABLE_NAME = "stations";

    @PrimaryKey
    private int id;
    private String station_name = "";
    private String station_prefix = "";
    private String station_address = "";
    private String station_number = "";
    private String station_description = "";
    private String station_image = "";
    private Date date_created;
    private Date date_modified;
    private int is_deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_prefix() {
        return station_prefix;
    }

    public void setStation_prefix(String station_prefix) {
        this.station_prefix = station_prefix;
    }

    public String getStation_address() {
        return station_address;
    }

    public void setStation_address(String station_address) {
        this.station_address = station_address;
    }

    public String getStation_number() {
        return station_number;
    }

    public void setStation_number(String station_number) {
        this.station_number = station_number;
    }

    public String getStation_description() {
        return station_description;
    }

    public void setStation_description(String station_description) {
        this.station_description = station_description;
    }

    public String getStation_image() {
        return station_image;
    }

    public void setStation_image(String station_image) {
        this.station_image = station_image;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Date date_modified) {
        this.date_modified = date_modified;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }
}
