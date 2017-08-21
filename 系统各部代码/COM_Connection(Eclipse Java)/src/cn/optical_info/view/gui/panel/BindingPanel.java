package cn.optical_info.view.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import cn.optical_info.dao.COMDAO;
import cn.optical_info.dao.SLDAO;
import cn.optical_info.domain.SL;
import cn.optical_info.session.UserSession;
import cn.optical_info.util.Binding;
import cn.optical_info.util.ShowMsg;
import cn.optical_info.view.gui.MainFrame;
import cn.optical_info.view.gui.impl.PanelImpl;

/**
 * 三:Java串口通讯模块绑定面板
 * 
 * @author skyfffire@outlook.com
 */
public class BindingPanel extends JPanel implements PanelImpl {
    private static final long serialVersionUID = -8370729566404937017L;
    
    private MainFrame mainFrame = null;                                         // 方便切换MainFrame的面板
    
    JComboBox<String> phySL = new JComboBox<String>();                          // 本地路灯
    JComboBox<String> webSL = new JComboBox<String>();                          // 服务端路灯
    List<SL> allSL = null;
    
    private SLDAO slDAO = new SLDAO();
    private COMDAO comDAO = new COMDAO();

    public BindingPanel(MainFrame _mainFrame) {
        this.mainFrame = _mainFrame;
        
        initThis();
        initAssembly();
    }
    
    @Override
    public void initThis() {
        setLayout(null);
    }

    @Override
    public void initAssembly() {        
        // 绑定按钮
        JButton binding = new JButton("绑定两者");
        binding.setBounds(100, 200, 260, 30);
        binding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 判断用户是否选择了正确的路灯
                if (phySL.getSelectedIndex() == 0
                        || webSL.getSelectedIndex() == 0) {
                    ShowMsg.showErrorMessage("请重新选择", "路灯选择提示");
                } else {
                    slDAO.binding(UserSession.getToolID(), 
                            Integer.parseInt(phySL.getSelectedItem()
                                    .toString().substring(2)),
                            allSL.get(webSL.getSelectedIndex() - 1).getID());
                    
                    ShowMsg.showSuccessMessage(phySL.getSelectedItem() + 
                            "与" + webSL.getSelectedItem(), "成功绑定");
                }
                
                flushData();
            }
        });
        this.add(binding);
        
        // 刷新按钮
        JButton flush = new JButton("刷新数据");
        flush.setBounds(370, 200, 130, 30);
        flush.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flushData();
            }
        });
        this.add(flush);
        
        // 退出绑定按钮
        JButton exitBinding = new JButton("退出绑定系统");
        exitBinding.setBounds(100, 240, 400, 30);
        exitBinding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changePanel("opreationPanel");
            }
        });
        this.add(exitBinding);
        
        // phySL初始化
        phySL.setBounds(100, 70, 400, 40);
        this.add(phySL);
        
        // webSL初始化
        webSL.setBounds(100, 120, 400, 40);
        this.add(webSL);
    }
    
    /**
     * 刷新数据
     */
    public void flushData() {
        phySL.removeAllItems();
        webSL.removeAllItems();
        
        // 检查数据库中是否存在此工具的ID
        if (!comDAO.isExistCOMID(UserSession.getToolID())) {
            comDAO.addToolID(UserSession.getToolID());                    
        }
        
        // 是未绑定的信息
        boolean[] isNotBindingInfo = 
                slDAO.canBindingSL(UserSession.getToolID());
        
        // 判断本地路灯是否完全被绑定
        if (Binding.isAllBindingInLocal(isNotBindingInfo)) {
            ShowMsg.showSuccessMessage("本地路灯已全部绑定！", "温馨提示");
            
            mainFrame.changePanel("opreationPanel");
            
            return;
        }
        
        int j = 1;
        phySL.addItem("请选择要绑定的本地路灯");
        for (boolean isNotBinding : isNotBindingInfo) {
            if (isNotBinding) {
                phySL.addItem("路灯" + j);
            }
            
            j++;
        }
        
        // 服务端未绑定路灯列表
        allSL = slDAO.getNotBindingSL(UserSession.getAdmin());
        
        if (allSL.size() == 0) {
            ShowMsg.showSuccessMessage("服务端路灯已全部被绑定， "
                    + "请前往http://www.optical-info.cn添加路灯", "温馨提示");
        }
        
        // 未绑定的云端路灯列表
        webSL.addItem("请选择要绑定的云端路灯");
        for (SL sl : allSL) {
            webSL.addItem(sl.getPlace());
        }
    }
}
