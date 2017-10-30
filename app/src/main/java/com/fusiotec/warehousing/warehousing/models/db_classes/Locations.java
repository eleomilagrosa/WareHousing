package com.fusiotec.warehousing.warehousing.models.db_classes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Owner on 10/15/2017.
 */

public class Locations extends RealmObject{

    public final static String TABLE_NAME = "locations";

    @PrimaryKey
    private int id;
    private String location;
    private String shelf;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }
}
