<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu","start");%>
<%pageContext.setAttribute("secondMenu","trace");%>
<%pageContext.setAttribute("thirdMenu","taskTrace");%>

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
<%--
    <link href="${ctx}/s/media/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
--%>
    <link rel='stylesheet' href='${ctx}/s/bootstrap/3.3.6/css/bootstrap.min.css' type='text/css'/>
    <link href="${ctx}/s/media/css/BootSideMenu.css" rel="stylesheet" type="text/css"/>
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
    <link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.4.3/themes/bootstrap/easyui.css" >
    <link rel="stylesheet" type="text/css" href="${ctx}/s/xform3/styles/xform.css">

    <style type="text/css">

        .mini{
            height: 22px;
        }
        .label{
            font-weight: 100 !important;
            font-size: 14px !important;
        }
        .input{
            border: 1px solid #ccc;
            padding: 4px 6px;
        }
        .collapse {
            display: block;
        }
        ul, ol {
            padding: 0;
        }

        .collapse {
            display: block;
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
       /* #BD_container > img {
            position: relative !important;
        }*/

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

            <div class="row-fluid">

                <div >
                    <div class="space10"></div>
                    <div class="space5"></div>
                    <!-- start of main -->
                    <div style="width:100%;border:0px solid gray;height: 400px;" id="BD_container"></div>
                    <!-- end of main -->
                </div>
                <div style="width: 550px;margin-top: 60px;margin-bottom: 35px;" id="rightTableDiv">
                    <div class="space10"></div>
                    <div class="portlet solid bordered light-grey" id="divForTaskTrace">

                        <div class="portlet-title">
                            <div class="caption"><i class="icon-globe"></i>任务信息</div>
                        </div>

                        <div class="portlet-body" >
                            <table class="table table-striped table-bordered table-advance table-hover table-full-width" id="taskTraceTable">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>流程号</th>
                                    <th>名称</th>
                                    <th>核实人ID</th>
                                    <th width="60">核实人</th>
                                    <th>下发时间</th>
                                    <th>核实时间</th>
                                    <th width="55">轨迹</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>

                    </div>

                </div>

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
<script src="${ctx}/s/media/js/BootSideMenu.js" type="text/javascript"></script>

<%--<script src="${ctx}/s/media/js/jquery.dataTables.min.js" type="text/javascript"></script>--%>
<script type="text/javascript" src="${ctx}/s/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="${ctx}/s/media/js/DT_bootstrap.js"></script>
<script src="${ctx}/s/media/js/select2.min.js" type="text/javascript"></script>
<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctx}/s/app/esri/EsriControl.js"></script>
<script type="text/javascript" src="${ctx}/s/app/bpm/workspaceTaskTrace.js"></script>
<script type="text/javascript" src="http://api.tianditu.com/js/maps.js"></script>
<script type="text/javascript" src="http://www.scgis.net/SCGCMap/OpenLayers-scgisExtension-2.12.03.js"></script>

<script type="text/javascript" src="${ctx}/s/xform3/xform-all.js"></script>

<script>

    $(function(){
        App.init();
        TaskTrace.init();
        TaskTrace.initTable();

      /*  $('#BD_container').find('img').css({
            position:relative
        })*/
    });

    $('#rightTableDiv').BootSideMenu({

        side:"right", // left or right

        autoClose:false // auto close when page loads

    });

    var logout = function() {
        window.location.href='${ctx}/user/sys-log-out.do';
    }

</script>
</body>
</html>


<%--<div align="left" id="BD_container"
     style='background: url("http://api.tianditu.com/img/map/bgImg.gif");
      height: 581px; overflow: hidden;
      position: relative; cursor: default;'>

    <div id="platform" style="left: 550.5px; top: 290.5px;
     overflow: visible; position: absolute; z-index: 100; cursor: default;">

        <div id="t_maskDiv" style='left: -550.5px; top: -290.5px; width: 1101px;
        height: 581px; position: absolute; z-index: 180;
        background-image: url("http://api.tianditu.com/img/map/mask.gif");'>
        </div>

        <div style="position: absolute; z-index: 1;">
            <div id="mapsDiv_11" style="position: absolute; z-index: 100;">--%>

<%--
<div id="t_overlaysDiv" style="width: 1101px; height: 577px; position: absolute; z-index: 500;"></div>--%>
