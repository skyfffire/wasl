package cn.optical_info.controller;

import cn.optical_info.domain.Admin;
import cn.optical_info.service.AdminService;
import cn.optical_info.service.RangeService;
import cn.optical_info.service.SLService;
import cn.skyfffire.web.SuperServlet;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 管理员的Controller
 */
public class AdminServlet extends SuperServlet {
    private AdminService adminService   = new AdminService();
    private SLService slService         = new SLService();
    private RangeService rangeService   = new RangeService();

    /**
     * 用于登录
     * @param req           用户登录的request
     * @param resp          用户登录的response
     * @throws Exception    用户登录时的异常
     */
    public void logon(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        // 获取用户的session以及表单提交中的信息
        HttpSession session = req.getSession();
        Map<String, String[]> pars = req.getParameterMap();

        // 装填提交的用户信息
        Admin admin = new Admin();
        BeanUtils.populate(admin, pars);

        // 检测是否登陆失败
        try {
            // 从数据库将用户提取出来
            admin = adminService.logon(admin.getPhone(), admin.getPassword());

            // 将登陆成功的用户保存到session中
            session.setAttribute("existAdmin", admin);

            // 当登录成功后就会重定向到成功的页面中
            resp.sendRedirect(req.getContextPath() + "/citylevel.jsp");
        } catch (NullPointerException e) {
            // 设置错误信息
            session.setAttribute("loginMSG", "😄或许是密码错误😄");

            // 登陆失败之后重新跳转回去
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }

    /**
     * 退出登录
     *
     * @param req           退出的request
     * @param resp          退出的response
     * @throws Exception    退出时的异常
     */
    public void logout(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        HttpSession session = req.getSession();

        session.removeAttribute("existAdmin");

        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    /**
     * 对管理员的操作
     *
     * @param req           操作的request
     * @param resp          操作的response
     * @throws Exception    操作的Exception
     */
    public void opreationAdmin(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String opreation = req.getParameter("opreation");
        String rangeID = req.getParameter("rangeID");
        Map<String, String[]> infos = req.getParameterMap();

        Admin admin = new Admin();
        BeanUtils.populate(admin, infos);

        // 对不同的操作进行分开处理
        if ("del".equals(opreation)) {                                          // 删除操作
            adminService.deleteAdmin(admin.getPhone());
        } else if ("add".equals(opreation)) {                                   // 添加操作
            int existAdminType =
                    Integer.parseInt(req.getParameter("existAdminType"));
            adminService.addAdmin(admin, existAdminType, rangeID);
        } else if ("update".equals(opreation)) {                                // 更新操作
            adminService.updateAdmin(admin.getID(), admin.getPhone(),
                    admin.getName(), admin.getPassword());
        } else if ("logon".equals(opreation)) {                                 // 登录操作
            logon(req, resp);

            return;
        }

        resp.sendRedirect(req.getContextPath() + "/citylevel.jsp");
    }
}