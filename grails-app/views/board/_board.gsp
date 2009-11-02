<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<div id="board">

  <g:each var="phase" in="${it.phases}">
    <div class="phaseHolder widthForcer">
      <h3>${phase.name}</h3>
      <ul class="phase" id="phase_${phase.id}">

        <g:each var="card" in="${phase.cards}">

          <li class="card" id="card_${card.id}">${card.title}</li>

        </g:each>

      </ul>

    </div>

  </g:each>

  <div class="leveler"></div>

</div>
