package cn.optical_info.impl;

import cn.optical_info.domain.Admin;

import java.util.List;

/**
 * 管理员功能的公有接口
 */
public interface AdminInterface {
    /**
     * 登录使用的方法
     * @param phone     用户的电话号码
     * @param password  用户的密码
     * @return          登录后的用户
     */
    public Admin logon(String phone, String password) throws Exception;

    /**
     * 对管理员进行删除操作
     *
     * @param phone     根据电话号码删除管理员
     */
    public void deleteAdmin(String phone);

    /**
     * 对管理员进行增加操作
     *
     * @param admin     需要增加的管理员
     * @param existAdminType 当前管理员的等级
     * @param rangeID   需要增加的管理员管理的地区ID
     */
    public void addAdmin(Admin admin, int existAdminType, String rangeID);

    /**
     * 对管理员进行更新
     *
     * @param ID        更新者的ID
     * @param phone     更新的电话
     * @param name      更新的姓名
     * @param password  更新的密码
     */
    public void updateAdmin(String ID, String phone,
                            String name, String password);

    /**
     * 获取当前管理员能管理的所有管理员
     *
     * @param admin
     * @return          所有满足条件的管理员
     */
    public List<Admin> getAllAdmin(Admin admin) throws Exception;
}
