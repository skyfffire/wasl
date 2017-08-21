package cn.optical_info.view.gui.panel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cn.optical_info.dao.AdminDAO;
import cn.optical_info.domain.Admin;
import cn.optical_info.session.UserSession;
import cn.optical_info.util.ShowMsg;
import cn.optical_info.view.gui.MainFrame;
import cn.optical_info.view.gui.impl.PanelImpl;

/**
 * 三:Java串口通讯模块登陆面板
 * 
 * @author skyfffire@outlook.com
 */
public class LoginPanel extends JPanel implements PanelImpl {
	private static final long serialVersionUID = 5417724488594521025L;
	
	private MainFrame mainFrame = null;                                         // 方便控制主面板切换页面
	private JTextField phoneField = null;                                       // 手机号输入框
	private JPasswordField passwordField = null;                                // 密码输入框
	private JButton logonButton = null;                                         // 登录按钮
	
	private AdminDAO adminDAO = new AdminDAO();
	
	public LoginPanel(MainFrame _mainFrame) {
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
        phoneField.setBounds(100, 100, 400, 30);
        phoneField.setText("12233334444");
        
        this.add(phoneField);
        
        // 对密码输入框的初始化
        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 200, 400, 30);
        passwordField.setText("123456");
        
        this.add(passwordField);
        
        // 对登录按钮的初始化
        logonButton = new JButton("登录");
        logonButton.setBounds(100, 300, 400, 40);
        logonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });
        
        this.add(logonButton);
    }
    
    @Override
    public void paint(Graphics graphics) {
        graphics.setFont(new Font("微软雅黑", Font.PLAIN, 25));
        
        graphics.drawString("手机号:", 100, 70);
        graphics.drawString("密码:", 100, 170);
        
        phoneField.repaint();
        passwordField.repaint();
        logonButton.repaint();
    }
}
