package cn.optical_info.impl;

/**
 * 串口相关功能的公用接口
 * 
 * @author skyfffire@outlook.com
 */
public interface COMInterface {
    /**
     * 添加当前串口工具的ID
     * 
     * @param ID
     */
    public void addToolID(String ID);
    
    /**
     * 判断当前串口ID是否存在
     * 
     * @param ID
     * @return                  是否存在
     */
    public boolean isExistCOMID(String ID);
}
