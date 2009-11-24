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
  <g:formRemote url="[controller:'phase',action:'ajaxSaveOrUpdate']" update="editPhaseDialog" name="phaseForm"
                onSuccess="phaseFormRefresh(data, '#editPhaseDialog', 'Success', 'Phase successfully updated')">
    
        <div class="header">
      <div class="info">
        <label for="name"><g:message code="_phaseForm.label.name"/></label>
        <input type="text" id="name" name="name"
               class="property ${hasErrors(bean:phaseInstance,field:'name','errors')}"
               value="${fieldValue(bean:phaseInstance,field:'name')}"/>

        <span class="date"></span>

        <div class="cardLimitWrapper">
          <label for="caseNumber"><g:message code="_phaseForm.label.cardLimit"/></label>
          <input type="text" id="cardLimit" name="cardLimit"
                 class="property ${hasErrors(bean:phaseInstance,field:'cardLimit','errors')}"
                 value="${fieldValue(bean:phaseInstance,field:'cardLimit')}"/>
        </div>
      </div>
    </div>
    <input type="hidden" name="phase.idx" value="${boardInstance.phases.indexOf(phaseInstance)}"
    <input type="hidden" name="id" value="${phaseInstance.id}" />
    <input type="hidden" name="board.id" value="${boardInstance.id}" />
    <input style="display: none;" type="submit"  />
     
  </g:formRemote>
</g:if>

<%--
	CREATE
--%>

<g:else>
  <g:formRemote url="[controller:'phase',action:'ajaxSaveOrUpdate']" update="createPhaseDialog" name="phaseForm"
                onSuccess="phaseFormRefresh(data,'#createPhaseDialog')">
    <div class="content">

       <div id="nameWrapper">
        <label for="name"><g:message code="_phaseForm.label.name"/></label>
        <input type="text" id="name" name="name" value="${phaseInstance?.name}"
               class="property ${hasErrors(bean:phaseInstance,field:'name','errors')}"/>
       </div>
       <div id="cardLimitWrapper">
         <label for="cardLimit"><g:message code="_phaseForm.label.cardLimit"/></label>
          <input type="text" id="cardLimit" name="cardLimit" value="${phaseInstance?.cardLimit}"
                 class="property ${hasErrors(bean:phaseInstance,field:'cardLimit','errors')}"/>
       </div>
       <div class="leveler"></div>
       <div class="placementWrapper">
	  <label for="phase.idx"><g:message code="_phaseForm.placement.label"/></label><br/>
	  <input type="hidden" name="phase.idx" value="${boardInstance.phases.size()}"/>
  	  <ul id="phasePlacer">
	      <g:each var="phase" in="${boardInstance.phases}">
	      	      <li class="phase old">
		      	  <ul class="titleName">
  		      	  <g:each var="ch" in="${phase.name}">
			  	 <li>${ch}</li>
			  </g:each>
			  </ul>
			  <img src="<g:resource dir="images" file="oldPhaseFade.png"/>" />
  		      </li>
	      </g:each>
	      <li class="phase new"></li>
	    </ul>
	</div>

    </div>

    <input type="hidden" name="id" value="${phaseInstance?.id}"/>
    <input type="hidden" name="board.id" value="${boardInstance.id}" />
    <input style="display: none;" type="submit" />
    
  </g:formRemote>
</g:else>




