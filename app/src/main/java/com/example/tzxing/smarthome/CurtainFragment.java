package com.example.tzxing.smarthome;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CurtainFragment extends Fragment
{
    private TextView textView_shock;
    private  TextView textView_state;
    private  TextView textView_light;
    private Context mContext;
    private DataProcess dataProcess;
    private mBroadcastReceiver mBroadcastReceiver = new mBroadcastReceiver() {
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

                    textView_light.setText(dataProcess.txt_cur_light);
                    textView_shock.setText(dataProcess.txt_cur_shock);
                    if (dataProcess.txt_cur_state == "0")
                        textView_state.setText("关");
                    else if(dataProcess.txt_cur_state == "1")
                        textView_state.setText("开");
                }
            });
        }
    };





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_curtain,container,false);
        textView_light = view.findViewById(R.id.cur_light_txt);
        textView_shock = view.findViewById(R.id.cur_shock_txt);
        textView_state = view.findViewById(R.id.cur_state_txt);
        mContext=this.getActivity();
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













}
