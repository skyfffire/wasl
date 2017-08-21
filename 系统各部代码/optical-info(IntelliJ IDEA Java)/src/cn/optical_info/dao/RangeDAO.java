package cn.optical_info.dao;

import cn.optical_info.domain.Admin;
import cn.optical_info.domain.Range;
import cn.optical_info.impl.RangeIterface;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.List;

/**
 * 区域处理数据持久层
 *
 * see:cn.optical_info.impl.RangeIterface
 */
public class RangeDAO implements RangeIterface {
    @Override
    public List<Range> getRanges(Admin admin) {
        String[] tables = {"t_province", "t_city", "t_area", "t_road"};
        String[] rowNames = {"cityID", "areaID", "roadID"};

        String sql = "select * from " + tables[admin.getType() + 1] +
                ((admin.getType() == 0)?"":(" where " + rowNames[admin.getType() - 1] + " = ?"));

        String queryID = null;

        if (admin.getType() == 1) {
            queryID = admin.getCityID();
        } else if (admin.getType() == 2) {
            queryID = admin.getAreaID();
        } else if (admin.getType() == 3) {
            queryID = admin.getRoadID();
        }

        List<Range> ranges = null;

        try {
            if (admin.getType() == 0) {
                ranges = OpticalInfoDAO.getQueryRunner().query(sql,
                        new BeanListHandler<Range>(Range.class));
            } else {
                ranges = OpticalInfoDAO.getQueryRunner().query(sql,
                        new BeanListHandler<Range>(Range.class), queryID);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ranges;
    }

    @Override
    public String getRangeName(Admin admin) {
        String[] tables = {"t_province", "t_city", "t_area", "t_road"};

        String queryID = null;

        switch (admin.getType()) {
            case 1: queryID = admin.getCityID(); break;
            case 2: queryID = admin.getAreaID(); break;
            case 3: queryID = admin.getRoadID(); break;
        }

        // 市级以上管理员：即最高级管理员
        if (queryID == null) {
            return "中华人民共和国";
        } else {
            String sql = "select name from " + tables[admin.getType()] + " where ID=?";

            try {
                return (String) OpticalInfoDAO.getQueryRunner().query(sql,
                        new ScalarHandler("name"), queryID);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取当前的管理员的管理地区等级(以字符串信息返回)
     * @return
     */
    protected static String comRange(Admin admin) {
        if (admin.getCityID() != null) {
            return "city";
        } else if (admin.getAreaID() != null) {
            return "area";
        } else if (admin.getRoadID() != null) {
            return "road";
        }

        return null;
    }
}
