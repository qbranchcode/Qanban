<%@ page contentType="text/html;charset=UTF-8"%>
<meta name='layout' content='inside'/>
<div id="users">
    <br/><br/><br/><br/>
    <h1>Online Users</h1><br/>
    <p>
    <g:each var="user" in="${onlineUsers}">
	${user.username}
    </g:each>
    </p>
</div>
