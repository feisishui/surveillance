<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu","personalCenter");%>
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
    <link href="${ctx}/s/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/s/media/css/blue.css" rel="stylesheet" type="text/css" id="style_color"/>
    <link href="${ctx}/s/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>

    <!-- 该页面所需样式 -->
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/select2_metro.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/DT_bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-validation/jquery.validate.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/userpicker3-v2/userpicker.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/s/xform3/styles/xform.css">
    <!-- tree -->
    <link rel="stylesheet" type="text/css" href="${ctx}/s/ztree/zTreeStyle/zTreeStyle.css"/>
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
        .tangram-suggestion-main {
            z-index: 9997 !important;
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
        /*.infowindow {*/
        /*z-index: 9997 !important;*/
        /*}*/
        <%--.infowindow .sprite {--%>
        <%--background-image: url(${ctx}/s/app/esri/soria.infowindow.png);--%>
        <%--}--%>
        <%--.dijit.dijitReset.dijitSliderV.dijitSlider {--%>
        <%--display: none;--%>
        <%--}--%>
    </style>

    <%--<link rel="shortcut icon" href="${ctx}/s/media/image/favicon.ico" />--%>

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

                        <div id="xformToolbar">
                            <c:forEach var="item" items="${buttons}">
                                <button id="${item.name}" type="button" class="btn btn-default" onclick="taskOperation.${item.name}()">${item.label}</button>
                            </c:forEach>
                        </div>

                        <form id="xform" method="post" action="${tenantPrefix}/operation/process-operation-startProcessInstance.do" class="xf-form" enctype="multipart/form-data">
                            <input id="processDefinitionId" type="hidden" name="processDefinitionId" value="${formDto.processDefinitionId}">
                            <input id="bpmProcessId" type="hidden" name="bpmProcessId" value="${bpmProcessId}">
                            <input id="autoCompleteFirstTask" type="hidden" name="autoCompleteFirstTask" value="${formDto.autoCompleteFirstTask}">
                            <input id="businessKey" type="hidden" name="businessKey" value="${businessKey}">
                            <!--
<input id="taskId" type="hidden" name="taskId" value="${taskId}">
-->
                            <div id="xf-form-table"></div>
                            <br>
                            <div id="nextStep">
                            </div>
                            <script>
                                $.getJSON('${tenantPrefix}/rs/bpm/next', {
                                    processDefinitionId: '${formDto.processDefinitionId}',
                                    activityId: '${formDto.activityId}'
                                }, function(data) {
                                    $('#nextStep').append('下个环节：');
                                    for (var i = 0; i < data.length; i++) {
                                        $('#nextStep').append(data[i].name);
                                        if(i != data.length-1)$('#nextStep').append(" 或 ");
                                    }
                                });
                            </script>
                        </form>

                    </section>
                    <!-- end of main -->

                    <form id="f" action="form-template-save.do" method="post" style="display:none;">
                        <textarea id="__gef_content__" name="content">${xform.content}</textarea>
                    </form>
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
<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

<!-- 该页面所需js文件 -->
<script type="text/javascript" src="${tenantPrefix}/s/app/operation/TaskOperation.js"></script>

<!-- bootbox -->
<script type="text/javascript" src="${ctx}/s/bootbox/bootbox.min.js"></script>

<script type="text/javascript" src="${ctx}/s/xform3/xform-all.js"></script>
<script type="text/javascript" src="${ctx}/s/userpicker3-v2/userpicker.js"></script>

<!-- validater -->
<script type="text/javascript" src="${ctx}/s/jquery-validation/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-validation/additional-methods.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-validation/localization/messages_${locale}.js"></script>

<!-- tree -->
<script type="text/javascript" src="${ctx}/s/ztree/jquery.ztree.all-3.5.min.js"></script>

<script type="text/javascript" src="http://www.scgis.net/SCGCMap/OpenLayers-scgisExtension-2.12.03.js"></script>
<script type="text/javascript" src="${ctx}/s/app/esri/EsriControl.js"></script>

<script type="text/javascript">
    ROOT_URL = '${ctx}';
    var taskOperation = new TaskOperation();
    var xform;

    var maxpicsize = 52428800;
    var picsuffix = "(JPG|GIF|BMP|PNG|JPEG)$";
    var maxmvsize = 52428800;
    var mvsuffix = "(MP4|AVI|MPG|MPEG|RM|RMVB|MOV|WMV|ASF|3GP)$";
    $(function() {

        App.init();

        xform = new xf.Xform('xf-form-table');
        xform.render();
        if ($('#__gef_content__').val() != '') {
            xform.doImport($('#__gef_content__').val());
        }
        if ('${xform.jsonData}' != '') {
            xform.setValue(${xform.jsonData});
        }

        $("#xform").validate({
            submitHandler: function(form) {
                var f = document.getElementsByName("uploadPic")[0].files;
                if (f.length > 0) {
                    var totalsize = 0;
                    for(var i = f.length - 1; i > -1; i--) {
                        totalsize += f[i].size;
                        if (!matchSuffix(f[i].name, picsuffix)) {
                            alert("上传照片仅支持常见照片格式文件 " + (picsuffix.substring(0, picsuffix.length-1)));
                            return;
                        }
                        if (totalsize > maxpicsize || totalsize < 0) {
                            alert("上传照片最大仅支持" + (new Number(maxpicsize/1024/1024).toFixed()) + "M！");
                            return;
                        }
                    }
                }
                f = document.getElementsByName("uploadMv")[0].files;
                if (f.length > 0) {
                    var totalsize = 0;
                    for(var i = f.length - 1; i > -1; i--) {
                        totalsize += f[i].size;
                        if (!matchSuffix(f[i].name, mvsuffix)) {
                            alert("上传视频仅支持常见视频格式文件 " + (mvsuffix.substring(0, mvsuffix.length-1)));
                            return;
                        }
                        if (totalsize > maxmvsize || totalsize < 0) {
                            alert("上传视频最大仅支持" + (new Number(maxmvsize/1024/1024).toFixed()) + "M！");
                            return;
                        }
                    }
                }

                bootbox.animate(false);
                var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
                form.submit();
            },
            errorClass: 'validate-error'
        });

        createUserPicker({
            multiple: true,
            searchUrl: '${tenantPrefix}/rs/user/search',
            treeUrl: '${tenantPrefix}/rs/party/startFormtree?partyStructTypeId=1',
            childUrl: '${tenantPrefix}/rs/party/searchUser'
        });

        setTimeout(function() {
            $('.datepicker').datepicker({
                autoclose: true,
                language: 'zh_CN',
                format: 'yyyy-mm-dd'
            })
        }, 500);

        //增加选择地点按钮
        var position = $("input[name='position']");
        if (position.size() === 1) {
            position.after("<button id='' type='button' class='btn btn-default' onclick='showTiandi();'>选择地点</button>");
        }
        //隐藏处理人员选项
        $(".userPicker").parent().parent().parent().attr("style", "display:none;");
        $.ajax({
            type: "post",
            url: "${tenantPrefix}/rs/party/taskFormTree?partyStructTypeId=1&taskName=" + encodeURI(encodeURI("${eventName}")),
            success: function (data) {
                var superviseCenter = '';
                var superviseCenter_name = '';
                var nodes = makeUser(data);
                if (nodes == null || nodes.length <= 0) return;
                for (var i = 0; i < nodes.length ; i++) {
                    if (nodes[i] == null || nodes[i] == undefined) continue;
                    superviseCenter += "," + nodes[i].ref;
                    superviseCenter_name += "," + nodes[i].name;
                }
                if (nodes.length > 0) {
                    superviseCenter = superviseCenter.substring(1);
                    superviseCenter_name = superviseCenter_name.substring(1);
                }
                console.log(superviseCenter);
                console.log(superviseCenter_name);
                $("input[name='superviseCenter']").val(superviseCenter);
                $("input[name='superviseCenter_name']").val(superviseCenter_name);
            },
            error: function (d) {//请求出错
                alert(d.message);
            }
        });
        //隐藏事件来源选项
        var handler = $("input[name='reportType']");
        if (handler != null && handler.length > 0) {
            var control = handler.parent().parent();
            control.html("<input name='reportType' type='text' value='公众上报'/>");
            control.parent().parent().attr("style", "display:none;");
        }
    });

    function makeUser(children) {
        if (children == null || children == undefined || children.length < 1) return null;
        var result = new Array();
        for (var i = 0; i < children.length; i ++) {
            var child = children[i];
            if (child.open) {
                var temp = makeUser(child.children);
                for (var j = 0; j < temp.length; j ++) {
                    result.push(temp[j]);
                }
            } else {
                result.push(child);
            }
        }
        return result;
    }

    function matchSuffix(name, suffix) {
        var index = name.lastIndexOf(".");
        name = name.substring(index, name.length).toUpperCase();
        return name.match(suffix);
    }

    var fire,map,layer,measureVectorLayer;
    var showTiandi = function(){
        $('#placePicker').modal();
        var token =CST.token;
        var tilemapUrl = CST.tilemap;
        if (map == null) {
            map = new OpenLayers.Map("BD_container");
        }
        if (layer == null) {
            var extent=[95.50484501954,28.198615625002, 110.65328251954, 32.284064843752];
            layer = new OpenLayers.Layer.SCGISTileMapEx("dlgMapLayer", tilemapUrl, {token:token,zoomToExtent:extent});
            map.addLayer(layer);
        }
        //区域图层
        if (measureVectorLayer == null) {
            var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
            renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderer;
            measureVectorLayer = new OpenLayers.Layer.Vector("layer geometry", {
                styleMap: new OpenLayers.StyleMap({
                    "default": new OpenLayers.Style(null, {
                        rules: [
                            new OpenLayers.Rule({
                                symbolizer: {
                                    "Polygon": {
                                        label:"${regionname}",
                                        strokeWidth: 2,
                                        strokeOpacity: 1,
                                        strokeColor: "#666666",
                                        fillColor: "blue",
                                        fillOpacity: 0.3
                                    }
                                }
                            })
                        ]
                    })
                }),
                renderer: renderer
            });
            map.addLayer(measureVectorLayer);
            $.ajax({
                type: "get",
                url: $('#context').val() + "/reg/region-list.do",
                dataType: "json",
                cache: false,
                success: function (r) {
                    if (r.success) {
                        for (var t = 0; t < r.data.length; t++) {
                            var regiondto = r.data[t];
                            AddRegionToMeasureLayer(regiondto.points, regiondto.regionName, regiondto.dbId);
                        }
                    } else {
                        alert(r.msg);
                    }
                },
                error: function (d) {//请求出错
                    alert(d.responseText);
                }
            });
        }

        if (fire == null) {
            fire = new OpenLayers.Control.DrawFeature(
                    map, OpenLayers.Handler.Point, {
                        drawFeature: drawFeatureCallback
                    }
            );
            map.addControl(fire);
        }
        fire.activate();
    };

    function drawFeatureCallback(geometry) {
        if ("OpenLayers.Geometry.Point" === geometry.CLASS_NAME) {
            if (confirm("确定选择?") === true) {
                $("#placePickerClose").click();
                $("input[name='position']").val(geometry.x + "," + geometry.y);
                if (fire != null) {
                    fire.deactivate();
                }
            }
        }
    };

    function AddRegionToMeasureLayer(pointsdto, regionname, regionid) {
        var points = new Array();
        for (var i = 0; i < pointsdto.length; i++) {
            points.push(new OpenLayers.Geometry.Point(pointsdto[i].x, pointsdto[i].y));
        }
        var linearRing = new OpenLayers.Geometry.LinearRing(points);
        var polygonFeature = new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Polygon([linearRing]),
                {regionname: regionname, regionid: regionid}
        );
        measureVectorLayer.addFeatures([polygonFeature]);
    }

    var logout = function(){
        window.location.href='${ctx}/user/sys-log-out.do';
    };
</script>

<div aria-hidden="false" id="placePicker" class="modal" style="z-index: 9996 ! important;display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="placePickerClose" class="close" data-dismiss="modal" aria-hidden="true" style="width: 14px; height: 14px;"></button>
                <h3>选择地点</h3>
            </div>
            <div class="modal-body">
                <div id="BD_container" class="soria" style="width: 100%; height: 350px;"></div>
            </div>
            <div class="modal-footer">
                <%--<span id="placePicker_result" style="float:left;"></span>--%>
                <%--<a id="placePicker_close" href="#" class="btn" data-dismiss="modal">关闭</a>--%>
                <%--<a id="placePicker_select" href="#" class="btn btn-primary">选择</a>--%>
            </div>
        </div>
    </div>
</div>
</body>
</html>
