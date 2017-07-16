<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%--<%pageContext.setAttribute("currentMenu","personalCenter");%>--%>

<% String src = request.getParameter("src");
    if(src!=null){
    pageContext.setAttribute("currentMenu","start");
    }
    else{
        pageContext.setAttribute("currentMenu","personalCenter");
    }

%>
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
    <link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.4.3/themes/bootstrap/easyui.css" >

    <link rel="stylesheet" type="text/css" href="${ctx}/s/xform3/styles/xform.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/s/media/css/DT_bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-validation/jquery.validate.css" />

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
    </style>

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
                    <section id="m-main" class="col-md-10" style="padding-top:25px;">

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                流程图 ${historicProcessInstance.name}
                                <div class="pull-right">
                                    <button class="btn btn-xs" onclick="replay.prev()"><i class="glyphicon glyphicon-backward"></i></button>
                                    <button class="btn btn-xs" onclick="replay.next()"><i class="glyphicon glyphicon-forward"></i></button>
                                    <button class="btn btn-xs" onclick="replay.replay()"><i class="glyphicon glyphicon-play"></i></button>
                                </div>
                            </div>

                            <div class="panel-body">

                                <div id="processGraphWrapper" class="content">

                                    <img src="workspace-graphHistoryProcessInstance.do?processInstanceId=${param.processInstanceId}">

                                    <div id="processGraphMask" style="position:absolute;">
                                        <c:forEach items="${nodeDtos}" var="item">
                                            <div style="position:absolute;left:${item.x}px;top:${item.y}px;width:${item.width}px;height:${item.height}px;" data-container="body" data-trigger="hover" data-toggle="popover" data-placement="bottom" data-html="true"

    <c:if test="${item.isTask && item.haveDone}">
        data-content="<table><tr><td style='width:70px;'>节点类型:</td><td>${item.type}</td></tr><tr><td>节点名称:</td><td>${item.name}</td></tr><tr><td> 处理人员:</td><td>${item.assigneeName}</td></tr></table>"
    </c:if>
    <c:if test="${item.isTask && !item.haveDone}">
        data-content="<table><tr><td style='width:70px;'>节点类型:</td><td>${item.type}</td></tr><tr><td>节点名称:</td><td>${item.name}</td></tr></table>"
    </c:if>
    <c:if test="${!item.isTask || !item.haveDone}">
        data-content="<table><tr><td style='width:70px;'>节点类型:</td><td>${item.type}</td></tr><tr><td>节点名称:</td><td>${item.name}</td></tr></table>"
    </c:if>

                                                    >
                                                 </div>
                                        </c:forEach>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="space10"></div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                事件描述
                            </div>
                            <div id="xf-form-table"></div>
                        </div>
                        <div class="space10"></div>

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                列表
                            </div>
                            <table id="demoGrid" class="table table-hover">
                                <thead>
                                <tr>
                                    <th class="sorting" name="name">名称</th>
                                    <th class="sorting" name="startTime">开始时间</th>
                                    <th class="sorting" name="endTime">结束时间</th>
                                    <th class="sorting" name="assignee">负责人</th>
                                    <th>状态</th>
                                    <th>意见</th>
                                </tr>
                                </thead>

                                <c:forEach items="${humanTasks}" var="item">
                                    <tbody>
                                    <tr>
                                        <td>${item.name}</td>
                                        <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                        <td><fmt:formatDate value="${item.completeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                        <td>
                                            <tags:user userId="${item.assignee}"/>
                                            <c:if test="${not empty item.owner && item.assignee != item.owner}">
                                                <b>(原执行人:<tags:user userId="${item.owner}"/>)</b>
                                            </c:if>
                                        </td>
                                        <td>${item.action}</td>
                                        <td>${item.comment}</td>
                                    </tr>
                                    <c:forEach items="${item.children}" var="child">
                                        <tr>
                                            <td class="active text-muted">&nbsp;*&nbsp;${child.name}</td>
                                            <td class="active text-muted"><fmt:formatDate value="${child.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                            <td class="active text-muted"><fmt:formatDate value="${child.completeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                            <td class="active text-muted">
                                                <tags:user userId="${child.assignee}"/>
                                                <c:if test="${not empty child.owner && child.assignee != child.owner}">
                                                    <b>(原执行人:<tags:user userId="${child.owner}"/>)</b>
                                                </c:if>
                                            </td>
                                            <td class="active text-muted">
                                                <c:if test="${child.catalog == 'communicate'}">
                                                    沟通
                                                </c:if>
                                                <c:if test="${child.catalog == 'copy'}">
                                                    抄送
                                                </c:if>
                                                <c:if test="${child.catalog == 'vote'}">
                                                    加签
                                                </c:if>
                                                (${child.action})
                                            </td>
                                            <td class="active text-muted">${child.comment}</td>
                                        </tr>
                                        <c:forEach items="${child.children}" var="third">
                                            <tr>
                                                <td class="active text-muted">&nbsp;**&nbsp;${third.name}</td>
                                                <td class="active text-muted"><fmt:formatDate value="${third.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td class="active text-muted"><fmt:formatDate value="${third.completeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td class="active text-muted">
                                                    <tags:user userId="${third.assignee}"/>
                                                    <c:if test="${not empty third.owner && third.assignee != third.owner}">
                                                        <b>(原执行人:<tags:user userId="${third.owner}"/>)</b>
                                                    </c:if>
                                                </td>
                                                <td class="active text-muted">
                                                    <c:if test="${third.catalog == 'vote'}">
                                                        加签
                                                    </c:if>
                                                    (${third.action})
                                                </td>
                                                <td class="active text-muted">${third.comment}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:forEach>
                                    </tbody>
                                </c:forEach>
                            </table>
                        </div>

                        <c:forEach items="${historicVariableInstances}" var="item">
                            <tr>
                                <td>${item.variableName}</td>
                                <td>${item.value}</td>
                            </tr>
                        </c:forEach>
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
<script src="${ctx}/s/ckplayer/ckplayer.js" type="text/javascript"></script>
<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctx}/s/xform3/xform-all.js"></script>

<script>
    $(function () {
        $('#processGraphMask').css($('#processGraphWrapper').position());
        $('#processGraphMask').width($('#processGraphWrapper').width());
        $('#processGraphMask').height($('#processGraphWrapper').height());

        $('[data-toggle="popover"]').popover();
    });
</script>
<script type="text/javascript" src="${tenantPrefix}/s/app/replay/scripts/JobExecutor.js"></script>
<script type="text/javascript" src="${tenantPrefix}/s/app/replay/scripts/Node.js"></script>
<script type="text/javascript" src="${tenantPrefix}/s/app/replay/scripts/Token.js"></script>
<script type="text/javascript" src="${tenantPrefix}/s/app/replay/scripts/Replay.js"></script>
<script type="text/javascript">
    REPLAY_TOKEN_IMAGE = '${tenantPrefix}/s/app/replay/images/user.png';
    <%--REPLAY_TOKEN_IMAGE = '${tenantPrefix}/s/media/image/avatar-1.jpg';--%>
    REPLAY_TOKEN_WIDTH = 32;
</script>
<script type="text/javascript">
    var processDefinition = [
        <c:forEach items="${nodeDtos}" var="item" varStatus="status">
        {
            name: '${item.id}',
            type: '${item.type}',
            x: ${item.x},
            y: ${item.y},
            w: ${item.width},
            h: ${item.height},
            ts: [
                <c:forEach items="${item.outgoings}" var="outgoing" varStatus="outgoingStatus">
                {
                    name: '${outgoing.id}',
                    to: '${outgoing.id}',
                    g: ${outgoing.g}
                }${outgoingStatus.last ? '' : ','}
                </c:forEach>
            ]
        }${status.last ? '' : ','}
        </c:forEach>
    ];

    var historyActivities = [
        <c:forEach items="${graph.nodes}" var="node" varStatus="status">
        <c:forEach items="${node.outgoingEdges}" var="edge">
        {
            name: '${edge.src.name}',
            t: '${edge.dest.name}'
        }${status.last ? '' : ','}
        </c:forEach>
        </c:forEach>
    ];

    var currentActivities = [
        <c:forEach items="${currentActivities}" var="item" varStatus="status">
        '${item}'${status.last ? '' : ','}
        </c:forEach>
    ];

    var replay = new Replay(
            processDefinition,
            historyActivities,
            currentActivities
    );
</script>
<script>
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
        //清理需要上传的按钮
        $("input[type='file']").each(function(i){
            $(this).before("未上传");
            this.type = 'hidden';
        });
        $("div[class='xf-handler']").each(function(i){
            var inner = $(this).html();
            if (inner == null || inner == undefined || inner.length <= 0) {
                $(this).html("未设置");
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
