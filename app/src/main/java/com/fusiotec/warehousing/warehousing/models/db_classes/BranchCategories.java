package com.fusiotec.warehousing.warehousing.models.db_classes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Owner on 10/27/2017.
 */

public class BranchCategories extends RealmObject {

    public final static String TABLE_NAME = "branch_categories";

    @PrimaryKey
    private int id;
    private int branch_id;
    private int category_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
