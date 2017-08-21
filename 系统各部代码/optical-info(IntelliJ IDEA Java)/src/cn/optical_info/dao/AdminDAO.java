package cn.optical_info.dao;

import cn.optical_info.domain.Admin;
import cn.optical_info.impl.AdminInterface;
import cn.skyfffire.other.SuperUUID;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 管理员数据持久层
 *
 * see: cn.optical_info.impl.AdminInterface
 */
public class AdminDAO implements AdminInterface {
    @Override
    public Admin logon(String phone, String password){
        Admin existAdmin = null;
        String sql = "select * from t_admin where " +
                "phone=? and password=?";
        
        // 执行SQL并封装到相应的类中
        try {
            existAdmin = OpticalInfoDAO.getQueryRunner().query(
                    sql, new BeanHandler<Admin>(Admin.class),
                    phone, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return existAdmin;
    }

    @Override
    public void deleteAdmin(String phone) {
        String sql = "delete from t_admin where phone=?";

        try {
            OpticalInfoDAO.getQueryRunner().update(sql, phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAdmin(Admin admin, int existAdminType, String rangeID) {
        String[] rowNames = {"cityID", "areaID", "roadID"};
        String ID = SuperUUID.getUUID() + SuperUUID.getUUID();
        String sql = "insert into t_admin(ID, name, phone, password, type, "
                + rowNames[existAdminType] + ") " +
                "values (?, ?, ?, ?, ?, ?)";
        String existSQL = "select * from t_admin " +
                "where phone = ?";

        try {
            // 判断手机号是否重复
            if (OpticalInfoDAO.getQueryRunner().query(existSQL, new ResultSetHandler<Boolean>() {
                @Override
                public Boolean handle(ResultSet resultSet) throws SQLException {
                    return !resultSet.next();
                }
            }, admin.getPhone()).booleanValue()){
                OpticalInfoDAO.getQueryRunner().update(sql,
                        ID, admin.getName(), admin.getPhone(), admin.getPassword(),
                        existAdminType + 1, rangeID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Admin> getAllAdmin(Admin admin) throws Exception {
        List<Admin> resultAdmins = null;

        // 1.获取当前管理员级别
        int type = admin.getType();
        // 2.根据级别来获取各级子管理员，并装填到List中
        switch (type) {
            case 0:
                String sql1 = "select * from t_admin where ID != ?";

                resultAdmins = OpticalInfoDAO.getQueryRunner().query(sql1,
                        new BeanListHandler<Admin>(Admin.class), admin.getID());

                break;
            case 1:
                String sql2 = "select * from t_admin where areaID in (" +
                        "select ID from t_area where cityID=?) " +
                        "or roadID in (select ID from t_road where areaID in (" +
                        "select ID from t_area where cityID=?))";

                resultAdmins = OpticalInfoDAO.getQueryRunner().query(sql2,
                        new BeanListHandler<Admin>(Admin.class), admin.getCityID(),
                        admin.getCityID());

                break;
            case 2:
                String sql3 = "select * from t_admin where roadID in (" +
                        "select ID from t_road where areaID=?)";
                resultAdmins = OpticalInfoDAO.getQueryRunner().query(sql3,
                        new BeanListHandler<Admin>(Admin.class), admin.getAreaID());

                break;
        }

        return resultAdmins;
    }

    @Override
    public void updateAdmin(String ID, String phone, String name, String password) {
        String sql = "update t_admin " +
                "set phone=?, name=?, password=? " +
                "where ID=?";

        try {
            OpticalInfoDAO.getQueryRunner().update(sql, phone, name, password, ID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
