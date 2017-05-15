package com.example.user.musicappdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.musicappdemo.R;
import com.example.user.musicappdemo.bean.User;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by user on 2017/4/12.
 */

public class UserLVAdapter extends BaseAdapter {
    Context context;
    List<User> users;
    public UserLVAdapter(Context context, List<User> users){
        this.context=context;
        this.users=users;
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //使用item布局文件 生产对应的view
        View view= LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        TextView tvNickName=(TextView)view.findViewById(R.id.tv_item_nick_name);
        TextView tvContent=(TextView)view.findViewById(R.id.tv_item_content);
        TextView tvPhone=(TextView)view.findViewById(R.id.tv_item_phone);
        TextView tvsex=(TextView)view.findViewById(R.id.tv_item_sex);
        User user=users.get(position);
        tvNickName.setText(user.getUserName());
        tvContent.setText(user.getContent());
        tvsex.setText(user.getSex());
        tvPhone.setText(user.getPhone());
        return view;
    }
}
