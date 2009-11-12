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
<g:formRemote url="[controller:'card',action:'ajaxSave']" update="createCardContent" name="cardForm" onSuccess="updateBoard();scanFormResult();">

  <div class="header">
    <div class="assignee">
      <!--input-->
    </div>
    <div class="info">

      	<input type="text" name="title" class="property" class="${hasErrors(bean:cardInstance,field:'title','errors')}"
             	<g:if test="${cardInstance}">
        		value="${fieldValue(bean:cardInstance,field:'title')}"
      		</g:if>
		<g:else>
			value="<g:message code="_cardForm.label.title"/>"
		</g:else> 
	/>
	<div id="caseNoWrapper">
		
		<input type="text" name="caseNumber" class="${hasErrors(bean:cardInstance,field:'caseNumber','errors')}"
			value="${fieldValue(bean:cardInstance,field:'caseNumber')}"/>
		<label for="caseNumber"><g:message code="_cardForm.label.caseNumber"/></label>

	</div>
      	<span class="date">Last updated: <g:formatDate format="MM/dd/yyyy hh:mm:ss" date="${cardInstance?.lastUpdated}" /></span>

    </div>
  </div>
  <div class="content">
    <label for="description"><g:message code="_cardForm.label.description"/></label>
    <textarea rows="10" cols="45" name="description" class="property ${hasErrors(bean:cardInstance,field:'description','errors')}">${fieldValue(bean:cardInstance,field:'description')}</textarea>
  </div>

      	<input type="hidden" name="phase.id" value="${boardInstance.phases[0].id}" />
  	<input style="display: none;" type="submit" value="<g:message code="_cardForm.button.submit"/>" />

</g:formRemote>
