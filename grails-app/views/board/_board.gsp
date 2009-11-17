<%@ page contentType="text/html;charset=UTF-8"%>

<div id="board">

  <ul id="phaseList">
	
    <style type="text/css">
	.phaseAutoWidth{ width: ${100/it.phases.size()-1}%; margin: 0 0.5%; }
	.phaseAutoHeight{ height: <g:maxCardCount phases="${it.phases}" cardHeight="30" unit="px"/>; }
    </style>
    
    <g:each var="phase" in="${it.phases}">	
	<g:render template="/phase/phase" bean="${phase}"/>
    </g:each>

  </ul>

  <div class="leveler"></div>

</div>
