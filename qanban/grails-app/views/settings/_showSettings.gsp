<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery"/>

<link rel="stylesheet" href="${resource(dir:'css',file:'settings.css')}" />
<div id="settings">
  <div id="users" class="column"><br/><h3 class="columnHeader">Users</h3><br/>
    <ul class="userList">
      <g:each var="user" in="${users}">
        <g:render template="/user/user" bean="${user}"/>
      </g:each>
    </ul>
    <div class="userFilter">Filter by role:<br/>
      <g:formRemote name="filterUserByRole" url="[controller:'mainView',action: 'filterUserByRole']">
        <g:each var="role" in="${roles}">
          <input type="checkbox" name="role.id" value="${role.id}" /><label>${role.authority}</label>
        </g:each>
        <br/>
        <input type="submit" class="ui-state-default ui-corner-all editInput" value="Filter"/>
      </g:formRemote>
    </div>
  </div>
  <div id="roles" class="column"><br/><h3 class="columnHeader">Roles</h3><br/>
    <ul class="roleList">
      <g:each var="role" in="${roles}">
        <g:render template="/role/role" bean="${role}"/>
      </g:each>
    </ul>
  </div>
  <div id="editField" class="column"><br/><h3 class="columnHeader">EditField</h3><br/>
    <div id="editBox">
      <g:if test="${editUser}">
        <g:render template="/user/editUser" model="[ 'editUser' : editUser , 'roles' : roles ]"/>
      </g:if>
      <g:if test="${editRole}">
        <g:render template="/role/editRole" bean="${editRole}"/>
      </g:if>
    </div>
  </div>
</div>