<%@ page contentType="text/html;charset=UTF-8"%>
<div class="editRole" id="role_${it.id}">
  <div class="editRoleData">
    <span class="authority">
      <label><g:message code="_role.authority"/></label>
      <input type="text" class="property" name="authority" value="${it.authority?.encodeAsHTML()}"/>
    </span><br/>
    <span class="description">
      <label><g:message code="_role.description"/></label>
      <input type="text" class="property" name="description" value="${it.description?.encodeAsHTML()}"/>
    </span>
    <input type="hidden" name="role.id" value="${it.id}"/>
    <input type="submit" class="ui-state-default ui-corner-all editInput" value="<g:message code="settings.update"/>"/>
  </div>
</div>