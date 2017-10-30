package com.fusiotec.warehousing.warehousing.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.activity.ReceivingActivity;
import com.fusiotec.warehousing.warehousing.activity.RegistrationActivity;
import com.fusiotec.warehousing.warehousing.models.db_classes.Accounts;
import com.fusiotec.warehousing.warehousing.models.db_classes.ReceivingSession;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;
import com.fusiotec.warehousing.warehousing.utilities.Utils;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;


/**
 * This is adapter Menu list
 * @author eleom
 * @author Eleojasmil Milagrosa
 * @version %I% %G%
 * @since 1.0
 */

public class ReceivingSessionListAdapter extends RecyclerView.Adapter<ReceivingSessionListAdapter.ViewHolder>{
    private RealmResults<ReceivingSession> receivingSessions;
    private Activity mContext;
    private RealmResults<Stations> stations;
    public ReceivingSessionListAdapter(Activity c, RealmResults<ReceivingSession> receivingSessions,RealmResults<Stations> stations){
        this.mContext = c;
        this.receivingSessions = receivingSessions;
        this.stations = stations;
        setChangeListener();
    }
    public void setData(RealmResults<ReceivingSession> receivingSessions){
        this.receivingSessions = receivingSessions;
        setChangeListener();
    }
    private void setChangeListener(){
        receivingSessions.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<ReceivingSession>>(){
            @Override
            public void onChange(RealmResults<ReceivingSession> receivingSessions, OrderedCollectionChangeSet changeSet){
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount(){
        return receivingSessions.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receiving_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_date,tv_branch,tv_quantity;
        public ViewHolder(View convertView){
            super(convertView);
            tv_date = convertView.findViewById(R.id.tv_date);
            tv_branch = convertView.findViewById(R.id.tv_branch);
            tv_quantity = convertView.findViewById(R.id.tv_quantity);
            convertView.setOnClickListener(this);
        }
        ReceivingSession receivingSessions;
        public void setReceivingSession(ReceivingSession receivingSessions){
            this.receivingSessions = receivingSessions;
        }

        @Override
        public void onClick(View view){
            Intent intent = new Intent(mContext, ReceivingActivity.class);
            intent.putExtra(ReceivingActivity.ID,receivingSessions.getId());
            mContext.startActivity(intent);
            mContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        ReceivingSession receivingSession = receivingSessions.get(position);
        holder.setReceivingSession(receivingSession);
        holder.tv_branch.setText(receivingSession.getBranch_id() == 0 ? "" : stations.where().equalTo("id",receivingSession.getBranch_id()).findFirst().getStation_name());
        holder.tv_date.setText(Utils.dateToString(receivingSession.getDate_opened(),"MMM dd, yyyy HH:mm:ss"));
        holder.tv_quantity.setText(receivingSession.getReceiving_items().size()+"");
    }
}
