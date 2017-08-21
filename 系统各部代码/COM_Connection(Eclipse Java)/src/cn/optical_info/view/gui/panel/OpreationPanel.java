package cn.optical_info.view.gui.panel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.optical_info.session.UserSession;
import cn.optical_info.util.COMTools;
import cn.optical_info.util.IDTool;
import cn.optical_info.util.ShowMsg;
import cn.optical_info.util.StartCommIO;
import cn.optical_info.view.gui.MainFrame;
import cn.optical_info.view.gui.impl.PanelImpl;
import gnu.io.CommPortIdentifier;

/**
 * 三:Java串口通讯模块操作面板
 * 
 * @author skyfffire@outlook.com
 */
public class OpreationPanel extends JPanel implements PanelImpl {
    private static final long serialVersionUID = -926022870867634036L;
                                                      
    private MainFrame mainFrame = null;                                         // 方便切换MainFrame中的面板
                                                                                
    private JComboBox<String> COMChooser = null;                                // 串口选择器
    private JButton startSimButton = null;                                      // 开始串口通信
    private JTextArea msgPanel = null;                                          // 串口消息显示面板
    private JButton bindingButton = null;                                       // 绑定路灯数据按钮
    private JTextField idTextField = null;                                      // ID显示处 
                                                                                
    private boolean isConnCOM = false;                                          // 串口通信已打开标志
    
    public OpreationPanel(MainFrame _mainFrame) {
        this.mainFrame = _mainFrame;
        
        initThis();
        initAssembly();
    }
    
    @Override
    public void initThis() {
        this.setLayout(null);
    }

    @Override
    public void initAssembly(){   
        try {
            initToolID();                                                       // 初始化ToolID
            initBindingDataButton();                                            // 绑定按钮
            initExitButton();                                                   // 退出按钮
            initMsgPanel();                                                     // 消息面板
            initCOMChoose();                                                    // 串口选择器
            initStartSimButton();                                               // 开始、暂停按钮
            initFlushButton();                                                  // 串口刷新按钮
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 添加刷新按钮
     */
    public void initFlushButton() {
        JButton flushButton = new JButton("刷新串口");
        
        flushButton.setBounds(330, 80, 100, 30);
        flushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                flushComm();
            }
        });
        
        add(flushButton);
    }

    /**
     * 用于绑定本地路灯与云端路灯的界面
     */
    public void initBindingDataButton() 
            throws Exception {
        bindingButton = new JButton("我要绑定路灯数据");
        
        bindingButton.setBounds(30, 40, 400, 30);
        bindingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changePanel("bindingPanel");
            }
        });
        
        this.add(bindingButton);
    }
    
    /**
     * 初始化ID
     */
    public void initToolID() throws IOException {
        // 将ToolID保存到UserSession中
        UserSession.setToolID(IDTool.getToolID());
        
        // 构造显示工具ID的图形界面
        idTextField = new JTextField("串口工具ID：" 
        + UserSession.getToolID(), 70);
        idTextField.setEditable(false);                                         // 设置不可编辑
        idTextField.setBounds(30, 10, 540, 20);
        
        this.add(idTextField);                                                  // 添加到图形界面中
    }
    
    /**
     * 初始化退出按钮
     */
    public void initExitButton() {
        JButton exit = new JButton("退出系统");
        exit.setBounds(440, 40, 130, 30);
        
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        add(exit);
    }
    
    /**
     * 显示消息面板
     */
    public void initMsgPanel() {
        msgPanel = new JTextArea(25, 38);
        msgPanel.setBounds(30, 145, 540, 400);
        msgPanel.setFont(new Font("宋体", Font.PLAIN, 18));
        msgPanel.setEditable(false);
        
        UserSession.setMsgPanel(msgPanel);
        
        this.add(msgPanel);
    }
    
    /**
     * 初始化开始串口通信的按钮
     */
    public void initStartSimButton() {
        startSimButton = new JButton("开始串口通信");
        
        startSimButton.setBounds(30, 80, 290, 30);
        startSimButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 已经打开时停止串口通信
                if (isConnCOM) {
                    startSimButton.setText("开始串口通信");
                    
                    // 关闭相应操作                  
                    UserSession.getCommIO().stopIO();
                    UserSession.setComms(null);
                    UserSession.setComm(null);
                    
                    isConnCOM = false;
                } else {
                    // 获取能通讯的串口
                    if (UserSession.getComms() == null) {
                        UserSession.setComms(COMTools.getCOMs());
                    }
                    // 初始化用户选择的通讯串口
                    if (UserSession.getComms() != null 
                            && COMChooser.getSelectedIndex() != 0) {
                        
                        UserSession.setComm(UserSession.getComms().get(
                                COMChooser.getSelectedIndex() - 1));
                    }
                    
                    // 经选择是否正常的判断之后开启串口
                    if (UserSession.getComm() == null) {
                        ShowMsg.showErrorMessage("请选择通讯串口", "温馨提示");
                    } else {
                        startSimButton.setText("停止串口通信");
                        
                        StartCommIO.start();
                        UserSession.setComms(COMTools.getCOMs());
                        
                        isConnCOM = true;
                    }
                }
            }
        });
        
        add(startSimButton);
    }
    
    /**
     * 初始化串口选择器
     */
    public void initCOMChoose() {        
        COMChooser = new JComboBox<String>(new String[]{"请选择通讯串口"});
        COMChooser.setBounds(440, 80, 130, 30);
        
        add(COMChooser);
        
        flushComm();
    }
    
    /**
     * 刷新串口
     */
    public void flushComm() {
        COMChooser.removeAllItems();

        UserSession.setComms(COMTools.getCOMs());
        
        // 添加能通讯的串口
        COMChooser.addItem("请选择通讯串口");
        for (CommPortIdentifier now : UserSession.getComms()) {
            COMChooser.addItem(now.getName());
        }
    }
}
