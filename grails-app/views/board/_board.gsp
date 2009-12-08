<%@ page contentType="text/html;charset=UTF-8"%>
    <style type="text/css">
	.phaseAutoWidth{ width: ${100/it.phases.size()-1}%; margin: 0 4px; }
	.phaseAutoHeight{ height: <qb:maxCardCount phases="${it.phases}" cardHeight="7" unit="em"/>; } /*7 em*/
    </style>
      <g:ifAllGranted role="ROLE_QANBANADMIN">
  <div id="adminmenu">
    <ul>
      <li><a href="${createLink(controller:'card',action:'create')}" class="addCardLink"><g:message code="layout.inside.menu.addCard"/></a></li>
      <li><a href="${createLink(controller:'phase',action:'create')}" class="addPhaseLink"><g:message code="layout.inside.menu.addPhase"/></a></li>
    </ul>
  </div>
</g:ifAllGranted>


<div id="board">


  <ul id="phaseList">
    
    <g:each var="phase" in="${it.phases}">	
	<g:render template="/phase/phase" model="['phase':phase]"/>
    </g:each>

  </ul>

  <div class="leveler"></div>

</div>
