package cn.optical_info.domain;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 路灯的实体
 */
public class SL implements Serializable {
    private static final long serialVersionUID = -263368917556729729L;
    
    private String ID;
    private int state;
    private String roadID;
    private String place;
    private int opreation;
    private String[] patterns = {"手动：关", "手动：开", "全自动"};
    private String[] states = {"需要维修", "状态良好"};

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRoadID() {
        return roadID;
    }

    public void setRoadID(String roadID) {
        this.roadID = roadID;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getOpreation() {
        return opreation;
    }

    public void setOpreation(int opreation) {
        this.opreation = opreation;
    }

    public String getOpreationString() {
        return patterns[opreation];
    }

    public String getStateString() {
        return states[state];
    }

    @Override
    public String toString() {
        return "SL{" +
                "ID='" + ID + '\'' +
                ", state=" + state +
                ", roadID='" + roadID + '\'' +
                ", place='" + place + '\'' +
                ", opreation=" + opreation +
                ", patterns=" + Arrays.toString(patterns) +
                '}';
    }
}
