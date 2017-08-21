package cn.optical_info.com;

import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JTextArea;

import cn.optical_info.dao.SLDAO;
import cn.optical_info.session.UserSession;
import cn.optical_info.util.BinaryTool;
import cn.optical_info.util.SendMsg;
import cn.optical_info.util.ShowMsg;

/**
 * 通讯IO
 * 
 * @author skyfffire@outlook.com
 */
public class CommIO {
    private boolean isStopIO = false;                                           // 标志是否停止这个IO
    private boolean isCloseInput = false;                                       // 是否停止了Input
    private boolean isCloseOutput = false;                                      // 是否停止了Output
    private InputStream in = null;                                              // 通过串口发来的数据流
    private OutputStream out = null;                                            // 串口反馈给单片机的数据流
    private boolean canSendMsg = true;                                          // 可以发送消息的指令
    
    private SLDAO slDAO = new SLDAO();                                          // 路灯DAO层
    
    /**
     * 停止IO通讯
     */
    public void stopIO() {
        isStopIO = true;
        
        while (true) {
            // 直到真正地关闭了IO流之后才开始关闭串口通讯
            if (isCloseInput && isCloseOutput) {
                UserSession.getSerialPort().close();
                
                break;
            }
            
            // 1s检测一次
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        ShowMsg.showSuccessMessage("停止成功", "消息提示");
    }
    
    /**
     * 为串口IO定制的抽象线程
     * 
     * @author skyfffire@outlook.com
     */
    public abstract class NeedThread extends Thread {        
       int arr[] = {-1, 0, 64, 128, 192};                                      // 对位处理的支持
                                                                                // 依次的四个二进制数分别为
                                                                                // 0000-0000
                                                                                // 0100-0000
                                                                                // 1000-0000
                                                                                // 1100-0000
                                                                                // 前两位代表是
                                                                                // 第几盏路灯发出的反馈信号
                                                                                // 在实现此抽象类的两个子类中
                                                                                // 都会用到右移六位取到路灯编号的处理
    }
    
    /**
     * 读取数据线程
     * 
     * @author skyfffire@outlook.com
     */
    public class SerialReader extends NeedThread {
        
        /**
         * 默认构造器
         * 
         * @param in
         * @param _msgPanel
         */
        public SerialReader(InputStream _in) {
            CommIO.this.in = _in;
        }
        
        /**
         * 异步线程
         */
        @Override
        public void run() {
            try {
                int commMsg = 0;

                // 不断地读取流中的数据
                while (true) {                    
                    // 读取数量计数器
                    int count = 0;
                    // 向数据库及界面更新数据
                    while (count != 2) {
                        // 保证读取到的四盏路灯数据为最新
                        if ((commMsg = in.read()) != -1) {                            
                            count++;
                            // 打印当前串口发送的数据
                            String binaryComm = 
                                    BinaryTool.toBinaryString(commMsg);
                            // 如果是状态检测
                            if (BinaryTool.isStateTesting(binaryComm)) {
                                // 保存每一路灯的状态及消息样式处理
                                String msg = "";
                                boolean[] state = new boolean[4];
                                for (int j = 0; j <= 3; j++) {
                                    // 红外信号被遮挡时就可以检测某一组灯的好坏
                                    state[j] = BinaryTool.
                                            isIntact(binaryComm, j);
                                    
                                    slDAO.updateState(
                                            UserSession.getToolID(), 
                                            j + 1, state[j]?1:0);
                                    
                                    msg += ("组" + (j + 1) + ": " 
                                    + (state[j]?"状态良好 ":"需要维修 "));
                                }
                                
                                // 温馨提示
                                if (msg.length() == 0) {
                                    msg = "遮挡红外后可以检测到损坏情况";
                                }
                                
                                // 如果用户选择了图形界面模式，应该同时打印到图形界面上
                                JTextArea msgPanel = UserSession.getMsgPanel();
                                if (msgPanel != null) {
                                    // 行最大处理
                                    if (msgPanel.getLineCount() == 15) {
                                        msgPanel.setText("");
                                    }
                                    
                                    // 将消息回显到面板上
                                    msgPanel.append(String.format("%2d | %s\n", 
                                            msgPanel.getLineCount(), msg));
                                } else {
                                    // 打印消息到控制台
                                    System.out.println(msg);
                                }
                            } else {
                                for (int c = 7; c >= 4; c--) {
                                    if (binaryComm.charAt(c) == '0') {
                                        continue;
                                    }
                                    
                                    String content = UserSession.getAdmin().getName() 
                                            + "先生您好，"
                                            + "您管理的" + slDAO.getSLPlace(UserSession.getToolID(), 8 - c) 
                                            + "附近有人遇见突发状况，需要帮助。";
                                    
                                    // 如果是报警
                                    if (canSendMsg) {
                                        // 五分钟内不能重发
                                        canSendMsg = false;
                                        new TimeThread().start();
                                        
                                        SendMsg.sendMsg(
                                                UserSession.getAdmin().getPhone(), 
                                                content);
                                    }
                                    
                                    System.out.println(content);
                                    
                                    // 如果用户选择了图形界面模式，应该同时打印到图形界面上
                                    JTextArea msgPanel = UserSession.getMsgPanel();
                                    if (msgPanel != null) {
                                        // 行最大处理
                                        if (msgPanel.getLineCount() == 15) {
                                            msgPanel.setText("");
                                        }
                                        
                                        // 将消息回显到面板上
                                        msgPanel.append(String.format("%2d | %s\n", 
                                                msgPanel.getLineCount(), content));
                                    }
                                }
                            }
                        }                       
                    }

                    if (isStopIO) {
                        in.close();
                        
                        isCloseInput = true;
                        
                        break;
                    }
                    
                    // 清空输入流:防止串口数据缓存而造成的数据不更新问题
                    flush(in);
                    
                    Thread.sleep(1000);
                }
            } catch (Exception e) {                
                e.printStackTrace();
            }
        }
        
        /**
         * 清空当前流中的内容，用于同步单片机方面的通信
         * 
         * @param in
         */
        public void flush(InputStream in) throws Exception {
           while ((in.read()) != -1); 
        }
    }

    /**
     * 发送数据线程
     * 
     * @author skyfffire@outlook.com
     */
    public class SerialWriter extends NeedThread {
        public SerialWriter(OutputStream _out) {
            CommIO.this.out = _out;
        }

        @Override
        public void run() {
            try {
                // 向串口发送控制信号
                while (true) {
                    // 依次取出该路灯的四个控制信号, 并发送到单片机方面
                    for (int i = 1; i <= 4; i++) {                        
                        out.write(arr[i] + slDAO.getOperation(UserSession.getToolID(), i));
                    }
                    
                    if (isStopIO) {
                        out.close();
                        
                        isCloseOutput = true;
                        
                        break;
                    }
                    
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private class TimeThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000 * 60 * 5);
                
                CommIO.this.canSendMsg = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
