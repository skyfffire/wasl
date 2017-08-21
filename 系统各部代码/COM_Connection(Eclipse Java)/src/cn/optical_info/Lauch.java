package cn.optical_info;

import cn.optical_info.view.console.MainConsole;
import cn.optical_info.view.gui.MainFrame;

/**
 * 用于启动时用户选择操作模式
 * 
 * @author skyfffire@outlook.com
 */
public class Lauch {
	public static void main(String[] args) {	    
		int inputNum = 1;
		while (inputNum != 1 && inputNum != 2) {
//			System.out.println("请选择操作模式:");
//			System.out.println("1.图形界面模式");
//			System.out.println("2.命令行模式");
			
//			inputNum = Integer.parseInt(UserSession.getInput().nextLine());
		}
		
		if (inputNum == 1) {
			new MainFrame();
		} else {
			new MainConsole();
		}
	}
}
