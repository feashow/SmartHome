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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class KitchenFragment extends Fragment {

    private TextView textView_temperature;
    private TextView textView_smoke;
    private Context mContext;
    private DataProcess dataProcess;
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
                    if (dataProcess.txt_kit_temperature != null)
                        textView_temperature.setText(dataProcess.txt_kit_temperature + "℃");
                    if (dataProcess.txt_kit_smoke != null)
                        textView_smoke.setText(dataProcess.txt_kit_smoke);
                    if (dataProcess.kit_alert != null && dataProcess.kit_alert.equals("1")) {
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
        View view = inflater.inflate(R.layout.fragment_kitchen, container, false);
        textView_temperature = view.findViewById(R.id.kitc_temp_txt);
        textView_smoke = view.findViewById(R.id.kitc_smoke_txt);
        mContext = this.getActivity();

        return view;
    }


    public void onResume() {
        super.onResume();
        //实例化BroadcastReceiver子类 & IntentFilter
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
