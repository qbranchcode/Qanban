<h2><g:message code="loginPage.auth.form.title"/></h2>
<form action='${postUrl}' method='POST' id='loginForm'>
  <div class="fieldWrapper">
    <label for='j_username'>
      <g:message code="loginPage.auth.form.label.username"/>
    </label>
    <input type='text' class='text_' name='j_username' id='j_username' value='${request.remoteUser}' />
  </div>
  <div class="fieldWrapper">
    <label for='j_password'>
      <g:message code="loginPage.auth.form.label.password"/>
    </label>
    <input type='password' class='text_' name='j_password' id='j_password' />
  </div>

  <div class="checkboxWrapper">

    <label for='remember_me'>
      <g:message code="loginPage.auth.form.label.remember"/>
    </label>

    <input type='checkbox' class='chk' name='_spring_security_remember_me' id='remember_me'
           <g:if test='${hasCookie}'>checked='checked'</g:if> />
  </div>

  <input type='submit' value='<g:message code="loginPage.auth.form.button.login"/>' />

</form>