<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "systemManagement");%>
<%pageContext.setAttribute("secondMenu", "logManagement");%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if IE 10]> <html lang="en" class="ie10"> <![endif]-->
<!--[if IE 11]> <html lang="en" class="ie11"> <![endif]-->

<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->

<head>

    <meta charset="utf-8" />

    <title>四川省地下管线监督管理子系统</title>

    <meta content="width=device-width, initial-scale=1.0" name="viewport" />

    <meta content="" name="description" />

    <meta content="" name="author" />

    <!-- BEGIN GLOBAL MANDATORY STYLES -->

    <link href="${ctx}/s/media/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/style-metro.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/style.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/blue.css" rel="stylesheet" type="text/css" id="style_color"/>

    <link href="${ctx}/s/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>

    <!-- END GLOBAL MANDATORY STYLES -->

    <!-- BEGIN PAGE LEVEL STYLES -->

    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/select2_metro.css" />

    <link rel="stylesheet" href="${ctx}/s/media/css/DT_bootstrap.css" />

    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/clockface.css" />

    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/datepicker.css" />

    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/timepicker.css" />

    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/daterangepicker.css" />

    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/datetimepicker.css" />

    <!--TODO List:此处统一换成网站的图标-->
    <%--<link rel="shortcut icon" href="${ctx}/s/media/image/favicon.ico" />--%>

    <!-- END PAGE LEVEL STYLES -->

    <style type="text/css">

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

<!-- END HEAD -->

<!-- BEGIN BODY -->

<body class="page-header-fixed">

<input id="context" type="hidden" value="${ctx}">

<!-- BEGIN HEADER -->

<%@include file="/common/layout/header.jsp"%>

<!-- END HEADER -->

<!-- BEGIN CONTAINER -->

<div class="page-container">

    <!-- BEGIN SIDEBAR -->

    <%@include file="/common/layout/menu_new.jsp"%>

    <!-- END SIDEBAR -->

    <!-- BEGIN PAGE -->

    <div class="page-content">

        <!-- BEGIN PAGE CONTAINER-->

        <div class="container-fluid">

            <div class="clearfix"></div>

            <div class="row-fluid">
                <div class="span12"></div>
            </div>

            <div class="row-fluid">

                <div class="span12">

                    <div class="portlet box blue">

                        <div class="portlet-title">

                            <div class="caption"><i class="icon-edit"></i>日志查询</div>

                            <div class="tools">

                                <a href="javascript:;" class="collapse"></a>

                            </div>

                        </div>

                        <div class="portlet-body">

                            <div class="row-fluid">

                                <div class="portlet-body">

                                    <div class="clearfix">

                                        <tr>

                                            <label>日期查询：</label>

                                        </tr>

                                        <tr>

                                            <td>

                                                <div class="m-wrap input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                                    <input id="txt_day_begin" class="m-wrap m-ctrl-medium" readonly size="16" type="text" value="" /><span class="add-on"><i class="icon-calendar"></i></span>
                                                </div>
                                                ~
                                                <div  class="m-wrap input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                                    <input id="txt_day_end" class="m-wrap m-ctrl-medium" readonly size="16" type="text" value="" /><span class="add-on"><i class="icon-calendar"></i></span>

                                                </div>

                                            <td style="border: none">

                                                <button id="btnSubmit" class="btn blue max pull-right">查询</button>

                                            </td>

                                            </td>

                                        </tr>

                                    </div>

                                    <table class="table table-striped table-hover table-bordered" id="table_log">

                                        <thead>

                                        <tr>

                                            <th>序号</th>

                                            <th>操作内容</th>

                                            <th>操作人员</th>

                                            <th>日志类型</th>

                                            <th>记录时间</th>

                                        </tr>

                                        </thead>

                                    </table>

                                </div>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </div>

    </div>

    <!-- END PAGE CONTAINER-->

    <div class="clearfix"></div>

</div>

<!-- END PAGE -->

</div>

<!-- END CONTAINER -->

<!-- BEGIN FOOTER -->

<%@include file="/common/layout/footer.jsp"%>

<!-- END FOOTER -->

<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->

<!-- BEGIN CORE PLUGINS -->

<script src="${ctx}/s/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>

<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->

<script src="${ctx}/s/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/bootstrap.min.js" type="text/javascript"></script>

<!--[if lt IE 9]>

<script src="${ctx}/s/media/js/excanvas.min.js"></script>

<script src="${ctx}/s/media/js/respond.min.js"></script>

<![endif]-->

<script src="${ctx}/s/media/js/jquery.slimscroll.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/jquery.blockui.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/jquery.cookie.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/jquery.uniform.min.js" type="text/javascript" ></script>

<!-- END CORE PLUGINS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->

<script type="text/javascript" src="${ctx}/s/media/js/select2.min.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/jquery.dataTables.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/DT_bootstrap.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/wysihtml5-0.3.0.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-wysihtml5.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/jquery.tagsinput.min.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/jquery.toggle.buttons.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-datepicker.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-datetimepicker.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/clockface.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/date.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/daterangepicker.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-timepicker.js"></script>

<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-timepicker.js"></script>

<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN PAGE LEVEL SCRIPTS -->

<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

<script src="${ctx}/s/app/log/log-info-list.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->
<script>

    $(function() {

        App.init(); // initlayout and core plugins

        logInfo.init();

    })

</script>

<!-- END JAVASCRIPTS -->

<!-- END BODY -->
</body>

</html>