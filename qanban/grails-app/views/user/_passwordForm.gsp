<%@ page contentType="text/html;charset=UTF-8" %>
<g:setProvider library="jquery"/>

<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>

<div class="content">

  <g:formRemote name="passwordForm"
          url="[controller:'user',action:'update',params:[format:'html']]"
          update="changePasswordDialog"
          onSuccess="alert('update plz');">

    <label for="oldPasswd"><g:message code="_userForm.label.oldPassword"/></label>
    <input type="password" name="oldPasswd"/>

    <label for="newPasswd"><g:message code="_userForm.label.newPassword"/></label>
    <input type="password" name="newPasswd"/>

    <label for="newPasswdRepeat"><g:message code="_userForm.label.newPasswordRepeat"/></label>
    <input type="password" name="newPasswdRepeat"/>

    <input type="hidden" name="id" value="${id}"/>
    <input style="display: none;" type="submit"/>

  </g:formRemote>
  
</div>