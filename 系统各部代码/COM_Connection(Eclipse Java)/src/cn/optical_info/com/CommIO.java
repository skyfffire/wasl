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
    private boolean[] lastState = new boolean[4];                               // 放置路灯的上次反馈的状态
    private boolean needShowState = false;                                      // 是否需要重新刷新信息
    
    private SLDAO slDAO = new SLDAO();                                          // 路灯DAO层
    
    /**
     * 停止IO通讯
     */
    public void stopIO() {
        isStopIO = true;

        // 不断地检测双向流是否已经关闭
        while (true) {
            // 关闭双向流有一个延迟，要确保关闭IO才能关闭串口
            if (isCloseInput && isCloseOutput) {
                UserSession.getSerialPort().close();
                
                break;
            }
        }
        
        ShowMsg.showSuccessMessage("停止成功", "消息提示");                      // 弹出成功提示
    }
    
    /**
     * 为串口IO定制的抽象线程
     * 
     * @author skyfffire@outlook.com
     */
    public abstract class NeedThread extends Thread {        
       int arr[] = {-1, 0, 64, 128, 192};                                       // 对位处理的支持
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
         * @param _in
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
                int commMsg = 0;                                                // 用于保存串口端发来的数据

                // 不断地读取流中的数据
                while (true) {
                    int msgCount = 0;                                           // 读取消息数量计数器
                    // 一次读取两组最新数据，然后进入消息等待
                    while (msgCount < 2) {
                        // 1.不断地读取，直到到一个合法的数据
                        if ((commMsg = in.read()) != -1) {
                            msgCount++;                                         // 读取到一个合法数据之后为数据计数器加一
                            // 2.将串口数据转换为二进制数据
                            String binaryComm = 
                                    BinaryTool.toBinaryString(commMsg);
                            // 3.判断串口数据所要表达的信息
                            if (BinaryTool.isStateTesting(binaryComm)) {        // 若为状态报告
                                // 4.保存每一路灯的状态及消息样式处理
                                String msg = "";
                                boolean[] state = new boolean[4];
                                for (int j = 0; j <= 3; j++) {
                                    // 5.通过单片机端处理好的信息来判断路灯状态
                                    state[j] = BinaryTool.
                                            isIntact(binaryComm, j);

                                    // 6.与上次路灯状态不同时
                                    //   才有必要更新数据到数据库
                                    if (state[j] != lastState[j]) {
                                        // 7.向数据库更新每一对路灯的状态
                                        slDAO.updateState(
                                                UserSession.getToolID(),
                                                j + 1, state[j]?1:0);
                                        // 8.此时才有必要更新
                                        //   显示在GUI上的信息
                                        needShowState = true;
                                    }

                                    // 7.组合消息
                                    msg += ("组" + (j + 1) + ": " 
                                    + (state[j]?"状态良好 ":"需要维修 "));
                                    // 8.更新上次路灯状态信息
                                    lastState[j] = state[j];
                                }

                                // 9.当信息有更新时才打印信息
                                if (needShowState) {
                                    // 10.如果用户选择了图形界面模式，应该同时打印到图形界面上
                                    JTextArea msgPanel = UserSession.getMsgPanel();
                                    if (msgPanel != null) {
                                        // 11.行最大处理
                                        if (msgPanel.getLineCount() == 15) {
                                            msgPanel.setText("");
                                        }

                                        // 12.将消息回显到面板上
                                        msgPanel.append(String.format("%2d | %s\n",
                                                msgPanel.getLineCount(), msg));
                                    } else {
                                        // 11.打印消息到控制台
                                        System.out.println(msg);
                                    }

                                    needShowState = false;
                                }
                            } else {
                                // 1.遍历后四位
                                for (int c = 7; c >= 4; c--) {
                                    if (binaryComm.charAt(c) == '0') {
                                        continue;
                                    }
                                    
                                    String content = UserSession.getAdmin().getName() 
                                            + "先生您好，+ 您管理的"
                                            + slDAO.getSLPlace(UserSession
                                            .getToolID(), 8 - c)
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
