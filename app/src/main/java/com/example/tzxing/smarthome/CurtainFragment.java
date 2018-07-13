package com.example.tzxing.smarthome;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CurtainFragment extends Fragment implements View.OnClickListener {
    private TextView textView_shock;
    private TextView textView_state;
    private TextView textView_light;
    private Context mContext;
    private DataProcess dataProcess;
    private Button auto_open;
    private Button auto_close;
    private Button manual;
    private Button openCurtain;
    private Button closeCurtain;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString("receiveMsg");
            System.out.println(msg);
            dataProcess = new DataProcess(msg);
            System.out.println(dataProcess.toString());
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (dataProcess.txt_cur_light != null)
                        textView_light.setText(dataProcess.txt_cur_light);
                    if (dataProcess.txt_cur_shock != null)
                        textView_shock.setText(dataProcess.txt_cur_shock);
                    try {
                        if (dataProcess.txt_cur_state.equals("0"))
                            textView_state.setText("关");
                        else if (dataProcess.txt_cur_state.equals("1"))
                            textView_state.setText("开");
                    } catch (Exception e) {

                    }
                    if (dataProcess.cur_alert != null && dataProcess.cur_alert.equals("1")) {
                        MediaPlayer mediaPlayer;
                        mediaPlayer = MediaPlayer.create(mContext, R.raw.enen);
                        mediaPlayer.start();
                    }
                }
            });
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curtain, container, false);
        textView_light = view.findViewById(R.id.cur_light_txt);
        textView_shock = view.findViewById(R.id.cur_shock_txt);
        textView_state = view.findViewById(R.id.cur_state_txt);
        auto_open = view.findViewById(R.id.auto_open);
        auto_open.setOnClickListener(this);
        auto_close = view.findViewById(R.id.auto_close);
        auto_close.setOnClickListener(this);
        manual = view.findViewById(R.id.manual);
        manual.setOnClickListener(this);
        openCurtain = view.findViewById(R.id.openCurtain);
        openCurtain.setOnClickListener(this);
        closeCurtain = view.findViewById(R.id.closeCurtain);
        closeCurtain.setOnClickListener(this);
        mContext = this.getActivity();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //实例化BroadcastReceiver子类 &  IntentFilter
        IntentFilter intentFilter = new IntentFilter();

        //设置接收广播的类型
        intentFilter.addAction("updateUI");

        //调用Context的registerReceiver（）方法进行动态注册
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);

    }

    @Override
    public void onPause() {
        super.onPause();
        //销毁在onResume()方法中的广播
        mContext.unregisterReceiver(mBroadcastReceiver);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auto_open: {
                try {
                    //向Activity发送请求
                    Intent mIntent = new Intent();
                    mIntent.putExtra("send", ":Cm0;\r\n");
                    mIntent.setAction("send");
                    mContext.sendBroadcast(mIntent);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.auto_close: {
                try {
                    //向Activity发送请求
                    Intent mIntent = new Intent();
                    mIntent.putExtra("send", ":Cm1;\r\n");
                    mIntent.setAction("send");
                    mContext.sendBroadcast(mIntent);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.manual: {
                try {
                    //向Activity发送请求
                    Intent mIntent = new Intent();
                    mIntent.putExtra("send", ":Cm2;\r\n");
                    mIntent.setAction("send");
                    mContext.sendBroadcast(mIntent);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.openCurtain: {
                try {
                    //向Activity发送请求
                    Intent mIntent = new Intent();
                    mIntent.putExtra("send", ":Cm3;\r\n");
                    mIntent.setAction("send");
                    mContext.sendBroadcast(mIntent);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.closeCurtain: {
                try {
                    //向Activity发送请求
                    Intent mIntent = new Intent();
                    mIntent.putExtra("send", ":Cm4;\r\n");
                    mIntent.setAction("send");
                    mContext.sendBroadcast(mIntent);
                } catch (Exception e) {

                }
                break;
            }
        }


    }
}
