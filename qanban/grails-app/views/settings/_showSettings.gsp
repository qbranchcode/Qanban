<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery"/>

<div id="settings">

  <div id="settingsMenu">
    <ul>
      <li class="active"><a href="#"><g:message code="settings.tabs.userManagement"/></a></li>
    </ul>
  </div>

  <div id="settingsContainer">
    <g:render template="/settings/userManagement" model="[users:users,roles:roles]"/>
  </div>

  <div id="settingSummary">
    <h3><g:message code="settings.infoHeader"/> </h3>
    <g:message code="settings.infoText"/>  
  </div>

</div>