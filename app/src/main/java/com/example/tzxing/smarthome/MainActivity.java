package com.example.tzxing.smarthome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private LivingRoomFragment livingRoomFragment=new LivingRoomFragment();
    private BedroomFragment bedroomFragment=new BedroomFragment();
    private KitchenFragment kitchenFragment=new KitchenFragment();
    private  CurtainFragment curtainFragment=new CurtainFragment();
    private Socket socket;
    private String ipAddress,portNumber;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    protected Handler handler;
    private Context mContext;
    private Bundle sendBundle;
    private long firstTime = 0;



    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mContext = this;
        handler = new Handler();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ipAddress = bundle.getString("ipAddress");
        portNumber=bundle.getString("portNumber");



        ExecutorService mThreadPool = Executors.newFixedThreadPool(10);
        mThreadPool.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // 创建Socket对象 & 指定服务端的IP 及 端口号
                    try
                    {
                        socket = new Socket(ipAddress, Integer.valueOf(portNumber));
                    } catch (ConnectException e)
                    {
                        System.out.println("not connect");
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    outputStream = socket.getOutputStream();
                    // 判断客户端和服务器是否连接成功
                    System.out.println(socket.isConnected());
                    if (socket != null && socket.isConnected())
                        System.out.println("connected");
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toasty.success(mContext, "连接成功", Toast.LENGTH_SHORT).show();

                        }
                    });
                    while (socket.isConnected())
                    {
                        char[] buffer = new char[32];
                        int length;
                        length = bufferedReader.read(buffer);
                        char[] msgReceiveArray = new char[length];
                        for (int i = 0; i < length; i++)
                            msgReceiveArray[i] = buffer[i];
                        sendBundle = new Bundle();
                        sendBundle.putString("receiveMsg", new String(msgReceiveArray));
                        Intent mIntent = new Intent();
                        mIntent.putExtras(sendBundle);
                        mIntent.setAction("updateUI");
                        sendBroadcast(mIntent);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });



    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000)
            {
                Toasty.info(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
            } else
            {
                System.exit(0);
            }
        }




    }



    public boolean onNavigationItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        if (id == R.id.nav_living_room)
        {
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main,livingRoomFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("客厅终端");
        } else if (id == R.id.nav_bedroom)
        {
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main,bedroomFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("卧室终端");
        } else if (id == R.id.nav_kitchen)
        {
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main,kitchenFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("厨房终端");
        } else if (id == R.id.nav_curtain)
        {
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main,curtainFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("智能窗帘");
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
