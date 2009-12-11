<%@ page contentType="text/html;charset=UTF-8"%>

<g:if test="${it.phases.size() > 0}">
    <style type="text/css">
	.phaseAutoWidth{ width: ${100/it.phases.size()-1}%; margin: 0 4px; }
	.phaseAutoHeight{ height: <qb:maxCardCount phases="${it.phases}" cardHeight="7" unit="em"/>; } /*7 em*/
    </style>
</g:if>


<g:ifAllGranted role="ROLE_QANBANADMIN">
  <div id="adminmenu">
    <ul>
      <li><a href="${createLink(controller:'card',action:'create')}" class="addCardLink"><g:message code="layout.inside.menu.addCard"/></a></li>
      <li><a href="${createLink(controller:'phase',action:'create')}" class="addPhaseLink"><g:message code="layout.inside.menu.addPhase"/></a></li>
    </ul>
  </div>
  <style>#board { margin-top: 30px !important; }</style>
</g:ifAllGranted>



<div id="board">


  <ul id="phaseList">

    <qb:renderPhases phases="${it.phases}" showArchive="false" template="/phase/phase"/>

  </ul>

  <div class="leveler"></div>

</div>
