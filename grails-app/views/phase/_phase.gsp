
<%@ page contentType="text/html;charset=UTF-8"%>

<li class="phaseWrapper phaseAutoWidth" id="phaseWrapper_${it.id}">
   <div class="phaseHolder">
   	<div class="phaseHeader">
            <h3><a href="${createLink(controller:'phase',action:'edit')}" class="editPhaseLink" id="phaseLink_${it.id}">${it.name}</a></h3>
            <div class="limitLine">
		<g:if test="${it.cardLimit}">${it.cards.size()}/${it.cardLimit}</g:if>
		<g:else><g:message code="_phase.noLimit"/></g:else>
	    </div>
      	</div>
        <ul class="phase phaseAutoHeight
	    <g:if test="${it.cardLimit && it.cards.size() > 0 && (it.cardLimit/it.cards.size()) != 1 || it.cardLimit != it.cards.size()}">
		available
	    </g:if>  
     	    <g:if test="${it.cardLimit}">
		cardLimit_${it.cardLimit}
	    </g:if>"
                id="phase_${it.id}">

            <g:each var="card" in="${it.cards}">
	    	<g:render template="/card/card" bean="${card}"/>
            </g:each>

      	</ul>
   </div>
</li>
