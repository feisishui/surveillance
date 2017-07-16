<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/taglibs.jsp" %>
<%pageContext.setAttribute("currentHeader", "party");%>
<%pageContext.setAttribute("currentMenu", "party");%>
<!doctype html>
<html>

<head>
    <%@include file="/common/meta.jsp"%>
    <title>编辑</title>
    <%@include file="/common/s3.jsp" %>
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
            $("#party-struct-ruleForm").validate({
                submitHandler: function(form) {
                    bootbox.animate(false);
                    var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
                    form.submit();
                },
                errorClass: 'validate-error'
            });
            $('#orgStructRule_orgStructType').select2({
                placeholder: "请选择类型",
                allowClear: true
            });
            $('#orgStructRule_parentOrgType').select2({
                placeholder: "请选择上级类型",
                allowClear: true
            });
            $('#orgStructRule_childOrgType').select2({
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



                <form id="orgStructRuleForm" method="post" action="party-struct-rule-save.do" class="form-horizontal">
                    <c:if test="${model != null}">
                        <input id="orgStructRule_orgStructRuleId" type="hidden" name="id" value="${model.id}">
                    </c:if>
                    <div class="form-group">
                        <label class="control-label col-md-1" for="orgStructRule_orgStructType"><spring:message code="org.structrule.input.type" text="类型"/></label>
                        <div class="col-sm-5">
                            <select class="span4 chosen" id="orgStructRule_orgStructType" name="partyStructTypeId">
                                <c:forEach items="${partyStructTypes}" var="item">
                                    <option value="${item.id}" ${model.partyStructType.id==item.id ? 'selected' : ''}>${item.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-1" for="orgStructRule_parentOrgType"><spring:message code="org.structrule.input.parenttype" text="上级类型"/></label>
                        <div class="col-sm-5">
                            <select class="span4 chosen" id="orgStructRule_parentOrgType" name="parentTypeId">
                                <c:forEach items="${partyTypes}" var="item">
                                    <option value="${item.id}" ${model.parentType.id==item.id ? 'selected' : ''}>${item.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-1" for="orgStructRule_childOrgType"><spring:message code="org.structrule.input.childtype" text="下级类型"/></label>
                        <div class="col-sm-5">
                            <select class="span4 chosen" id="orgStructRule_childOrgType" name="childTypeId">
                                <c:forEach items="${partyTypes}" var="item">
                                    <option value="${item.id}" ${model.childType.id==item.id ? 'selected' : ''}>${item.name}</option>
                                </c:forEach>
                            </select>
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

