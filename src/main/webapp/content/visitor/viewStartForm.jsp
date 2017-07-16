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

    </style>

    <%--<link rel="shortcut icon" href="${ctx}/s/media/image/favicon.ico" />--%>

    <!-- 系统页面所需js文件 -->
    <script src="${ctx}/s/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

    <!-- 该页面所需js文件 -->
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

</head>

<body class="page-header-fixed" scroll="no">

<input id="context" type="hidden" value="${ctx}">

<div class="header navbar navbar-inverse navbar-fixed-top">

    <div class="head-img">

    </div>
    <div class="back-img">
        <img style="height:37px" src="${ctx}/s/media/image/banner.png"/>
    </div>

    <div class="navbar-inner">

        <div class="container-fluid">

            <ul class="nav pull-right">

                <li class="dropdown user">

                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">

                        <img alt="" style="width: 35px;" src="${ctx}/s/media/image/avatar-1.jpg"/>
                        <span class="username">游客</span>
                        <i class="icon-angle-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${ctx}/login.jsp"><i class="icon-key"></i> 登录</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>

<div class="page-container" style="background-color: #ffffff;">

    <!-- BEGIN SIDEBAR -->

    <div class="page-content">

        <div class="container-fluid">

            <div class="clearfix"></div>

            <div class="row-fluid">

                <div class="span12">
                    <!-- start of main -->
                    <section id="m-main" class="col-md-10" style="padding-top:45px;">

                        <div id="xformToolbar">
                            <c:forEach var="item" items="${buttons}">
                                <button id="${item.name}" type="button" class="btn btn-default" onclick="${item.name}('xform')">${item.label}</button>
                            </c:forEach>
                        </div>

                        <form id="xform" method="post" action="" class="xf-form" enctype="multipart/form-data">
                            <input id="processDefinitionId" type="hidden" name="processDefinitionId" value="${formDto.processDefinitionId}">
                            <input id="bpmProcessId" type="hidden" name="bpmProcessId" value="${bpmProcessId}">
                            <input id="autoCompleteFirstTask" type="hidden" name="autoCompleteFirstTask" value="${formDto.autoCompleteFirstTask}">
                            <input id="businessKey" type="hidden" name="businessKey" value="${businessKey}">
                            <div id="xf-form-table"></div>
                            <br>
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

<script type="text/javascript" src="http://www.scgis.net/SCGCMap/OpenLayers-scgisExtension-2.12.03.js"></script>
<script type="text/javascript" src="${ctx}/s/app/esri/EsriControl.js"></script>
<script type="text/javascript" src="${ctx}/s/app/esri/ControlManager.js"></script>

