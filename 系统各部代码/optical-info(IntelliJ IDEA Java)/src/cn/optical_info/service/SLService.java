package cn.optical_info.service;

import cn.optical_info.dao.SLDAO;
import cn.optical_info.domain.Admin;
import cn.optical_info.domain.SL;
import cn.optical_info.impl.SLInterface;

import java.util.List;

/**
 * Created by skyfffire@outlook.com on 2017/8/20 0020.
 */
public class SLService implements SLInterface {
    private SLDAO slDAO = new SLDAO();

    @Override
    public void addSL(SL sl) {
        slDAO.addSL(sl);
    }

    @Override
    public int getSLState(Admin admin, int state) {
        return slDAO.getSLState(admin, state);
    }

    @Override
    public List<SL> getAllSL(Admin admin) {
        return slDAO.getAllSL(admin);
    }

    @Override
    public List<SL> getNotBindingSL(Admin admin) {
        return slDAO.getNotBindingSL(admin);
    }

    @Override
    public void operation(String ID, int operation) {
        slDAO.operation(ID, operation);
    }

    @Override
    public void binding(String toolID, int num, String SLID) {
        slDAO.binding(toolID, num, SLID);
    }

    @Override
    public boolean[] canBindingSL(String toolID) {
        return slDAO.canBindingSL(toolID);
    }

    @Override
    public int getOperation(String toolID, int num) {
        return slDAO.getOperation(toolID, num);
    }

    @Override
    public String getSLPlace(String toolID, int num) {
        return slDAO.getSLPlace(toolID, num);
    }

    @Override
    public void updateState(String toolID, int num, int state) {
        slDAO.updateState(toolID, num, state);
    }
}
