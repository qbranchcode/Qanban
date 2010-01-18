<%@ page contentType="text/html;charset=UTF-8" %>

<g:setProvider library="jquery"/>


<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>

  <g:hasErrors bean="${event}">
    <div>
      <g:renderErrors bean="${event}" as="list" />
    </div>
  </g:hasErrors>

<div class="content">
  <g:formRemote url="[controller:'user',action: 'update', params: [format: 'html']]"
          update="editUserDialog" name="userForm"
          onSuccess="alert('update plz');"
          >

    <div class="header">

      <avatar:gravatar email="${event.email}" size="38"/>
      <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
      <input type="text" class="property" name="userRealName" value="${event.userRealName?.encodeAsHTML()}"/>

    </div>

    <div class="properties">

      <label for="email" class="email"><g:message code="_userForm.label.email"/></label>
      <input type="text" class="property" name="email" value="${event.email?.encodeAsHTML()}"/>

      <label for="username"><g:message code="_userForm.label.username"/></label>
      <input type="text" class="property" value="${event.username?.encodeAsHTML()}"/>

      <label for="description"><g:message code="_userForm.label.description"/></label>
      <textarea id="tafix" type="text" class="property" name="description" value="${event.description?.encodeAsHTML()}"></textarea>

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
          <input type="password" name="passwdRepeat">
      </div>
      
    </div>

    <input type="hidden" name="id" value="${event.user.id}"/>
    <input style="display: none;" type="submit"/>

  </g:formRemote>

</div>

