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
            $("#party-entityForm").validate({
                submitHandler: function(form) {
                    bootbox.animate(false);
                    var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
                    form.submit();
                },
                errorClass: 'validate-error'
            });
            $('#orgentity_orgType').select2({
                placeholder: "请选择类型",
                allowClear: true
            });
            $('#orgentity_ref').select2({
                placeholder: "请选择引用人员",
                allowClear: true
            });
            ;
            $('#orgentity_region').select2({
                placeholder: "请选择区域",
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


                            <form id="orgEntityForm" method="post" action="party-entity-save.do" class="form-horizontal">
                                <c:if test="${model != null}">
                                    <input id="orgentity_id" type="hidden" name="id" value="${model.id}">
                                </c:if>
                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgentity_orgType"><spring:message code="org.org.input.type" text="类型"/>:</label>
                                    <div class="col-sm-5">
                                        <select class="span5 chosen" id="orgentity_orgType" name="partyTypeId">
                                            <c:forEach items="${partyTypes}" var="item">
                                                <option value="${item.id}" ${model.partyType.id==item.id ? 'selected' : ''}>${item.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgentity_name"><spring:message code="org.org.input.name" text="名称"/>:</label>
                                    <div class="col-sm-5">
                                        <input id="orgentity_name" type="text" name="name"style="font-size: 12px" value="${model.name}" class="span5 form-control required" maxlength="50">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgentity_region">区域:</label>
                                    <div class="col-sm-5">
                                        <select class="span5 chosen" id="orgentity_region" name="region">
                                            <option value="" >无</option>
                                            <option value="成都市" ${model.region=='成都市' ? 'selected' : ''}>成都市</option>
                                            <option value="自贡市" ${model.region=='自贡市' ? 'selected' : ''}>自贡市</option>
                                            <option value="攀枝花市" ${model.region=='攀枝花市' ? 'selected' : ''}>攀枝花市</option>
                                            <option value="泸州市" ${model.region=='泸州市' ? 'selected' : ''}>泸州市</option>
                                            <option value="德阳市" ${model.region=='德阳市' ? 'selected' : ''}>德阳市</option>
                                            <option value="绵阳市" ${model.region=='绵阳市' ? 'selected' : ''}>绵阳市</option>
                                            <option value="广元市" ${model.region=='广元市' ? 'selected' : ''}>广元市</option>
                                            <option value="遂宁市" ${model.region=='遂宁市' ? 'selected' : ''}>遂宁市</option>
                                            <option value="内江市" ${model.region=='内江市' ? 'selected' : ''}>内江市</option>
                                            <option value="乐山市" ${model.region=='乐山市' ? 'selected' : ''}>乐山市</option>
                                            <option value="南充市" ${model.region=='南充市' ? 'selected' : ''}>南充市</option>
                                            <option value="眉山市" ${model.region=='眉山市' ? 'selected' : ''}>眉山市</option>
                                            <option value="宜宾市" ${model.region=='宜宾市' ? 'selected' : ''}>宜宾市</option>
                                            <option value="广安市" ${model.region=='广安市' ? 'selected' : ''}>广安市</option>
                                            <option value="达州市" ${model.region=='达州市' ? 'selected' : ''}>达州市</option>
                                            <option value="雅安市" ${model.region=='雅安市' ? 'selected' : ''}>雅安市</option>
                                            <option value="巴中市" ${model.region=='巴中市' ? 'selected' : ''}>巴中市</option>
                                            <option value="资阳市" ${model.region=='资阳市' ? 'selected' : ''}>资阳市</option>
                                            <option value="阿坝藏族羌族自治州" ${model.region=='阿坝藏族羌族自治州' ? 'selected' : ''}>阿坝藏族羌族自治州</option>
                                            <option value="甘孜藏族自治州" ${model.region=='甘孜藏族自治州' ? 'selected' : ''}>甘孜藏族自治州</option>
                                            <option value="凉山彝族自治州" ${model.region=='凉山彝族自治州' ? 'selected' : ''}>凉山彝族自治州</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgentity_ref">引用:</label>
                                    <div class="col-sm-5">
                                        <%--<input id="orgentity_ref" type="text" name="ref" value="${model.ref}" class="text" maxlength="50">--%>
                                        <select class="span5 chosen" id="orgentity_ref" name="ref">
                                            <option value="" >无</option>
                                            <c:forEach items="${users}" var="item">
                                                <option value="${item.id}" ${model.ref==item.id ? 'selected' : ''}>${item.userName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-1" for="orgentity_level">级别:</label>
                                    <div class="col-sm-5">
                                        <input id="orgentity_level" type="text" style="font-size: 12px" name="level" value="${model.level}" class="span5 form-control" maxlength="50">
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
                <!-- end of main -->
            </div>
        </div>
    </div>
    <%@include file="/common/layout/footer.jsp" %>
</div>

</body>

</html>

