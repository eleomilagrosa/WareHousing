package com.fusiotec.warehousing.warehousing.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.models.db_classes.Items;
import com.fusiotec.warehousing.warehousing.models.db_classes.ReceivingItems;

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

public class SearchItemsListAdapter extends RecyclerView.Adapter<SearchItemsListAdapter.ViewHolder>{
    private RealmResults<Items> items;
    private Activity mContext;
    SearchItemsListAdapterListener listener;
    public SearchItemsListAdapter(Activity c, RealmResults<Items> items,SearchItemsListAdapterListener listener){
        this.mContext = c;
        this.items = items;
        this.listener = listener;
        setChangeListener();
    }
    public void setData(RealmResults<Items> items){
        this.items = items;
        setChangeListener();
    }
    private void setChangeListener(){
        items.addChangeListener(new RealmChangeListener<RealmResults<Items>>() {
            @Override
            public void onChange(RealmResults<Items> itemses) {
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_items, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_barcode,tv_desc;
        ImageView iv_add;
        public ViewHolder(View convertView){
            super(convertView);
            tv_barcode = convertView.findViewById(R.id.tv_barcode);
            tv_desc = convertView.findViewById(R.id.tv_desc);
            iv_add = convertView.findViewById(R.id.iv_add);
            iv_add.setOnClickListener(this);
        }
        Items items;
        public void setItems(Items items){
            this.items = items;
        }

        @Override
        public void onClick(View view){
            listener.selectedItem(items);
        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Items item = items.get(position);
        holder.setItems(item);
        holder.tv_barcode.setText(item.getBarcode());
        holder.tv_desc.setText(item.getDesc());
    }
    public interface SearchItemsListAdapterListener{
        void selectedItem(Items item);
    }
}
