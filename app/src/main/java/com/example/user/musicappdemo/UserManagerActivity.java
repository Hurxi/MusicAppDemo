package com.example.user.musicappdemo;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.musicappdemo.adapter.UserLVAdapter;
import com.example.user.musicappdemo.bean.User;
import com.example.user.musicappdemo.utils.MyDBHelper;

import java.util.ArrayList;
import java.util.List;

public class UserManagerActivity extends BaseActivity {

    ListView lvUser;
    UserLVAdapter adapter;
    List<User> users;
    MyDBHelper dbHelper;
    AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        dbHelper=new MyDBHelper(this,"MyUser.db",null,3);
        //数据源 adapter的构建
        lvUser=(ListView)findViewById(R.id.lv_user);

        dialog=new AlertDialog.Builder(UserManagerActivity.this);
        users=new ArrayList<>();
        adapter=new UserLVAdapter(this,users);
        lvUser.setAdapter(adapter);
        initData();
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String userName=users.get(position).getUserName();
                dialog.setTitle("你确定删除此用户？")
                        .setMessage("用户名"+userName)
                        .setCancelable(false)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db=dbHelper.getWritableDatabase();
//                                Cursor cursor=db.query("User",new String[]{"userPwd"},
//                                        "userName=?",new String[]{userName},null,null,null)
                                db.delete("User","userName=?",new String[]{userName});
                                Toast.makeText(UserManagerActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
//                                int id=cursor.getInt(0);
//                                String userName=cursor.getString(1);
//                                String userPwd=cursor.getString(2);
//                                String sex=cursor.getString(3);
//                                String nickName=cursor.getString(4);
//                                String phone=cursor.getString(5);
//                                String content=cursor.getString(6);
//                                User user=new User(id,userName,userPwd,sex,nickName,phone,content);
                                users.remove(position);
                                adapter.notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                dialog.show();
                adapter.notifyDataSetChanged();

            }

        });

    }

    private void initData() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("User",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String userName=cursor.getString(1);
            String userPwd=cursor.getString(2);
            String sex=cursor.getString(3);
            String nickName=cursor.getString(4);
            String phone=cursor.getString(5);
            String content=cursor.getString(6);

            User user=new User(id,userName,userPwd,sex,nickName,phone,content);
//            User user=new User(id,userName,userPwd,"123","dddd","zzz","dddd");

            users.add(user);
        }
        adapter.notifyDataSetChanged();
    }
}
