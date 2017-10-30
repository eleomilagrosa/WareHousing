package com.fusiotec.warehousing.warehousing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.adapters.SearchItemsListAdapter;
import com.fusiotec.warehousing.warehousing.models.db_classes.Category;
import com.fusiotec.warehousing.warehousing.models.db_classes.Items;

import io.realm.Case;
import io.realm.RealmResults;

/**
 * Created by Owner on 10/17/2017.
 */

public class SearchItemActivity extends BaseActivity{

    Button btn_cancel;
    TextView et_category,et_filter;
    SearchItemsListAdapter adapter;
    int category_id = 0;

    public final static String ITEM_ID = "item_id";
    public final static String PRINCIPAL_ID = "principal_id";

    RealmResults<Category> categories;
    RealmResults<Items> items;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        Intent in = getIntent();
        categories = realm.where(Category.class)
                .equalTo("branch_categories.branch_id",in.getExtras().getInt(PRINCIPAL_ID,0))
                .findAll();
        initViews();
    }

    public void initViews(){
        et_category = (TextView) findViewById(R.id.et_category);
        et_filter = (TextView) findViewById(R.id.et_filter);
        et_category.setKeyListener(null);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });

        items = realm.where(Items.class).findAll();

        RecyclerView rv_list = (RecyclerView) findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchItemsListAdapter(this, items
                , new SearchItemsListAdapter.SearchItemsListAdapterListener() {
                    @Override
                    public void selectedItem(Items item) {
                        Intent intent = new Intent();
                        intent.putExtra(ITEM_ID,item.getId());
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
        rv_list.setAdapter(adapter);
        et_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] st = new String[categories.size()+1];
                st[0] = "All";
                for(int i = 0; i < categories.size(); i++){
                    st[i+1] = categories.get(i).getName();
                }

                new MaterialDialog.Builder(SearchItemActivity.this)
                        .title("Select Class Type")
                        .items(st)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                category_id = position == 0 ? 0 : categories.get(position-1).getId();
                                et_category.setText(position == 0 ? "All" :categories.get(position-1).getName());
                                search(et_filter.getText().toString());
                            }
                        })
                        .positiveText("Choose")
                        .show();
            }
        });
        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void afterTextChanged(Editable editable) {
                search(editable.toString());
            }
        });
    }
    public void search(String search){
        if(category_id == 0){
            items = realm.where(Items.class).contains("barcode",search, Case.INSENSITIVE).or().contains("desc",search, Case.INSENSITIVE).findAll();
        }else{
            items = realm.where(Items.class).equalTo("category_id",category_id)
                    .beginGroup()
                    .contains("barcode",search, Case.INSENSITIVE).or().contains("desc",search, Case.INSENSITIVE)
                    .endGroup().findAll();
        }
        adapter.setData(items);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    @Override
    void setReceiver(String response, int process, int status){

    }
}
