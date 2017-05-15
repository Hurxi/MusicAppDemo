package com.example.user.musicappdemo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.musicappdemo.adapter.MyAdapter;
import com.example.user.musicappdemo.bean.Music;
import com.example.user.musicappdemo.utils.MyDBHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView lvMusic;
    MyAdapter adapter;
    static List<Music> musicList;
    MediaPlayer mp=new MediaPlayer();
    static ImageButton ibPause;
    ImageButton ibNext;
    boolean isPause = false;
    boolean isPlay=false;
    static TextView tvTitlePlaying;
    static TextView tvAuthorPlaying,tvTimePlaying,tvTimingPlaying;
    TextView tvNickName,tvContent;
    int lastPos = -1;

    public static SeekBar seek;
    private int now;
    MyDBHelper dbHelper;
    MusicService.MusicBinder mBinder;
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Activity与Service建立连接后执行的方法 即绑定后  拿IBinder对象
            mBinder= (MusicService.MusicBinder) service;
            mBinder.readList(musicList);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static Handler handler=new Handler(){
        //处理service发来的消息

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //修改textView内容
                    int duration=msg.arg1;
                    int progress=msg.arg2;
                    seek.setMax(duration);
                    Date date1=new Date(duration);
                    Date date2=new Date(progress);
                    seek.setProgress(progress);
                    int timeBegin1=date1.getMinutes();
                    int timeBegin2=date2.getMinutes();
                    int timeStop1=date1.getSeconds();
                    int timeStop2=date2.getSeconds();
                    if(timeBegin1<10){
                        if (timeStop1<10) {
                            tvTimePlaying.setText("0" + timeBegin1 + ":" + "0" + timeStop1);
                        }
                        else {
                            tvTimePlaying.setText("0" + timeBegin1 + ":" +  timeStop1);
                        }
                    }
                    else {
                        if (timeStop1<10) {
                            tvTimePlaying.setText(timeBegin1 + ":" + "0" + timeStop1);
                        }
                        else {
                            tvTimePlaying.setText( timeBegin1 + ":" +  timeStop1);
                        }
                    }


                    if(timeBegin2<10){
                        if (timeStop2<10) {
                            tvTimingPlaying.setText("0" + timeBegin2 + ":" + "0" + timeStop2);
                        }
                        else {
                            tvTimingPlaying.setText("0" + timeBegin2 + ":" +  timeStop2);
                        }
                    }
                    else {
                        if (timeStop2<10) {
                            tvTimingPlaying.setText(timeBegin2 + ":" + "0" + timeStop2);
                        }
                        else {
                            tvTimingPlaying.setText( timeBegin2 + ":" +  timeStop2);
                        }
                    }



//                    Log.d("", "handleMessage: ");
                    break;
                case  2:
                    int a   = msg.arg1;
                    Log.d("", "handleMessage: ");
                    if (a==1){
//                        ibPause.setImageResource(R.drawable.pause);
//                        ibPause.setImageResource();
                        ibPause.setBackgroundResource(R.drawable.ic_play_circle_outline_red_400_24dp);
                    }else {
//                        ibPause.setImageResource(R.drawable.play);
                        ibPause.setBackgroundResource(R.drawable.ic_pause_circle_outline_red_400_24dp);

                    }
                    break;
                case 3:
                    int pos=msg.arg1;
                    tvTitlePlaying.setText(musicList.get(pos).getTitle());
                    tvAuthorPlaying.setText("("+musicList.get(pos).getArtist()+")");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,MusicService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);
        lvMusic=(ListView)findViewById(R.id.lvMusicList);
        ibPause=(ImageButton)findViewById(R.id.ib_pause);
        ibNext=(ImageButton)findViewById(R.id.ib_next);
        dbHelper=new MyDBHelper(this,"MyUser.db",null,3);
        tvTitlePlaying=(TextView)findViewById(R.id.tv_title_playing) ;
        tvAuthorPlaying=(TextView)findViewById(R.id.tv_title_playing_author);
        tvTimingPlaying=(TextView)findViewById(R.id.tv_title_timing);
        tvTimePlaying=(TextView)findViewById(R.id.tv_title_time);


        seek=(SeekBar)findViewById(R.id.seek);
        ibPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mBinder.pause();
