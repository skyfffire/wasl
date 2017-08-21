package cn.optical_info.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;

/**
 * C3P0连接池
 */
public class OpticalInfoDAO {
    private static ComboPooledDataSource dataSource = null;

    /**
     * 初始化连接池
     */
    static {
        dataSource = 
                new ComboPooledDataSource();
    }
    
    /**
     * 获取一个执行体
     * @return QueryRunner
     */
    public static QueryRunner getQueryRunner() {
        return new QueryRunner(dataSource);
    }
}
