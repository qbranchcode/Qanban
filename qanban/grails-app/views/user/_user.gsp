<%@ page contentType="text/html;charset=UTF-8"%>
<li class="user" id="user_${it.id}">
  <div class="userAvatar">
    <avatar:gravatar email="${it.email}" size="38"/>
  </div>
  <div class="userData">
    <span class="username"><g:message code="users.username"/>: ${it.username}</span><br/>
    <span class="userRealName"><g:message code="users.userRealName"/>: ${it.userRealName}</span><br/>
    <span class="email"><g:message code="users.email"/>: ${it.email}</span>
  </div>
</li>