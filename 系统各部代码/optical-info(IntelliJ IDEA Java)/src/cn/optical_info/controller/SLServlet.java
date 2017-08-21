package cn.optical_info.controller;

import cn.optical_info.domain.Admin;
import cn.optical_info.domain.SL;
import cn.optical_info.service.SLService;
import cn.skyfffire.other.SuperUUID;
import cn.skyfffire.web.SuperServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 路灯的Controller
 */
public class SLServlet extends SuperServlet {
    private SLService slService = new SLService();

    /**
     * 添加路灯
     *
     * @param req           添加路灯的请求
     * @param resp          添加路灯的响应
     * @throws Exception    添加路灯的异常
     */
    public void addSL(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        HttpSession session = req.getSession();
        String place = req.getParameter("place");
        int minNum = Integer.parseInt(req.getParameter("minNum"));
        int maxNum = Integer.parseInt(req.getParameter("maxNum"));
        Admin admin = (Admin) session.getAttribute("existAdmin");

        // 根据传递的参数生成一系列的路灯
        for (int i = minNum; i <= maxNum; i++) {
            String ID = SuperUUID.getUUID() + SuperUUID.getUUID();

            SL sl = new SL();

            sl.setPlace(place + i + "号路灯");
            sl.setRoadID(admin.getRoadID());
            sl.setID(ID);
            sl.setState(0);

            slService.addSL(sl);
        }

        session.setAttribute("num", 3);

        resp.sendRedirect(req.getContextPath() + "/citylevel.jsp");
    }

    /**
     * 用于更新路灯信息
     *
     * @param req           操作的request
     * @param resp          操作的response
     * @throws Exception    操作的Exception
     */
    public void opreationSL(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String ids = req.getParameter("ids");
        String opreation = req.getParameter("opreation");
        int opreationNum = 0;

        String[] idArr = ids.split("-");

        if ("open".equals(opreation)) {
            opreationNum = 1;
        } else if ("close".equals(opreation)) {
            opreationNum = 0;
        } else if ("auto".equals(opreation)) {
            opreationNum = 2;
        }

        for (String id: idArr) {
            slService.operation(id, opreationNum);
        }

        resp.sendRedirect(req.getContextPath() + "/citylevel.jsp");
    }
}
