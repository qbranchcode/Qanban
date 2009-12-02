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
  <g:formRemote url="[controller:'phase',action:'updateAndMove']" update="editPhaseDialog" name="phaseForm"
                onSuccess="phaseFormRefresh(data, '#editPhaseDialog', 'Success', 'Phase successfully updated')"
                onComplete="toggleSpinner()">
    
   
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
  	  <ul id="phasePlacer">
	      <g:each var="phase" in="${boardInstance.phases}">
	      	      <li class="phase
                            <g:if test='${phase.id == phaseInstance.id}'>current</g:if>
                            <g:else>old</g:else>
                          ">
		      	  <ul class="titleName">
  		      	  <g:each var="ch" in="${phase.name}">
			  	 <li>${ch}</li>
			  </g:each>
			  </ul>
                          <g:if test='${phase.id == phaseInstance.id}'><img src="<g:resource dir="images" file="currentPhaseFade.png"/>" /></g:if>
                          <g:else><img src="<g:resource dir="images" file="oldPhaseFade.png"/>" /></g:else>
			  
  		      </li>
	      </g:each>
	    </ul>
	</div>

    </div>
    <input type="hidden" name="newPhaseidx" value="${boardInstance.phases.indexOf(phaseInstance)}"
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
                onSuccess="phaseFormRefresh(data,'#createPhaseDialog')"
                onComplete="toggleSpinner()">
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
	  
  	  <ul id="phasePlacer">
	      <g:each var="phase" status="index" in="${boardInstance.phases}">

                      <g:if test="${phasePosition==index}">
                          <li class="phase new"></li>
                      </g:if>
	      	      <li class="phase old ${index}">
		      	  <ul class="titleName">
  		      	  <g:each var="ch" in="${phase.name}">
                              <li>${ch}</li>
			  </g:each>
			  </ul>
			  <img src="<g:resource dir="images" file="oldPhaseFade.png"/>" />
  		      </li>
	      </g:each>
              <g:if test="${ phasePosition == null || phasePosition == boardInstance.phases.size()}">
                <li class="phase new"></li>
              </g:if>
	    </ul>
	</div>

    </div>
    <input type="hidden" name="phase.idx" value="<g:if test='${phasePosition != null}'>${phasePosition}</g:if><g:else>${boardInstance.phases.size()}</g:else>"/>
    <input type="hidden" name="id" value="${phaseInstance?.id}"/>
    <input type="hidden" name="board.id" value="${boardInstance.id}" />
    <input style="display: none;" type="submit" />
    
  </g:formRemote>
</g:else>




