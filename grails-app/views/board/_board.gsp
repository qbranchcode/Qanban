<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<div id="board">

  <ul id="phaseList">
    
    <g:each var="phase" in="${it.phases}">

      <li class="phaseWrapper" id="phaseWrapper_${phase.id}"style="width:${100/it.phases.size()-1}%; margin: 0 0.5%;">

        <div class="phaseHolder">
          <div class="phaseHeader">
            <h3><a href="${createLink(controller:'phase',action:'edit')}" class="editPhaseLink" id="phaseLink_${phase.id}">${phase.name}</a></h3>
            <!--<a href="${createLink(controller:'phase',action:'edit')}" class="editPhaseLink" id="phaseLink_${phase.id}">
              <img src="" alt="edit"/>
            </a>-->
            <div class="limitLine"><g:if test="${phase.cardLimit}">${phase.cards.size()}/${phase.cardLimit}</g:if></div>
          </div>
          <ul class="<g:if test="${phase.cardLimit && phase.cards.size() > 0 && (phase.cardLimit/phase.cards.size()) != 1 || phase.cardLimit != phase.cards.size()}">available</g:if> phase <g:if test="${phase.cardLimit}">cardLimit_${phase.cardLimit}</g:if>"
              id="phase_${phase.id}"
              style="height: <g:maxCardCount phases="${it.phases}" cardHeight="30" unit="px"/>" >

            <g:each var="card" in="${phase.cards}">

                <li class="card" id="card_${card.id}">${card.title}</li>

            </g:each>

          </ul>

        </div>

      </li>

    </g:each>
  </ul>
  <div class="leveler"></div>

</div>
