package com.example.huanghongfa.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by huanghongfa on 2017/3/3.
 */

public class PrinterUtils {

    private Context mContext;
    /**
     * 复位打印机
     */
    public static final byte[] RESET = {0x1b, 0x40};

    /**
     * 检查是否有纸指令
     */
    public static final byte[] CHECK_PAPER = new byte[]{0x10, 0x04, 0x04};

    /**
     * 切纸并且走纸
     */
    public static final byte[] CUT = new byte[]{0x1b, 0x69};

    /**
     * 切纸并且走纸
     */
    public static final byte[] zouzhi = new byte[]{0x1d, 0x56, 0x42, 0x00};

    /**
     * 左对齐
     */
    public static final byte[] ALIGN_LEFT = {0x1b, 0x61, 0x00};

    /**
     * 中间对齐
     */
    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x01};

    /**
     * 右对齐
     */
    public static final byte[] ALIGN_RIGHT = {0x1b, 0x61, 0x02};

    /**
     * 选择加粗模式
     */
    public static final byte[] BOLD = {0x1b, 0x45, 0x01};

    /**
     * 取消加粗模式
     */
    public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};

    /**
     * 宽高加倍
     */
    public static final byte[] DOUBLE_HEIGHT_WIDTH = {0x1d, 0x21, 0x11};

    /**
     * 宽加倍
     */
    public static final byte[] DOUBLE_WIDTH = {0x1d, 0x21, 0x10};

    /**
     * 高加倍
     */
    public static final byte[] DOUBLE_HEIGHT = {0x1d, 0x21, 0x01};

    /**
     * 字体不放大
     */
    public static final byte[] NORMAL = {0x1d, 0x21, 0x00};
    /**
     * 字体设置中号
     */
    public static final byte[] MIDDLE = {0x1d, 0x21, 0x02};
    /**
     * 字体设置大号(宽高加倍)
     */
    public static final byte[] LARGE = {0x1d, 0x21, 0x11};

    /**
     * 设置默认行间距
     */
    public static final byte[] LINE_SPACING_DEFAULT = {0x1b, 0x32};
    /**
     * 打印纸一行最大的字节 58mm
     */
    private static final int LINE_BYTE_SIZE = 32;

    /**
     * 打印纸一行最大的字节 80mm
     */
    private static final int LINE_BYTE_SIZE_80 = 48;

    /**
     * 打印三列时，中间一列的中心线距离打印纸左侧的距离
     */
    private static final int LEFT_LENGTH = 18;
    /**
     * 打印三列时，中间一列的中心线距离打印纸右侧的距离
     */
    private static final int RIGHT_LENGTH = 14;
    /**
     * 打印三列时，中间一列的中心线距离打印纸左侧的距离
     */
    private static final int LEFT_LENGTH_80 = 28;
    /**
     * 打印三列时，中间一列的中心线距离打印纸右侧的距离
     */
    private static final int RIGHT_LENGTH_80 = 20;


    /**
     * 打印三列时，第一列汉字最多显示几个文字
     */
    private static final int LEFT_TEXT_MAX_LENGTH = 7;


    private static final byte ESC = 27;//换码
    /**
     * 打印机尺寸,默认58mm
     */
    private static int printerSize = 0;


    public void setContext(Context context) {
        mContext = context;
    }

    private OutputStream outputStream;

    public boolean isconnected() {
        return isconnected;
    }

    public void setIsconnected(boolean isconnected) {
        this.isconnected = isconnected;
    }

    private boolean isconnected;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 绘制下划线（1点宽）
     *
     * @return
     */
    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    /**
     * 进纸并全部切割
     *
     * @throws IOException
     */
    public static void feedAndCut(OutputStream outputStream) {
        try {
            outputStream.write(0x1D);
            outputStream.write(86);
            outputStream.write(65);
            //切纸前走纸多少
            outputStream.write(85);
            outputStream.flush();
            //另外一种切纸的方式
            byte[] bytes = {29, 86, 0};
            outputStream.write(bytes);
        } catch (Exception e) {
            Log.e("", "feedAndCut" + e);
        }
    }

    /**
     * 切纸命令
     */
    public static byte[] getCutPaperByte() {
        byte[] buffer = new byte[5];
        buffer[0] = '\n';//命令必须是单行
        buffer[1] = 29;
        buffer[2] = 86;
        buffer[3] = 66;
        buffer[4] = 1;
        return buffer;
    }

    public int doCheckPaperState(Socket socket) {
        /**1：正常，0：异常，-1：链接失败*/
        int flag = 0;
        try {
            InputStream bis = socket.getInputStream();
//            outputStream.write(CHECK_PAPER);
//            outputStream.flush();
            int tmp = bis.read();
            if (tmp == 18) {
                flag = 1;
            } else {
                flag = 0;
            }
        } catch (Exception e) {
            flag = -1;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 绘制下划线（2点宽）
     *
     * @return
     */
    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 水平方向向右移动col列
     *
     * @param col
     * @return
     */
    public static byte[] set_HT_position(byte col) {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        return result;
    }

    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);
        int marginBetweenMiddleAndRight;
        // 计算两侧文字中间的空格
        if (printerSize == 0) {
            marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;
        } else {
            marginBetweenMiddleAndRight = LINE_BYTE_SIZE_80 - leftTextLength - rightTextLength;
        }
        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }


    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        int marginBetweenLeftAndMiddle;
        // 计算两侧文字中间的空格
        if (printerSize == 0) {
            marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;
        } else {
            marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;
        }
        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);
        Log.e("", "printThreeData sb left " + sb.toString());
        int marginBetweenMiddleAndRight;
        if (printerSize == 0) {
            // 计算右侧文字和中间文字的空格长度
            marginBetweenMiddleAndRight = (RIGHT_LENGTH - middleTextLength / 2 - rightTextLength) + 1;
        } else {
            marginBetweenMiddleAndRight = RIGHT_LENGTH_80 - middleTextLength / 2 - rightTextLength;
        }
        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        Log.e("", "printThreeData sb right " + sb.toString());
        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        Log.e("", "printThreeData sb right =" + sb.toString());
        return sb.toString();
    }


    /**
     * 设置打印格式
     *
     * @param command 格式指令
     */
    public static void selectCommand(OutputStream outputStream, byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            Log.e("", "selectCommand " + e);
        }
    }

    /**
     * 设置打印格式
     *
     * @param command 格式指令
     */
    public void selectCommand(byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {

        }
    }


    public String getLine58() {
        return "--------------------------------";
    }

    public String getLine80() {
        return "------------------------------------------------";
    }

    /**
     * 打印文字
     *
     * @param text 要打印的文字
     */
    public void printText(String text) {
        try {
            if (outputStream == null) {
                Toast.makeText(mContext, "请先连接上打印机", Toast.LENGTH_SHORT).show();
                return;
            }
            selectCommand(outputStream, PrinterUtils.RESET);
            selectCommand(outputStream, PrinterUtils.LINE_SPACING_DEFAULT);
            selectCommand(outputStream, PrinterUtils.ALIGN_LEFT);
            //默认使用小号
            selectCommand(outputStream, PrinterUtils.NORMAL);
            byte[] data = text.getBytes("GB2312");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
        } catch (IOException e) {
            Log.e("", "printText " + e);
        }
    }

    /**
     * 打印文字
     *
     * @param text 要打印的文字
     */
    public void printText(OutputStream outputStream, String text) {
        try {
            selectCommand(outputStream, PrinterUtils.RESET);
            selectCommand(outputStream, PrinterUtils.LINE_SPACING_DEFAULT);
            selectCommand(outputStream, PrinterUtils.ALIGN_LEFT);
            //默认使用小号
            selectCommand(outputStream, PrinterUtils.NORMAL);
            byte[] data = text.getBytes("GB2312");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
        } catch (IOException e) {
            Log.e("", "printText " + e);
        }
    }


    public int getPrinterSize() {
        return printerSize;
    }

    public void setPrinterSize(int Size) {
        printerSize = Size;
    }
}
