package com.example.huanghongfa.myapplication.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.huanghongfa.myapplication.interfaces.IConnectWiFiCallBackListener;
import com.example.huanghongfa.myapplication.threadtask.ConnectPrinterTask;

import java.net.Socket;

/**
 * Created by huanghongfa on 2017/3/21.
 */

public class ConnectWiFiPrinterManager {

    private static ConnectWiFiPrinterManager instance;

    private static final Object lock = new Object();

    private IConnectWiFiCallBackListener mListener;

    private static Context mContext;


    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Socket socket = null;


    public static ConnectWiFiPrinterManager getInstance(Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ConnectWiFiPrinterManager();
                }
            }
        }
        mContext = context;
        return instance;
    }

    public void setCallBackListener(IConnectWiFiCallBackListener listener) {
        this.mListener = listener;
    }

    /**
     * 连接WiFi打印机
     *
     * @param ip
     * @param port
     */
    public void connectWiFiPrinter(final String ip, final int port) {
        ConnectPrinterTask task = new ConnectPrinterTask(this.mListener);
        task.execute(ip, String.valueOf(port));
    }


}
