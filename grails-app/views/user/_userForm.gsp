<%@ page contentType="text/html;charset=UTF-8" %>

<g:setProvider library="jquery"/>


<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>

<div class="content">
  <g:formRemote url="[controller:'user',action: 'update', params: [format: 'html']]"
          update="editUserDialog" name="userForm"
          onSuccess="userFormRefresh(data,'#editUserDialog','Success','Successfully updated the your user');"
          onClose="toggleSpinner()">

  <div class="header">

    <avatar:gravatar email="${person.email}" size="38"/>
    <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
    <input type="text" class="property" name="userRealName" value="${person.userRealName?.encodeAsHTML()}"/>

  </div>
  <div class="properties">

    <label for="email" class="email"><g:message code="_userForm.label.email"/></label>
    <input type="text" class="property" name="email" value="${person.email?.encodeAsHTML()}"/>

    <%--
    <label for="emailShow">Show email</label>
    <input type="checkbox" class="property"  value="${person.emailShow}"/>
    --%>
    <label for="description"><g:message code="_userForm.label.description"/></label>
    <textarea type="text" class="property" name="description" value="${person.description?.encodeAsHTML()}"></textarea>

  </div>


  <div class="static">
    <span class="label"><g:message code="_userForm.label.username"/></span>
    <span id="username" class="property">${person.username?.encodeAsHTML()}</span>
    <ul>
    <g:each in="${roleNames}" var='role'>
       <li>${role.encodeAsHTML()}</li>
    </g:each>
    </ul>
  </div>
  
  </g:formRemote>

</div>

