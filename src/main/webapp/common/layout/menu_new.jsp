<%@ taglib prefix="region" uri="http://www.casic203.com/region/tags" %>
<%@ page language="java" pageEncoding="UTF-8" %>

<!-- BEGIN SIDEBAR -->

<div class="page-sidebar nav-collapse collapse">

    <ul class="page-sidebar-menu">

        <li>

            <!-- BEGIN SIDEBAR TOGGLER BUTTON -->

            <div class="sidebar-toggler hidden-phone"></div>

            <!-- BEGIN SIDEBAR TOGGLER BUTTON -->

        </li>

        <region:region-permission permission="运行总览:read" region="0">
            <li class="${currentMenu == 'start' ? 'active' : ''}">

                <a href="javascript:;">
                    <i class="icon-home"></i>
                    <span class="title">运行总览</span>
                    <span class="selected"></span>
                    <span class="arrow ${currentMenu == 'start' ? 'open' : ''}"></span>
                </a>

                <ul class="sub-menu">
                    <li class = "${secondMenu == 'statistics' ? 'active' : ''}">
                        <a href="${ctx}/bpm/home.do">
                            统计分析</a>
                    </li>
                    <%--<li class = "${secondMenu == 'evaluateRule' ? 'active' : ''}">--%>
                        <%--<a href="${ctx}/content/evaluate/indicator-info-list.jsp">--%>
                            <%--评价规则--%>
                        <%--</a>--%>
                    <%--</li>--%>
                    <li class = "${secondMenu == 'evaluate' ? 'active' : ''}">
                        <a href="javascript:;">
                            考核评价
                            <span class="arrow ${secondMenu == 'start' ? 'open' : ''}"></span>
                        </a>
                        <ul class="sub-menu">
                            <li class = "${thirdMenu == 'yearEvaluate' ? 'active' : ''}">
                                <a href="${ctx}/evaluation/evaluation-info.do?department=指挥中心&type=年度考核">
                                    年度考核查询</a>
                            </li>
                            <li class = "${thirdMenu == 'quarterEvaluate' ? 'active' : ''}">
                                <a href="${ctx}/evaluation/evaluation-info.do?department=指挥中心&type=季度考核">
                                    季度考核查询</a>
                            </li>
                            <li class = "${thirdMenu == 'monthEvaluate' ? 'active' : ''}">
                                <a href="${ctx}/evaluation/evaluation-info.do?department=指挥中心&type=月度考核">
                                    月度考核查询</a>
                            </li>
                            <li class = "${thirdMenu == 'anyEvaluate' ? 'active' : ''}">
                                <a href="${ctx}/evaluation/evaluation-info.do?department=指挥中心&type=任意周期考核">
                                    任意周期查询</a>
                            </li>
                        </ul>
                    </li>
                    <li class = "${secondMenu == 'bpm-process' ? 'active' : ''}">
                        <a href="${ctx}/bpm/workspace-listRunningProcessInstances.do">
                            流程督办</a>
                    </li>
                    <li class = "${secondMenu == 'time-manage' ? 'active' : ''}">
                        <a href="${ctx}/humantask/workspace-historyInstances.do">
                            计时管理</a>
                    </li>
                    <li class = "${secondMenu == 'trace' ? 'active' : ''}">
                        <a href="javascript:;">
                            轨迹查询
                            <span class="arrow ${secondMenu == 'start' ? 'open' : ''}"></span>
                        </a>
                        <ul class="sub-menu">
                            <li class = "${thirdMenu == 'personTrace' ? 'active' : ''}">
                                <a href="${ctx}/position/workspace-historyTrace.do">
                                    人员轨迹</a>
                            </li>
                            <li class = "${thirdMenu == 'taskTrace' ? 'active' : ''}">
                                <a href="${ctx}/position/workspace-taskTrace.do">
                                    任务轨迹</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
        </region:region-permission>

        <region:region-permission permission="个人中心:read" region="0">
            <li class="${currentMenu == 'personalCenter' ? 'active' : ''}">

                <a href="javascript:;">
                    <i class="icon-group"></i>
                    <span class="title">个人中心</span>
                    <span class="selected"></span>
                    <span class="arrow ${currentMenu == 'personalCenter' ? 'open' : ''}"></span>
                </a>

                <ul class="sub-menu">

                    <%--<li class = "${secondMenu == 'personalCenter' ? 'active' : ''}">--%>
                        <%--<a href="${ctx}/portal/index.do">我的流程</a>--%>
                    <%--</li>--%>
                <region:region-permission permission="专业公司待办任务:read" region="0">
                    <li class = "${secondMenu == 'myTasks' ? 'active' : ''}">
                        <a href="${ctx}/todotask/workspace-toDoTasks.do">待办任务</a>
                    </li>
                </region:region-permission>

                 <region:region-permission permission="指挥中心待办任务:read" region="0">

                    <li class = "${secondMenu == 'agency' ? 'active' : ''}">
                        <a href="javascript:;">待办任务<span class="arrow"></span></a>

                        <ul class="sub-menu">

                            <li class = "${thirdMenu == 'to_assign' ? 'active' : ''}"><a href="${ctx}/todotask/workspace-toBeAssignedTasks.do">待指派</a></li>

                            <li class = "${thirdMenu == 'to_record' ? 'active' : ''}"><a href="${ctx}/todotask/workspace-toBeRecordedTasks.do">待立案</a></li>

                            <li class = "${thirdMenu == 'to_finish' ? 'active' : ''}"><a href="${ctx}/todotask/workspace-toBeFinishedTasks.do">待结案</a></li>

                        </ul>
                    </li>
                    </region:region-permission>

                    <li class = "${secondMenu == 'start' ? 'active' : ''}">
                        <a href="${ctx}/humantask/workspace-historyTasks.do">
                            已办任务</a>
                    </li>
                    <li class = "${secondMenu == 'msg' ? 'active' : ''}">
                        <a href="${ctx}/msg/msg-info-listReceived.do">
                            个人消息</a>
                    </li>

                </ul>

            </li>

        </region:region-permission>

        <region:region-permission permission="系统配置:read" region="0">
            <li class="${currentMenu == 'systemManagement' ? 'start active' : ''}">

                <a href="javascript:;">
                    <i class="icon-cogs"></i>
                    <span class="title">系统配置</span>
                    <span class="selected"></span>
                    <span class="arrow ${currentMenu == 'systemManagement' ? 'open' : ''}"></span>
                </a>

                <ul class="sub-menu">
                    <li class = "${secondMenu == 'userManagement' ? 'active' : ''}">
                        <a href="${ctx}/content/user/user-info-list.jsp">
                            用户管理</a>
                    </li>
                    <li class = "${secondMenu == 'overtimeManagement' ? 'active' : ''}">
                        <a href="${ctx}/content/overtime/overtime-info-list.jsp">
                            超时管理</a>
                    </li>
                    <li class = "${secondMenu == 'logManagement' ? 'active' : ''}">
                        <a href="${ctx}/content/log/log-info-list.jsp">
                            日志管理</a>
                    </li>
                    <li class = "${secondMenu == 'start' ? 'active' : ''}">
                        <a href="${ctx}/party/org-list.do">
                            组织结构管理</a>
                    </li>
                </ul>
            </li>
        </region:region-permission>

    </ul>
</div>

<!-- END SIDEBAR MENU -->
