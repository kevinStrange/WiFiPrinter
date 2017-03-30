package com.example.huanghongfa.myapplication.threadtask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.huanghongfa.myapplication.interfaces.IConnectWiFiCallBackListener;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by huanghongfa on 2017/3/29.
 */

public class ConnectPrinterTask extends AsyncTask<String, Void, Void> {


    private ConnectPrinterHandler handler;
    private Socket socket = null;


    private IConnectWiFiCallBackListener callBackListener;


    public ConnectPrinterTask( IConnectWiFiCallBackListener callBackListener1) {
        this.callBackListener = callBackListener1;
        handler = new ConnectPrinterHandler(this.callBackListener);
    }


    @Override
    protected Void doInBackground(String... params) {
        try {
            if (params.length == 0) {
                handler.sendEmptyMessage(1);//连接失败
                return null;
            }
            if (params[0] != null && params[1] != null) {
                Log.d("MainActivity", "正在开始连接====" + Integer.parseInt(params[1]));
                socket = new Socket(params[0], Integer.parseInt(params[1]));
                if (socket.isConnected()) {
                    Log.d("MainActivity", "连接成功");
                    handler.setConnectData(params[0], Integer.parseInt(params[1]), socket);
                    handler.sendEmptyMessage(0);
                } else {
                    Log.d("MainActivity", "连接失败");
                    handler.sendEmptyMessage(1);
                }
            }
        } catch (Exception e) {
            Log.d("MainActivity", "正在开始连接===Exception=" + e);
            handler.sendEmptyMessage(1);

        }
        return null;
    }


    /**
     * 内部静态handler
     */
    private static class ConnectPrinterHandler extends Handler {

        IConnectWiFiCallBackListener callBackListener;
        private String mIp;
        private int mPort;
        private Socket mSocket;

        public void setConnectData(String ip, int port, Socket socket) {
            mIp = ip;
            mPort = port;
            mSocket = socket;
        }

        public ConnectPrinterHandler(IConnectWiFiCallBackListener callBackListener1) {
            callBackListener = callBackListener1;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        this.callBackListener.callBackListener(mIp, mPort, mSocket, mSocket.getOutputStream());
                    } catch (Exception e) {
                        callBackListener.callBackListener("", 0, null, null);
                    }
                    break;
                case 1:
                    callBackListener.callBackListener("", 0, null, null);
                    break;
            }
        }
    }


    /**
     * 连接WiFi打印机
     *
     * @param ip
     * @param port
     */
    public void connectWiFiPrinter(final String ip, final int port) {
        Log.d("MainActivity", "正在开始连接");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "正在开始连接");
                try {
                    Looper.prepare();
                    socket = new Socket(ip, port);
                    Log.d("MainActivity", "正在开始连接====");
                    //当前连接成功，并且是处于活跃状态
                    if (socket.isConnected() && socket.getKeepAlive()) {
                        handler.sendEmptyMessage(0);//连接成功
                        return;
                    }
                } catch (Exception e) {
                    Log.d("MainActivity", "connectWiFiPrinter Exception " + e);
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException e1) {
                        Log.d("MainActivity", "close IOException e1 " + e1);
                    }
                    handler.sendEmptyMessage(1);//连接失败
                    return;
                }
                Log.d("MainActivity", "正在开始连接====失败");
                handler.sendEmptyMessage(1);//连接失败
            }
        });
    }
}
