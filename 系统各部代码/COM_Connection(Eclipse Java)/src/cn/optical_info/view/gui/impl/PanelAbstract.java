package cn.optical_info.view.gui.impl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 三:各面板通用接口
 * 
 * @author skyfffire@outlook.com
 */
public abstract class PanelAbstract
        extends JPanel
        implements PanelInterface {

    public ImageIcon backgroundImage = new ImageIcon(
            getClass().getResource("./img/logon_background.png"));              // 背景图
    public ImageIcon exitBackground1 = new ImageIcon(
            getClass().getResource("./img/exitButton1.png"));                   // 退出按钮_未进入时
    public ImageIcon exitBackground2 = new ImageIcon(
            getClass().getResource("./img/exitButton2.png"));                   // 退出按钮_进入时

    public JButton exitButton = null;                                          // 退出按钮


    public PanelAbstract() {
        // 对退出按钮的初始化
        exitButton = new JButton();
        exitButton.setBounds(560, 10, 30, 30);
        exitButton.setBorderPainted(false);
        exitButton.setIcon(exitBackground1);
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setIcon(exitBackground2);
                exitButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setIcon(exitBackground1);
                exitButton.repaint();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(exitButton);
    }
}
