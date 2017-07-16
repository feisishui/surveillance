<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu","start");%>
<%pageContext.setAttribute("secondMenu","time-manage");%>
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8"/>

    <title>四川省地下管线监督管理子系统</title>

    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>

    <meta content="" name="description"/>

    <meta content="" name="author"/>

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
    <link rel='stylesheet' type='text/css' href='${ctx}/s/portal/dashboard.css' media='screen' />
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
        table.table {
            margin-bottom: 0px !important;
        }
    </style>



</head>

<body class="page-header-fixed">

<input id="context" type="hidden" value="${ctx}">

<%@include file="/common/layout/header.jsp"%>

<div class="page-container" style="overflow-x: hidden;">

    <%@include file="/common/layout/menu_new.jsp"%>

    <div class="page-content">

        <div class="row-fluid" class="col-md-10" style="margin-left: 20px">
            <div class="span12">
                <section id="m-main" style="padding-top:15px;;margin-right: 30px">

                    <div style="margin-bottom: 20px;">

                        <div class="pull-right">
                            每页显示
                            <select class="m-page-size form-control" style="display:inline;width:auto;">
                                <option value="10">10</option>
                                <option value="20">20</option>
                                <option value="50">50</option>
                            </select>
                            条
                        </div>

                        <div class="clearfix"></div>

                    </div>

                    <form id="pimRemindGridForm" name="pimRemindGridForm" method='post' action="pim-note-remove.do" class="m-form-blank">

                        <div class="panel panel-default">

                            <div class="panel-heading">
                                <i class="glyphicon glyphicon-list"></i>
                                列表
                            </div>

                            <table id="pimRemindGrid" class="table table-hover">

                                <thead>
                                <tr>
                                    <th>流水号</th>
                                    <th>标题</th>
                                    <th>到达时间</th>
                                    <th>完成时间</th>
                                    <th>持续时间</th>
                                    <th>流程</th>
                                    <th>状态</th>
                                    <th width="110">&nbsp;</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${page.result}" var="item">
                                    <tr>
                                        <td>${item.businessKey}</td>
                                        <td>${item.name}</td>
                                        <td><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                        <td><fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                        <td><tags:duration startTime="${item.startTime}" endTime="${item.endTime}"/></td>
                                        <td><tags:processName processDefinitionId="${item.processDefinitionId}"/></td>
                                        <td>完成</td>
                                        <td>
                                            <a href="${tenantPrefix}/bpm/workspace-viewHistory.do?processInstanceId=${item.processInstanceId}">详情</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>

                            </table>

                        </div>

                    </form>

                    <div>

                        <div class="m-page-info pull-left">
                            共100条记录 显示1到10条记录
                        </div>

                        <div class="btn-group m-pagination pull-right">
                            <button class="btn btn-default">&lt;</button>
                            <button class="btn btn-default">1</button>
                            <button class="btn btn-default">&gt;</button>
                        </div>

                        <div class="clearfix"></div>

                    </div>

                </section>

                <div id="modal" class="modal fade">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-body">
                                <form>
                                    <input type="hidden" id="humanTaskId" name="humanTaskId" value=""/>
                                    <div class="input-group userPicker" style="width:200px;">
                                        <input id="_assignee_key_" type="hidden" name="assignee" class="input-medium" value="">
                                        <input type="text" class="form-control" name="username" placeholder="" value="">
                                        <div class="input-group-addon"><i class="glyphicon glyphicon-user"></i></div>
                                    </div>
                                    <br>
                                    <button class="btn btn-default">提交</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
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
        var config = {
            id: 'processGrid',
            pageNo: ${page.pageNo},
            pageSize: ${page.pageSize},
            totalCount: ${page.totalCount},
            resultSize: ${page.resultSize},
            pageCount: ${page.pageCount},
            orderBy: '${page.orderBy == null ? "" : page.orderBy}',
            asc: ${page.asc},
            params: {
            },
            selectedItemClass: 'selectedItem',
            gridFormId: 'processGridForm',
            exportUrl: 'process-export.do'
        };

        var table;

        table = new Table(config);
        table.configPagination('.m-pagination');
        table.configPageInfo('.m-page-info');
        table.configPageSize('.m-page-size');

        createUserPicker({
            modalId: 'userPicker',
            searchUrl: '${tenantPrefix}/rs/user/search',
            treeUrl: '${tenantPrefix}/rs/party/tree?partyStructTypeId=1',
            childUrl: '${tenantPrefix}/rs/party/searchUser'
        });

        var ROOT_URL = '${tenantPrefix}';

        function doTransfer(humanTaskId) {
            $('#modal form').attr('action', ROOT_URL + '/humantask/workspace-transferTask.do');
            $('#humanTaskId').val(humanTaskId);
            $('#modal').modal();
        }

        App.init();

    </script>

</body>

</html>
