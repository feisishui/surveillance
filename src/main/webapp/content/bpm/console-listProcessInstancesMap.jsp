<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu","start");%>
<%pageContext.setAttribute("secondMenu","statistics");%>
<%pageContext.setAttribute("regionname", "${regionname}");%>

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
    <link href="${ctx}/s/media/css/BootSideMenu.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
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
                    <div class="space20">
                        <input type="button" id="addploygen" value="增加区域" onclick="DrawLayer();">
                        <input type="button" id="delploygen" value="删除区域" onclick="DeleteRegion();">
                        <ul class="nav navbar-nav pull-right">
                            <li>
                                <input type="text" id="searchName" style="width: 200px; padding-bottom: 8px"/>
                            </li>
                            <li>
                                <button type="button" id="searchButton" onclick="flash();">查询</button>
                            </li>
                        </ul>
                    </div>
                    <div class="space5"></div>
                    <!-- start of main -->
                    <div style="width:100%;border:0px solid gray;height: 400px;" id="BD_container"></div>
                    <!-- end of main -->
                </div>
                <div style="width: 350px;margin-top: 85px;margin-bottom: 35px;" id="demo">
                    <div class="space10"></div>
                    <div class="portlet solid bordered light-grey" id="divForRegion">

                        <div class="portlet-title">
                            <div class="caption"><i class="icon-globe"></i>区域信息</div>
                        </div>

                        <div class="portlet-body" id="setForClearTable">
                        <table class="table table-striped table-bordered table-advance table-hover table-full-width" id="regionTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>名称</th>
                                    <th>事件数</th>
                                    <th>扩展信息</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                        </div>

                    </div>

                    <div class="portlet solid bordered light-grey" id="divForDetails" style="display: none;">

                        <div class="portlet-title">
                            <div class="caption"><i class="icon-globe"></i>事件描述</div>
                            <div class="pull-right" style="font-size: 16px;font-weight: 400;" onclick="TableAdvanced.loadTable();"><input type="button" style="color: #000000;" value="返回"/></div>

                        </div>

                        <div class="portlet-body" id="form-table-div">
                            <div id="xf-form-table"></div>
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

<script src="${ctx}/s/media/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="${ctx}/s/media/js/select2.min.js" type="text/javascript"></script>
<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctx}/s/app/esri/EsriControl.js"></script>
<script type="text/javascript" src="${ctx}/s/app/bpm/console-listProcessInstancesMap.js"></script>
<script type="text/javascript" src="http://www.scgis.net/SCGCMap/OpenLayers-scgisExtension-2.12.03.js"></script>
<script type="text/javascript" src="${ctx}/s/app/esri/ControlManager.js"></script>

<script type="text/javascript" src="${ctx}/s/xform3/xform-all.js"></script>