<script type="text/javascript">
    ROOT_URL = '${ctx}';

    var xform;
    function makeXform() {//拼装表格信息
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
                if (!checkFile()) {
                    return;
                }
                bootbox.animate(false);
                var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
                $('#uploadfile').modal();
                form.submit();
            },
            errorClass: 'validate-error'
        });
    }

    $(function() {

        handleResponsive();
        resizewindow();

        makeXform();
        addPositionButton();//与流程表单具有耦合性，需要表单中有position字段
        setUserPicker();//与流程表单具有耦合性，需要表单中有userPicker属性
        hideEventSource();//与流程表单具有耦合性，需要表单中有reportType字段
    });

    function checkFile() {
        var maxpicsize = 52428800;
        var picsuffix = "(JPG|GIF|BMP|PNG|JPEG)";
        var maxmvsize = 52428800;
        var mvsuffix = "(MP4|AVI|MPG|MPEG|RM|RMVB|MOV|WMV|ASF|3GP)";
        if (!ControlManager.checkFileSize("uploadPic", maxpicsize)) {
            alert("上传照片最大仅支持" + (new Number(maxpicsize/1024/1024).toFixed()) + "M！");
            return false;
        }
        if (!ControlManager.checkFileSuffix("uploadPic", picsuffix)) {
            alert("上传照片仅支持常见照片格式文件 " + picsuffix);
            return false;
        }
        if (!ControlManager.checkFileSize("uploadMv", maxmvsize)) {
            alert("上传视频最大仅支持" + (new Number(maxmvsize/1024/1024).toFixed()) + "M！");
            return false;
        }
        if (!ControlManager.checkFileSuffix("uploadMv", mvsuffix)) {
            alert("上传视频仅支持常见视频格式文件 " + mvsuffix);
            return false;
        }
        return true;
    }

    /**
     * 增加地点选择按钮，同时地点信息不可随意修改
     */
    function addPositionButton() {
        var position = $("input[name='position']");
        if (position.size() === 1) {
            position.after("<button type='button' class='btn btn-default' onclick='showTiandi();'>选择地点</button>");
            position.after("<input id='position_display' type='text' class='form-control required' required='true' style='margin-bottom:0px;' maxlength='200' readonly='readonly'/>");
        }
        position.attr("type", "hidden");
    }

    /**
     * 设置下一步接收任务的人员设置
     */
    function setUserPicker() {
        if ($('.userPicker').size() === 0) {
            console.info("本任务不需要指定任务接收人");
            return;
        }
        $(".userPicker").parent().parent().parent().attr("style", "display:none;");
        $.ajax({
            type: "post",
            url: $('#context').val() + "/rs/party/taskFormTree?partyStructTypeId=1&taskName=" + encodeURI(encodeURI("${eventName}")),
            success: function (data) {
                var ids = '';
                var nodes = makeUser(data);
                if (!nodes || nodes.length <= 0) return;
                for (var i = 0; i < nodes.length ; i++) {
                    if (!nodes[i]) continue;
                    ids += "," + nodes[i].ref;
                }
                if (ids.length > 0) {
                    ids = ids.substring(1);
                }
                var child = $($(".userPicker").children().get(0));
                child.val(ids);
            },
            error: function (d) {//请求出错
                console.log(d.responseText);
            }
        });
    }

    /**
     * 根据组织结构树获取指派的人员array
     */
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

    /**
     * 提交数据按钮触发事件
     */
    function confirmStartProcess(formId) {
        $('#' + formId).attr('action', ROOT_URL + '/confirmStartProcess.do');
        $('#' + formId).submit();
    }

    /**
     * 隐形增加事件上报来源
     */
    function hideEventSource() {
        var handler = $("input[name='reportType']");
        if (handler != null && handler.length > 0) {
            var control = handler.parent().parent();
            control.html("<input name='reportType' type='text' value='公众上报'/>");
            control.parent().parent().attr("style", "display:none;");
        }
    }

    /*******************自适应窗口strat**********************/
    function resizewindow() {
        var resize;
        $(window).resize(function() {
            if (resize) {
                clearTimeout(resize);
            }
            resize = setTimeout(function() {
                console.log('resize');
                handleResponsive();
            }, 50); // wait 50ms until window resize finishes.
        });
    }

    function handleResponsive() {
        var content = $('.page-content');
        var sidebar = $('.page-sidebar');
        var body = $('body');

        var height = $(window).height();
        height = height - $('.header').height() - $('.footer').height();
        body.attr('style', 'height: 1px;');
        if (height >= content.height()) {
            content.attr('style', 'min-height:' + height + 'px !important;margin:0px auto !important;');
        }
    }
    /*******************自适应窗口end**********************/

    /*******************天地图strat**********************/
    var showTiandi = function(){
        $('#placePicker').modal();
        ControlManager.initMap("BD_container");
        ControlManager.addRegionLayer();
        ControlManager.flashRegions();
        ControlManager.activateSelectPosition(drawFeatureCallback);
    };

    function drawFeatureCallback(geometry) {
        if ("OpenLayers.Geometry.Point" === geometry.CLASS_NAME) {
            if (confirm("确定选择?") === true) {
                timeOut(geometry.x, geometry.y);
                ControlManager.searchNearestPlaceByLonLat(geometry.x, geometry.y, dealWithPosition);
            }
        }
    }

    /**
     * 防止异步请求超时
     */
    function timeOut(lon, lat) {
        $("#placePickerClose").click();
        $("input[name='position']").val(lon + "," + lat);
        $("#position_display").val(lon + "," + lat);
        ControlManager.controlFires(false);
    }

    function dealWithPosition(lon, lat, nearName) {
        $("#placePickerClose").click();
        if (nearName) {
            $("input[name='position']").val(lon + "," + lat + "," + nearName);
            $("#position_display").val(nearName + "["
                    + lon + "," + lat + "]");
        } else {
            alert("未能获取正确地址信息");
            $("input[name='position']").val(lon + "," + lat);
            $("#position_display").val(lon + "," + lat);
        }
        ControlManager.controlFires(false);
    }
    /*******************天地图end**********************/

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
            </div>
        </div>
    </div>
</div>

<div aria-hidden="false" id="uploadfile" class="modal" style="z-index: 9996 ! important;display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3>文件上传中</h3>
            </div>
            <div class="modal-body">
                <div id="progress_all">
                    <img src="${ctx}/images/gif/ajax-loader.gif"/>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>
</body>
</html>
