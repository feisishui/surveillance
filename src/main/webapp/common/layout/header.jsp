<%--<%@ page import="com.casic.patrol.user.domain.User" %>--%>
<%--<%@ page import="java.util.HashSet" %>--%>
<%--<%@ page import="com.casic.patrol.util.StringUtils" %>--%>
<%--<%@ page language="java" pageEncoding="UTF-8" %>--%>
<%--<script>--%>

<%--</script>--%>

<%--<div class="header navbar navbar-inverse navbar-fixed-top">--%>

    <%--<div class="back-img">--%>
        <%--<img style="height:37px" src="${ctx}/s/media/image/banner.png"/>--%>
    <%--</div>--%>

    <%--<div class="navbar-inner">--%>

        <%--<div class="container-fluid">--%>

            <%--&lt;%&ndash;<ul class="nav pull-right">&ndash;%&gt;--%>
                <%--<ul class="nav navbar-right">--%>

                    <%--<li>--%>
                        <%--<a href="${ctx}/msg/msg-info-listReceived.do">--%>
                            <%--<i class="glyphicon glyphicon-bell"></i>--%>
                            <%--<i id="unreadMsg" class="badge"></i>--%>
                        <%--</a>--%>
                    <%--</li>--%>
                <%--<li class="dropdown user">--%>

                    <%--<a href="#" class="dropdown-toggle" data-toggle="dropdown">--%>

                        <%--<img alt="" style="width: 35px;" src="${ctx}/s/media/image/avatar-1.jpg"/>--%>

                        <%--<span class="username"><%=((User) session.getAttribute(StringUtils.SYS_USER)).getUserName()%></span>--%>

                        <%--<i class="icon-angle-down"></i>--%>

                    <%--</a>--%>

                    <%--<ul class="dropdown-menu">--%>

                        <%--<li class="divider"></li>--%>

                        <%--<li><a href="${ctx}/user/sys-log-out.do"><i class="icon-key"></i> 退出</a></li>--%>

                    <%--</ul>--%>

                <%--</li>--%>

            <%--</ul>--%>

        <%--</div>--%>

    <%--</div>--%>


<%--</div>--%>
<%@ page import="com.casic.patrol.user.domain.User" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="com.casic.patrol.util.StringUtils" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<style>
    .badge {
        display: inline-block;
        padding: 2px 4px;
        font-size: 11.844px;
        font-weight: bold;
        line-height: 14px;
        color: #fff;
        text-shadow: 0 -1px 0 rgba(0,0,0,0.25);
        white-space: nowrap;
        vertical-align: baseline;
        background-color: #999;
    }
    @font-face {
        font-family: 'Glyphicons Halflings';
        src: url('${ctx}/s/bootstrap/3.3.6/fonts/glyphicons-halflings-regular.eot');
        src: url('${ctx}/s/bootstrap/3.3.6/fonts/glyphicons-halflings-regular.eot?#iefix') format('embedded-opentype'), url('${ctx}/s/bootstrap/3.3.6/fonts/glyphicons-halflings-regular.woff') format('woff'), url('${ctx}/s/bootstrap/3.3.6/fonts/glyphicons-halflings-regular.ttf') format('truetype'), url('${ctx}/s/bootstrap/3.3.6/fonts/glyphicons-halflings-regular.svg#glyphicons_halflingsregular') format('svg');
    }
    .glyphicon {
        position: relative;
        top: 1px;
        display: inline-block;
        font-family: 'Glyphicons Halflings';
        -webkit-font-smoothing: antialiased;
        font-style: normal;
        font-weight: normal;
        line-height: 1;
        -moz-osx-font-smoothing: grayscale;
    }
    .glyphicon-bell:before {
        content: "\e123";
    }
</style>
<script type='text/javascript' src='${ctx}/s/media/js/jquery-1.10.1.min.js'></script>
<script type="text/javascript">
    function unreadCount() {
        <%--alert("${ctx}");--%>
        $.getJSON('${ctx}/rs/msg/unreadCount?_sed=' + new Date().getTime(), {}, function(data) {
            if (data.data == 0) {
                $('#unreadMsg').html('');
            } else {
                $('#unreadMsg').html(data.data);
            }
        });
    }

    unreadCount();
    setInterval(unreadCount, 10000);
</script>
<div class="header navbar navbar-inverse navbar-fixed-top">
    <div class="head-img">

    </div>
    <div class="back-img">
        <img style="height:37px" src="${ctx}/s/media/image/banner.png"/>
    </div>

    <div class="navbar-inner">
        <%--<div class="navbar navbar-fixed-top">--%>

        <div class="container-fluid">
            <%--<div class="navbar-collapse collapse">--%>
            <%--<ul class="nav pull-right">--%>
            <ul class="nav navbar-nav pull-right">
                <li>
                    <a href="${ctx}/msg/msg-info-listReceived.do">
                        <i class="glyphicon glyphicon-bell"></i>
                            <%--<i class="icon-envelope"></i>--%>
                            <%--<i class="icon-compass"></i>--%>
                        <span id="unreadMsg" class="badge"></span>
                    </a>
                </li>
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
    <%--</div>--%>
</div>