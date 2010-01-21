<%@ page contentType="text/html;charset=UTF-8"%>
<div class="emptyEdit">
  <div class="editUserData">
    <span class="username">
      <label><g:message code="_userForm.label.username"/></label>
      <input type="text" class="property" name="username"/>
    </span><br/>
    <span class="userRealName">
      <label><g:message code="_userForm.label.userRealName"/></label>
      <input type="text" class="property" name="userRealName"/>
    </span><br/>
    <span class="email">
      <label><g:message code="_userForm.label.email"/></label>
      <input type="text" class="property" name="email"/>
    </span>
    <span class="description">
      <label><g:message code="_userForm.label.description"/></label>
      <textarea type="text" class="property description" name="description"></textarea>
    </span>
    <div class="roles">
      <label><g:message code="_userForm.label.roles"/></label><br/>
      <g:each var="role" in="${roles}">
        <g:checkBox name="${role.authority}"/> <label>${role.authority}</label>
      </g:each>
    </div>
  </div>
</div>