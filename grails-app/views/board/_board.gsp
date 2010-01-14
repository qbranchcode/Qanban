<%@ page contentType="text/html;charset=UTF-8"%>

<g:if test="${it.phases.size() > 0}">
    <style type="text/css">
    <g:if test="${session.showArchive}">
      .phaseAutoWidth{ width: ${ ( 800/it.phases.size() )  - 8 }px; margin: 0 4px; }
    </g:if>
    <g:else>
      .phaseAutoWidth{ width: ${ ( 800/(it.phases.size() - 1 ) )  - 8 }px; margin: 0 4px; }
    </g:else>

	.phaseAutoHeight{ height: <qb:maxCardCount phases="${it.phases}" cardHeight="7" unit="em"/>; } /*7 em*/
    </style>
</g:if>


<g:ifAllGranted role="ROLE_QANBANADMIN">
  <div id="adminmenu">
    <ul>
      <li><a href="#" class="addCardLink"><g:message code="layout.inside.menu.addCard"/></a></li>
      <li><a href="#" class="addPhaseLink"><g:message code="layout.inside.menu.addPhase"/></a></li>
    </ul>
  </div>
  <style>.board { margin-top: 30px !important; }</style>
</g:ifAllGranted>



<div id="board_${it.id}" class="board">


  <ul id="phaseList">

    <qb:renderPhases phases="${it.phases}" showArchive="${session.showArchive}" template="/phase/phase"/>

  </ul>

  <div class="leveler"></div>

</div>
