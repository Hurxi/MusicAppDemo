package com.example.user.musicappdemo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.musicappdemo.bean.Music;
import com.example.user.musicappdemo.R;

import java.util.List;

import static com.example.user.musicappdemo.R.drawable.playing;

/**
 * Created by user on 2017/4/10.
 */

public class MyAdapter extends BaseAdapter {
    Context context;
    List<Music> musicList;

    public MyAdapter(Context context,List<Music> musicList) {
        this.context=context;
        this.musicList=musicList;

    }

    @Override
    public int getCount() {
        return musicList.size();
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
        //构建item的内容

        View view= LayoutInflater.from(context).inflate(R.layout.item_music,parent,false);
        TextView tvTitle= (TextView) view.findViewById(R.id.tv_item_title);
        TextView tvArtist= (TextView) view.findViewById(R.id.tv_item_artist);
        TextView tvId=(TextView)view.findViewById(R.id.tv_music_id);
        if (musicList.get(position).getPos()==100){
            tvId.setText("");
//            Drawable drawable=(Drawable)view.findViewById(playing);
            Drawable drawable = context.getResources().getDrawable(R.drawable.playing);

            tvId.setBackground(drawable);

        }else {
            tvId.setText(musicList.get(position).getPos()+"");

        }

        tvTitle.setText(musicList.get(position).getTitle());
        tvArtist.setText(musicList.get(position).getArtist());

        return view;
    }
}
