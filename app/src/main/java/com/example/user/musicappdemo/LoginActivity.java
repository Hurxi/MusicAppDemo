package com.example.user.musicappdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.musicappdemo.utils.MyDBHelper;

public class LoginActivity extends BaseActivity {
    TextView tvRegeist;
    EditText etUserName,etUserPwd;
    Button btnLogin;
    MyDBHelper dbHelper;
    CheckBox cbRemember;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        tvRegeist=(TextView)findViewById(R.id.tv_new_user);
        etUserName=(EditText)findViewById(R.id.et_user_name);
        etUserPwd=(EditText)findViewById(R.id.et_user_pwd);
        btnLogin=(Button)findViewById(R.id.btn_login);
        cbRemember=(CheckBox)findViewById(R.id.cb_remember);
        boolean isRemenber=pref.getBoolean("remember_password",false);
        if(isRemenber){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            etUserName.setText(account);
            etUserPwd.setText(password);
            cbRemember.setChecked(true);
        }
        dbHelper=new MyDBHelper(this,"MyUser.db",null,3);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=etUserName.getText().toString();
                String userPwd=etUserPwd.getText().toString();
                //根据用户名去数据库中取密码然后比对密码
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                Cursor cursor=db.query("User",null,
                        "userName=?",new String[]{userName},null,null,null);
                if(cursor.moveToFirst()){
                    String pwdTemp=cursor.getString(2);
                    String nickname=cursor.getString(4);
                    String content=cursor.getString(6);

                    if(pwdTemp.equals(userPwd)){
                        editor=pref.edit();
                        if(cbRemember.isChecked()){
                            editor.putBoolean("remember_password",true);
                            editor.putString("account",userName);
                            editor.putString("password",userPwd);
                        }
                        else {
                            editor.clear();
                        }
                        editor.apply();
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("userName",userName);
                        intent.putExtra("userPwd",userPwd);
                        intent.putExtra("nickname",nickname);
                        intent.putExtra("content",content);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"密码或账号错误",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,"不存在该用户",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvRegeist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册的Activity
                Intent intent=new Intent(LoginActivity.this,RegeistActivity.class);

                startActivity(intent);
            }
        });
    }
}
