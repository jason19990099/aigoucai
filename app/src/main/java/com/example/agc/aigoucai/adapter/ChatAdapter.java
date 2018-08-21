package com.example.agc.aigoucai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.bean.ChatBean;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    List<ChatBean> userList;

    public ChatAdapter(Context context,  List<ChatBean> userList){
        this.context=context;
        this.userList=userList;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return null==userList?0:userList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = mInflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            holder.text_id=view.findViewById(R.id.text_id);
            holder.text_id_sp=view.findViewById(R.id.text_id_sp);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        holder.text_id.setText(userList.get(position).getKey());
        holder.text_id_sp.setText(userList.get(position).getValue());
        return view;
    }


    private class ViewHolder {
         TextView text_id;
         TextView text_id_sp;
    }

}
