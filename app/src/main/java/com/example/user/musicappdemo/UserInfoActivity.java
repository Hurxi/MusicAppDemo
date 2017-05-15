package com.example.user.musicappdemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.musicappdemo.bean.User;
import com.example.user.musicappdemo.utils.MyDBHelper;

public class UserInfoActivity extends BaseActivity {

    EditText etNickName,etPwd,etPwd2,etSex,etPhone,etContent;
    Button btnUpdate;
    MyDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        etNickName=(EditText)findViewById(R.id.et_update_nick_name);
        etPwd=(EditText)findViewById(R.id.et_update_pwd);
        etPwd2=(EditText)findViewById(R.id.et_update_pwd2);
        etSex=(EditText)findViewById(R.id.et_update_sex);
        etPhone=(EditText)findViewById(R.id.et_update_phone);
        etContent=(EditText)findViewById(R.id.et_update_content);
        btnUpdate=(Button)findViewById(R.id.btn_update);
        dbHelper=new MyDBHelper(this,"MyUser.db",null,3);
         final Intent intent=getIntent();
//        String userName=intent.getStringExtra("userName1");
//        SQLiteDatabase db=dbHelper.getWritableDatabase();
////        Cursor cursor=db.query("User",new String[]{"nickName,sex,phone,content"},"userName=?",new String[]{userName},null,null,null);
//        Cursor cursor=db.rawQuery("select * from User where userName=?",new String[]{userName});
//        if(cursor.moveToFirst()){
//            do{
//                etNickName.setText(cursor.getString(cursor.getColumnIndex("nickName")));
//                etSex.setText(cursor.getString(cursor.getColumnIndex("sex")));
//                etPhone.setText(cursor.getString(cursor.getColumnIndex("phone")));
//                etContent.setText(cursor.getString(cursor.getColumnIndex("content")));
//
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
        initData(userName(intent));
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(cursor(userName(getIntent())).moveToFirst()){
//                    do{
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("userPwd", etPwd.getText().toString());
//                values.put("userPwd","111");
                values.put("sex",etSex.getText().toString());
                values.put("nickName",etNickName.getText().toString());
                values.put("phone",etPhone.getText().toString());
                values.put("content",etContent.getText().toString());

                db.update("User",values,"userName = ?",new String[]{userName(getIntent())});
                Toast.makeText(UserInfoActivity.this,"修改信息成功",Toast.LENGTH_SHORT).show();
                finish();

//                        if(etPwd.getText().toString()!=null) {
//                            values.put("userPwd", etPwd.getText().toString());
//                        }
//                        else{
//                            values.put("userPwd","");
//                        }
//                        if(etSex.getText().toString()!=null) {
//                            values.put("sex", etSex.getText().toString());
//                        }
//                        else{
//                            values.put("sex","");
//                        }
//                        if(etNickName.getText().toString()!=null) {
//                            values.put("nickName", etNickName.getText().toString());
//                        }
//                        else{
//                            values.put("nickName","");
//                        }
//
//                        if(etPhone.getText().toString()!=null) {
//                            values.put("phone", etPhone.getText().toString());
//                        }
//                        else{
//                            values.put("phone","");
//                        }
//                        if(etContent.getText().toString()!=null) {
//                            values.put("content", etContent.getText().toString());
//                        }
//                        else{
//                            values.put("content","");
//                        }
//                        values.put("sex",etSex.getText().toString());
//                        values.put("nickName",etNickName.getText().toString());
//                        values.put("phone",etPhone.getText().toString());
//                        values.put("content",etContent.getText().toString());
//                        db.update("User",values,"userName = ?",new String[]{userName(getIntent())});

//                    }while (cursor(userName(getIntent())).moveToNext());
                }

        });

    }
    private Cursor cursor(String userName){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from User where userName=?",new String[]{userName});
        return cursor;
    }
    private String userName(Intent intent){
        String userName=intent.getStringExtra("userName1");
        return  userName;
    }
    private void initData(String userName){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from User where userName=?",new String[]{userName});
        if(cursor.moveToFirst()){
            do{
                etNickName.setText(cursor.getString(cursor.getColumnIndex("nickName")));
                etSex.setText(cursor.getString(cursor.getColumnIndex("sex")));
                etPhone.setText(cursor.getString(cursor.getColumnIndex("phone")));
                etContent.setText(cursor.getString(cursor.getColumnIndex("content")));

            }while (cursor.moveToNext());
        }
        cursor.close();
    }
}
