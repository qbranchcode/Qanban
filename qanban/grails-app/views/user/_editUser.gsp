<%@ page contentType="text/html;charset=UTF-8"%>

<g:setProvider library="jquery"/>

<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>

<g:hasErrors bean="${person}">
  <div>
    <g:renderErrors bean="${person}" as="list" />
  </div>
</g:hasErrors>

<div class="editUser" id="user_${person.id}">
  <div class="editUserData">
    <g:if test="${person.id == loggedInUser.id}"><g:message code="_userForm.updateYourself"/></g:if>
    <g:formRemote url="[controller:'user',action: 'update', params: [ format : 'html' , template : 'editUser' ]]"
            update="editBox" name="editUserForm"
            onSuccess="jQuery('#editBox').qRefreshDialog({formData: data, url: resources.userShowURL});">
      <span class="username">
        <label for="username"><g:message code="_userForm.label.username"/></label>
        <input type="text" class="property ${hasErrors(bean:person,field:'username','errors')}" name="username" readonly="readonly" value="${person.username?.encodeAsHTML()}"/>
      </span><br/>
      <span class="userRealName">
        <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
        <input type="text" class="property ${hasErrors(bean:person,field:'userRealName','errors')}" name="userRealName" value="${person.userRealName?.encodeAsHTML()}"/>
      </span><br/>
      <span class="email">
        <label for="email"><g:message code="_userForm.label.email"/></label>
        <input type="text" class="property ${hasErrors(bean:person,field:'email','errors')}" name="email" value="${person.email?.encodeAsHTML()}"/>
      </span><br/>
      <span class="description">
        <label for="description"><g:message code="_userForm.label.description"/></label>
        <textarea type="text" class="property description ${hasErrors(bean:person,field:'description','errors')}" name="description">${person.description?.encodeAsHTML()}</textarea>
      </span><br/>
      <div class="roles">
        <label for="roles"><g:message code="_userForm.label.roles"/></label><br/>
        <g:each var="entry" in="${roleMap}">
          ${entry.key.authority.encodeAsHTML()}
          <g:checkBox name="${entry.key.authority}" value="${entry.value}"/>
        </g:each>
        <input type="hidden" name="id" value="${person.id}"/>
        <input type="submit" class="ui-state-default ui-corner-all editInput" <g:if test="${person.id == loggedInUser.id}">disabled="disabled"</g:if> value="<g:message code="settings.update"/>"/>
      </div>
    </g:formRemote>
    <a href="#" class="deleteUserLink" id="user_${person.id}"><button class="ui-state-default ui-corner-all editInput" id="deleteUserDialog"><g:message code="settings.delete"/></button></a>
  </div>
</div>

