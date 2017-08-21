package cn.optical_info.util;

import jdk.nashorn.internal.scripts.JO;

import java.awt.Toolkit;

import javax.swing.JOptionPane;

/**
 * 提供显示信息的工具类
 * 
 * @author skyfffire@outlook.com
 */
public class ShowMsg {
    /**
     * 显示指定错误信息
     */
    public static void showErrorMessage(String msg, String title) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, 
                msg, 
                title, 
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * 显示指定正确信息
     */
    public static void showSuccessMessage(String msg, String title) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, 
                msg, 
                title, 
                JOptionPane.PLAIN_MESSAGE);
    }
}