//                seek.setProgress(0);
//                seek.setMax(0);

            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seek.setProgress(0);
                seek.setMax(0);

                if(now<adapter.getCount()-1){
                    now=now+1;}
                else {
                    now=0;
                }
                tvTitlePlaying.setText(musicList.get(now).getTitle());
                tvAuthorPlaying.setText("("+musicList.get(now).getArtist()+")");
                 mBinder.playNext(now);

                musicList.get(now).setPos(100);
                if (lastPos!=-1){
                    if(lastPos!=now) {
                        musicList.get(lastPos).setPos(lastPos);
                    }
                }
                lastPos = now;
                adapter.notifyDataSetChanged();
            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //监听正在拖动 停止计时器  拖动结束后关掉计时器
                mBinder.seekWait();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //停止拖动时 调整播放器的播放进度
                //1.得到用户拖动的值
                //2.将播放器调整到这个位置继续播放
                int progress=seek.getProgress();
                mBinder.seekTo(progress);
                mBinder.seekBegin();
            }
        });




        musicList=new ArrayList<>();
        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,   int position, long id) {
                //列表项的点击监听
                //播放音乐
                now=position;
                Log.d("dd", "onItemClick: "+position);

                mBinder.play(position);
                isPlay=true;
                tvTitlePlaying.setText(musicList.get(position).getTitle());
                tvAuthorPlaying.setText("("+musicList.get(position).getArtist()+")");
                musicList.get(position).setPos(100);
                if (lastPos!=-1){
                    if(lastPos!=position) {
                        musicList.get(lastPos).setPos(lastPos);
                    }
                }
                lastPos = position;
                adapter.notifyDataSetChanged();

                }
//                Toast.makeText(MainActivity.this,musicList.get(position).getTitle(),Toast.LENGTH_SHORT).show();

        });

        adapter=new MyAdapter(MainActivity.this,musicList);
        lvMusic.setAdapter(adapter);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=0){
            //没权限 申请
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }
        else {
            initMusicList();//需要权限
        }











        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//标题栏
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//侧滑栏
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvNickName=(TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_nick_name);
        tvContent=(TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_content);
        String nick  = getIntent().getStringExtra("nickname");
        String content  = getIntent().getStringExtra("content");
//        tvNickName.setText(nick);
//        tvContent.setText(content);
        Intent intent2=getIntent();
        String userName2=intent2.getStringExtra("userName");
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from User where userName=?",new String[]{userName2});
        cursor.moveToFirst();
        tvNickName.setText(cursor.getString(cursor.getColumnIndex("nickName")));
        tvContent.setText(cursor.getString(cursor.getColumnIndex("content")));
        cursor.close();
        tvTitlePlaying.setText(musicList.get(0).getTitle());
        tvAuthorPlaying.setText("("+musicList.get(0).getArtist()+")" );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==0){
                    initMusicList();
                }
                else {
                    Toast.makeText(this,"权限拒绝",Toast.LENGTH_SHORT).show();
                }
                break;
            default:

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    private void initMusicList() {
        //初始化音乐列表 读本地音乐文件
        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null
        ,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //遍历cursor中的内容
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String title=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String url=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            int duration=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            int size=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            //读取一条音乐数据 存入数据源
            musicList.add(new Music(id,title,artist,album,url,duration,size,cursor.getPosition()+1));
        }
        //读完数据后刷新列表
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        Intent intent2=getIntent();
//        String userName2=intent2.getStringExtra("userName");
//        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        Cursor cursor=db.rawQuery("select * from User where userName=?",new String[]{userName2});
//        tvNickName.setText(cursor.getString(cursor.getColumnIndex("nickName")));
//        tvContent.setText(cursor.getString(cursor.getColumnIndex("content")));
//        cursor.close();

        int id = item.getItemId();

        if (id == R.id.nav_user_manager) {
            // Handle the camera action
            //用户管理
            //跳转到用户管理的activity
            Intent intent=new Intent(MainActivity.this,UserManagerActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_user_info) {
            Intent intent1=getIntent();
            String userName=intent1.getStringExtra("userName");
            String userPwd=intent1.getStringExtra("userPwd");
            Intent intent=new Intent(MainActivity.this,UserInfoActivity.class);
            intent.putExtra("userName1",userName);
            intent.putExtra("userPwd1",userPwd);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_quit) {
            ActivityCotroller.finishAll();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
