<%@ page contentType="text/html;charset=UTF-8"%>
<li class="user" id="user_${it.id}">
  <div class="userAvatar">
    <avatar:gravatar email="${it.email}" size="38"/>
  </div>
  <div class="userData">
    <span class="username">Username: ${it.username}</span><br/>
    <span class="userRealName">Real Name: ${it.userRealName}</span><br/>
    <span class="email">Email: ${it.email}</span>
  </div>
</li>