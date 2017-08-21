package cn.optical_info.domain;

import java.io.Serializable;

public class Area extends Range implements Serializable {
    private static final long serialVersionUID = 2520905854893997621L;
    
    private String cityID;

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }
}
