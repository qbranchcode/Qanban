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
  <g:formRemote url="[controller:'user',action: 'update', params: [format: 'html']]"
          update="editUserDialog" name="userForm"
          onSuccess="jQuery('#editUserDialog').qRefreshDialog({formData:data,successTitle:'${g.message(code:'_userForm.update.successTitle')}',successMessage:'${g.message(code:'_userForm.update.successMsg')}'});"
          onLoading="jQuery.toggleSpinner()"
          onComplete="jQuery.toggleSpinner()">

    <div class="header">

      <avatar:gravatar email="${person.email}" size="38"/>
      <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
      <input type="text" class="property ${hasErrors(bean:person,field:'userRealName','errors')}" name="userRealName" value="${person.userRealName?.encodeAsHTML()}"/>

    </div>

    <div class="properties">

      <label for="email" class="email"><g:message code="_userForm.label.email"/></label>
      <input type="text" class="property ${hasErrors(bean:person,field:'email','errors')}" name="email" value="${person.email?.encodeAsHTML()}"/>

      <label for="username"><g:message code="_userForm.label.username"/></label>
      <input type="text" class="property ${hasErrors(bean:person,field:'username','errors')}" name="username" value="${person.username?.encodeAsHTML()}"/>

      <label for="description"><g:message code="_userForm.label.description"/></label>
      <textarea id="tafix" type="text" class="property ${hasErrors(bean:person,field:'description','errors')}" name="description">${person.description?.encodeAsHTML()}</textarea>

      <div class="roles">

        <span class="label"><g:message code="_userForm.label.roles"/></span>
        <ul>
          <g:each in="${roleNames}" var='role' status='i'>
            <li>${role.encodeAsHTML()}</li>
          </g:each>
        </ul>

      </div>

      <div class="confirmPass">
          <label for="passwdRepeat"><g:message code="_userForm.label.passwd"/></label>
          <input type="password" name="passwdRepeat" class="${hasErrors(bean:person,field:'passwd','errors')}">
      </div>
      
    </div>

    <input type="hidden" name="id" value="${person.id}"/>
    <input style="display: none;" type="submit"/>

  </g:formRemote>

</div>

