<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="cn.optical_info.domain.Admin" %>
<%@ page import="cn.optical_info.domain.Range" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.optical_info.service.AdminService" %>
<%@ page import="cn.optical_info.domain.SL" %>
<%@ page import="cn.optical_info.service.RangeService" %>
<%@ page import="cn.optical_info.service.SLService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 渲染准备 -->
<%
    Admin existAdmin = (Admin) session.getAttribute("existAdmin");

    // 用户必须要登录才能进入这个页面
    if (existAdmin == null) {
        session.setAttribute("loginMSG", "😁请登录后再操作😁");

        existAdmin = new Admin();        // 防止空指针

        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    AdminService adminService = new AdminService();
    SLService slService = new SLService();
    RangeService rangeService = new RangeService();

    // 统一在此处更新信息
    session.setAttribute("slList", slService.getAllSL(existAdmin));
    if (existAdmin.getType() < 3) {
        session.setAttribute("adminList", adminService.getAllAdmin(existAdmin));
        session.setAttribute("rangeList", rangeService.getRanges(existAdmin));
    }
%>

<!DOCTYPE html>
<html lang="en" style="font-family: '微软雅黑 Light'">
    <head>
		<meta charset="UTF-8" />
        <title>智敏云路灯系统管理平台</title>
        <link rel="stylesheet" href="css/citylevel.css" />
        <script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
        <script type="text/javascript" src="js/citylevel.js" ></script>

    </head>
    <body>
        <div class="container">


			<div class="st-container">

				<input type="radio" name="radio-set" checked="checked" id="st-control-1"/>
				<a href="#st-panel-1">路灯信息</a>
				<input type="radio" name="radio-set" id="st-control-2"/>
				<a href="#st-panel-2">路灯管理</a>
				<input type="radio" name="radio-set" id="st-control-3"/>
                <c:choose>
                    <c:when test="${sessionScope.existAdmin.type == 3 }">
                        <a href="#st-panel-4">添加路灯</a>
                    </c:when>
                    <c:otherwise>
                        <a href="#st-panel-4">人员管理</a>
                    </c:otherwise>
                </c:choose>

				<p id="welcome">欢迎您，
					<%=existAdmin.getName() %> |
                    <a href="${pageContext.request.contextPath}/AdminServlet?method=logout">退出</a>
				</p>

				<div class="st-scroll">
                    <!-- 路灯总体信息 -->
					<section class="st-panel" id="st-panel-1">
						<div class="st-deco" data-icon="
							G"></div>
						<h2>在<%=existAdmin.getRangeName()%>这片区域中：</h2>
						<p style="color: white">需要维修:<input style="color:#1E90FF" type="text" value="<%=slService.getSLState(existAdmin, 0) %>"
                                      readonly="readonly" class="case"/>台</p><br /><br /><br />
						<p style="color: white">状态良好:<input style="color:#1E90FF" type="text" value="<%=slService.getSLState(existAdmin, 1) %>"
                                      readonly="readonly" class="case"/>台</p>
					</section>
					<!-- 路灯详细信息 -->
					<section class="st-panel st-color" id="st-panel-2">
						<div class="st-deco" data-icon="_"></div>
                        <form action="<%=request.getContextPath()%>/SLServlet" method="post">
                            <input type="hidden" name="method" value="opreationSL" />
                            <input type="hidden" id="sl_ids" name="ids" value="" />
                            <input type="hidden" id="opreationHidden" name="opreation" value="" />

                            <div>
                                <input type="button" id="check*" value="全选" class="check"/>
                                <input type="button" id="checkf" value="反选" class="check"/>
                                <input type="text" id="checks" placeholder="请输入你需要查找的信息" name="ck" oninput="filter()" />
                                <input type="button" value="开" class="chk_btn" id="chk_open" />
                                <input type="button" value="自动" class="chk_btn" id="chk_auto" />
                                <input type="button" value="关" class="chk_btn" id="chk_close" />

                                </div>
                                <div id="check_box" style="height: 450px;overflow-y: auto;width: 600px;">
                                    <div id="chk_input">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.slList }">
                                                <!-- 遍历路灯 -->
                                                <table  bgcolor="white" id="tb" style="border: 1px solid deepskyblue">
                                                <tr style="font-weight: bold;">
                                                    <td style="width: 74px" class="ts"></td>
                                                    <td  style="width: 232px;" class="ts">详细地址</td>
                                                    <td style="width:142px;" class="ts">状态</td>
                                                    <td class="ts">当前模式</td>
                                                </tr>
                                                </table>
                                                <table   bgcolor="white" id="tab" style="border: 2px solid deepskyblue">
                                                    <c:forEach items="${sessionScope.slList }" var="sl">
                                                        <tr>
                                                            <td><input type="checkbox" value="${sl.ID }" style="background: white" class="chk" name="hh"/></td>
                                                            <td>${sl.place }</td>
                                                            <td>${sl.getStateString() }</td>
                                                            <td>${sl.getOpreationString() }</td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </c:when>
                                        <c:otherwise>
                                            数据为空
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <input type="submit" id="opreation_submit" style="display: none;" />
                        </form>
					</section>

                    <!-- 人员信息及路灯添加 -->
                    <section class="st-panel" id="st-panel-3">
                        <c:choose>
                            <c:when test="${sessionScope.existAdmin.type == 3 }">
                                <div class="add">
                                    <p1>请输入需要添加的路灯详细名字，然后在路灯通讯工具中与具体路灯绑定起来：<br /><br />
                                        以例子的形式填写将会生成以下路灯：<br />
                                        裕兴路1号路灯<br />
                                        裕兴路2号路灯<br />
                                        裕兴路3号路灯<br />
                                        裕兴路4号路灯<br /></p1>
                                    <form action="<%=request.getContextPath()%>/SLServlet" method="post">
                                        <input type="hidden" name="method" value="addSL" />
                                        <input type="text" placeholder="如：裕兴路" name="place" class="add_input"/>
                                        <input type="text" placeholder="如：1号" name="minNum" class="add_input" />
                                        <p2>-</p2><input type="text" placeholder="如：4号" name="maxNum" class="add_input" /><br />

                                        <input type="submit" value="确认无误，提交路灯信息" id="add_btn"/><br />
                                    </form>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="st-deco" data-icon="K"></div>
                                <form action="<%=request.getContextPath()%>/AdminServlet" method="post">
                                    <input type="hidden" name="method" value="opreationAdmin" />
                                    <input type="hidden" name="opreation" value="add" />
                                    <input type="hidden" name="existAdminType"
                                           value="<% out.print(existAdmin.getType()); %>" />
                                    <h2 style="top: 20%">添加新的管理员：</h2>
                                    <div id="per">

                                        <input name="name" type="text" id="input_name" placeholder="姓名" />
                                        <input name="phone" type="text" id="input_ph" placeholder="电话" />
                                        <input name="password" type="password" placeholder="密码" />
                                        <select name="rangeID">
                                            <option>选择负责区域</option>
                                            <%
                                                List<Range> ranges = rangeService.getRanges(existAdmin);

                                                for (Range r : ranges) {
                                                    out.print("<option value='" + r.getID() + "'>" + r.getName() + "</option>");
                                                }
                                            %>
                                        </select>
                                    </div>
                                    <input type="submit" id="input_submit" value="添加（确认无误，执行）"/>
                                </form>
                                <h2>当前管理员列表：</h2>
                                <input type="text" placeholder="请输入你需要查找的信息" id="gl_cx" name="gl" oninput="ft()" />
                                <div id="gl_box" style="overflow-y: auto;height: 500px;width: 628px">
                                    <table id="gl_list">
                                    <tr bgcolor="white">
                                        <th style="width: 110px">姓名</th>
                                        <th style="width: 110px">电话号码</th>
                                        <th style="width: 110px">密码</th>
                                        <th style="width: 110px">类型</th>
                                        <th style="width: 110px">负责区域</th>
                                        <th>操作</th>
                                    </tr>
                                    </table>
                                    <!-- 当前管理员 -->
                                    <table id="table_gl" >

                                    <form action="<%=request.getContextPath()%>/AdminServlet" method="post">
                                        <input type="hidden" name="ID" value="${sessionScope.existAdmin.ID}" />
                                        <input type="hidden" name="method" value="opreationAdmin" />
                                        <input type="hidden" name="opreation" value="update" />

                                        <tr bgcolor="white">
                                            <th><input class="adminInfo" name="name" value="${sessionScope.existAdmin.getName()}" style="text-align: center;"/> </th>
                                            <th><input class="adminInfo" name="phone" value="${sessionScope.existAdmin.getPhone()}" /></th>
                                            <th><input class="adminInfo" name="password" value="${sessionScope.existAdmin.getPassword()}" /></th>
                                            <th><input class="adminInfo" disabled="disabled" name="type" value="${sessionScope.existAdmin.getTypeName()}" /></th>
                                            <th><input class="adminInfo" disabled="disabled" value="${sessionScope.existAdmin.getRangeName()}" /></th>
                                            <th>
                                                <input id="submit" type="submit" value="更新" class="tr_btn" />
                                            </th>
                                        </tr>
                                    </form>

                                    <!-- 其他管理员 -->
                                    <c:forEach items="${sessionScope.adminList }" var="admin">
                                        <form action="<%=request.getContextPath()%>/AdminServlet" method="post">
                                            <input type="hidden" name="ID" value="${admin.ID}" />
                                            <input type="hidden" name="method" value="opreationAdmin" />
                                            <input id="oprea${admin.phone}" type="hidden" name="opreation" />
                                            <tr bgcolor="white">
                                                <th><input class="adminInfo" name="name" value="${admin.getName()}" style="text-align: center;"/> </th>
                                                <th><input class="adminInfo" name="phone" value="${admin.getPhone()}" /></th>
                                                <th><input class="adminInfo" name="password" value="${admin.getPassword()}" /></th>
                                                <th><input class="adminInfo" disabled="disabled" name="type" value="${admin.getTypeName()}" /></th>
                                                <th><input class="adminInfo" disabled="disabled" value="${admin.getRangeName()}" /></th>
                                                <th>
                                                    <input type="button" value="更新"
                                                           onclick="javscript:document.getElementById('submit${admin.phone}').click();"
                                                           onmouseenter="changeOpreation('update', ${admin.phone})" class="tr_btn" /><br />
                                                    <input type="button" value="删除"
                                                           onclick="javscript:document.getElementById('submit${admin.phone}').click();"
                                                           onmouseenter="changeOpreation('del', ${admin.phone})" class="tr_btn"/><br />
                                                    <input type="button" value="登录" class="tr_btn"
                                                           onclick="javscript:document.getElementById('submit${admin.phone}').click();"
                                                           onmouseenter="changeOpreation('logon', ${admin.phone})" />

                                                    <input id="submit${admin.phone}" type="submit" style="display: none;" />
                                                </th>
                                            </tr>
                                        </form>
                                    </c:forEach>
                                </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </section>
				</div>
			</div>
        </div>
	</body>
</html>