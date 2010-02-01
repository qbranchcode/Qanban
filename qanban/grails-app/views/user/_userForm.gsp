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
  <g:if test="${person.id}">
    <g:formRemote url="[controller:'user',action: 'update', params: [format: 'html']]"
            update="editUserDialog" name="userForm"
            onSuccess="jQuery('#editUserDialog').qRefreshDialog({formData:data,successTitle:'${g.message(code:'_userForm.update.successTitle')}',successMessage:'${g.message(code:'_userForm.update.successMsg')}'});"
            onLoading="jQuery.toggleSpinner()"
            onComplete="jQuery.toggleSpinner()">

      <g:render template="userFormContent" model="[person:person,roleNames:roleNames]"/>

    </g:formRemote>
  </g:if>
  <g:else>
    <g:formRemote url="[controller:'user',action: 'save', params: [format: 'html', template: 'userForm']]"
            update="createUserDialog" name="userForm"
            onSuccess="jQuery('#createUserDialog').qRefreshDialog({formData:data,successTitle:'${g.message(code:'_userForm.create.successTitle')}',successMessage:'${g.message(code:'_userForm.create.successMsg')}'});
            jQuery('#editBox').qRefreshDialog({formData: data, url: resources.userShowURL});"
            onLoading="jQuery.toggleSpinner()"
            onComplete="jQuery.toggleSpinner()">

      <g:render template="userFormContent" model="[person:person,roleNames:roleNames]"/>

    </g:formRemote>
  </g:else>

</div>