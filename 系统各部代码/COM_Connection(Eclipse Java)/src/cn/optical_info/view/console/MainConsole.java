package cn.optical_info.view.console;

import java.util.List;

import cn.optical_info.dao.AdminDAO;
import cn.optical_info.dao.SLDAO;
import cn.optical_info.domain.Admin;
import cn.optical_info.domain.SL;
import cn.optical_info.session.UserSession;
import cn.optical_info.util.Binding;
import cn.optical_info.util.COMTools;
import cn.optical_info.util.IDTool;
import cn.optical_info.util.StartCommIO;
import gnu.io.CommPortIdentifier;

/**
 * 命令行模式起始处, 用于初始化串口通讯
 * 
 * @author skyfffire@outlook.com
 */
public class MainConsole {
    private AdminDAO adminDAO = new AdminDAO();
    private SLDAO slDAO = new SLDAO();
    
	public MainConsole() {
	    int commsCount = 0;                                                    // 串口数计数器
	    int inputNum = 0;                                                      // 用户输入
	    
	    // 用户需登录再操作
	    while (true) {
	        String phone;
	        String password;
	        Admin admin;
	        
	        System.out.println("管理员您好, 请登录:");
	        System.out.print("请输入电话:"); 
	        phone = UserSession.getInput().nextLine();
	        System.out.print("请输入密码:"); 
	        password = UserSession.getInput().nextLine();
	       
	        admin = adminDAO.logon(phone, password);
	        
	        if (admin == null) {
	            System.err.println("密码错误");
	        } else {
	            UserSession.setAdmin(admin);
	            
                System.out.println("登陆成功, 欢迎您！" 
            + UserSession.getAdmin().getName() + "先生！");
	            
	            break;
	        }
	        
	        try {
	            Thread.sleep(1000);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    // 提示用户绑定路灯
	    System.out.println("要进行绑定路灯操作吗?(Y/N)");
	    if ("Y".equals(UserSession.getInput().nextLine())) {
	        binding();
	    }
	    
	    // 用户可以在此刷新串口数据
		while (true) {
		    commsCount = 0;
	        // 获取串口数据
	        UserSession.setComms(COMTools.getCOMs());
	        // 打印选择列表
	        System.out.println("请选择通讯串口:");
	        for (CommPortIdentifier now : UserSession.getComms()) {
	            System.out.format("%3d.%s\n", ++commsCount, now.getName());
	        }
	        System.out.format("%3d.刷新\n", ++commsCount);
	        
	        // 获取用户输入的数据
	        inputNum = UserSession.getInput().nextInt();
	        
	        // 说明选择的不是刷新
	        if (inputNum != commsCount) {
	            UserSession.getInput().close();
	            
	            break;
	        }
		}
		
		// 生成一个串口通讯工具的ID
		UserSession.setToolID(IDTool.getToolID());
		System.out.println("工具ID获取成功:" + UserSession.getToolID());
		
		// 设置用户选择的串口通讯
		UserSession.setComm(UserSession.getComms().get(inputNum - 1));
		
		StartCommIO.start();
	}
	
	/**
	 * 用于绑定路灯
	 */
	public void binding() {
	    String[] slNames = {"路灯1", "路灯2", "路灯3", "路灯4"};
	    int a;
	    int b;
	    
	    while (true) {
	        List<SL> notBindingSL = slDAO.getAllSL(UserSession.getAdmin());
	        boolean[] notBindingSLInLocal = 
	                slDAO.canBindingSL(UserSession.getToolID());
	        
	        // 服务端路灯非空验证
	        if (notBindingSL.isEmpty()) {
	            System.out.println("服务端没有路灯可以进行绑定, "
	                    + "请前往http://www.optical-info.cn进行添加路灯操作");
	            
	            return;
	        }
	        
	        // 本地端全绑定验证
	        if (Binding.isAllBindingInLocal(notBindingSLInLocal)) {
	            System.out.println("该工具所控制的路灯已全部与服务端进行绑定,"
	                    + " 若信息有误, 请删除ID.conn文件(请谨慎操作)");
	            
	            return;
	        }
	        
	        // 选择本地端路灯
	        while (true) {
	            System.out.println("请选择本地端路灯:");
	            int i = 1;
	            for (boolean now : notBindingSLInLocal) {
	                if (now) {
	                    System.out.println(i + ":" + i + "号路灯");
	                }
	                
	                i++;
	            }
	            
	            a = Integer.parseInt(UserSession.getInput().nextLine());
	            
	            if (a >= 0 && a <= 3) {
	                if (notBindingSLInLocal[a]) {
	                    break;
	                }
	            }
	            
	            System.out.println("选择错误");
	        }
	        
	        // 选择服务端路灯
            while (true) {
                System.out.println("请选择服务端路灯:");
                
                int i = 1;
                for (SL now : notBindingSL) {
                    System.out.println(i + ":" + i + now.getPlace());
                    
                    i++;
                }
                
                b = Integer.parseInt(UserSession.getInput().nextLine());
                
                if (b >= 0 && b <= notBindingSL.size() - 1) {
                    break;
                }
                
                System.out.println("选择错误");
            }
	        
            // 绑定前的最后确认
            System.out.println("您选择的是本地" + slNames[a] + "与" + 
            notBindingSL.get(b).getPlace() + "进行绑定,确定?(Y/N)");
            
            if ("Y".equals(UserSession.getInput().nextLine())) {
                // 将选择的两端绑定起来
                slDAO.binding(UserSession.getToolID(), 
                        a, notBindingSL.get(b).getID());
            }
            
	        System.out.println("您要继续绑定路灯吗?(Y/N)");
	        if (!("Y".equals(UserSession.getInput().nextLine()))) {
	            System.out.println("绑定完毕");
	            
	            break;
	        }
	    }
	    
	    StartCommIO.start();
	}
}
