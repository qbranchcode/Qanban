<%@ page contentType="text/html;charset=UTF-8"%>
<g:each var="user" in="${it}">
  <li class="user" id="user_${user.id}">
    <div class="userAvatar">
      <avatar:gravatar email="${user.email}" size="38"/>
    </div>
    <div class="userData">
      <span class="username"><g:message code="users.username"/>": ${user.username}</span><br/>
      <span class="userRealName"><g:message code="users.userRealName"/>: ${user.userRealName}</span><br/>
      <span class="email"><g:message code="users.email"/>: ${user.email}</span>
    </div>
  </li>
</g:each>