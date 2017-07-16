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
    <link href='${ctx}/s/bootstrap/3.3.6/css/bootstrap.min.css' rel='stylesheet' type='text/css' media='screen' />
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
        .label {
            font-size: 100%;
        }
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

</head>

<body class="page-header-fixed">

<input id="context" type="hidden" value="${ctx}">

<%@include file="/common/layout/header.jsp"%>

<div class="page-container" style="overflow-x: hidden;">

    <!-- BEGIN SIDEBAR -->

    <%@include file="/common/layout/menu_new.jsp"%>

    <div class="page-content">

        <div class="container-fluid">

            <div class="clearfix"></div>

            <div class="row-fluid">

                <div class="span12">
                    <!-- start of main -->
                    <section id="m-main" class="col-md-10" style="padding-top:45px;">

                        <c:if test="${not empty children}">
                            <div class="alert alert-info" role="alert">
                                <c:forEach var="item" items="${children}">
                                    <p>
                                            ${item.catalog == 'communicate' ? '沟通反馈' : ''}
                                        <tags:user userId="${item.assignee}"/>
                                        <fmt:formatDate value="${item.completeTime}" type="both"/>
                                            ${item.comment}</p>
                                </c:forEach>
                            </div>
                        </c:if>

                        <div id="xformToolbar">
                            <c:if test="${humanTask.catalog == 'normal'}">
                                <c:forEach var="item" items="${buttons}">
                                    <button id="${item.name}" type="button" class="btn btn-default" onclick="taskOperation.${item.name}()">${item.label}</button>
                                </c:forEach>
                            </c:if>

                            <c:if test="${humanTask.catalog == 'vote'}">
                                <button id="approve" type="button" class="btn btn-default" onclick="taskOperation.approve()">同意</button>
                                <button id="reject" type="button" class="btn btn-default" onclick="taskOperation.reject()">反对</button>
                                <button id="abandon" type="button" class="btn btn-default" onclick="taskOperation.abandon()">弃权</button>
                            </c:if>

                            <c:if test="${humanTask.catalog == 'copy'}">
                            </c:if>

                            <c:if test="${humanTask.catalog == 'communicate'}">
                                <div class="alert alert-info" role="alert">
                                    来自<tags:user userId="${parentHumanTask.assignee}"/>的沟通：
                                        ${humanTask.message}
                                </div>
                                <button id="callback" type="button" class="btn btn-default" onclick="taskOperation.callback()">反馈</button>
                            </c:if>

                            <c:if test="${humanTask.catalog == 'start'}">
                                <button id="saveDraft" type="button" class="btn btn-default" onclick="taskOperation.saveDraft()">暂存</button>
                                <button id="completeTask" type="button" class="btn btn-default" onclick="taskOperation.completeTask()">提交</button>
                            </c:if>
                        </div>


                        <c:if test="${humanTask.catalog != 'communicate'}">
                            <div id="previousStep">
                            </div>

                            <script>
                                $.getJSON('${tenantPrefix}/rs/bpm/previous', {
                                    processDefinitionId: '${formDto.processDefinitionId}',
                                    activityId: '${formDto.activityId}'
                                }, function(data) {
                                    $('#previousStep').append('上个环节：');
                                    for (var i = 0; i < data.length; i++) {
                                        $('#previousStep').append(data[i].name);
                                        if(i != data.length-1)$('#previousStep').append(" 或 ");
                                    }
                                });
                            </script>

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
                        </c:if>

                        <form id="xform" method="post" action="${tenantPrefix}/operation/task-operation-completeTask.do" class="xf-form" enctype="multipart/form-data">
                            <input id="humanTaskId" type="hidden" name="humanTaskId" value="${humanTaskId}">
                            <div id="xf-form-table"></div>

                            <c:if test="${humanTask.catalog == 'normal' || humanTask.catalog == 'vote'}">
                                <div class="padding-top:20px;">
                                    <fieldset>
                                        <legend>意见</legend>
                                        <input type="hidden" id="_humantask_action_" name="_humantask_action_" value="">
                                        <textarea name="_humantask_comment_" class="form-control"></textarea>
                                    </fieldset>
                                </div>
                            </c:if>

                        </form>

                        <div>
                            <c:forEach var="item" items="${logHumanTaskDtos}">
                                <c:if test="${not empty item.completeTime}">
                                    <p>
                                        <tags:user userId="${item.assignee}"/>
                                        <fmt:formatDate value="${item.completeTime}" type="both"/>
                                            ${item.comment}</p>
                                    </p>
                                </c:if>
                            </c:forEach>
                        </div>

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
<script src="${ctx}/s/ckplayer/ckplayer.js" type="text/javascript"></script>
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

