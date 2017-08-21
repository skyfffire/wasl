package cn.optical_info.util;

import cn.optical_info.session.UserSession;
import gnu.io.SerialPort;

/**
 * 用于初始化串口通讯模块
 * 
 * @author skyfffire@outlook.com
 */
public class InitWithComm {
    public InitWithComm() throws Exception {
        // 序列化串口通讯
        SerialPort serialPort = (SerialPort)
                UserSession.getComm().open(
                        UserSession.getComm().getName(), 1000);
        serialPort.setSerialPortParams(
                9600, SerialPort.DATABITS_8, 
                SerialPort.STOPBITS_1, 
                SerialPort.PARITY_NONE);
        // 保存序列号端口到Session中方便使用
        UserSession.setSerialPort(serialPort);
    }
}
