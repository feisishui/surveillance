<%--
  Created by IntelliJ IDEA.
  User: yxw
  Date: 2015/6/25
  Time: 13:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--Start secord level title-->

<!--TODO LIST:修改成自己的子模块名称-->
<div class="row-fluid">

    <div class="span12">

        <div class="m-spacer"></div>
        <div class="space20"></div>

        <ul class="breadcrumb">

            <li>

                <i class="icon-plus"></i>

                <%--<a id="add-construct-menu" href="#">新增施工</a>--%>
                <a href="${ctx}/content/service/service-manage.jsp">新增施工</a>
            </li>

            <li>

                <i class="icon-cogs"></i>

                <a href="${ctx}/content/system/construct-history-list.jsp">施工问题反馈</a>

            </li>

        </ul>

    </div>

</div>

<!--End secord level title-->

