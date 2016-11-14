package com.example.kouding.class_practiceone_log;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.text.Editable;
import android.text.TextWatcher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;
import static com.example.kouding.class_practiceone_log.R.layout.activity_main;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private ArrayList<User> mUsers; // 用户列表
    private Button mLoginButtonOK; // 确认登录按钮
    private Button mSignUpButtonOK;//确认注册按钮
    private DropEditText mIdEditText; // 登录ID编辑框
    private EditText mPwdEditText; // 登录密码编辑框
    private String mIdString;
    private String mPwdString;
    ArrayAdapter<String> mAdapter;//listView的监听器

    // private  DropEditText mDropEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        mUsers = new ArrayList<User>();

        SetTabs();
        initView();
        setListener();
        /* 获取已经保存好的用户密码 */
        mUsers = Utils.getUserList(MainActivity.this);
        if (mUsers!=null&&mUsers.size() > 0) {
            initDropEditText();
            ;
        }

    }

    private void initDropEditText() {
        //mIdEditText.setText(mUsers.get(0).getName(), TextView.BufferType.EDITABLE);
        //mPwdEditText.setText(mUsers.get(0).getPwd());
        mIdEditText = (DropEditText) findViewById(R.id.logInput_user);
        ArrayList<String> names = new ArrayList<String>();
        for (User user : mUsers) {
            names.add(user.getName());
        }
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        mIdEditText.setAdapter(mAdapter);
    }

    private void initView() {
        mIdEditText = (DropEditText) findViewById(R.id.logInput_user);
        mPwdEditText = (EditText) findViewById(R.id.logInput_passw);
        // mMoreUser = (ImageView) findViewById(R.id.login_more_user);
        mLoginButtonOK = (Button) findViewById(R.id.logIn_btn_ok);
        mSignUpButtonOK = (Button) findViewById(R.id.signUp_btn_ok);
        //                                                         initLoginingDlg();
    }

    User findNameInUserList(String name) {
        if (mUsers!=null&&mUsers.size() > 0) {
            for (User user : mUsers) {
                String theName = user.getName();
                if (name.equals(theName))
                    return user;
            }
        }
        return null;
    }

    private void setListener() {
        mIdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mIdString = s.toString();
                User theUser = findNameInUserList(mIdString);
                if (theUser != null) {
                    mPwdEditText.setText(theUser.getPwd());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mPwdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPwdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mLoginButtonOK.setOnClickListener(this);
        mSignUpButtonOK.setOnClickListener(this);

    }

    // Implement the OnClickListener callback
    public void onClick(View v) {
        // do something when the button is clicked
        switch (v.getId()) {
            case R.id.logIn_btn_ok:
                // 启动登录
                Log.i("main确认登陆", mIdString + "  " + mPwdString);
                if (mIdString == null || mIdString.equals("")) { // 账号为空时
                    Toast.makeText(MainActivity.this, "请输入账号", Toast.LENGTH_SHORT)
                            .show();
                } else if (mPwdString == null || mPwdString.equals("")) {// 密码为空时
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT)
                            .show();
                } else {// 账号和密码都不为空时
                    boolean mIsSave = true;
                    try {
                        Log.i("main", "保存用户列表");
                        for (User user : mUsers) { // 判断本地文档是否有此ID用户
                            if (user.getName().equals(mIdString)) {
                                mIsSave = false;
                                break;
                            }
                        }
                        if (mIsSave) { // 将新用户加入users
                            User user = new User();
                            user.setName(mIdString);
                            user.setPwd(mPwdString);
                            mUsers.add(user);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //closeLoginingDlg();// 关闭对话框
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

                    try {
                        Utils.saveUserList(MainActivity.this, mUsers);
                        initDropEditText();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ChangeToMenu();
                    // finish();
                }
                break;
            case R.id.signUp_btn_ok:
                // 启动注册
                // Toast.makeText(this, "正在注册", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //跳转到主菜单
    void ChangeToMenu()
    {
        Intent intent=new Intent();
        //确定目标组件
        intent.setClass(MainActivity.this,MainMenu.class);
        startActivity(intent);
    }
void SetTabs()
{
    TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
    tabHost.setup();
    //创建第一个tab页对象，TabSpec代表一个选项卡页面，要设置标题和内容，内容是布局文件中FrameLayout中
    TabSpec login = tabHost.newTabSpec("login");
    login.setIndicator("登陆");//设置标题
    login.setContent(R.id.logIn);//设置内容
    //添加tab页
    tabHost.addTab(login);
    //创建第2个tab页对象，TabSpec代表一个选项卡页面，要设置标题和内容，内容是布局文件中FrameLayout中
    TabSpec signup = tabHost.newTabSpec("signup");
    signup.setIndicator("注册");//设置标题
    signup.setContent(R.id.signUp);//设置内容
    //添加tab页
    tabHost.addTab(signup);
    tabHost.setCurrentTab(0);
}

}
