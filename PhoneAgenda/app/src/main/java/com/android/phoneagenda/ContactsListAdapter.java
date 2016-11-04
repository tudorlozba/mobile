package com.android.phoneagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tudorlozba on 02/11/2016.
 */
public class ContactsListAdapter extends BaseAdapter {

    private final ArrayList<Contact> contacts;
    private final Context mContext;
    private final ListItemClickListener mListener;

    public ContactsListAdapter(Context context, ArrayList<Contact> contacts, ListItemClickListener listener){
        this.contacts = contacts;
        this.mContext = context;
        this.mListener = listener;
    }
    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Contact currentListData = getItem(position);

        mViewHolder.tv_name.setText(currentListData.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListItemClicked(currentListData);
            }
        });
        return convertView;
    }

    private class MyViewHolder {
        TextView tv_name;

        public MyViewHolder(View item) {
            tv_name = (TextView) item.findViewById(R.id.tvTitle);
        }
    }

    public interface ListItemClickListener{
        void onListItemClicked(Contact c);
    }
}
