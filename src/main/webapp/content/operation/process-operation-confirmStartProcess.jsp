<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu","personalCenter");%>

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
    <link href="${ctx}/s/media/css/style-metro.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/blue.css" rel="stylesheet" type="text/css" id="style_color"/>
    <link href="${ctx}/s/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>

    <!-- 该页面所需样式 -->
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/select2_metro.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/DT_bootstrap.css" />
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
        .col-md-10 {
            width: 100%;
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

    <%--<link rel="shortcut icon" href="${ctx}/s/media/image/favicon.ico" />--%>

    <!-- 系统页面所需js文件 -->
    <script src="${ctx}/s/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

</head>

<body class="page-header-fixed" scroll="no">

<input id="context" type="hidden" value="${ctx}">

<%@include file="/common/layout/header.jsp"%>

<div class="page-container">

    <!-- BEGIN SIDEBAR -->

    <%@include file="/common/layout/menu_new.jsp"%>

    <div class="page-content">

        <div class="container-fluid">

            <div class="clearfix"></div>

            <div class="row-fluid">

                <div class="span12">
                    <!-- start of main -->
                    <section id="m-main" class="col-md-10" style="padding-top:45px;">

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                确认发起流程
                            </div>

                            <div class="panel-body">

                                <form id="demoForm" method="post" action="process-operation-startProcessInstance.do" class="form-horizontal">
                                    <input id="demo_id" type="hidden" name="bpmProcessId" value="${bpmProcessId}">
                                    <input type="hidden" name="businessKey" value="${businessKey}">
                                    <div class="control-group">
                                        <div class="controls">
                                            <button id="submitButton" type="submit" class="btn btn-default">发起流程</button>
                                            &nbsp;
                                            <button type="button" onclick="history.back();" class="btn btn-link">返回</button>
                                        </div>
                                    </div>
                                </form>

                            </div>
                        </div>

                    </section>
                    <!-- end of main -->
                </div>

                <div class="clearfix"></div>

            </div>

        </div>

        <div class="clearfix"></div>

    </div>

    <%@include file="/common/layout/footer.jsp"%>


    <script type="text/javascript">

        $(function() {

            App.init();
            var ss = $(".page-sidebar .nav-collapse .collapse").css('display');
            $(".page-sidebar.nav-collapse.collapse").css('display','block');
        });

    </script>

</body>
</html>
