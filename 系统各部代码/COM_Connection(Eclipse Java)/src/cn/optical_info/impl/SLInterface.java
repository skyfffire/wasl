package cn.optical_info.impl;

import cn.optical_info.domain.Admin;
import cn.optical_info.domain.SL;

import java.util.List;

/**
 * 路灯相关功能的共有接口
 */
public interface SLInterface {
    /**
     * 添加一个路灯
     *
     * @param sl        新添加的路灯
     */
    public void addSL(SL sl);

    /**
     * 获取一个状态下的路灯的数量
     *
     * @param admin
     * @param state
     */
    public int getSLState(Admin admin, int state);

    /**
     * 获取当前管理员能管理的所有路灯
     *
     * @param admin
     * @return
     */
    public List<SL> getAllSL(Admin admin);
    
    /**
     * 获取当前管理员管理区域未被绑定的所有路灯
     * 
     * @param admin
     * @return
     */
    public List<SL> getNotBindingSL(Admin admin);

    /**
     * 操作某个路灯
     *
     * @param ID
     * @param opreation
     */
    public void operation(String ID, int operation);
    
    /**
     * 绑定当前工具与某一路灯
     * 
     * @param toolID            当前工具ID
     * @param num               当前工具控制的第几盏路灯
     * @param SLID              路灯ID
     */
    public void binding(String toolID, int num, String SLID);
    
    /**
     * 获取当前路灯列表哪些位置可以绑定路灯
     * 
     * @param toolID            当前工具ID
     * @return                  路灯是否绑定的数组
     */
    public boolean[] canBindingSL(String toolID);
    
    /**
     * 获取当前工具控制的某一盏路灯的指令
     * 
     * @param toolID            当前工具ID
     * @param num               当前工具控制的第几盏路灯
     * @return                  对路灯的操作
     */
    public int getOperation(String toolID, int num);
    
    /**
     * 获取路灯的地址
     * 
     * @param toolID            当前工具ID
     * @param num               当前工具控制的第几盏路灯
     * @return                  获取到的路灯的详细地址
     */
    public String getSLPlace(String toolID, int num);
    
    /**
     * 对与当前工具绑定的某一盏路灯的状态进行更新
     * 
     * @param toolID            当前工具ID
     * @param num               当前工具控制的第几盏路灯
     * @param state             要更改为哪种状态
     */
    public void updateState(String toolID, int num, int state);
}
