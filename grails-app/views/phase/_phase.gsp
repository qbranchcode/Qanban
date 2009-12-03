
<%@ page contentType="text/html;charset=UTF-8"%>

<li class="phaseWrapper phaseAutoWidth" id="phaseWrapper_${phase.id}">
   <div class="phaseHolder">
   	<div class="phaseHeader">
          <g:ifAllGranted role="ROLE_QANBANADMIN">
            <h3><a href="${createLink(controller:'phase',action:'edit')}" class="editPhaseLink" id="phaseLink_${phase.id}">${phase.title}</a></h3>
          </g:ifAllGranted>
          <g:ifNotGranted role="ROLE_QANBANADMIN">
            <h3>${phase.title}</h3>
          </g:ifNotGranted>
            <div class="limitLine">
		<g:if test="${phase.cardLimit}">${phase.cards.size()}/${phase.cardLimit}</g:if>
		<g:else><g:message code="_phase.noLimit"/></g:else>
	    </div>
      	</div>

        <ul class="phase phaseAutoHeight
	    <g:if test="${phase.cardLimit && phase.cards.size() > 0 && (phase.cardLimit/phase.cards.size()) != 1 || phase.cardLimit != phase.cards.size()}">
		available
	    </g:if>
     	    <g:if test="${phase.cardLimit}">
		cardLimit_${phase.cardLimit}
	    </g:if>"
                id="phase_${phase.id}">

            <g:each var="card" in="${phase.cards}">
	    	<g:render template="/card/card" bean="${card}"/>
            </g:each>

      	</ul>
   </div>
</li>
