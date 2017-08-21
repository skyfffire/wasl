package cn.optical_info.domain;

import java.io.Serializable;

public class Road extends Range implements Serializable {
    private static final long serialVersionUID = 862600414735262931L;
    
    private String areaID;

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }
}
