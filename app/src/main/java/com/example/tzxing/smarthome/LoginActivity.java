package com.example.tzxing.smarthome;

import android.content.Context;
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


    private TextView txt_ip;
    private TextView txt_port;
    private Button btnConfirm;
    private Context mContext;
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        btnConfirm = findViewById(R.id.button_connect);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ipAddressStr = txt_ip.getText().toString();
                final String portNumberStr = txt_port.getText().toString();
                if (isCorrect(ipAddressStr).equals("isCorrect")) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ipAddress", ipAddressStr);
                    bundle.putString("portNumber", portNumberStr);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });

        txt_ip = findViewById(R.id.ipAddress);
        txt_ip.addTextChangedListener(this);//设置监听器
        txt_port = findViewById(R.id.port);
        txt_port.addTextChangedListener(this);//设置监听器

    }


    //判断IP地址是否合法
    public String isCorrect(String ipAddressStr) {
        if (ipAddressStr.equals("")) {
            Toasty.error(btnConfirm.getContext(), "IP地址不能为空", Toast.LENGTH_SHORT).show();
            txt_ip.setText("");
            return "";
        }
        int dotNum = 0;
        for (int j = 0; j < ipAddressStr.length(); j++) {
            if (ipAddressStr.charAt(j) == '.') dotNum++;
            else if (ipAddressStr.charAt(j) > '9' || ipAddressStr.charAt(j) < '0') {
                Toasty.error(btnConfirm.getContext(), "IP地址只能输入0-9和.", Toast.LENGTH_SHORT).show();
                txt_ip.setText("");
                return "";
            }
        }
        if (dotNum != 3) {
            Toasty.error(btnConfirm.getContext(), "IP地址需要4个值", Toast.LENGTH_SHORT).show();
            txt_ip.setText("");
            return "";
        }
        String perStr = "";
        for (int j = 0; j < ipAddressStr.length(); j++) {
            if (ipAddressStr.charAt(j) != '.') {
                perStr += ipAddressStr.charAt(j);
            } else {
                if (Integer.parseInt(perStr) > 255) {
                    Toasty.error(btnConfirm.getContext(), "IP地址的每个值必须小于255", Toast.LENGTH_SHORT).show();
                    txt_ip.setText("");
                    return "";
                }
                perStr = "";
            }
        }
        return "isCorrect";
    }


    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toasty.info(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
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
        if (txt_ip.getText().length() != 0 && txt_port.getText().length() != 0)
            btnConfirm.setEnabled(true);
        else
            btnConfirm.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
