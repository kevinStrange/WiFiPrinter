package com.example.huanghongfa.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huanghongfa.myapplication.dialog.ConnectWiFiPrinterDialog;
import com.example.huanghongfa.myapplication.interfaces.IConnectWiFiCallBackListener;
import com.example.huanghongfa.myapplication.manager.ConnectWiFiPrinterManager;
import com.example.huanghongfa.myapplication.utils.PrinterUtils;

import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends BaseActivity implements IConnectWiFiCallBackListener {

    private ConnectWiFiPrinterDialog dialog;

    private ConnectWiFiPrinterManager manager;
    private PrinterUtils printerUtils = new PrinterUtils();
    private TextView show_connect_ip_text, show_connect_port_text;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        show_connect_ip_text = (TextView) findViewById(R.id.show_connect_ip_text);
        show_connect_port_text = (TextView) findViewById(R.id.show_connect_port_text);
        dialog = new ConnectWiFiPrinterDialog(this);
        manager = ConnectWiFiPrinterManager.getInstance(this);
        manager.setCallBackListener(this);
        printerUtils.setContext(this);
    }

    @Override
    public void callBackListener(String ip, int port, Socket socket, OutputStream outputStream) {
        if (outputStream != null) {
            handler.sendEmptyMessage(0);
            show_connect_ip_text.setText(ip);
            show_connect_port_text.setText(String.valueOf(port));
            printerUtils.setIsconnected(true);
            printerUtils.setOutputStream(outputStream);
        } else {
            printerUtils.setIsconnected(false);
            handler.sendEmptyMessage(1);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_connect_printer:
                dialog.show();
                break;
            case R.id.start_print:
                printerUtils.printText("测试打印。。。\n");
                break;
        }
    }

    /**
     * 内部静态handler
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(mContext, "连接成功", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(mContext, "连接失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
