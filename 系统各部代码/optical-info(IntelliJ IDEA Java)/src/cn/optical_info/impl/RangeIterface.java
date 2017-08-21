package cn.optical_info.impl;

import cn.optical_info.domain.Admin;
import cn.optical_info.domain.Range;

import java.util.List;

public interface RangeIterface {
    /**
     * 根据管理员获取管理区域
     * @param admin
     * @return
     */
    public List<Range> getRanges(Admin admin);

    /**
     * 获取当前管理员管理的这块地的名字
     * @param admin
     */
    public String getRangeName(Admin admin);
}