<script>

    $(function(){
        App.init();
    });

    $('#demo').BootSideMenu({

        side:"right", // left or right

        autoClose:false // auto close when page loads

    });

    document.onreadystatechange = function () {
        if (document.readyState == "complete") {
            jQuery("#BD_container").attr('style', 'height:' + ($(".page-content").height() - $(".space20").height() - $(".footer").height()) + "px;");
            // 图层加载
            ControlManager.initMap("BD_container");
            ControlManager.addMarkLayer();
            ControlManager.addRegionLayer();
            // 事件绑定
            ControlManager.activateMarkClick(onFeatureClick);
            ControlManager.registerDrawRegion(PrepareDrawLayerAndStore);
            ControlManager.registerDeleteRegion(onFeatureSelectForDel);
            // 页面刷新
            flash();
        }
    }

    /**
     * 画图回调函数
     */
    function PrepareDrawLayerAndStore(bounds) {
        if (bounds) {
            var points = bounds.toString();
            points = points.substring(points.indexOf("((") + 2, points.lastIndexOf("))"));
            if (points) {
                $("#regionPickerPoints").val(points);
                $('#regionPicker').modal();
                return;
            }
        }
        console.warn("未画任何图形！");
        finallydo();
    }

    /**
     * 点击mark回调函数
     */
    function onFeatureClick(feature) {
        window.open("${ctx}/bpm/workspace-viewHistory.do?processInstanceId=" + feature.attributes.processid);
    }

    /**
     * 删除回调函数
     * @param feature
     */
    function onFeatureSelectForDel(feature) {
        if (!feature) return;
        if (confirm("确定删除区域[" + feature.attributes.regionname + "]?") === true) {
            $.ajax( {
                type: "POST",
                url: $("#context").val() + "/reg/region-delete.do",
                data: "dbId=" + feature.attributes.regionid,
                success: function(data) {
                    var jData = eval(data);
                    alert(jData.message);
                    feature.layer.removeFeatures(feature);
                    flash();
                },
                error:function(request) {
                    console.log(request.message);
                }
            });
        }
        ControlManager.controlFires(false);
    }

    function flash() {
        ControlManager.flashRegions($("#searchName").val(), afterFlashMarkers);
    }

    /**
     * 刷新页面回调函数
     */
    function afterFlashMarkers() {
        initAndClearTable();
        resolveRegionsForTable(ControlManager.getRegions());
        TableAdvanced.init(ControlManager.getRegions());
    }

    /**
     * 以下为解析地区与事件关系的相关方法——start
     */
    function resolveRegionsForTable(regionArray) {
        for (var i = 0; i < regionArray.length; i ++) {
            var region = regionArray[i].region;
            var list = regionArray[i].list;
            if (!ControlManager.isArray(list)) {
                list = new Array();
            }
            var spread = i;
            appendTable(region.attributes.regionid, region.attributes.regionname, list.length, spread);
        }
    }
    function initAndClearTable() {
        $("#setForClearTable").empty();
        var table = '<table class="table table-striped table-bordered table-hover table-full-width" id="regionTable">';
        table += '<thead><tr><th>ID</th><th>名称</th><th>事件数</th><th>扩展信息</th></tr></thead><tbody></tbody></table>';
        $("#setForClearTable").append(table);
    }
    function appendTable(id, name, size, spread) {
        var sOut = '<tr>';
        sOut += '<th>' + id + '</th>';
        sOut += '<th>' + name + '</th>';
        sOut += '<th>' + size + '</th>';
        sOut += '<th>' + spread + '</th>';
        sOut += '</tr>';
        $("#regionTable > tbody").append(sOut);
    }
    /**
     * 以上为解析地区与事件关系的相关方法——end
     */

    function DrawLayer() {
        ControlManager.toggleControl(ControlManager.getDrawRegionName());
    }

    function DeleteRegion() {
        ControlManager.toggleControl(ControlManager.getDelRegionName());
    }

    var logout = function() {
        window.location.href='${ctx}/user/sys-log-out.do';
    }

</script>
<div aria-hidden="false" id="regionPicker" class="modal fade in" style="z-index: 9997 ! important;display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="regionPickerClose" class="close" data-dismiss="modal" aria-hidden="true" style="width: 14px; height: 14px;"></button>
                <h3>新增网格地区</h3>
            </div>
            <div class="modal-body">
                <input type="hidden" id="regionPickerPoints"/>
                网格名称<span style="color: red">*</span><input type="text" id="regionPickerName" style="width: 300px;"/>
                <br>
                <br>
                网格描述<span style="color: red">*</span><textarea rows="5" cols="80" id="regionPickerDesc"></textarea>
            </div>
            <div class="modal-footer">
                <span id="regionPicker_result" style="float:left;"></span>
                <a id="regionPicker_close" href="#" class="btn" data-dismiss="modal">取消</a>
                <a id="regionPicker_select" href="#" class="btn btn-primary">确定</a>
            </div>
        </div>
    </div>
</div>
<script>
    function finallydo() {
        ControlManager.controlFires(false);
        flash();
        $("#regionPickerName").val("");
        $("#regionPickerDesc").val("");
    }
    $("#regionPicker_select").click(function (e) {
        e.preventDefault();
        var name = $('#regionPickerName').val();
        var description = $('#regionPickerDesc').val();

        if(name == null || name == undefined || name.length <=0) {
            alert("请输入网格名称！");
            return;
        }
        if(description == null || description == undefined || description.length <=0) {
            alert("请输入网格描述！");
            return;
        }
        try {
            var param=[encodeURI(encodeURI(name)),encodeURI(encodeURI(description))];
            $.ajax( {
                type: "POST",
                url: $("#context").val() + "/reg/region-add.do",
                data: "name=" + param[0] + "&description=" + param[1] +
                        "&points=" + $('#regionPickerPoints').val(),
                success: function(data) {
                    var jData = eval(data);
                    if(jData.success==true) {
                        $("#regionPickerClose").click();
                    } else {
                        alert(jData.message);
                    }
                },
                error:function(request) {
                    console.log(request.message);
                }
            });
        } catch(e) {
            if (name != null && name.length > 0) {
                parent.videoIfr.deleteRegion(name);
            }
            console.log("添加失败," + e.message);
        } finally {
            finallydo();
        }

    });
    $("#regionPicker_close").click(function (e) {
        finallydo();
    });
    $("#regionPickerClose").click(function (e) {
        finallydo();
    });
</script>
</body>
</html>
