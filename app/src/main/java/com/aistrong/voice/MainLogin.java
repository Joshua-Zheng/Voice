package com.aistrong.voice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;

public class MainLogin extends Activity {

    static SharedPreferences sp;
    private EditText mUserName;
    private CheckBox mRemember;
    private ImageButton mLogin;
    private String userNameValue;
    static String sendUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        //Android 6.0 以上动态权限申请
        applyPermission(this, getPermission(this, new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        }));

        // 初始化用户ID、记住密码、登录按钮
        mUserName = findViewById(R.id.userID);
        mRemember = findViewById(R.id.rem_userID);
        mLogin = findViewById(R.id.loginBtn);

        //通过mUserName改变发送按钮的颜色
        mUserName.addTextChangedListener(textChange);

        sp = getSharedPreferences("userInfo", 0);
        String name = sp.getString("USER_NAME", "");


        boolean choseRemember = sp.getBoolean("autologin", false);
        //      Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mUserName.setText(name);
            mRemember.setChecked(true);
        }

        //自动登陆
        autoLogin();

        mLogin.setOnClickListener(v -> {
            userNameValue = mUserName.getText().toString();
            SharedPreferences.Editor editor =sp.edit();

            // TODO Auto-generated method stub
            if (TextUtils.isEmpty(userNameValue)) {
                Toast.makeText(MainLogin.this, R.string.login_false, Toast.LENGTH_SHORT).show();
            }else {
                Toast toast = Toast.makeText(MainLogin.this, R.string.login_success, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                //保存用户ID
                editor.putString("USER_NAME", userNameValue);

                //是否记住密码
                if(mRemember.isChecked()){
                    editor.putBoolean("autologin", true);
                }else{
                    editor.putBoolean("autologin", false);
                }
                editor.apply();
                sendUserID = sp.getString("USER_NAME", "");
                //跳转
                Intent intent = new Intent(MainLogin.this,MainSpeech.class);
                startActivity(intent);
                MainLogin.this.finish();
            }
        });

    }

    public void autoLogin(){
        if (!mUserName.getText().toString().equals("")){
            sendUserID = sp.getString("USER_NAME", "");
            Intent intent =new Intent(MainLogin.this,MainSpeech.class);
            startActivity(intent);
            MainLogin.this.finish();
        }
    }

    public void clearInfo(){
        sp.edit().clear().apply();
    }

    //实时监听mUserName输入情况，改变发送颜色
    TextWatcher textChange = new TextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            if(mUserName.length()>0){
                mLogin.setImageDrawable(getResources().getDrawable(R.drawable.login_ready));
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mLogin.setImageDrawable(getResources().getDrawable(R.drawable.login));
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }};

    /**
     * Android 6.0 以上需要动态权限
     */
    public static void applyPermission(Activity activity, String[] permissions) {
        if (permissions != null) {
            ActivityCompat.requestPermissions(activity, permissions, 123);
        }
    }

    public static String[] getPermission(Activity activity, String[] permissions) {
        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, perm)) {
                toApplyList.add(perm);
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            return toApplyList.toArray(tmpList);
        }
        return null;
    }
    // 以上是Android 6.0+ 动态授权申请
}

