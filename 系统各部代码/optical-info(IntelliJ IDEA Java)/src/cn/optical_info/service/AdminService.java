package cn.optical_info.service;

import cn.optical_info.dao.AdminDAO;
import cn.optical_info.domain.Admin;
import cn.optical_info.impl.AdminInterface;

import java.util.List;

/**
 * Created by skyfffire@outlook.com on 2017/8/20 0020.
 */
public class AdminService implements AdminInterface {
    private AdminDAO adminDAO = new AdminDAO();

    @Override
    public Admin logon(String phone, String password) throws Exception {
        return adminDAO.logon(phone, password);
    }

    @Override
    public void deleteAdmin(String phone) {
        adminDAO.deleteAdmin(phone);
    }

    @Override
    public void addAdmin(Admin admin, int existAdminType, String rangeID) {
        adminDAO.addAdmin(admin, existAdminType, rangeID);
    }

    @Override
    public void updateAdmin(String ID, String phone, String name, String password) {
        adminDAO.updateAdmin(ID, phone, name, password);
    }

    @Override
    public List<Admin> getAllAdmin(Admin admin) throws Exception {
        return adminDAO.getAllAdmin(admin);
    }
}
