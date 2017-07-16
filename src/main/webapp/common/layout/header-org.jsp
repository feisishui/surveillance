<%@ page import="com.casic.patrol.user.domain.User" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="com.casic.patrol.util.StringUtils" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<script>

</script>

<div class="header navbar navbar-inverse navbar-fixed-top">

    <div class="head-img">

    </div>

    <div class="back-img">
        <img style="height:37px" src="${ctx}/s/media/image/banner.png"/>
    </div>

    <div class="navbar-inner">

        <div class="container-fluid">

           <ul class="nav pull-right">
                <li class="dropdown user">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img alt="" style="width: 35px;" src="${ctx}/s/media/image/avatar-1.jpg"/>
                        <span class="username"><%=((User) session.getAttribute(StringUtils.SYS_USER)).getUserName()%></span>
                        <i class="icon-angle-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="divider"></li>
                        <li><a href="${ctx}/user/sys-log-out.do"><i class="icon-key"></i> 退出</a></li>
                    </ul>
                </li>
            </ul>

        </div>

    </div>


</div>
