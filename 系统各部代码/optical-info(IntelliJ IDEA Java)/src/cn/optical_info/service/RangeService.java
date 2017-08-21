package cn.optical_info.service;

import cn.optical_info.dao.RangeDAO;
import cn.optical_info.domain.Admin;
import cn.optical_info.domain.Range;
import cn.optical_info.impl.RangeIterface;

import java.util.List;

/**
 * Created by skyfffire@outlook.com on 2017/8/20 0020.
 */
public class RangeService implements RangeIterface {
    private RangeDAO rangeDAO = new RangeDAO();

    @Override
    public List<Range> getRanges(Admin admin) {
        return rangeDAO.getRanges(admin);
    }

    @Override
    public String getRangeName(Admin admin) {
        return rangeDAO.getRangeName(admin);
    }
}
