<h2><g:message code="loginPage.addUser.form.title"/></h2>
<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>
<g:hasErrors bean="${person}">
  <div class="errors">
    <g:renderErrors bean="${person}" as="list" />
  </div>
</g:hasErrors>
<g:formRemote name="loginForm" url="[action:'save',controller:'user']" method="POST">
  <ul>
    <li>
      <label for="username"><g:message code="loginPage.addUser.form.label.username"/></label>
      <input name="username" type="text" class="property ${hasErrors(bean:person,field:'username','errors')}"
             id="it.username" value="${it?.username?.encodeAsHTML()}"/>
    </li>

    <li>
      <label for="userRealName"><g:message code="loginPage.addUser.form.label.userRealName"/></label>
      <input name="userRealName" type="text" class="property ${hasErrors(bean:person,field:'userRealName','errors')}"
             id="it.userRealName" value="${it?.userRealName?.encodeAsHTML()}"/>
    </li>
    <li>
      <label for="email"><g:message code="loginPage.addUser.form.label.eMail"/></label>
      <input name="email" type="text" class="property ${hasErrors(bean:person,field:'email','errors')}"
             id="it.email" value="${it?.email?.encodeAsHTML()}"/>
    </li>
    <li>
      <input type="hidden" name="enabled" value="true"/>
      <input type="submit" value="<g:message code="loginPage.addUser.form.button.submit"/>"/>
    </li>
  </ul>
</g:formRemote>
