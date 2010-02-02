<%@ import="se.qbranch.qanban.Board" %>

<head>

  <meta name='layout' content='inside'/>

  <title>Qanban</title>

  <g:javascript library="jquery"/>

  <g:javascript src="jquery/jquery.ui.core.js"/>
  <g:javascript src="jquery/jquery.ui.sortable.js"/>
  <g:javascript src="jquery/jquery.ui.draggable.js"/>
  <g:javascript src="jquery/jquery.ui.droppable.js"/>
  <g:javascript src="jquery/jquery.ui.dialog.js"/>
  <g:javascript src="jquery/jquery.qanbanScripts.js"/>

  <g:render template="/javascript/qResources"/>

  <script type="text/javascript">

  jQuery().ready(function(options){

    <g:render template="/javascript/dialogs"/>

    $.qInit();
    $('.board').qInit();
    $('.phaseWrapper').qInit();

  });

  </script>

  
</head>

<body>

  <div id="thinTop">

    <div id="mainmenu">
      <ul>
        <li><a class="tab active" href="${createLink(controller:'mainView',action:'showBoard',params:[ 'board.id': board.id])}"><g:message code="mainView.tabs.board"/></a></li>
        <li><a class="tab" href="${createLink(controller:'mainView',action:'showLog',params:[ 'board.id': board.id])}"><g:message code="mainView.tabs.log"/></a></li>
        <li><a class="tab" href="${createLink(controller:'mainView',action:'showArchive',params:[ 'board.id': board.id])}">${board.phases[-1].title.encodeAsHTML()}</a></li>
        <g:ifAllGranted role="ROLE_QANBANADMIN"><li><a class="tab" href="${createLink(controller:'mainView',action:'showSettings',params:[ 'board.id': board.id])}"><g:message code="mainView.tabs.settings"/></a></li></g:ifAllGranted>
      </ul>
    </div>

    <div id="logout">
      <avatar:gravatar email="${loggedInUser.email}" size="26" />
      <span class="name">${loggedInUser.userRealName}</span><br/>
      <a href="${createLink(controller:'logout')}"><g:message code="layout.inside.menu.logOut"/></a>
    </div>

    <!--<img id="logo" src="${resource(dir:'images',file:'small_logo.png')}" alt="qanban"/>-->
    <!--<img id="logo" src="${resource(dir:'images',file:'qanban_jap_small.png')}" alt="qanban"/>-->
    <!--<img id="logo" src="${resource(dir:'images',file:'qanban_jap_small_w.png')}" alt="qanban"/>-->
    <!--<img id="logo" src="${resource(dir:'images',file:'qanban_red_small.png')}" alt="qanban"/>-->
    <img id="logo" src="${resource(dir:'images',file:'qanban_small.png')}" alt="qanban"/>


  </div>
  <div id="wrapPlacer">
  <div id="wrapper">
        <g:render template="/board/board" bean="${board}" />
  </div>
  </div>
</body>

