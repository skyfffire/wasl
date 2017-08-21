package cn.optical_info.dao;

import cn.optical_info.impl.COMInterface;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author skyfffire@outlook.com
 */
public class COMDAO implements COMInterface {
    private static QueryRunner queryRunner = null;
    
    static {
        queryRunner = 
                    new QueryRunner(new ComboPooledDataSource());
    }
    
    public void addToolID(String ID) {
        String sql = "insert into t_com(ID) values(?)";
        
        try {
            queryRunner.update(sql, ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean isExistCOMID(String ID) {
        String sql = "select * from t_com where ID=?";
        
        try {
            return queryRunner.query(sql, new ResultSetHandler<Boolean>() {
                @Override
                public Boolean handle(ResultSet rs) 
                        throws SQLException {
                    return new Boolean(rs.next());
                }
                
            }, ID).booleanValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
///:~