package com.example.user.musicappdemo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.user.musicappdemo.adapter.MyAdapter;
import com.example.user.musicappdemo.bean.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MusicService extends Service {
    List<Music> musics;
    Timer timer;
    MediaPlayer mp;
    int pos;
    boolean isException = false;
    boolean isFirst = true;

    private MusicBinder mBinder=new MusicBinder();
    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp=new MediaPlayer();
        timer=new Timer();
//        musics=new ArrayList<>();
//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//
////                mBinder.play(pos);
////                Toast.makeText(MusicService.this,"重新播放",Toast.LENGTH_SHORT).show();
////                mp.reset();//重置
////                try {
////
////                    mp.setDataSource(musics.get(pos).getUrl());//用路径设置资源
////                    mp.prepare();
////                    mp.start();
//////                Message msg = new Message();
//////                msg.what = 3;
//////                MainActivity.handler.sendMessage(msg);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//
//
//                    int a=musics.size();
//                    if (pos>a-2)
//                        pos=-1;
//                    mBinder.playNext(pos+1);
//                    Message msg = new Message();
//                    msg.arg1=pos;
//                    msg.what = 3;
//                    MainActivity.handler.sendMessage(msg);
//
//
//
//            }
//        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    class MusicBinder extends Binder{
        public void play(int position ) {
            pos = position;
            try {
                if (mp.isPlaying()) {
                    mp.reset();//重置
                }
                mp.setDataSource(musics.get(position).getUrl());//用路径设置资源

                mp.prepare();
                mp.start();
                moveSeek();

//                Message msg = new Message();
//                msg.what = 2;
//                MainActivity.handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                isException = true;

            }

        }
        public void pause(){
//            if (isFirst){
//                mBinder.play(0);
//            }else {
                Message msg = new Message();
                msg.what = 2;

                if(mp.isPlaying()) {
                    mp.pause();
                    msg.arg1  = 1;
                    MainActivity.handler.sendMessage(msg);
//                ibPause.setImageResource(R.drawable.pause);
                }
                else{
                    mp.start();
                    msg.arg1  = 0;
                    MainActivity.handler.sendMessage(msg);
//                ibPause.setImageResource(R.drawable.play);
                }
//            }


        }
        public void playNext(int pos1  ){
            try {
                pos = pos1;
                mp.reset();
                mp.setDataSource(musics.get(pos1).getUrl());
                mp.prepare();
                mp.start();
//                tvTitlePlaying.setText(musicList.get(next+1).getTitle());

            } catch (Exception e) {
                e.printStackTrace();
                isException = true;

            }

        }
        public void seekTo(int progress){
            //调整MediaPlayer的进度
            mp.seekTo(progress);
        }
        public void seekWait(){
            //停止计时器
            timer.cancel();
        }
        public void seekBegin(){
            timer=new Timer();
            moveSeek();
        }
        public void readList(List<Music> musics1){
             musics=musics1;
        }
    }
    public void moveSeek() {
//        int duration = mp.getDuration();//时长
//        if (duration !=1) {
            TimerTask timerTask = new TimerTask() {//描述具体做什么
                @Override
                public void run() {
                    //间隔执行的任务
                    int duration = mp.getDuration();//时长
                    int progress = mp.getCurrentPosition();//进度
                    //handler

                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = duration;

//                    if (duration==progress){
//                        msg.arg2=0;
//                        Toast.makeText(MusicService.this,"重新播放",Toast.LENGTH_SHORT).show();
//                    }
//                    else {
                    msg.arg2 = progress;
//                    }
                    MainActivity.handler.sendMessage(msg);
                }
            };
            timer.schedule(timerTask, 5, 1000);//任务 延迟值 每间隔多少毫秒执行一次  开启一个计时任务
        }
//        else {
//            Toast.makeText(MusicService.this,"没有音乐在播放",Toast.LENGTH_SHORT).show();
//        }
//    }

}
