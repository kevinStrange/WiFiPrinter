package com.example.huanghongfa.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huanghongfa.myapplication.R;
import com.example.huanghongfa.myapplication.manager.ConnectWiFiPrinterManager;

/**
 * Created by huanghongfa on 2017/3/21.
 */

public class ConnectWiFiPrinterDialog extends Dialog {

    private TextView ip_text, port_text;
    private TextView connect_cancel_text, connect_determine_text;
    private Context mContext;

    public ConnectWiFiPrinterDialog(Context context) {
        super(context);
        mContext = context;
        initDialog(context);
    }

    public ConnectWiFiPrinterDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initDialog(context);
    }


    private void initDialog(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_connect_printer, null);
        addContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        setContentView(layout);
        ip_text = (TextView) findViewById(R.id.connect_ip_text);
        port_text = (TextView) findViewById(R.id.connect_port_text);
        connect_cancel_text = (TextView) findViewById(R.id.connect_cancel_text);
        connect_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        connect_determine_text = (TextView) findViewById(R.id.connect_determine_text);
        connect_determine_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(getTextByConnectIp())) {
                    Toast.makeText(mContext, "请先输入ip地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(getTextByConnectPort())) {
                    Toast.makeText(mContext, "请先输入端口号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext, "正在连接", Toast.LENGTH_SHORT).show();
                dismiss();
                connectWiFiPrinter("192.168.1.104", 9100, context);
            }
        });
    }

    public void determine() {

    }

    public String getTextByConnectIp() {
        return ip_text.getText().toString();
    }

    public String getTextByConnectPort() {
        return port_text.getText().toString();
    }

    public void connectWiFiPrinter(final String ip, final int port, final Context context) {

        ConnectWiFiPrinterManager.getInstance(mContext).connectWiFiPrinter(ip, port);
    }
}
