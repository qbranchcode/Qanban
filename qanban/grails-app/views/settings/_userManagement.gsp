<%@ page contentType="text/html;charset=UTF-8"%>

<g:setProvider library="jquery"/>

<div id="users" class="userColumn">
  <span class="ui-icon ui-icon-contact"/>
  <a href="#" class="addUserLink ui-icon ui-icon-circle-plus right"> </a>
  <h3 class="columnHeader"><g:message code="settings.usersHeader"/></h3>

  <ul class="userList">
    <g:each var="user" in="${users}">
      <g:render template="/user/user" bean="${user}"/>
    </g:each>
  </ul>
  <div class="userFilter">
    <label for="role.id"><g:message code="settings.filter"/></label>
    <g:each var="role" in="${roles}">
      <input type="radio" name="role.id" value="${role.id}" id="filterButton"/><label>${role.authority}</label>
    </g:each>
    <br/>
  </div>
</div>

<div id="editUserCol">
  <span class="ui-icon ui-icon-person"/>
  <h3 class="columnHeader"><g:message code="settings.editFieldHeader"/></h3>
  <div id="editBox">
    <g:render template="/user/editUser"/>
  </div>

</div>
<img class="bottomGfx" src="${resource(dir:"images", file:"userSettings.png")}" alt="user settings"/>