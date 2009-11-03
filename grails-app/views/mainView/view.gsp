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

    /*****
    * Board logic
    */

    var sort = false;

    $('.phase').sortable({
      //connectWith: '.phase',
      //delay: 100,
      start: function(event,ui){
        sort = true;
      },
      update: function(event,ui){
        var newPos = ui.item.prevAll().length;
        var cardId = ui.item.attr('id').split('_')[1];
        $.post('${createLink(controller:'mainView',action:'moveCard')}',{'id': cardId , 'moveTo' : newPos },"json");
      }
    });

    $('.card').click(function(){
      showCard( $(this).attr('id').split('_')[1] );
    });


    function showCard(id){
      if( !sort ){
        //alert('klick on card #' + id);
      }
      sort = false;
    }


  </jq:jquery>

  <g:javascript>

    function updateBoard(){
      $('#boardWrapper').load('${createLink(controller:'mainView',action:'showBoard')}');
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

    <div id="cardFormWrapper" class="dialog">
      <g:render template="/card/cardForm"/>
    </div>

  </div>

</body>

