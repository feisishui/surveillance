<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu","start");%>
<%pageContext.setAttribute("secondMenu","trace");%>
<%pageContext.setAttribute("thirdMenu","personTrace");%>


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
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/datepicker.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/select2_metro.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/DT_bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.4.3/themes/bootstrap/easyui.css" >
    <link rel="stylesheet" type="text/css" href="${ctx}/s/xform3/styles/xform.css">

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
        .col-md-10{
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
        .table th, .table td {
            line-height: 10px;
            font-size: small;
        }
        .datepicker {
            z-index: 999 !important;
        }

    </style>


</head>

<body class="page-header-fixed" scroll="no" style="overflow-x: hidden;">

<input id="context" type="hidden" value="${ctx}">

<%@include file="/common/layout/header.jsp"%>

<div class="page-container">

    <!-- BEGIN SIDEBAR -->

    <%@include file="/common/layout/menu_new.jsp"%>

    <div class="page-content">

        <div class="container-fluid">
            <div class="clearfix"></div>
            <div class="row-fluid" style="height: 10px"></div>
            <table width="60%">
                <tr>
                    <td  colspan="3">
                        巡检人员：
                        <select class="chosen" data-with-diselect="1" tabindex="1" name="partyEntity" id="patrols">
                            <c:forEach items="${users}" var="item" varStatus="s" >
                                <% boolean selected = false;%>
                                <c:if test="${s.first}">
                                    <% selected = true;%>
                                </c:if>
                                <option value="${item.id}" selected="<%=selected%>">${item.userName}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>&#8195;&#8195;&#8195;&#8195;&#8195;&#8195;</td>
                    <td>巡检日期：</td>
                    <td colspan="3">
                        <div class="m-wrap input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                            <input id="txt-date" class="m-wrap m-ctrl-medium" readonly size="16" type="text" value="" /><span class="add-on"></span>
                        </div>
                    </td>
                    <td>&#8195;&#8195;&#8195;&#8195;&#8195;&#8195;</td>
                    <td>
                        <button type="button" id="searchButton" onclick="flash();">查询</button>
                    </td>
                </tr>
            </table>
            <%--<div class="row-fluid">--%>
                <%--</div>--%>
                    <!-- start of main -->
                    <div style="width:100%;border:0px solid gray;height: 400px;" id="BD_container"></div>
                    <!-- end of main -->
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

<%--<script src="${ctx}/s/media/js/jquery.dataTables.min.js" type="text/javascript"></script>--%>
<%--<script src="${ctx}/s/media/js/select2.min.js" type="text/javascript"></script>--%>
<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-datepicker.js"></script>
<%--<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-datetimepicker.js"></script>--%>
<script type="text/javascript" src="${ctx}/s/app/esri/EsriControl.js"></script>
<script type="text/javascript" src="${ctx}/s/app/bpm/workspaceHistoryTrace.js"></script>
<script type="text/javascript" src="http://api.tianditu.com/js/maps.js"></script>
<script type="text/javascript" src="http://www.scgis.net/SCGCMap/OpenLayers-scgisExtension-2.12.03.js"></script>
<%--<script type="text/javascript" src="${ctx}/s/app/esri/ControlManager.js"></script>--%>

<script type="text/javascript" src="${ctx}/s/xform3/xform-all.js"></script>

<script>

    $(function(){
        App.init();
        HistoryTrace.init();
    });
    var logout = function() {
        window.location.href='${ctx}/user/sys-log-out.do';
    }

</script>
</body>
</html>
