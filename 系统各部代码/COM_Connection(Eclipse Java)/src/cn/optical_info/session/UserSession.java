package cn.optical_info.session;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTextArea;

import cn.optical_info.com.CommIO;
import cn.optical_info.domain.Admin;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * 三:会话数据存储模块
 * 
 * @author skyfffire@outlook.com
 */
public class UserSession {
    private static Admin admin = null;                                          // 当前登录的管理员
    private static ArrayList<CommPortIdentifier> comms = null;                  // 通讯端口
    private static Scanner input = new Scanner(System.in);                      // 获取键盘输入流
    private static CommPortIdentifier comm = null;                              // 选择的通讯串口
    private static SerialPort serialPort = null;                                // 序列化端口
    private static String toolID = null;                                        // 本工具的ID
    private static CommIO commIO = null;                                        // 串口IO控制模块
    private static JTextArea msgPanel = null;                                   // 串口消息回显面板
    
    public static Admin getAdmin() {
        return admin;
    }
    public static void setAdmin(Admin admin) {
        UserSession.admin = admin;
    }
    public static ArrayList<CommPortIdentifier> getComms() {
    	return comms;
    }
    public static void setComms(ArrayList<CommPortIdentifier> comms) {
        UserSession.comms = comms;
    }
    public static Scanner getInput() {
        return input;
    }
    public static CommPortIdentifier getComm() {
        return comm;
    }
    public static void setComm(CommPortIdentifier comm) {
        UserSession.comm = comm;
    }
    public static void setInput(Scanner input) {
        UserSession.input = input;
    }
    public static SerialPort getSerialPort() {
        return serialPort;
    }
    public static void setSerialPort(SerialPort serialPort) {
        UserSession.serialPort = serialPort;
    }
    public static String getToolID() {
        return toolID;
    }
    public static void setToolID(String toolID) {
        UserSession.toolID = toolID;
    }
    public static CommIO getCommIO() {
        return commIO;
    }
    public static void setCommIO(CommIO commIO) {
        UserSession.commIO = commIO;
    }
    public static JTextArea getMsgPanel() {
        return msgPanel;
    }
    public static void setMsgPanel(JTextArea msgPanel) {
        UserSession.msgPanel = msgPanel;
    }
}
