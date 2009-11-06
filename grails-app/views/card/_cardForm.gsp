<%@ page contentType="text/html;charset=UTF-8" %>

<g:setProvider library="jquery"/>



  <g:if test="${flash.message}">
    <div>${flash.message}</div>
  </g:if>

  <g:hasErrors bean="${cardInstance}">
    <div>
      <g:renderErrors bean="${cardInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:formRemote url="[controller:'card',action:'ajaxSave']" update="createCard" name="cardForm" onSuccess="updateBoard()">
    <ul>

      <li class="prop">
        <label for="caseNumber"><g:message code="_cardForm.label.caseNumber"/></label>
        <input type="text" id="caseNumber" name="caseNumber"
               class="${hasErrors(bean:cardInstance,field:'caseNumber','errors')}"
               value="${fieldValue(bean:cardInstance,field:'caseNumber')}" />
      </li>
      <li class="prop">
        <label for="title"><g:message code="_cardForm.label.title"/></label>
        <input type="text" id="title" name="title"
               class="${hasErrors(bean:cardInstance,field:'title','errors')}"
               value="${fieldValue(bean:cardInstance,field:'title')}"/>
      </li>
      <li class="prop">
        <label for="description"><g:message code="_cardForm.label.description"/></label>
        <input type="text" id="description" name="description"
               class="${hasErrors(bean:cardInstance,field:'description','errors')}"
               value="${fieldValue(bean:cardInstance,field:'description')}"/>
        <input type="hidden" name="phase.id" value="1" />
      </li>

     

    </ul>

    <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
        <input class="save ui-state-default ui-corner-all" type="submit" value="<g:message code="_cardForm.button.submit"/>" />
        <button class="ui-state-default ui-corner-all" type="button" onclick="closeDialog()"><g:message code="_cardForm.button.close"/></button>
    </div>
  </g:formRemote>
