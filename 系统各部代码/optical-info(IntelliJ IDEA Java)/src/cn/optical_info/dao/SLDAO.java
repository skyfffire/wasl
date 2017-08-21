package cn.optical_info.dao;

import cn.optical_info.domain.Admin;
import cn.optical_info.domain.SL;
import cn.optical_info.impl.SLInterface;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 路灯相关功能的数据持久层
 *
 * see:cn.optical_info.impl.SLInterface
 */
public class SLDAO implements SLInterface {
    @Override
    public void addSL(SL sl) {
        String sql = "insert into t_doublesl values (?,?,?,?,?,?)";

        try {
            OpticalInfoDAO.getQueryRunner().update(sql,
                    sl.getID(), sl.getState(), 
                    null, sl.getRoadID(), sl.getPlace(), 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSLState(Admin admin, int state) {
        int result = 0;
        List<SL> allSL = new ArrayList<SL>();

        try {
            allSL = getAllSL(admin);

            for (SL sl : allSL) {
                if (sl.getState() == state) {
                    result++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<SL> getAllSL(Admin admin) {
        List<SL> resultSL = null;

        try {
            switch (admin.getType()) {
                case 0:
                    String sql1 = "select * from t_doubleSL where roadID in " +
                            "(select ID from t_road where areaID in " +
                            "(select ID from t_area where cityID in " +
                            "(select ID from t_city where provinceID in " +
                            "(select ID from t_province)))) order by place asc";
                    resultSL = OpticalInfoDAO.getQueryRunner().query(sql1,
                            new BeanListHandler<SL>(SL.class));
    
                    break;
                case 1:
                    String sql2 = "select * from t_doubleSL where roadID in " +
                            "(select ID from t_road where areaID in " +
                            "(select ID from t_area where cityID =?)) order by place asc";
                    resultSL = OpticalInfoDAO.getQueryRunner().query(sql2,
                            new BeanListHandler<SL>(SL.class), admin.getCityID());
    
                    break;
                case 2:
                    String sql3 = "select * from t_doubleSL where roadID in " +
                            "(select ID from t_road where areaID = ?) order by place asc";
                    resultSL = OpticalInfoDAO.getQueryRunner().query(sql3,
                            new BeanListHandler<SL>(SL.class), admin.getAreaID());
    
                    break;
                case 3:
                    String sql4 = "select * from t_doubleSL where roadID = ? order by place asc";
                    resultSL = OpticalInfoDAO.getQueryRunner().query(sql4,
                            new BeanListHandler<SL>(SL.class), admin.getRoadID());
    
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultSL;
    }

    @Override
    public List<SL> getNotBindingSL(Admin admin) {
        String sql = "select * from t_doubleSL "
                + "where roadID = ? and comID is null "
                + "order by place asc";
        
        try {
            return OpticalInfoDAO.getQueryRunner().query(sql, 
                    new BeanListHandler<SL>(SL.class), admin.getRoadID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }  

    @Override
    public void operation(String ID, int operation) {
        String sql = "update t_doubleSL " +
                "set opreation=? " +
                "where ID=?";

        try {
            OpticalInfoDAO.getQueryRunner().update(sql, operation, ID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean[] canBindingSL(String toolID) {
        boolean[] isBinding = new boolean[4];
        
        for (int i = 0; i <= isBinding.length - 1; i++) {
            String sql = 
                    "select * from t_com where SL" + 
                            (i + 1) + " is null and ID=?";

            try {
                isBinding[i] = OpticalInfoDAO.getQueryRunner().query(
                        sql, new ResultSetHandler<Boolean>() {
                    @Override
                    public Boolean handle(ResultSet rs) 
                            throws SQLException {
                        return new Boolean(rs.next());
                    }                    
                }, toolID).booleanValue();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        return isBinding;
    }
    
    @Override
    public void binding(String toolID, int num, String SLID) {
        String updateToSL = "update t_doubleSL set comID=? where ID=?";
        String updateToCOM = "update t_com set SL" + num + "=? where ID=?";
        
        try {
            OpticalInfoDAO.getQueryRunner().update(updateToSL, toolID, SLID);
            OpticalInfoDAO.getQueryRunner().update(updateToCOM, SLID, toolID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateState(String toolID, int num, int state) {
        String sql = "update t_doubleSL set state=? where ID=(select SL" 
    + num + " from t_com where ID=?)";
        
        try {
            OpticalInfoDAO.getQueryRunner().update(sql, state, toolID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int getOperation(String COMID, int num) {
        String sql = "select opreation from t_doubleSL where ID=("
                + "select SL" + num + " from t_com where ID=?)";
        int result = 0;
        
        try {
            result = OpticalInfoDAO.getQueryRunner().query(
                    sql, new ResultSetHandler<Integer>() {
                @Override
                public Integer handle(ResultSet rs) 
                        throws SQLException {
                    if (rs.next()) {
                        return new Integer(rs.getString("opreation"));
                    }
                    
                    return new Integer(0);
                }
            }, COMID).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    @Override
    public String getSLPlace(String toolID, int num) {
        String sql = "select place from t_doubleSL "
                + "where ID=(select SL" + num + " from t_com "
                        + "where ID=?)";
        
        try {
            return OpticalInfoDAO.getQueryRunner().query(
                    sql, new ResultSetHandler<String>() {
                @Override
                public String handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return rs.getString(1);
                    } else {
                        return null;
                    }
                }
            }, toolID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }  
}
