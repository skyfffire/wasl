package cn.optical_info.view.gui.panel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import cn.optical_info.dao.AdminDAO;
import cn.optical_info.domain.Admin;
import cn.optical_info.session.UserSession;
import cn.optical_info.util.ShowMsg;
import cn.optical_info.view.gui.MainFrame;
import cn.optical_info.view.gui.impl.PanelAbstract;

/**
 * 三:Java串口通讯模块登陆面板
 * 
 * @author skyfffire@outlook.com
 */
public class LoginPanel extends PanelAbstract {
	private static final long serialVersionUID = 5417724488594521025L;
	
	private MainFrame mainFrame = null;                                         // 方便控制主面板切换页面
	private JTextField phoneField = null;                                       // 手机号输入框
	private JPasswordField passwordField = null;                                // 密码输入框
	private JButton logonButton = null;                                         // 登录按钮

    private ImageIcon buttonBackground1 = new ImageIcon(
            getClass().getResource("./img/logon_button_1.png"));                // 登录按钮_未进入时
    private ImageIcon buttonBackground2 = new ImageIcon(
            getClass().getResource("./img/logon_button_2.png"));                // 登录按钮_进入时

	private AdminDAO adminDAO = new AdminDAO();
	
	public LoginPanel(MainFrame _mainFrame) {
	    super();

        this.mainFrame = _mainFrame;
        
        initThis();
        initAssembly();
    }

    @Override
    public void initThis() {
        this.setLayout(null);
    }

    @Override
    public void initAssembly() {
        // 对手机号码输入框的初始化
        phoneField = new JTextField(20);
        phoneField.setBounds(225, 260, 270, 40);
        phoneField.setBorder(new EmptyBorder(0, 0, 0, 0));
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        phoneField.setHorizontalAlignment(JTextField.CENTER);
        this.add(phoneField);
        
        // 对密码输入框的初始化
        passwordField = new JPasswordField(20);
        passwordField.setBounds(225, 330, 270, 40);
        passwordField.setBorder(new EmptyBorder(0, 0, 0, 0));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    logonButton.doClick();
                }
            }
        });
        this.add(passwordField);
        
        // 对登录按钮的初始化
        logonButton = new JButton();
        logonButton.setBounds(220, 400, 281, 44);
        logonButton.setBorderPainted(false);
        logonButton.setIcon(buttonBackground1);
        logonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 消息合法性校验
                if (phoneField.getText().equals("")
                        || passwordField.getPassword().equals("")) {
                    ShowMsg.showErrorMessage("请不要留空",
                            "消息提交错误");
                } else {
                    Admin admin = adminDAO.logon(phoneField.getText(),
                            new String(passwordField.getPassword()));               // 尝试登录

                    if (admin == null) {
                        ShowMsg.showErrorMessage("请检查手机号或密码是否错误",
                                "登录提示信息");
                    } else {
                        UserSession.setAdmin(admin);

                        ShowMsg.showSuccessMessage("登陆成功, 欢迎您！"
                                        + UserSession.getAdmin().getName() + "先生！",
                                "登录提示信息");

                        mainFrame.changePanel("opreationPanel");
                    }
                }
            }
        });
        logonButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logonButton.setIcon(buttonBackground2);
                logonButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logonButton.setIcon(buttonBackground1);
                logonButton.repaint();
            }
        });
        this.add(logonButton);
    }
    
    @Override
    public void paint(Graphics graphics) {
	    graphics.drawImage(backgroundImage.getImage(), 0, 0, null);
        
        phoneField.repaint();
        passwordField.repaint();
        logonButton.repaint();
        exitButton.repaint();

        phoneField.requestFocus();
    }
}
