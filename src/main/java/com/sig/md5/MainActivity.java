package com.sig.md5;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {
    private EditText mUsername = null;
    private EditText mPassword = null;
    private Button mSave = null;
    private Button mLogin = null;
    private SharedPreferences mPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        save();
        login();
    }

    private void init() {
        mUsername = (EditText) this.findViewById(R.id.et_username);
        mPassword = (EditText) this.findViewById(R.id.et_password);
        mSave = (Button) this.findViewById(R.id.btn_save);
        mLogin = (Button) this.findViewById(R.id.btn_login);
        mPrefs = this.getSharedPreferences("md5demo", 0x0000);
    }

    private void save() {
        mSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "用户名不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isNameSaved = mPrefs.edit().putString("username", username).commit();
                boolean isPwdSaved = mPrefs.edit().putString("password", md5(password)).commit();
                if (isNameSaved && isPwdSaved)
                    Toast.makeText(getApplicationContext(), "保存成功!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "保存失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        final String namePrefs = mPrefs.getString("username", null);
        final String pwdPrefs = mPrefs.getString("password", null);

        if (!TextUtils.isEmpty(namePrefs))
            mUsername.setText(namePrefs);

        mLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "用户名不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.equals(namePrefs) && md5(password).equals(pwdPrefs))
                    Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "登录失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String md5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            char[] charArr = str.toCharArray();
            byte[] byteArr = new byte[charArr.length];
            for (int i = 0; i < charArr.length; i++) {
                byteArr[i] = (byte) charArr[i];
            }
            byte[] md5ByteArr = md5.digest(byteArr);
            StringBuilder hexVal = new StringBuilder();
            for (byte aMd5ByteArr : md5ByteArr) {
                int val = ((int) aMd5ByteArr) & 0xff;
                if (val < 16)
                    hexVal.append("0");
                hexVal.append(Integer.toHexString(val));
            }
            return hexVal.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

