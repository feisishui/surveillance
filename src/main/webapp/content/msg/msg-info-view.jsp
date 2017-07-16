<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/taglibs.jsp" %>
<%pageContext.setAttribute("currentMenu", "personalCenter");%>
<%pageContext.setAttribute("secondMenu", "msg");%>
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8"/>
    <title>四川省地下管线监督管理子系统</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>

    <meta content="" name="description"/>

    <meta content="" name="author"/>

    <!-- 系统页面所需样式 -->
    <link rel='stylesheet' href='${ctx}/s/bootstrap/3.3.6/css/bootstrap.min.css' type='text/css' media='screen'/>
    <link href="${ctx}/s/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/blue.css" rel="stylesheet" type="text/css" id="style_color"/>
    <link href="${ctx}/s/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>

    <!-- 该页面所需样式 -->
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/select2_metro.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/DT_bootstrap.css"/>
    <link rel='stylesheet' type='text/css' href='${ctx}/s/portal/dashboard.css' media='screen'/>
    <link rel="stylesheet" type="text/css" href="${ctx}/s/userpicker3-v2/userpicker.css">
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

<body class="page-header-fixed">
<input id="context" type="hidden" value="${ctx}">

<%@include file="/common/layout/header.jsp" %>

<div class="page-container">
    <%@include file="/common/layout/menu_new.jsp" %>
    <div class="page-content">

        <div class="row-fluid" class="col-md-10" style="margin-left: 20px">
            <div class="span12">
                <!-- start of main -->
                <section id="m-main" class="col-md-10" style="margin-top:65px;">

                    <div class="panel panel-default">

                        <div class="panel-body">

                            <p><span class="label label-default"><tags:user userId="${model.senderId}"/></span> <span class="label label-default"><fmt:formatDate value="${model.createTime}" type="both"/></span></p>

                            <p>${model.content}</p>

                        </div>
                    </div>

                </section>
                <!-- end of main -->
            </div>
        </div>
    </div>

    <%@include file="/common/layout/footer.jsp" %>

    <!-- 系统页面所需js文件 -->
    <script src="${ctx}/s/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

    <!-- 该页面所需js文件 start-->
    <script type='text/javascript' src='${ctx}/s/portal/dashboard.js'></script>
    <script type="text/javascript" src="${ctx}/s/portal/portal.js"></script>
    <script type="text/javascript" src="${ctx}/s/pagination/pagination.js"></script>
    <script type="text/javascript" src="${ctx}/s/table/table.js"></script>
    <script type="text/javascript" src="${ctx}/s/table/messages_${locale}.js"></script>
    <script type="text/javascript" src="${ctx}/s/userpicker3-v2/userpicker.js"></script>

    <script type="text/javascript">
        App.init();
    </script>

</body>

</html>
