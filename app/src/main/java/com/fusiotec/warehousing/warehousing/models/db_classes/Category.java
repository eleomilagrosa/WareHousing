package com.fusiotec.warehousing.warehousing.models.db_classes;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Owner on 10/15/2017.
 */

public class Category extends RealmObject{

    public final static String TABLE_NAME = "category";

    @PrimaryKey
    private int id;
    private String name;
    private String desc;
    private RealmList<BranchCategories> branch_categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public RealmList<BranchCategories> getBranch_categories() {
        return branch_categories;
    }

    public void setBranch_categories(RealmList<BranchCategories> branch_categories) {
        this.branch_categories = branch_categories;
    }
}
