<%@ page contentType="text/html;charset=UTF-8"%>

<g:setProvider library="jquery"/>


<div class="editUser" id="user_${person?.id}">
    <g:formRemote url="[controller:'user',action: 'update', params: [ format : 'html' , template : 'editUser' ]]"
            update="editBox" name="editUserForm"
            onSuccess="jQuery('#editBox').qRefreshDialog({formData: data, url: resources.userShowURL});">

        <label for="username"><g:message code="_userForm.label.username"/></label>
        <input type="text" class="property ${hasErrors(bean:person,field:'username','errors')}" name="username" readonly="readonly" value="${person?.username?.encodeAsHTML()}"/>

        <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
        <input type="text" class="property ${hasErrors(bean:person,field:'userRealName','errors')}" name="userRealName" value="${person?.userRealName?.encodeAsHTML()}"/>

        <label for="email"><g:message code="_userForm.label.email"/></label>
        <input type="text" class="property ${hasErrors(bean:person,field:'email','errors')}" name="email" value="${person?.email?.encodeAsHTML()}"/>

        <label for="description"><g:message code="_userForm.label.description"/></label>
        <textarea type="text" class="property description ${hasErrors(bean:person,field:'description','errors')}" name="description">${person?.description?.encodeAsHTML()}</textarea>

      <g:if test="${roleMap}">

      <label for="roles"><g:message code="_userForm.label.roles"/></label>


        <div class="roles">
          <ul>
            <g:each var="entry" in="${roleMap}">
              <li>
                <g:checkBox name="${entry.key.authority}" value="${entry.value}"/>
                <span class='roleLabel'>${entry.key.authority.encodeAsHTML()}</span>
              </li>
            </g:each>
          </ul>
        </div>
      </g:if>

      <input type="hidden" name="id" value="${person?.id}"/>
      <input type="submit" class="ui-state-default ui-corner-all editInput" <g:if test="${person?.id == loggedInUser?.id}">disabled="disabled"</g:if> value="<g:message code="settings.update"/>"/>
      <button class="ui-state-default ui-corner-all editInput deleteUserLink" id="user_${person?.id}"><g:message code="settings.delete"/></button>
      
    </g:formRemote>



</div>

<g:if test="${flash.message}">
  <div class="message ui-state-highlight"><span class="ui-icon ui-icon-info"/>${flash.message}</div>
</g:if>
<g:if test="${person && person.id == loggedInUser.id}">
  <div class="message ui-state-highlight"><span class="ui-icon ui-icon-info"/><g:message code="_userForm.updateYourself"/></div>
</g:if>
<g:hasErrors bean="${person}">
  <div class="message ui-state-error"><span class="ui-icon ui-icon-alert"/>
    <g:renderErrors bean="${person}" as="list" />
  </div>
</g:hasErrors>