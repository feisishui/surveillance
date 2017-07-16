<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu","personalCenter");%>
<%pageContext.setAttribute("secondMenu","personalCenter");%>

<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->

<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->

<!--[if IE 10]> <html lang="en" class="ie10"> <![endif]-->

<!--[if IE 11]> <html lang="en" class="ie11"> <![endif]-->

<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->

<head>
    <!--TODO LIST:修改为对应系统-->
    <title>四川省地下管线监督管理子系统</title>
    <meta content="四川省地下管线监督管理子系统" name="description" />
    <meta content="中国航天科工集团第二研究院智慧管网技术研究与发展中心" name="author" />

    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <meta content="" name="description" />
    <meta content="" name="author" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE10" content="ie=edge"/>

    <!-- 系统页面所需样式 -->
    <link rel='stylesheet' href='${ctx}/s/bootstrap/3.3.6/css/bootstrap.min.css' type='text/css' media='screen' />
    <link href="${ctx}/s/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/blue.css" rel="stylesheet" type="text/css" id="style_color"/>
    <link href="${ctx}/s/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>

    <!-- 该页面所需样式 -->
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/select2_metro.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/DT_bootstrap.css" />
    <link rel='stylesheet' type='text/css' href='${ctx}/s/portal/dashboard.css' media='screen' />
    <style type="text/css">
        .collapse {
            display: block;
        }
        ul, ol {
            padding: 0;
        }
        .navbar {
            min-height: 1px;
            border: 0px;
        }
        .container-fluid {
            padding-right: 20px;
            padding-left: 20px;
            *zoom: 1;
        }
        .back-img {
            position: absolute;
            text-align: center;
            width: 93%;
            top: 5px;
        }
        .head-img {
            background-image:url(${ctx}/s/media/image/banner_top.png);
            background-repeat: repeat-x;
            width:100%;
            position:absolute;
        }
    </style>

</head>

<body class="page-header-fixed" scroll="no">

<input id="context" type="hidden" value="${ctx}">
<input id="flashMessages" type="hidden" value="${flashMessages}">

<%@include file="/common/layout/header.jsp"%>

<div class="page-container">

    <!-- BEGIN SIDEBAR -->

    <%@include file="/common/layout/menu_new.jsp"%>

    <div class="page-content">

        <div class="container-fluid">

            <div class="clearfix"></div>
            <div data-height="300" class="container-fluid dashboard dashboard-draggable" id="dashboard" style="margin-top:30px;">
                <header></header>
                <section class="row">
                    <c:forEach items="${map}" var="entry">
                        <div class="portal-col col-md-6 col-sm-12" data-id="${entry.key}" data-order="${entry.key}">
                            <c:forEach items="${entry.value}" var="item">
                                <div data-id="${item.id}" class="portlet" data-order="${item.rowIndex}">
                                    <div data-url="${tenantPrefix}${item.portalWidget.url}" class="panel panel-default" id="panel${item.id}" data-id="${item.id}">
                                        <div class="panel-heading">
                                            <div class="panel-actions">
                                                <%--<button class="btn btn-sm refresh-panel"><i class="glyphicon glyphicon-refresh"></i></button>--%>
                                                <%--<div class="dropdown">--%>
                                                    <%--<button data-toggle="dropdown" class="btn btn-sm" role="button"><span class="caret"></span></button>--%>
                                                    <%--<ul aria-labelledby="dropdownMenu1" role="menu" class="dropdown-menu">--%>
                                                        <%--<li><a class="remove-panel" href="#"><i class="glyphicon glyphicon-remove"></i> 移除</a></li>--%>
                                                    <%--</ul>--%>
                                                <%--</div>--%>
                                            </div>
                                            <i class="glyphicon glyphicon-list"></i> ${item.name}
                                        </div>
                                        <div class="panel-body">
                                            <table class="table table-hover">
                                                <thead>
                                                <tr>
                                                    <th>编号</th>
                                                    <th>名称</th>
                                                    <th>创建时间</th>
                                                    <th>&nbsp;</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${personalTasks.result}" var="item">
                                                    <tr>
                                                        <td>${item.id}</td>
                                                        <td>${item.name}</td>
                                                        <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                        <td>
                                                            <a href="${tenantPrefix}/operation/task-operation-viewTaskForm.do?humanTaskId=${item.id}" class="btn btn-xs btn-primary">处理</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:forEach>

                </section>
            </div>
            <div class="clearfix"></div>

        </div>

    </div>

    <div class="clearfix"></div>

</div>

<%@include file="/common/layout/footer.jsp"%>

<!-- 系统页面所需js文件 -->
<script src="${ctx}/s/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>
<script src="${ctx}/s/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script src="${ctx}/s/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>
<script src="${ctx}/s/media/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

<!-- 该页面所需js文件 start-->
<script type='text/javascript' src='${ctx}/s/portal/dashboard.js'></script>
<script type="text/javascript" src="${ctx}/s/portal/portal.js"></script>
<script type="text/javascript" src="${ctx}/s/table/table.js"></script>
<script type="text/javascript" src="${ctx}/s/table/messages_${locale}.js"></script>
<!-- 该页面所需js文件 end-->
<script>

    $(function(){
        App.init();
        var message = $("#flashMessages").val();
        if (message != null && message != undefined && message.length > 0) {
            alert(message);
        }
    });

    var logout = function(){
        window.location.href='${ctx}/user/sys-log-out.do';
    };

</script>
</body>
</html>
