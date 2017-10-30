package com.fusiotec.warehousing.warehousing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.models.db_classes.Items;
/**
 * Created by Owner on 10/17/2017.
 */

public class ReceivingItemsActivity extends BaseActivity{

    EditText et_barcode,et_desc,et_quantity,et_notes;
    Button btn_cancel,btn_next;

    public final static int CREATE_RECEIVE_ITEMS = 1;
    public final static int SEARCH_ITEM = 2;

    public final static String ID = "id";
    int receiving_session_id = 0;
    int item_id = 0;
    int branch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_receiving_item);

        Intent in = getIntent();
        receiving_session_id = in.getIntExtra(ID,0);
        branch_id = in.getIntExtra(SearchItemActivity.PRINCIPAL_ID,0);

        initViews();
    }


    public void initViews(){
        et_barcode = (EditText) findViewById(R.id.et_barcode);
        et_desc = (EditText) findViewById(R.id.et_desc);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        et_notes = (EditText) findViewById(R.id.et_notes);
        et_barcode.setKeyListener(null);
        et_desc.setKeyListener(null);

        et_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemSearch();
            }
        });
        et_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemSearch();
            }
        });

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveItem();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    public void saveItem(){
        if(item_id == 0){
            Toast.makeText(this, "No Item Selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Integer.parseInt(et_quantity.getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "Not a Integer Format for Quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        create_receive_items(item_id);
    }

    public void create_receive_items(int item_id){
        Items item = realm.where(Items.class).equalTo("id",item_id).findFirst();
        requestManager.setRequestAsync(requestManager.getApiService().create_receive_items(item.getBarcode(),
                item.getDesc(),
                Integer.parseInt(et_quantity.getText().toString()),
                item.getId(),
                receiving_session_id,
                accounts.getId(),
                et_notes.getText().toString()
                ), CREATE_RECEIVE_ITEMS);
    }

    public void openItemSearch(){
        Intent intent = new Intent(ReceivingItemsActivity.this, SearchItemActivity.class);
        intent.putExtra(SearchItemActivity.PRINCIPAL_ID,branch_id);
        startActivityForResult(intent,SEARCH_ITEM);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
    @Override
    void setReceiver(String response, int process, int status){
        showProgress(false);
        switch (process){
            case CREATE_RECEIVE_ITEMS:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case SEARCH_ITEM:
                    item_id = data.getIntExtra(SearchItemActivity.ITEM_ID,0);
                    Items item = realm.where(Items.class).equalTo("id",item_id).findFirst();
                    et_barcode.setText(item.getBarcode());
                    et_desc.setText(item.getDesc());
                    break;
            }
        }
    }
}
