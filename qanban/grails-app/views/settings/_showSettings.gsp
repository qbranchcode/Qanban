<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery"/>

<link rel="stylesheet" href="${resource(dir:'css',file:'settings.css')}" />
<div id="settings">
  <div id="addUserMenu">
    <ul>
      <li><a href="#" class="addUserLink"><g:message code="settings.menu.addUser"/></a></li>
    </ul>
  </div>
  <style>.board { margin-top: 30px !important; }</style>

  <div id="users" class="userColumn"><br/><h3 class="columnHeader"><g:message code="settings.usersHeader"/></h3><br/>
    <ul class="userList">
      <g:each var="user" in="${users}">
        <g:render template="/user/user" bean="${user}"/>
      </g:each>
    </ul>
    <div class="userFilter"><g:message code="settings.filter"/><br/>
      <g:each var="role" in="${roles}">
        <input type="radio" name="role.id" value="${role.id}" id="filterButton"/><label>${role.authority}</label>
      </g:each>
      <br/>
    </div>
  </div>
  <div id="editField" class="editColumn"><br/><h3 class="columnHeader"><g:message code="settings.editFieldHeader"/></h3><br/>
    <div id="editBox">
      <g:if test="${editUser}">
        <g:render template="/user/editUser" model="[ 'editUser' : editUser , 'roles' : roles ]"/>
      </g:if>
      <g:else>
        <g:render template="/settings/emptyEdit" model="[ 'roles' : roles]"/>
      </g:else>
    </div>
    <div id="passwordBox">
      <g:render template="/settings/generatePassword"/>
    </div>
  </div>
  <div id="settingsInfo" class="infoColumn"><br/><h3 class="columnHeader"><g:message code="settings.infoHeader"/></h3><br/>
    <div id="infoBox">
      <g:message code="settings.infoText"/>
    </div>
  </div>
</div>