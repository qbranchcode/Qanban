<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<div id="board">

  <ul id="phaseList">
    
    <g:each var="phase" in="${it.phases}">

      <li class="phaseWrapper" style="width:${100/it.phases.size()-1}%; margin: 0 0.5%;">

        <div class="phaseHolder">

          <h3><a href="${createLink(controller:'phase',action:'edit')}" class="editPhaseLink" id="phaseLink_${phase.id}">${phase.name}</a></h3>
          <ul class="phase" id="phase_${phase.id}">

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
