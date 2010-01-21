<%@ page contentType="text/html;charset=UTF-8"%>

<g:setProvider library="jquery"/>

<g:formRemote url="[controller:'user',action: 'update', params: [ format : 'html' , template : 'editUser' ]]"
        update="editBox" name="editUserForm">
  <div class="editUser" id="user_${editUser.id}">
    <div class="editUserData">
      <span class="username">
        <label for="username"><g:message code="_userForm.label.username"/></label>
        <input type="text" class="property" name="username" value="${editUser.username?.encodeAsHTML()}"/>
      </span><br/>
      <span class="userRealName">
        <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
        <input type="text" class="property" name="userRealName" value="${editUser.userRealName?.encodeAsHTML()}"/>
      </span><br/>
      <span class="email">
        <label for="email"><g:message code="_userForm.label.email"/></label>
        <input type="text" class="property" name="email" value="${editUser.email?.encodeAsHTML()}"/>
      </span>
      <span class="description">
        <label for="description"><g:message code="_userForm.label.description"/></label>
        <textarea type="text" class="property description" name="description">${editUser.description?.encodeAsHTML()}</textarea>
      </span>
      <div class="roles">
        <label for="roles"><g:message code="_userForm.label.roles"/></label><br/>
        <g:each var="role" in="${roles}">
          <g:checkBox name="${role.authority}"/> <label for="role">${role.authority}</label>
        </g:each>
        <input type="hidden" name="id" value="${editUser.id}"/>
        <input type="submit" class="ui-state-default ui-corner-all editInput" value="<g:message code="settings.update"/>"/>
      </div>
    </div>
  </div>
</g:formRemote>
