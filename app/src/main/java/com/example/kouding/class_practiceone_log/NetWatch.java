package com.example.kouding.class_practiceone_log;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by kouding on 2016/11/7.
 * 用于网络监控
 */

public class NetWatch extends Service
{
    // Class that answers queries about the state of network connectivity.
    // 系统网络连接相关的操作管理类.

    private ConnectivityManager connectivityManager;
    // Describes the status of a network interface.
    // 网络状态信息的实例
    private NetworkInfo info;
    NotificationManager mNotificationManager;
    /**
     * 当前处于的网络
     * 0 ：null
     * 1 ：2G/3G
     * 2 ：wifi
     */
    public static int networkStatus;

    public static final String NETWORKSTATE = "com.text.android.network.state"; // An action name

    /**
     * 广播实例
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // The action of this intent or null if none is specified.
            // action是行动的意思，也许是我水平问题无法理解为什么叫行动，我一直理解为标识（现在理解为意图）
            String action = intent.getAction(); //当前接受到的广播的标识(行动/意图)



         //   mNotificationManager.notify(0, mBuilder.build());
            // 当当前接受到的广播的标识(意图)为网络状态的标识时做相应判断
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                // 获取网络连接管理器
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                // 获取当前网络状态信息
                info = connectivityManager.getActiveNetworkInfo();
                String notify;
                if (info != null && info.isAvailable()) {

                    //当NetworkInfo不为空且是可用的情况下，获取当前网络的Type状态
                    //根据NetworkInfo.getTypeName()判断当前网络
                    String name = info.getTypeName();

                    //更改NetworkStateService的静态变量，之后只要在Activity中进行判断就好了
                    if (name.equals("WIFI")) {
                        networkStatus = 2;
                        notify="WIFI连接";
                    } else {
                        networkStatus = 1;
                        notify="无线连接";
                    }

                } else {

                    // NetworkInfo为空或者是不可用的情况下
                    networkStatus = 0;
                    notify="无网络";
                    //Toast.makeText(context, "没有可用网络!\n请连接网络后刷新本界面", Toast.LENGTH_SHORT).show();
                }


                    Intent it = new Intent();
                    it.putExtra("networkStatus", networkStatus);
                    it.setAction(NETWORKSTATE);
                    sendBroadcast(it); //发送无网络广播给注册了当前服务广播的Activity
                    setNotification(context,notify);
                    /**
                     * 这里推荐使用本地广播的方式发送:
                     * LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                     */

            }
        }
    };
            void setNotification(Context context,String notify)
            {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        context).setSmallIcon(R.drawable.logo)
                        .setContentTitle("MyApp");
                mBuilder.setTicker("当前网络连接状态:"+notify);//第一次提示消息的时候显示在通知栏上
                mBuilder.setNumber(12);
                mBuilder.setAutoCancel(true);//自己维护通知的消失

                //构建一个Intent
                Intent resultIntent = new Intent();
                //封装一个Intent
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        context, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                // 设置通知主题的意图
                mBuilder.setContentText(notify);
                mNotificationManager.notify(0, mBuilder.build());

            }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //注册网络状态的广播，绑定到mReceiver
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);



        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销接收
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取网络连接管理器
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // 获取当前网络状态信息
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }
}
