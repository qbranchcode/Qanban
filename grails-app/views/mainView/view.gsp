<%@ import="se.qbranch.qanban.Board" %>

<head>

  <meta name='layout' content='inside'/>

  <title>Qanban</title>

  <g:javascript library="jquery"/>
  <g:javascript src="jquery/jquery.ui.core.js"/>
  <g:javascript src="jquery/jquery.ui.sortable.js"/>
  <g:javascript src="jquery/jquery.ui.draggable.js"/>
  <g:javascript src="jquery/jquery.ui.dialog.js"/>
  <g:render template="/javascript/qanbanOnLoad"/>
  <g:render template="/javascript/qanbanFunctions"/>

</head>

<body>
  <div id="wrapper">
        <g:render template="/board/board" bean="${board}" />
  </div>
</body>

