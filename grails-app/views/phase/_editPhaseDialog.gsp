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

<%--
	EDIT / SHOW 
--%>
<g:if test="${phaseInstance?.id}">
  <g:formRemote url="[controller:'phase',action:'ajaxSaveOrUpdate']" update="editPhaseDialog" name="phaseForm" onSuccess="refreshMainView('#editPhaseDialog', 'Success', 'Phase successfully updated')">
    <ul>


      <li class="prop">

        <label for="name"><g:message code="_phaseForm.label.name"/></label>
        <input type="text" id="name" name="name"
               class="value ${hasErrors(bean:phaseInstance,field:'name','errors')}"
               value="${fieldValue(bean:phaseInstance,field:'name')}"/>
      </li>

      <li class="prop">
        <label for="cardLimit"><g:message code="_phaseForm.label.cardLimit"/></label>
        <input type="text" id="cardLimit" name="cardLimit"
               class="${hasErrors(bean:phaseInstance,field:'cardLimit','errors')}"
               value="${fieldValue(bean:phaseInstance,field:'cardLimit')}" />
      </li>



    </ul>

    <input type="hidden" name="id" value="${phaseInstance.id}" />
    <input type="hidden" name="board.id" value="1" />

    
      <input style="display: none;" type="submit" value="<g:message code="_phaseForm.button.update"/>" />
     
  </g:formRemote>
</g:if>

<%--
	CREATE
--%>

<g:else>
  <g:formRemote url="[controller:'phase',action:'ajaxSaveOrUpdate']" update="createPhaseDialog" name="phaseForm" onSuccess="phaseFormRefresh(data,'#createPhaseDialog')">
    <ul>


      <li class="prop">

        <label for="name"><g:message code="_phaseForm.label.name"/></label>
        <input type="text" id="name" name="name"
               class="value ${hasErrors(bean:phaseInstance,field:'name','errors')}"
               value="${fieldValue(bean:phaseInstance,field:'name')}"/>
      </li>

      <li class="prop">
        <label for="cardLimit"><g:message code="_phaseForm.label.cardLimit"/></label>
        <input type="text" id="cardLimit" name="cardLimit"
               class="${hasErrors(bean:phaseInstance,field:'cardLimit','errors')}"
               value="${fieldValue(bean:phaseInstance,field:'cardLimit')}" />
      </li>



    </ul>

    <input type="hidden" name="board.id" value="1" />

    <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
      <input style="display: none;" type="submit" value="<g:message code="_phaseForm.button.save"/>" />
    </div>

  </g:formRemote>
</g:else>




