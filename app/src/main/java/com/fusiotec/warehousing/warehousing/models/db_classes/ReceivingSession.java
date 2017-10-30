package com.fusiotec.warehousing.warehousing.models.db_classes;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Owner on 10/15/2017.
 */

public class ReceivingSession extends RealmObject{

    public final static String TABLE_NAME = "receiving_session";

    @PrimaryKey
    private int id;
    private int user_id;
    private Date date_opened;
    private Date date_closed;
    private int branch_id;
    private String ref_no;
    private String receive_no;

    private RealmList<ReceivingItems> receiving_items;
    private RealmList<Images> images;

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getDate_opened() {
        return date_opened;
    }

    public void setDate_opened(Date date_opened) {
        this.date_opened = date_opened;
    }

    public Date getDate_closed() {
        return date_closed;
    }

    public void setDate_closed(Date date_closed) {
        this.date_closed = date_closed;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public RealmList<ReceivingItems> getReceiving_items() {
        return receiving_items;
    }

    public void setReceiving_items(RealmList<ReceivingItems> receiving_items) {
        this.receiving_items = receiving_items;
    }

    public RealmList<Images> getImages() {
        return images;
    }

    public void setImages(RealmList<Images> images) {
        this.images = images;
    }

    public String getRef_no() {
        return ref_no;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    public String getReceive_no() {
        return receive_no;
    }

    public void setReceive_no(String receive_no) {
        this.receive_no = receive_no;
    }
}
