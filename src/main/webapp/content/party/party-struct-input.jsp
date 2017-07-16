<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentHeader", "party");%>
<%pageContext.setAttribute("currentMenu", "party");%>
<!doctype html>
<html>

<head>
    <%@include file="/common/meta.jsp"%>
    <title>编辑</title>
    <%@include file="/common/s3.jsp"%>
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
    <script type="text/javascript">
        $(function() {
            $("#party-structForm").validate({
                submitHandler: function(form) {
                    bootbox.animate(false);
                    var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
                    form.submit();
                },
                errorClass: 'validate-error'
            });
            $('#orgStruct_partyStructType').select2({
                placeholder: "请选择类型",
                allowClear: true
            });
            $('#orgStruct_parentEntity').select2({
                placeholder: "请选择上级类型",
                allowClear: true
            });
            $('#orgStruct_childEntity').select2({
                placeholder: "请选择下级类型",
                allowClear: true
            });
        })
    </script>
</head>

<body class="page-header-fixed">
<%--<%@include file="/header/org.jsp"%>--%>

<%@include file="/common/layout/header-org.jsp" %>
<div class="page-container">
    <%@include file="/menu/party.jsp" %>
    <div class="page-content">
        <div class="row-fluid" class="col-md-10" style="margin-left: 10px;">

            <div class="span12">
                <!-- start of main -->
                <section id="m-main"  style="padding-top:25px;margin-right: 30px">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="glyphicon glyphicon-list"></i>
                            编辑
                        </div>

                        <div class="panel-body">


                            <form id="orgStructForm" method="post" action="party-struct-save.do" class="form-horizontal">
                                <c:if test="${model != null}">
                                    <input id="orgStruct_orgStructId" type="hidden" name="id" value="${model.id}">
                                </c:if>
                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgStruct_partyStructType"><spring:message code="org.struct.input.type" text="类型"/></label>
                                    <div class="col-sm-5">
                                        <select class="span4 chosen" id="orgStruct_partyStructType" name="partyStructTypeId">
                                            <c:forEach items="${partyStructTypes}" var="item">
                                                <option value="${item.id}" ${model.partyStructType.id==item.id ? 'selected' : ''}>${item.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgStruct_parentEntity"><spring:message code="org.struct.input.parententity" text="上级组织"/></label>
                                    <div class="col-sm-5">
                                        <select class="span4 chosen" id="orgStruct_parentEntity" name="parentEntityId">
                                            <option value="" }></option>
                                            <c:forEach items="${partyEntities}" var="item">
                                                <option value="${item.id}" ${model.parentEntity.id==item.id ? 'selected' : ''}>${item.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgStruct_childEntity"><spring:message code="org.struct.input.childentity" text="下级组织"/></label>
                                    <div class="col-sm-5">
                                        <select class="span4 chosen" id="orgStruct_childEntity" name="childEntityId">
                                            <c:forEach items="${partyEntities}" var="item">
                                                <option value="${item.id}" ${model.childEntity.id==item.id ? 'selected' : ''}>${item.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgStruct_partTime0">兼职</label>
                                    <div class="col-sm-5">
                                        <label><input id="orgStruct_partTime0" type="radio" name="partTime" value="0" ${model.partTime == 0 ? 'checked' : ''}>全职</label>
                                        <label><input id="orgStruct_partTime1" type="radio" name="partTime" value="1" ${model.partTime != 0 ? 'checked' : ''}>兼职</label>
                                    </div>
                                </div>
                                <%--<div class="form-group">--%>
                                    <%--<label class="control-label col-md-1" for="orgStruct_link">关联</label>--%>
                                    <%--<div class="col-sm-5">--%>
                                        <%--<input id="orgStruct_link" type="text" name="link" value="${model.link}" size="40" class="form-control number" maxlength="10">--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<div class="form-group">--%>
                                    <%--<label class="control-label col-md-1" for="orgStruct_priority">排序</label>--%>
                                    <%--<div class="col-sm-5">--%>
                                        <%--<input id="orgStruct_priority" type="text" name="priority" value="${model.priority}" size="40" class="form-control number" maxlength="10">--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgStruct_admin0">管理</label>
                                    <div class="col-sm-5">
                                        <label><input id="orgStruct_admin0" type="radio" name="admin" value="0" ${model.admin == 0 ? 'checked' : ''}>员工</label>
                                        <label><input id="orgStruct_admin0" type="radio" name="admin" value="1" ${model.admin != 0 ? 'checked' : ''}>负责人</label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-5">
                                        <button id="submitButton" class="btn"><spring:message code='core.input.save' text='保存'/></button>
                                        <button type="button" onclick="history.back();" class="btn"><spring:message code='core.input.back' text='返回'/></button>
                                    </div>
                                </div>
                            </form>

                        </div>
                        </article>
                    </div>
                </section>
            </div>
        </div>
    </div>
    <%@include file="/common/layout/footer.jsp" %>
</div>

</body>

</html>

