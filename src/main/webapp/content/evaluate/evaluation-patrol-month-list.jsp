<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "start");%>
<%pageContext.setAttribute("secondMenu", "evaluate");%>
<%pageContext.setAttribute("thirdMenu", "monthEvaluate");%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if IE 10]> <html lang="en" class="ie10"> <![endif]-->
<!--[if IE 11]> <html lang="en" class="ie11"> <![endif]-->

<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->

<head>

    <meta charset="utf-8" />
    <!--TODO LIST:修改为对应系统-->
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
<input id="patrol_text" type="hidden" value="巡查部门">
<input id="month_text" type="hidden" value="月度考核">

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

            <div class="row-fluid">

                <div class="span12">

                    <div class="m-spacer"></div>

                </div>

            </div>

            <!--Start secord level title-->

            <%@include file="/common/layout/second-menu-evaluate-month.jsp"%>

            <!--End secord level title-->

            <!--Start the content-->

            <div class="row-fluid">

                <div class="span12">

                    <div class="portlet-body">

                        <div class="clearfix">

                            <td style="border: none">

                                <select class="span2 chosen" data-with-diselect="1" data-placeholder="请选择区域" tabindex="1" name="region" id="region_search_patrol_month"
                                        onchange="EvaluationInfoList.getPartyEntitiesByRegionAndDepartment(this.value,'巡查部门',$('#partyEntity_search_patrol_month'))">

                                    <option value="" ></option>

                                    <option value="不限" >不限</option>

                                    <option value="成都市" >成都市</option>

                                    <option value="自贡市" >自贡市</option>

                                    <option value="攀枝花市" >攀枝花市</option>

                                    <option value="泸州市" >泸州市</option>

                                    <option value="德阳市" >德阳市</option>

                                    <option value="绵阳市" >绵阳市</option>

                                    <option value="广元市" >广元市</option>

                                    <option value="遂宁市" >遂宁市</option>

                                    <option value="内江市" >内江市</option>

                                    <option value="乐山市" >乐山市</option>

                                    <option value="南充市" >南充市</option>

                                    <option value="眉山市" >眉山市</option>

                                    <option value="宜宾市" >宜宾市</option>

                                    <option value="广安市" >广安市</option>

                                    <option value="达州市" >达州市</option>

                                    <option value="雅安市" >雅安市</option>

                                    <option value="巴中市" >巴中市</option>

                                    <option value="资阳市" >资阳市</option>

                                    <option value="阿坝藏族羌族自治州" >阿坝藏族羌族自治州</option>

                                    <option value="甘孜藏族自治州" >甘孜藏族自治州</option>

                                    <option value="凉山彝族自治州" >凉山彝族自治州</option>

                                </select>

                            </td>

                            <td style="border: none">

                                <select class="span2 chosen" data-with-diselect="1" data-placeholder="请选择查询对象" tabindex="1" name="partyEntity" id="partyEntity_search_patrol_month"
                                        onchange="EvaluationInfoList.setPartyEntities($('#partyEntity_search_patrol_month'))">

                                    <option value="" ></option>

                                    <option value="不限" >不限</option>

                                </select>

                            </td>

                            <td style="border: none;line-height: 34px;">

                                起始日期

                            </td>

                            <td>

                                <div class="m-wrap input-append date form_date" data-date-format="yyyy-MM" data-date-viewmode="years">
                                    <input id="txt_patrol_month_begin" class="m-wrap m-ctrl-medium" readonly size="16" type="text" value="" />
                                </div>
                                ~
                                <div  class="m-wrap input-append date form_date" data-date-format="yyyy-MM" data-date-viewmode="years">
                                    <input id="txt_patrol_month_end" class="m-wrap m-ctrl-medium" readonly size="16" type="text" value="" />
                                </div>

                            </td>

                            <div class="btn-group">

                                <!--<button id="sample_editable_1_new" class="btn green"> -->
                                <button id="btnSearch_patrol_month" class="btn blue">

                                    查询 <i class="icon-search"></i>

                                </button>

                            </div>



                        </div>

                    </div>

                    <div class="portlet box yellow">

                        <div class="portlet-title">

                            <!--TODO LIST：换成模块名称，即使一级标题的名称-->
                            <div class="caption"><i class="icon-edit"></i>巡查部门人员月度考核列表</div>

                            <div class="tools">

                                <a href="javascript:;" class="collapse"></a>

                            </div>

                        </div>

                        <div class="portlet-body">

                            <div class="clearfix">

                            </div>

                            <div class="row-fluid">

                                <!--TODO LIST:增加相应的表单以及表格信息-->
                                <div class="portlet-body">

                                    <!--TODO LIST:此处把table_user改成相关子模块的ID即可-->
                                    <table class="table table-striped table-hover table-bordered" id="table_evaluation_patrol_month">

                                        <thead>

                                        <tr>

                                            <th>考核对象</th>

                                            <th>考核月份</th>

                                            <th>核实率得分</th>

                                            <th>按期核实得分</th>

                                            <th>超期未核实得分</th>

                                            <th>漏报率得分</th>

                                            <th>总分数</th>

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

<%--<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-datepicker.js"></script>--%>

<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-datetimepicker.js"></script>

<%--<script type="text/javascript" src="${ctx}/s/media/js/clockface.js"></script>--%>

<%--<script type="text/javascript" src="${ctx}/s/media/js/date.js"></script>--%>

<%--<script type="text/javascript" src="${ctx}/s/media/js/daterangepicker.js"></script>--%>

<%--<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-timepicker.js"></script>--%>

<%--<script type="text/javascript" src="${ctx}/s/media/js/bootstrap-timepicker.js"></script>--%>

<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN PAGE LEVEL SCRIPTS -->

<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

<script src="${ctx}/s/app/evaluate/evaluation-info-list.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->
<script>

    $(function() {

        App.init(); // initlayout and core plugins

        EvaluationInfoList.init_patrol_month();

    })

</script>

<!-- END JAVASCRIPTS -->

<!-- END BODY -->
</body>

</html>