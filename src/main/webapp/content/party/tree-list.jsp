<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/taglibs.jsp" %>
<%pageContext.setAttribute("currentHeader", "party");%>
<%pageContext.setAttribute("currentMenu", "party");%>
<%@page import="java.util.*" %>
<%@page import="com.casic.patrol.party.persistence.domain.*" %>
<%!
    public String generatePartyEntities(List<PartyEntity> partyEntities, long partyStructTypeId) {
        if (partyEntities == null) {
            return "";
        }
        try {
            String text = "<ul>";
            for (PartyEntity partyEntity : partyEntities) {
                text += generatePartyEntity(partyEntity, partyStructTypeId);
            }
            text += "</ul>";
            return text;
        } catch (Exception ex) {
            System.out.println("19 : " + ex);
            // ex.printStackTrace();
            return "";
        }
    }

    public String generatePartyEntity(PartyEntity partyEntity, long partyStructTypeId) {
        try {
            String text = "<li>";
            text += partyEntity.getName();
            List<PartyEntity> partyEntities = new ArrayList<PartyEntity>();
            for (PartyStruct partyStruct : partyEntity.getChildStructs()) {
                if (partyStruct.getPartyStructType().getId() == partyStructTypeId) {
                    partyEntities.add(partyStruct.getChildEntity());
                }
            }
            text += generatePartyEntities(partyEntities, partyStructTypeId);
            text += "</li>";
            return text;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("35 : " + ex);
            return "";
        }
    }
%>
<!doctype html>
<html>

<head>
    <%@include file="/common/meta.jsp" %>
    <title>&nbsp;</title>
    <%@include file="/common/s3.jsp" %>
    <script type="text/javascript">
        $(function () {
            $("#tree-listForm").validate({
                submitHandler: function (form) {
                    bootbox.animate(false);
                    var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
                    form.submit();
                },
                errorClass: 'validate-error'
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
        <div class="row-fluid" class="col-md-10" style="margin-left: 20px;">

            <div class="span12">
                <!-- start of main -->
                <section id="m-main" style="padding-top:65px;;margin-right: 30px">

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="glyphicon glyphicon-list"></i>
                            &nbsp;
                        </div>

                        <div class="panel-body">
                            <form name="orgForm" method="post" action="tree-list.do" class="form-inline">
                                <select name="partyStructTypeId" class="form-control">
                                    <c:forEach items="${partyStructTypes}" var="item">
                                        <option value="${item.id}" ${param.partyStructTypeId == item.id ? 'selected' : ''}>${item.name}</option>
                                    </c:forEach>
                                </select>
                                <button class="btn"><spring:message code='org.tree.list.view' text='查看'/></button>
                            </form>
                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="glyphicon glyphicon-list"></i>
                            &nbsp;
                        </div>

                        <div class="panel-body">


                            <c:set var="partyEntities" value="${partyEntities}"/>
                            <%
                                long partyStructTypeId = 0L;
                                String id = request.getParameter("partyStructTypeId");
                                try {
                                    partyStructTypeId = Long.parseLong(id);
                                } catch (Exception ex) {
                                }
                                List<PartyEntity> partyEntities = (List<PartyEntity>) pageContext.getAttribute("partyEntities");
                                out.print(generatePartyEntities(partyEntities, partyStructTypeId));
                            %>


                        </div>
                        </article>

                </section>
                <!-- end of main -->
            </div>
        </div>
    </div>
    <%@include file="/common/layout/footer.jsp" %>
</div>

</body>

</html>

