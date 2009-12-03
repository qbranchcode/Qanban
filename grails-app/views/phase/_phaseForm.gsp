<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<g:setProvider library="jquery"/>



<%--
	EDIT / SHOW 
--%>

<g:if test="${updateEvent}">

  <g:if test="${flash.message}">
    <div>${flash.message}</div>
  </g:if>

  <g:hasErrors bean="${updateEvent}">
    <div>
      <g:renderErrors bean="${updateEvent}" as="list" />
    </div>
  </g:hasErrors>

  <g:formRemote url="[controller:'phase',action:'update',params: [format: 'html']]" update="editPhaseDialog" name="phaseForm"
                onSuccess="phaseFormRefresh(data, '#editPhaseDialog', 'Success', 'Phase successfully updated')">
  
    <div class="content">

       <div id="nameWrapper">
        <label for="name"><g:message code="_phaseForm.label.name"/></label>
        <input type="text" id="name" name="name" value="${updateEvent?.name}"
               class="property ${hasErrors(bean:updateEvent,field:'name','errors')}"/>
       </div>
       <div id="cardLimitWrapper">
         <label for="cardLimit"><g:message code="_phaseForm.label.cardLimit"/></label>
          <input type="text" id="cardLimit" name="cardLimit" value="${updateEvent?.cardLimit}"
                 class="property ${hasErrors(bean:updateEvent,field:'cardLimit','errors')}"/>
       </div>
       <div class="leveler"></div>
       <div class="placementWrapper">
	  <label for="phase.idx"><g:message code="_phaseForm.placement.label"/></label><br/>
  	  <ul id="phasePlacer">
	      <g:each var="phase" in="${updateEvent.board.phases}">
	      	      <li class="phase
                            <g:if test='${phase.id == updateEvent.phase.id}'>current</g:if>
                            <g:else>old</g:else>
                          ">
		      	  <ul class="titleName">
  		      	  <g:each var="ch" in="${phase.name}">
			  	 <li>${ch}</li>
			  </g:each>
			  </ul>
                          <g:if test='${phase.id == updateEvent.phase.id}'><img src="<g:resource dir="images" file="currentPhaseFade.png"/>" /></g:if>
                          <g:else><img src="<g:resource dir="images" file="oldPhaseFade.png"/>" /></g:else>
			  
  		      </li>
	      </g:each>
	    </ul>
	</div>

    </div>
    <input type="hidden" name="position" value="${updateEvent.board.phases.indexOf(updateEvent.phase)}"
    <input type="hidden" name="id" value="${updateEvent.phase.id}" />
    <input style="display: none;" type="submit"  />
     
  </g:formRemote>
</g:if>




<%--
	CREATE
--%>


<g:elseif test="${createEvent}">

  <g:if test="${flash.message}">
    <div>${flash.message}</div>
  </g:if>

  <g:hasErrors bean="${createEvent}">
    <div>
      <g:renderErrors bean="${createEvent}" as="list" />
    </div>
  </g:hasErrors>
  
  <g:formRemote url="[controller:'phase',action:'create',params: [format: 'html']]" update="createPhaseDialog" name="phaseForm"
                onSuccess="phaseFormRefresh(data,'#createPhaseDialog')">

    <div class="content">

       <div id="nameWrapper">
        <label for="name"><g:message code="_phaseForm.label.name"/></label>
        <input type="text" id="name" name="name" value="${createEvent?.name}"
               class="property ${hasErrors(bean:createEvent,field:'name','errors')}"/>
       </div>
       <div id="cardLimitWrapper">
         <label for="cardLimit"><g:message code="_phaseForm.label.cardLimit"/></label>
          <input type="text" id="cardLimit" name="cardLimit" value="${createEvent?.cardLimit}"
                 class="property ${hasErrors(bean:createEvent,field:'cardLimit','errors')}"/>
       </div>
       <div class="leveler"></div>
       <div class="placementWrapper">
	  <label for="phase.idx"><g:message code="_phaseForm.placement.label"/></label><br/>
	  
  	  <ul id="phasePlacer">
	      <g:each var="phase" status="index" in="${boardInstance.phases}">

                      <g:if test="${createEvent?.position==index}">
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
              <g:if test="${ createEvent?.position == null || createEvent?.position == boardInstance.phases.size()}">
                <li class="phase new"></li>
              </g:if>
	    </ul>
	</div>

    </div>
    <input type="hidden" name="position" value="<g:if test='${createEvent?.position != null}'>${createEvent?.position}</g:if><g:else>${boardInstance.phases.size()}</g:else>"/>
    <input type="hidden" name="id" value="${createEvent?.phase?.id}"/>
    <input type="hidden" name="board.id" value="${boardInstance.id}" />
    <input style="display: none;" type="submit" />
    
  </g:formRemote>

</g:elseif>




