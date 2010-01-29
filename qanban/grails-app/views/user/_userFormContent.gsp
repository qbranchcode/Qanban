<g:if test="${person.id}">
  <div class="header">
    <avatar:gravatar email="${person.email}" size="38"/>
    <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
    <input type="text" class="property ${hasErrors(bean:person,field:'userRealName','errors')}" name="userRealName" value="${person.userRealName?.encodeAsHTML()}"/>
  </div>
</g:if>
<g:else>
  <div class="header create">
    <label for="userRealName"><g:message code="_userForm.label.userRealName"/></label>
    <input type="text" class="property ${hasErrors(bean:person,field:'userRealName','errors')}" name="userRealName" value="${person.userRealName?.encodeAsHTML()}"/>
  </div>
</g:else>



<div class="properties">

  <label for="email" class="email"><g:message code="_userForm.label.email"/></label>
  <input type="text" class="property ${hasErrors(bean:person,field:'email','errors')}" name="email" value="${person.email?.encodeAsHTML()}"/>

  <label for="username"><g:message code="_userForm.label.username"/></label>
  <input type="text" class="property ${hasErrors(bean:person,field:'username','errors')}" name="username" value="${person.username?.encodeAsHTML()}"/>

  <label for="description"><g:message code="_userForm.label.description"/></label>
  <textarea id="tafix" type="text" class="property ${hasErrors(bean:person,field:'description','errors')}" name="description">${person.description?.encodeAsHTML()}</textarea>

  <g:if test="${person.id}">

    <div class="roles">

      <span class="label"><g:message code="_userForm.label.roles"/></span>
      <ul>
        <g:each in="${roleNames}" var='role' status='i'>
          <li>${role.encodeAsHTML()}</li>
        </g:each>
      </ul>

    </div>

    <div class="confirmPass">
      <label for="passwdRepeat"><g:message code="_userForm.label.confirmPasswd"/></label>
      <input type="password" name="passwdRepeat" class="${hasErrors(bean:person,field:'passwd','errors')}">
    </div>

  </g:if>
  <g:else>
    <div class="createRoles">

      <span class="label"></span>
      <g:each var="entry" in="${roleNames}">
        ${entry.key.authority.encodeAsHTML()}
        <g:checkBox name="${entry.key.authority}" value="${entry.value}"/>
      </g:each>
    </div>

    <div class="password">
      <label for="passwd"><g:message code="_userForm.label.passwd"/></label>
      <input type="password" name="passwd" class="${hasErrors(bean:person,field:'passwd','errors')}"><br/>
      <label for="passwdRepeat" class="repeatPass"><g:message code="_userForm.label.passwdRepeat"/></label>
      <input type="password" name="passwdRepeat" class="${hasErrors(bean:person,field:'passwdRepeat','errors')} repeatPassInput">
    </div>
  </g:else>

</div>

<input type="hidden" name="id" value="${person?.id}"/>
<input style="display: none;" type="submit"/>