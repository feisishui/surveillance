<%@ page language="java" pageEncoding="UTF-8" %>
<style type="text/css">
#accordion .panel-heading {
	cursor: pointer;
}
#accordion .panel-body {
	padding:0px;
}
.col-md-2 {
    width: 225px;
}
</style>

      <!-- start of sidebar -->
<div class="panel-group col-md-2" id="accordion" role="tablist" aria-multiselectable="true" style="padding-top:15px;margin-bottom: 0px">

  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="collapse-header-party" data-toggle="collapse" data-parent="#accordion" href="#collapse-body-party" aria-expanded="true" aria-controls="collapse-body-party">
      <h4 class="panel-title">
	    <i class="glyphicon glyphicon-list"></i>
        组织机构管理
      </h4>
    </div>
    <div id="collapse-body-party" class="panel-collapse collapse ${currentMenu == 'party' ? 'in' : ''}" role="tabpanel" aria-labelledby="collapse-header-party">
      <div class="panel-body">
        <ul class="nav nav-list">
		  <li><a href="${tenantPrefix}/party/org-list.do"><i class="glyphicon glyphicon-list"></i> <spring:message code="layout.leftmenu.tree" text="人力资源组织"/></a></li>

            <div id="collapse-body-org" class="panel-collapse collapse ${currentMenu == 'party' ? 'in' : ''}" role="tabpanel" aria-labelledby="collapse-header-org">
                <div class="panel-body">

                    <select style="width:100%" onchange="location.href='org-list.do?partyStructTypeId=' + this.value">
                        <c:forEach items="${partyStructTypes}" var="item">
                            <option value="${item.id}" ${item.id == param.partyStructTypeId ? 'selected' : ''}>${item.name}</option>
                        </c:forEach>
                    </select>
                    <ul id="treeMenu" class="ztree"></ul>
                </div>
            </div>

          <li><a href="${tenantPrefix}/party/party-entity-list.do"><i class="glyphicon glyphicon-list"></i> <spring:message code="layout.leftmenu.org" text="组织机构"/></a></li>
		  <li><a href="${tenantPrefix}/party/party-struct-list.do"><i class="glyphicon glyphicon-list"></i> <spring:message code="layout.leftmenu.struct" text="组织机构结构"/></a></li>
		  <%--<li><a href="${tenantPrefix}/party/party-type-list.do"><i class="glyphicon glyphicon-list"></i> <spring:message code="layout.leftmenu.type" text="组织机构类型"/></a></li>--%>
		  <%--<li><a href="${tenantPrefix}/party/party-struct-type-list.do"><i class="glyphicon glyphicon-list"></i> <spring:message code="layout.leftmenu.struct.type" text="组织机构结构类型"/></a></li>--%>
		  <%--<li><a href="${tenantPrefix}/party/party-struct-rule-list.do"><i class="glyphicon glyphicon-list"></i> <spring:message code="layout.leftmenu.struct.rule" text="组织机构结构规则"/></a></li>--%>
          <li><a href="${tenantPrefix}/content/user/user-info-list.jsp"><i class="glyphicon glyphicon-list"></i> <spring:message code="layout.leftmenu.struct.rule" text="返回上一级"/></a></li>
        </ul>
      </div>
    </div>
  </div>


		<%--<footer id="m-footer" class="text-center">--%>
		  <%--<hr>--%>
		  <%--&copy;--%>
		<%--</footer>--%>

</div>
      <!-- end of sidebar -->

<script type="text/javascript">
    var setting = {
        async: {
            enable: true,
            url: "${tenantPrefix}/rs/party/tree?partyStructTypeId=${partyStructType.id}"
        },
        callback: {
            onClick: function(event, treeId, treeNode) {
                location.href = '${tenantPrefix}/party/org-list.do?partyStructTypeId=${partyStructTypeId}&partyEntityId=' + treeNode.id;
            }
        }
    };

    var zNodes =[];

    $(function(){
        $.fn.zTree.init($("#treeMenu"), setting, zNodes);
    });
</script>

