
<%@ page contentType="text/html;charset=UTF-8"%>

<li class="phaseWrapper phaseAutoWidth" id="phaseWrapper_${phase.id}">
   <div class="phaseHolder">
   	<div class="phaseHeader">

            <g:ifAllGranted role="ROLE_QANBANADMIN">
              <a href="#edit" class="editPhaseLink" id="phaseLink_${phase.id}"> </a>
            </g:ifAllGranted>
            <h3>${phase.title}</h3>

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
            <li class="cardSpacer"></li>
            <g:each var="card" in="${phase.cards}">
              
	    	<g:render template="/card/card" bean="${card}"/>
            </g:each>

      	</ul>
   </div>
</li>
