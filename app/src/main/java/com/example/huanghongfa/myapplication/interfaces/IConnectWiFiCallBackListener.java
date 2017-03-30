package com.example.huanghongfa.myapplication.interfaces;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by huanghongfa on 2017/3/21.
 * <p>
 * 连接WiFi打印机接口回调
 */

public interface IConnectWiFiCallBackListener {

    void callBackListener(String ip,int port,Socket socket, OutputStream outputStream);
}
