<%@ import="se.qbranch.qanban.Board" %>

<head>

  <meta name='layout' content='inside'/>

  <title>Qanban</title>

  <g:javascript library="jquery"/>
  <g:javascript src="jquery/jquery.ui.core.js"/>
  <g:javascript src="jquery/jquery.ui.sortable.js"/>
  <g:javascript src="jquery/jquery.ui.draggable.js"/>
  <g:javascript src="jquery/jquery.ui.dialog.js"/>

  <jq:jquery>

    $editPhaseDialog = $('<div id="editPhase" class="dialog"></div>');
    $editPhaseDialog.dialog({
      autoOpen: false,
      modal: true,
      title: "Change Card Limit"
    });

    $('.editPhaseLink').click(function(event){
      var phaseId = $(this).attr('id').split('_')[1];
      $editPhaseDialog.dialog('open');
      $editPhaseDialog.load('${createLink(controller:'phase',action:'ajaxPhaseForm')}',{'id': phaseId });
      event.preventDefault();
    });

    $createCardDialog = $('<div id="createCard" class="dialog">as</div>');
    $createCardDialog.dialog({
      autoOpen: false,
      modal: true,
      title: "Add new card"
    });

    $('.addCardLink').click(function(event){
      $createCardDialog.dialog('open');
      $createCardDialog.load('${createLink(controller:'card',action:'ajaxShowForm')}');
      event.preventDefault();
    });

    /*****
    * Board logic
    */

    var sort = false;

    /*  Enables the phases to be sortable
    $('#phaseList').sortable({
      
    });
    */

    $('.card').click(function(){
      showCard( $(this).attr('id').split('_')[1] );
    });


    function showCard(id){
      if( !sort ){
        //alert('klick on card #' + id);
      }
      sort = false;
    }

    setupSortable();


  </jq:jquery>

  <g:javascript>

  function setupSortable(){
     $('.phase').sortable({
      connectWith: '.phase',
      start: function(event,ui){
        sort = true;
      },
      stop: function(event,ui){
        var newPos = ui.item.prevAll().length;
        var cardId = ui.item.attr('id').split('_')[1];
        var newPhase = ui.item.parent().attr('id').split('_')[1];
        $.post(
          '${createLink(controller:'mainView',action:'moveCard')}',
          {'id': cardId , 'movePosition' : newPos , 'movePhase' : newPhase},
          function(data){
            if( !data.result ){
              updateBoard();
            }
          },
          "json");
      }
    });
    }

    function updateBoard(){
      $('#boardWrapper').load('${createLink(controller:'mainView',action:'showBoard')}',function(){setupSortable();});
    }

    function closeAddCard(){
        $createCardDialog.dialog('close');
    }

    function closeEditPhaseDialog(){
        $editPhaseDialog.dialog('close');
    }

  </g:javascript>

  <style type="text/css">

    .widthForcer { width:${100/board.phases.size()-1}%; margin: 0 0.5%;}
  </style>



</head>

<body>

  <div id="wrapper">

    <div id="boardWrapper">
      <g:render template="/board/board" bean="${board}" />
    </div>

  </div>

</body>

