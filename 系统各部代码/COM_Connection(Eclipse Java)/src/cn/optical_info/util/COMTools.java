package cn.optical_info.util;

import java.util.ArrayList;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

/**
 * 提供GUI方面需要用到的工具类
 * 
 * @author skyfffire@outlook.com
 */
public class COMTools {
	/**
	 * 获取当前能检测到的串口模块
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<CommPortIdentifier> getCOMs() {
		Enumeration<CommPortIdentifier> comms = 
				CommPortIdentifier.getPortIdentifiers();
		
		ArrayList<CommPortIdentifier> commNames = 
				new ArrayList<CommPortIdentifier>();
		
		while (comms.hasMoreElements()) {
			CommPortIdentifier cpi = comms.nextElement();
		
			commNames.add(cpi);
		}
		
		return commNames;
	}
}
