<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<g:setProvider library="jquery"/>

<g:if test="${flash.message}">
  <div>${flash.message}</div>
</g:if>

<g:hasErrors bean="${phaseInstance}">
  <div>
    <g:renderErrors bean="${phaseInstance}" as="list" />
  </div>
</g:hasErrors>

<g:formRemote url="[controller:'phase',action:'ajaxSaveOrUpdate']" update="editPhase" name="editPhaseLink">
  <ul>


    <li class="prop">

      <label for="name">Name:</label>
      <input type="text" id="name" name="name"
             class="value ${hasErrors(bean:phaseInstance,field:'name','errors')}"
             value="${fieldValue(bean:phaseInstance,field:'name')}"/>
    </li>

    <li class="prop">
      <label for="cardLimit">Card Limit:</label>
      <input type="text" id="cardLimit" name="cardLimit"
             class="${hasErrors(bean:phaseInstance,field:'cardLimit','errors')}"
             value="${fieldValue(bean:phaseInstance,field:'cardLimit')}" />
    </li>

   

  </ul>

  <g:if test="${phaseInstance?.id}">
      <input type="hidden" name="id" value="${phaseInstance.id}" />
  </g:if>
  <input type="hidden" name="board.id" value="1" />

  <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
    <input class="save ui-state-default ui-corner-all" type="submit" value="Update" />
    <button class="ui-state-default ui-corner-all" type="button" onclick="closeEditPhaseDialog()">Close</button>
  </div>
</g:formRemote>