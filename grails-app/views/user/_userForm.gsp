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
    <h3>${person.userRealName?.encodeAsHTML()}</h3>

  </div>
  <div class="properties">

    <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
    <input type="text" class="property" name="userRealName" value="${person.userRealName?.encodeAsHTML()}"/>

    <label for="email"><g:message code="_userForm.label.email"/></label>
    <input type="text" class="property" name="email" value="${person.email?.encodeAsHTML()}"/>

    <%--
    <label for="emailShow">Show email</label>
    <input type="checkbox" class="property"  value="${person.emailShow}"/>
    --%>
    <label for="description"><g:message code="_userForm.label.description"/></label>
    <textarea type="text" class="property" name="description" value="${person.description?.encodeAsHTML()}</textarea>

  </div>
  
  <div class="roles">
    <g:each in="${roleNames}" var='name'>
      <li>${name}</li>
    </g:each>
  </div>
  
  </g:formRemote>

</div>

