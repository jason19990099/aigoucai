package com.example.agc.aigoucai.adapter;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.activity.MainWebviewActivity;
import com.example.agc.aigoucai.bean.ChatBean;
import com.example.agc.aigoucai.util.LogUtil;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    List<ChatBean> userList;

    public ChatAdapter(Context context, List<ChatBean> userList) {
        this.context = context;
        this.userList = userList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return null == userList ? 0 : userList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = mInflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            holder.text_id = view.findViewById(R.id.text_id);
            holder.text_id_sp = view.findViewById(R.id.text_id_sp);
            holder.ll_listview = view.findViewById(R.id.ll_listview);


            holder.iv_img = view.findViewById(R.id.iv_img);
            if (userList.get(position).getCode().equals("qq")) {
                holder.iv_img.setImageResource(R.mipmap.qq);
                holder.text_id_sp.setTextColor(Color.parseColor("#3ccbff"));
            }

            if (userList.get(position).getCode().equals("email")) {
                holder.iv_img.setImageResource(R.mipmap.email);
                holder.text_id_sp.setTextColor(Color.parseColor("#3ccbff"));

            }

            if (userList.get(position).getCode().equals("wechat")) {
                holder.iv_img.setImageResource(R.mipmap.wechat);
                holder.text_id_sp.setTextColor(Color.parseColor("#3ccbff"));
            }

            if (userList.get(position).getCode().equals("web")) {
                holder.iv_img.setImageResource(R.mipmap.kefu);
                holder.text_id_sp.setTextColor(Color.parseColor("#ffffff"));
            }
            holder.iv_img.setVisibility(View.VISIBLE);
            holder.text_id.setText(userList.get(position).getKey());
            holder.text_id_sp.setText(userList.get(position).getValue());
            if (userList.get(position).getCode().equals("web")) {
                holder.text_id_sp.setText(">");
            }

            holder.ll_listview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userList.get(position).getCode().equals("qq")) {
                        try {
                            Toast.makeText(context, "跳转添加qq", Toast.LENGTH_LONG).show();
                            //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + userList.get(position).getValue();//uin是发送过去的qq号码
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "请检查是否安装QQ", Toast.LENGTH_LONG).show();
                        }
                    }

                    if (userList.get(position).getCode().equals("email")) {
                        // 必须明确使用mailto前缀来修饰邮件地址,如果使用
                        // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
                        Uri uri = Uri.parse("mailto:" + userList.get(position).getValue());
                        String[] email = {userList.get(position).getValue()};
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
                        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
                        context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
                    }

                    if (userList.get(position).getCode().equals("wechat")) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(cmp);
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
                        }
                    }

                    if (userList.get(position).getCode().equals("web")) {
                        Bundle bundleTab = new Bundle();
                        bundleTab.putString("url", userList.get(position).getValue());
                        Intent intent = new Intent();
                        intent.setClass(context, MainWebviewActivity.class);
                        intent.putExtras(bundleTab);
                        context.startActivity(intent);
                    }

                }
            });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (userList.get(position).getValue().equals("")) {
            holder.ll_listview.setVisibility(View.GONE);
        }else{
            holder.ll_listview.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private class ViewHolder {
        ImageView iv_img;
        LinearLayout ll_listview;
        TextView text_id;
        TextView text_id_sp;
    }

}
