package cn.optical_info.domain;

import java.io.Serializable;

/**
 * 所有区域的父类，装载共有属性
 */
public class Range implements Serializable {
    private static final long serialVersionUID = -4648496760597997029L;
    
    private String ID;
    private String name;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
