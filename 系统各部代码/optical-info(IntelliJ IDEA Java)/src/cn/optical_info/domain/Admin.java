package cn.optical_info.domain;

import cn.optical_info.dao.RangeDAO;

import java.io.Serializable;

public class Admin implements Serializable {
    private static final long serialVersionUID = -3297289600752669517L;
    
    private String ID;
    private String name;
    private String phone;
    private String password;
    private int type;
    private String cityID;
    private String areaID;
    private String roadID;

    private RangeDAO rangeDAO = new RangeDAO();

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        switch (type) {
            case 0: return "国家级管理员";
            case 1: return "市级管理员";
            case 2: return "区县级管理员";
            case 3: return "道路级管理员";
        }

        return "未知类型";
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public String getRoadID() {
        return roadID;
    }

    public void setRoadID(String roadID) {
        this.roadID = roadID;
    }

    public String getRangeName() {
        return rangeDAO.getRangeName(this);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", cityID='" + cityID + '\'' +
                ", areaID='" + areaID + '\'' +
                ", roadID='" + roadID + '\'' +
                '}';
    }
}
