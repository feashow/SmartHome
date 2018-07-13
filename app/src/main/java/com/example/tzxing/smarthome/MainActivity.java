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
    protected Handler handler;
    Toolbar toolbar;
    private LivingRoomFragment livingRoomFragment = new LivingRoomFragment();
    private BedroomFragment bedroomFragment = new BedroomFragment();
    private KitchenFragment kitchenFragment = new KitchenFragment();
    private CurtainFragment curtainFragment = new CurtainFragment();
    private BalconyFragment balconyFragment = new BalconyFragment();
    private Socket socket;
    private String ipAddress, portNumber;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private Context mContext;
    private Bundle sendBundle;
    private long firstTime = 0;
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
                        outputStream.write(msg);
                        outputStream.flush();
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);   //给DrawerLayout设置开关的监听
        toggle.syncState(); //实现toolbar和Drawer的联动

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mContext = this;
        handler = new Handler();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ipAddress = bundle.getString("ipAddress");
        portNumber = bundle.getString("portNumber");


        ExecutorService mThreadPool = Executors.newFixedThreadPool(10);
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建Socket对象 & 指定服务端的IP 及 端口号
                    try {
                        socket = new Socket(ipAddress, Integer.valueOf(portNumber));
                    } catch (ConnectException e) {
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    outputStream = socket.getOutputStream();
                    // 判断客户端和服务器是否连接成功
                    if (socket != null && socket.isConnected())
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.success(mContext, "连接成功", Toast.LENGTH_SHORT).show();

                            }
                        });
                    while (socket.isConnected()) {
                        char[] buffer = new char[86];
                        bufferedReader.read(buffer);
                        sendBundle = new Bundle();
                        sendBundle.putString("receiveMsg", new String(buffer));
                        Intent mIntent = new Intent();
                        mIntent.putExtras(sendBundle);
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
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);

        init();
    }


    //初始化
    private void init() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main, livingRoomFragment);
        fragmentTransaction.add(R.id.main, bedroomFragment);
        fragmentTransaction.add(R.id.main, kitchenFragment);
        fragmentTransaction.add(R.id.main, curtainFragment);
        fragmentTransaction.add(R.id.main, balconyFragment);
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
            if (secondTime - firstTime > 2000) {
                Toasty.info(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
            } else {
                System.exit(0);
            }
        }


    }

    //隐藏所有fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (livingRoomFragment != null) {
            transaction.hide(livingRoomFragment);
        }
        if (bedroomFragment != null) {
            transaction.hide(bedroomFragment);
        }
        if (kitchenFragment != null) {
            transaction.hide(kitchenFragment);
        }
        if (curtainFragment != null) {
            transaction.hide(curtainFragment);
        }
        if (balconyFragment != null) {
            transaction.hide(balconyFragment);
        }
    }


    //切换fragment
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_living_room) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(livingRoomFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("客厅终端");
        } else if (id == R.id.nav_bedroom) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(bedroomFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("卧室终端");
        } else if (id == R.id.nav_kitchen) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(kitchenFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("厨房终端");
        } else if (id == R.id.nav_curtain) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(curtainFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("智能窗帘");
        } else if (id == R.id.nav_balcony) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragment(fragmentTransaction);
            fragmentTransaction.show(balconyFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("阳台终端");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mBroadcastReceiver);
    }
}
