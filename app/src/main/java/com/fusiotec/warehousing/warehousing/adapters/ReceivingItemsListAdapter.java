package com.fusiotec.warehousing.warehousing.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.models.db_classes.ReceivingItems;
import com.fusiotec.warehousing.warehousing.models.db_classes.ReceivingSession;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
import com.fusiotec.warehousing.warehousing.utilities.Utils;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * This is adapter Menu list
 * @author eleom
 * @author Eleojasmil Milagrosa
 * @version %I% %G%
 * @since 1.0
 */

public class ReceivingItemsListAdapter extends RecyclerView.Adapter<ReceivingItemsListAdapter.ViewHolder>{
    private RealmList<ReceivingItems> receivingItems;
    private Activity mContext;
    public ReceivingItemsListAdapter(Activity c, RealmList<ReceivingItems> receivingItems){
        this.mContext = c;
        this.receivingItems = receivingItems;
        setChangeListener();
    }
    public void setData(RealmList<ReceivingItems> receivingItems){
        this.receivingItems = receivingItems;
        setChangeListener();
    }
    private void setChangeListener(){
        receivingItems.addChangeListener(new RealmChangeListener<RealmList<ReceivingItems>>() {
            @Override
            public void onChange(RealmList<ReceivingItems> receivingItemses) {
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return receivingItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receiving, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_barcode,tv_desc,tv_quantity;
        public ViewHolder(View convertView){
            super(convertView);
            tv_barcode = convertView.findViewById(R.id.tv_barcode);
            tv_desc = convertView.findViewById(R.id.tv_desc);
            tv_quantity = convertView.findViewById(R.id.tv_quantity);
            convertView.setOnClickListener(this);
        }
        ReceivingItems ReceivingItems;
        public void setReceivingItems(ReceivingItems ReceivingItems){
            this.ReceivingItems = ReceivingItems;
        }

        @Override
        public void onClick(View view){
//            Intent intent = new Intent(mContext, RegistrationActivity.class);
//            intent.putExtra(RegistrationActivity.ACCOUNT_ID,receivingSessions.getId());
//            mContext.startActivity(intent);
//            mContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        ReceivingItems receivingItem = receivingItems.get(position);
        holder.setReceivingItems(receivingItem);
        holder.tv_barcode.setText(receivingItem.getBarcode());
        holder.tv_desc.setText(receivingItem.getDesc());
        holder.tv_quantity.setText(receivingItem.getQuantity()+"");
    }
}
