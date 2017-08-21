package cn.optical_info.util;

/**
 * 为处理二进制位而制作的工具类
 * 
 * @author skyfffire@outlook.com
 */
public class BinaryTool {
    /**
     * 将串口数据转换成需要的二进制数据
     * 
     * @param commMsg   串口数据
     * @return          满足格式要求的二进制数据
     */
    public static String toBinaryString(int commMsg) {
        StringBuffer resultString = 
                new StringBuffer(Integer.toBinaryString(commMsg));
        
        // 补0处理
        while (resultString.length() < 8) {
            resultString.insert(0, '0');
        }
        
        return resultString.toString();
    }
    
    /**
     * 检测某一位置的路灯是否完好
     * 
     * @param binary        满足格式的二进制数据
     * @param location         
     * @return
     */
    public static boolean isIntact(String binary, int localtion) {
        return binary.charAt(7 - localtion) == '1';
    }
    
    /**
     * 是否是状态检测
     * 
     * @return 是用于路灯状态检测的信号
     */
    public static boolean isStateTesting(String binary) {
        return binary.substring(0, 4).equals("1111");
    }
}
