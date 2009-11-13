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
<g:if test="${cardInstance?.id}">
  <g:formRemote url="[controller:'card',action:'ajaxSave']" update="editCardDialog" name="cardForm" onSuccess="refreshMainView('#editCardDialog')">
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
        <input type="hidden" name="phase.id" value="${boardInstance.phases[0].id}" />
      </li>

      <g:if test="${cardInstance.events}">
        <li class="prop">
          <select name="events" size="4" multiple>
            <g:each in="${cardInstance.events}" var='event'>
              <option>${event.user.username} moved to ${event.newPhase.name}, on CardIndex: ${event.newCardIndex}</option>
            </g:each>
          </select>
        </li>
      </g:if>
    </ul>

      <input style="display: none;" class="save ui-state-default ui-corner-all" type="submit" value="<g:message code="_cardForm.button.submit"/>" />
  </g:formRemote>
</g:if>
<g:else>
  <g:formRemote url="[controller:'card',action:'ajaxSave']" update="createCardDialog" name="cardForm" onSuccess="refreshMainView('#createCardDialog')">
    <ul>

      <li class="prop">
        <label for="caseNumber"><g:message code="_cardForm.label.caseNumber"/></label>
        <input type="text" id="caseNumber" name="caseNumber"
               class="property ${hasErrors(bean:cardInstance,field:'caseNumber','errors')}"
               value="${fieldValue(bean:cardInstance,field:'caseNumber')}" />
      </li>
      <li class="prop">
        <label for="title"><g:message code="_cardForm.label.title"/></label>
        <input type="text" id="title" name="title"
               class="property ${hasErrors(bean:cardInstance,field:'title','errors')}"
               value="${fieldValue(bean:cardInstance,field:'title')}"/>
      </li>
      <li class="prop">
        <label for="description"><g:message code="_cardForm.label.description"/></label>
        <input type="text" id="description" name="description"
               class="property ${hasErrors(bean:cardInstance,field:'description','errors')}"
               value="${fieldValue(bean:cardInstance,field:'description')}"/>
        <input type="hidden" name="phase.id" value="${boardInstance.phases[0].id}" />
      </li>



    </ul>

      <input style="display: none;" type="submit" value="<g:message code="_cardForm.button.submit"/>" />
  </g:formRemote>
</g:else>