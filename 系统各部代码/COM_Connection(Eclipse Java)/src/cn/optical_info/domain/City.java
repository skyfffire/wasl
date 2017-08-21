package cn.optical_info.domain;

import java.io.Serializable;

public class City extends Range implements Serializable {
    private static final long serialVersionUID = 8045648761422755216L;
    
    private String provinceID;

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }
}