<script>
    ROOT_URL = '${tenantPrefix}';
    var taskOperation = new TaskOperation();
    var xform;
    $(function(){
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
                bootbox.animate(false);
                var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
                form.submit();
            },
            errorClass: 'validate-error'
        });

        createUserPicker({
            multiple: true,
            searchUrl: '${tenantPrefix}/rs/user/search',
            treeUrl: '${tenantPrefix}/rs/party/taskFormTree?partyStructTypeId=1&taskName=' + encodeURI(encodeURI("${humanTask.name}")),
            childUrl: '${tenantPrefix}/rs/party/searchUser'
        });

        setTimeout(function() {
            $('.datepicker').datepicker({
                autoclose: true,
                language: 'zh_CN',
                format: 'yyyy-mm-dd'
            })
        }, 500);

        //清理需要上传的按钮
        $("input[type='file']").each(function(i){
            if(this.readOnly)
            {
                $(this).before("未上传");
                this.type = 'hidden';
            }
        });
        showPic();
        showVideo();
    });

    /**
     * 增加图片缩略图
     */
    function showPic() {
        $("div[class='xf-handler'] a").each(function(i){
            var href = $(this).attr("href");
            var model = href.match(/model=(\S*)&/)[1];
            var key = href.match(/key=(\S*)$/)[1];
            var _this = $(this);
            if (matchSuffix(key, "JPG|GIF|BMP|PNG|JPEG")) {
                $.ajax({
                    type: "get",
                    url: $('#context').val() + "/bpm/getStorePath.do",
                    dataType: "json",
                    data: "key=" + key + "&model=" + model,
                    cache: false,
                    success: function (r) {
                        if (r.success) {
                            _this.after("<img data-src='holder.js/200x200' " +
                                    "class='img-thumbnail' style='height: 200px;' " +
                                    "src='${ctx}/" + r.path +
                                    "' data-holder-rendered='true'>");
                        } else {
                            console.log(r.msg);
                        }
                    },
                    error: function (d) {//请求出错
                        console.log(d.responseText);
                    }
                });
            }
        });
    }

    /**
     * 增加播放器
     */
    function showVideo() {
        $("div[class='xf-handler'] a").each(function(i){
            var href = $(this).attr("href");
            var model = href.match(/model=(\S*)&/)[1];
            var key = href.match(/key=(\S*)$/)[1];
            var _this = $(this);
            if (matchSuffix(key, "MP4|3GP|FLV|F4V|M3U8")) {
                $.ajax({
                    type: "get",
                    url: $('#context').val() + "/bpm/getStorePath.do",
                    dataType: "json",
                    data: "key=" + key + "&model=" + model,
                    cache: false,
                    success: function (r) {
                        if (r.success) {
                            _this.after("<br/><div id='a1'></div>");
                            var flashvars={
                                f:'${ctx}/' + r.path,
                                c:0,
                                b:1
                            };
                            var params={
                                bgcolor:'#FFF',
                                allowFullScreen:true,
                                allowScriptAccess:'always',
                                wmode:'transparent'
                            };
                            CKobject.embedSWF(
                                    '${ctx}/s/ckplayer/ckplayer.swf','a1',
                                    'ckplayer_a1','600','400',
                                    flashvars,params
                            );
                        } else {
                            console.log(r.msg);
                        }
                    },
                    error: function (d) {//请求出错
                        console.log(d.responseText);
                    }
                });
            }
        });
    }

    /**
     * 检查文件后缀名是否在给的集合中
     * @param name
     * @param suffix
     * @returns {boolean}
     */
    function matchSuffix(name, suffix) {
        if (!name) return false;
        var index = name.lastIndexOf(".");
        name = name.substring(index + 1, name.length).toUpperCase();
        suffix = suffix.toUpperCase();
        return suffix.indexOf(name) > -1;
    }

    var logout = function(){
        window.location.href='${ctx}/user/sys-log-out.do';
    };

</script>
</body>
</html>
