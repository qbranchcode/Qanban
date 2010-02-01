<%@ page contentType="text/html;charset=UTF-8" %>
<g:setProvider library="jquery"/>

<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>

<g:hasErrors bean="${person}">
  <div>
    <g:renderErrors bean="${person}" as="list" />
  </div>
</g:hasErrors>

<div class="content">

  <g:formRemote name="passwordForm"
          url="[controller:'user',action:'updatePassword',params:[format:'html',template:'passwordForm']]"
          update="changePasswordDialog"
          onSuccess="jQuery('#changePasswordDialog').qRefreshDialog({formData:data,successTitle:'${g.message(code:'_userPassForm.successTitle')}',successMessage:'${g.message(code:'_userPassForm.successMsg')}'});"
          onLoading="jQuery.toggleSpinner()"
          onComplete="jQuery.toggleSpinner()">

    <label for="oldPasswd"><g:message code="_userPassForm.label.oldPassword"/></label>
    <input type="password" class="${hasErrors(bean:person,field:'passwd','errors')}" name="passwdRepeat"/>

    <label for="newPasswd"><g:message code="_userPassForm.label.newPassword"/></label>
    <input type="password" class="${hasErrors(bean:person,field:'newPasswd','errors')}"  name="newPasswd"/>

    <label for="newPasswdRepeat"><g:message code="_userPassForm.label.newPasswordRepeat"/></label>
    <input type="password" class="${hasErrors(bean:person,field:'newPasswdRepeat','errors')}"  name="newPasswdRepeat"/>

    <input type="hidden" name="id" value="${person.id}"/>
    <input type="hidden" name="email" value="${person.email}"/>
    <input style="display: none;" type="submit"/>

  </g:formRemote>
  
</div>