<%@ page contentType="text/html;charset=UTF-8"%>
<div class="generatePassword">
  <div class="passwordData">
    <h3 class="columnHeader"><g:message code="settings.generatePassword"/><br/></h3>
    <g:formRemote url="[controller:'user',action: 'generatePassword', params: [format: 'html']]"
    update="generatePassword" name="user"
    onSuccess="alert('passwordGenerate user');">
      <span class="email">
        <label><g:message code="_userForm.label.email"/></label>
        <input type="text" class="property" name="email"/>
      </span>
      <input type="submit" class="ui-state-default ui-corner-all editInput" value="<g:message code="settings.generatePassword"/>"/>
    </g:formRemote>
  </div>
</div>