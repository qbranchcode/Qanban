<%@ page contentType="text/html;charset=UTF-8"%>
<g:each var="user" in="${it}">
  <li class="user" id="user_${user.id}">
    <div class="userAvatar">
      <avatar:gravatar email="${user.email}" size="38"/>
    </div>
    <div class="userData">
      <span class="username">Username: ${user.username}</span><br/>
      <span class="userRealName">Real Name: ${user.userRealName}</span><br/>
      <span class="email">Email: ${user.email}</span>
    </div>
  </li>
</g:each>