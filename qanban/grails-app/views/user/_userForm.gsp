<%@ page contentType="text/html;charset=UTF-8" %>

<g:setProvider library="jquery"/>


<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>

<div class="content">
  <g:formRemote url="[controller:'user',action: 'update', params: [format: 'html']]"
          update="editUserDialog" name="userForm"
          onSuccess="userFormRefresh(data,'#editUserDialog','Success','Successfully updated the your user');"
          >

    <div class="header">

      <avatar:gravatar email="${person.email}" size="38"/>
      <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
      <input type="text" class="property" name="userRealName" value="${person.userRealName?.encodeAsHTML()}"/>

    </div>

    <div class="properties">

      <label for="email" class="email"><g:message code="_userForm.label.email"/></label>
      <input type="text" class="property" name="email" value="${person.email?.encodeAsHTML()}"/>

      <label for="username"><g:message code="_userForm.label.username"/></label>
      <input type="text" class="property" value="${person.username?.encodeAsHTML()}"/>

      <label for="description"><g:message code="_userForm.label.description"/></label>
      <textarea id="tafix" type="text" class="property" name="description" value="${person.description?.encodeAsHTML()}"></textarea>

      <div class="roles">

        <span class="label"><g:message code="_userForm.label.roles"/></span>
        <ul>
          <g:each in="${roleNames}" var='role' status='i'>
            <li>${role.encodeAsHTML()}</li>
          </g:each>
        </ul>

      </div>
      
    </div>

  </g:formRemote>

</div>

