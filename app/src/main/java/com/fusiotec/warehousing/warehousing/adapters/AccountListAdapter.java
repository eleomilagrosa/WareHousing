package com.fusiotec.warehousing.warehousing.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fusiotec.warehousing.warehousing.R;
import com.fusiotec.warehousing.warehousing.activity.RegistrationActivity;
import com.fusiotec.warehousing.warehousing.models.db_classes.Accounts;
import com.fusiotec.warehousing.warehousing.models.db_classes.Stations;

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

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder>{
    private RealmResults<Accounts> accounts;
    private Activity mContext;
    public AccountListAdapter(Activity c, RealmResults<Accounts> accounts){
        this.mContext = c;
        this.accounts = accounts;
        setChangeListener();
    }
    public void setData(RealmResults<Accounts> accounts){
        this.accounts = accounts;
        setChangeListener();
    }
    private void setChangeListener(){
        accounts.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Accounts>>() {
            @Override
            public void onChange(RealmResults<Accounts> accounts, OrderedCollectionChangeSet changeSet) {
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return accounts.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accounts, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_name,tv_mobile_number,tv_email;
        public ViewHolder(View convertView){
            super(convertView);
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_mobile_number = convertView.findViewById(R.id.tv_mobile_number);
            tv_email = convertView.findViewById(R.id.tv_email);
            convertView.setOnClickListener(this);
        }
        Accounts selected_account;
        public void setAccount(Accounts selected_account){
            this.selected_account = selected_account;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, RegistrationActivity.class);
            intent.putExtra(RegistrationActivity.ACCOUNT_ID,selected_account.getId());
            mContext.startActivity(intent);
            mContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Accounts account = accounts.get(position);
        holder.setAccount(account);
        holder.tv_name.setText(account.getLast_name() + ", " + account.getFirst_name());
        holder.tv_mobile_number.setText(account.getPhone_no());
        holder.tv_email.setText(account.getEmail());
    }
}
