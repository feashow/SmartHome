package com.example.tzxing.smarthome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Handler handler;
    private Toolbar mToolbar;
    private LivingRoomFragment mLivingRoomFragment = new LivingRoomFragment();
    private BedroomFragment mBedroomFragment = new BedroomFragment();
    private KitchenFragment mKitchenFragment = new KitchenFragment();
    private CurtainFragment mCurtainFragment = new CurtainFragment();
    private BalconyFragment mBalconyFragment = new BalconyFragment();
    private Socket mSocket;
    private String mIpAddress, mPortNumber;
    private BufferedReader mBufferedReader;
    private OutputStream mOutputStream;
    private Bundle mSendBundle;
    private long mFirstTime = 0;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()    //匿名内部类
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            final byte[] msg = intent.getExtras().getString("send").getBytes(); //获取fragment中需要发送的内容
            Thread thread = new Thread(new Runnable()   //耗时操作放在子线程中进行
            {
                @Override
                public void run() {
                    try {
                        mOutputStream.write(msg);
                        mOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 设置布局
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);   //给DrawerLayout设置开关的监听
        toggle.syncState(); //实现toolbar和Drawer的联动

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        handler = new Handler();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mIpAddress = bundle.getString("ipAddress");
        mPortNumber = bundle.getString("portNumber");

        ExecutorService mThreadPool = Executors.newFixedThreadPool(10);
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建Socket对象 & 指定服务端的IP 及 端口号
                    try {
                        mSocket = new Socket(mIpAddress, Integer.valueOf(mPortNumber));
                    } catch (ConnectException e) {
                    }
                    mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    mOutputStream = mSocket.getOutputStream();
                    // 判断客户端和服务器是否连接成功
                    if (mSocket != null && mSocket.isConnected())
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //运行在主线程
                                Toasty.success(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();

                            }
                        });
                    while (mSocket.isConnected()) {
                        char[] buffer = new char[86];
                        mBufferedReader.read(buffer);
                        mSendBundle = new Bundle();
                        mSendBundle.putString("receiveMsg", new String(buffer));
                        Intent mIntent = new Intent();
                        mIntent.putExtras(mSendBundle);
                        mIntent.setAction("updateUI");
                        sendBroadcast(mIntent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //实例化BroadcastReceiver子类 & IntentFilter
        IntentFilter intentFilter = new IntentFilter();

        //设置接收广播的类型
        intentFilter.addAction("send");

        //调用Context的registerReceiver（）方法进行动态注册
        registerReceiver(mBroadcastReceiver, intentFilter);

        init();
    }



    //初始化
    private void init() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main, mLivingRoomFragment);
        fragmentTransaction.add(R.id.main, mBedroomFragment);
        fragmentTransaction.add(R.id.main, mKitchenFragment);
        fragmentTransaction.add(R.id.main, mCurtainFragment);
        fragmentTransaction.add(R.id.main, mBalconyFragment);
        hideFragment(fragmentTransaction);
        fragmentTransaction.commit();
    }


    @Override
    //双击返回键退出
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - mFirstTime > 2000) {
                Toasty.info(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mFirstTime = secondTime;
            } else {
                System.exit(0);
            }
        }


    }

    //隐藏所有fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (mLivingRoomFragment != null) {
            transaction.hide(mLivingRoomFragment);
        }
        if (mBedroomFragment != null) {
            transaction.hide(mBedroomFragment);
        }
        if (mKitchenFragment != null) {
            transaction.hide(mKitchenFragment);
        }
        if (mCurtainFragment != null) {
            transaction.hide(mCurtainFragment);
        }
        if (mBalconyFragment != null) {
            transaction.hide(mBalconyFragment);
        }
    }


    //切换fragment
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_living_room) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(mLivingRoomFragment);
            fragmentTransaction.commit();
            mToolbar.setTitle("客厅终端");
        } else if (id == R.id.nav_bedroom) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(mBedroomFragment);
            fragmentTransaction.commit();
            mToolbar.setTitle("卧室终端");
        } else if (id == R.id.nav_kitchen) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(mKitchenFragment);
            fragmentTransaction.commit();
            mToolbar.setTitle("厨房终端");
        } else if (id == R.id.nav_curtain) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(mCurtainFragment);
            fragmentTransaction.commit();
            mToolbar.setTitle("智能窗帘");
        } else if (id == R.id.nav_balcony) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(mBalconyFragment);
            fragmentTransaction.commit();
            mToolbar.setTitle("阳台终端");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
