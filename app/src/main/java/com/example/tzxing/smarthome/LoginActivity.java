package com.example.tzxing.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements TextWatcher {


    private TextView mIPTextView;
    private TextView mPortTextView;
    private Button mConfirmButton;
    private long mFirstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mConfirmButton = findViewById(R.id.button_connect);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ipAddressStr = mIPTextView.getText().toString();
                final String portNumberStr = mPortTextView.getText().toString();
                if (isCorrect(ipAddressStr)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ipAddress", ipAddressStr);
                    bundle.putString("portNumber", portNumberStr);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mIPTextView = findViewById(R.id.ipAddress);
        mIPTextView.addTextChangedListener(this);//设置监听器
        mPortTextView = findViewById(R.id.port);
        mPortTextView.addTextChangedListener(this);//设置监听器

    }


    //判断IP地址是否合法
    public boolean isCorrect(String ipAddressStr) {
        if (ipAddressStr.equals("")) {
            Toasty.error(mConfirmButton.getContext(), "IP地址不能为空", Toast.LENGTH_SHORT).show();
            mIPTextView.setText("");
            return false;
        }
        int dotNum = 0;
        for (int j = 0; j < ipAddressStr.length(); j++) {
            if (ipAddressStr.charAt(j) == '.') dotNum++;
            else if (ipAddressStr.charAt(j) > '9' || ipAddressStr.charAt(j) < '0') {
                Toasty.error(mConfirmButton.getContext(), "IP地址只能输入0-9和.", Toast.LENGTH_SHORT).show();
                mIPTextView.setText("");
                return false;
            }
        }
        if (dotNum != 3) {
            Toasty.error(mConfirmButton.getContext(), "IP地址需要4个值", Toast.LENGTH_SHORT).show();
            mIPTextView.setText("");
            return false;
        }
        String perStr = "";
        for (int j = 0; j < ipAddressStr.length(); j++) {
            if (ipAddressStr.charAt(j) != '.') {
                perStr += ipAddressStr.charAt(j);
            } else {
                if (Integer.parseInt(perStr) > 255) {
                    Toasty.error(mConfirmButton.getContext(), "IP地址的每个值必须小于255", Toast.LENGTH_SHORT).show();
                    mIPTextView.setText("");
                    return false;
                }
                perStr = "";
            }
        }
        return true;
    }


    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - mFirstTime > 2000) {
            Toasty.info(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mFirstTime = secondTime;
        } else {
            System.exit(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    //当文本框不为空时，按钮才可以点击
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mIPTextView.getText().length() != 0 && mPortTextView.getText().length() != 0)
            mConfirmButton.setEnabled(true);
        else
            mConfirmButton.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
