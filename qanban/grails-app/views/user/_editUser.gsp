<%@ page contentType="text/html;charset=UTF-8"%>

<g:setProvider library="jquery"/>

<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>

<g:hasErrors bean="${event}">
  <div>
    <g:renderErrors bean="${event}" as="list" />
  </div>
</g:hasErrors>

  <div class="editUser" id="user_${editUser.id}">
    <div class="editUserData">
      <g:formRemote url="[controller:'user',action: 'update', params: [ format : 'html' , template : 'editUser' ]]"
        update="editBox" name="editUserForm"
        onSuccess="jQuery('#editBox').qRefreshDialog({formData: data, url: resources.userShowURL});">
      <span class="username">
        <label for="username"><g:message code="_userForm.label.username"/></label>
        <input type="text" class="property ${hasErrors(bean:event,field:'username','errors')}" name="username" readonly="readonly" value="${event.username?.encodeAsHTML()}"/>
      </span><br/>
      <span class="userRealName">
        <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
        <input type="text" class="property ${hasErrors(bean:event,field:'userRealName','errors')}" name="userRealName" value="${event.userRealName?.encodeAsHTML()}"/>
      </span><br/>
      <span class="email">
        <label for="email"><g:message code="_userForm.label.email"/></label>
        <input type="email" class="property ${hasErrors(bean:event,field:'email','errors')}" name="email" value="${event.email?.encodeAsHTML()}"/>
      </span>
      <span class="description">
        <label for="description"><g:message code="_userForm.label.description"/></label>
        <textarea type="text" class="property description ${hasErrors(bean:event,field:'description','errors')}" name="description">${event.description?.encodeAsHTML()}</textarea>
      </span>
      <div class="roles">
        <label for="roles"><g:message code="_userForm.label.roles"/></label><br/>
        <g:each var="role" in="${roles}">
          <g:checkBox name="${role.authority}"/> <label for="role">${role.authority}</label>
        </g:each>
        <input type="hidden" name="id" value="${editUser.id}"/>
        <input type="submit" class="ui-state-default ui-corner-all editInput" value="<g:message code="settings.update"/>"/>
      </div>
      </g:formRemote>
      <g:if test="${editUser.id != loggedInUser.id}">
        <a href="#" class="deleteUserLink" id="user_${editUser.id}"><button class="ui-state-default ui-corner-all editInput" id="deleteUserDialog"><g:message code="settings.delete"/></button></a>
      </g:if>
    </div>
  </div>

