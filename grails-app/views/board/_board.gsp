<%@ page contentType="text/html;charset=UTF-8"%>
    <style type="text/css">
	.phaseAutoWidth{ width: ${100/it.phases.size()-1}%; margin: 0 4px; }
	.phaseAutoHeight{ height: <g:maxCardCount phases="${it.phases}" cardHeight="7" unit="em"/>; } /*7 em*/
    </style>
<div id="board">

  <ul id="phaseList">
    
    <g:each var="phase" in="${it.phases}">	
	<g:render template="/phase/phase" model="['phase':phase]"/>
    </g:each>

  </ul>

  <div class="leveler"></div>

</div>
