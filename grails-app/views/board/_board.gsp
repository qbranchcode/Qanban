<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<div id="board">

  <ul id="phaseList">
    
    <g:each var="phase" in="${it.phases}">

      <li class="phaseWrapper widthForcer">

        <div class="phaseHolder">

          <h3>${phase.name}</h3>
          <ul class="phase" id="phase_${phase.id}">

            <g:each var="card" in="${phase.cards}">

              <g:if test="${card}">
                <li class="card" id="card_${card.id}">${card.title}</li>
              </g:if>
            </g:each>

          </ul>

        </div>

      </li>

    </g:each>

  </ul>
  <div class="leveler"></div>

</div>
