package cn.optical_info.view.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import cn.optical_info.view.gui.panel.BindingPanel;
import cn.optical_info.view.gui.panel.LoginPanel;
import cn.optical_info.view.gui.panel.OpreationPanel;

/**
 * 用于摆放各个Panel, 图形界面模式起始处
 * 
 * @author skyfffire@outlook.com
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 9069520843013665633L;

	private LoginPanel loginPanel = new LoginPanel(this);                       // 登录面板
	private OpreationPanel opreationPanel = new OpreationPanel(this);           // 操作面板
	private BindingPanel bindingPanel = new BindingPanel(this);                 // 绑定面板
	
	public MainFrame() {
	    this.setTitle("三:路灯控制工具");
	    this.setBounds(700, 200, 600, 600);
	    this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {                
                System.exit(0);
            }
        });
	    
        this.add(loginPanel);

        this.setVisible(true);
    }
	
	/**
	 * 设置该视图上的面板
	 * 
	 * @param panelName    需要设置的面板Name
	 */
	public void changePanel(String panelName) {
	    loginPanel.setVisible(false);
	    opreationPanel.setVisible(false);
	    bindingPanel.setVisible(false);
	    
	    if ("opreationPanel".equals(panelName)) {
	        this.add(opreationPanel);  
            opreationPanel.setVisible(true);
	    } else if ("bindingPanel".equals(panelName)) {
	        this.add(bindingPanel);
	        
	        bindingPanel.flushData();
	        bindingPanel.setVisible(true);
	    } else if ("loginPanel".equals(panelName)) {
	        this.add(loginPanel);
	        loginPanel.setVisible(true);
	    }

	    this.repaint();
	}
}
