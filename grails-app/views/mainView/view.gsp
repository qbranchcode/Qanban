<%@ import="se.qbranch.qanban.Board" %>

<head>

  <meta name='layout' content='inside'/>

  <title>Qanban</title>

  <%-- <link rel="stylesheet" href="${resource(dir:'css',file:'qDialog.css')}" /> --%>

  <g:javascript library="jquery"/>
  <g:javascript src="jquery/jquery.ui.core.js"/>
  <g:javascript src="jquery/jquery.ui.sortable.js"/>
  <g:javascript src="jquery/jquery.ui.draggable.js"/>
  <g:javascript src="jquery/jquery.ui.dialog.js"/>
  <g:javascript src="qanban.js"/>
  <jq:jquery>

    var dilogFormBuffer = {};
    
    $createCardDialog = $('<div id="createCardDialog"></div>');

    $createCardDialog.dialog({
      autoOpen: false,
      modal: true,
      title: "<g:message code="mainView.jQuery.dialog.addCardForm.title"/>",
      buttons: {
                    <g:message code="_cardForm.button.submit"/> : function(){ 
                      $createCardDialog.find('input[type="submit"]').click();                      
                    }
      } 
    });

    $('.addCardLink').click(function(event){
      $createCardDialog.load('${createLink(controller:'card',action:'ajaxShowForm')}',{'board.id':${board.id}},function(){$createCardDialog.dialog('open');});
      event.preventDefault();
    });

    $createPhaseDialog = $('<div id="createPhaseDialog"></div>');
    
    $createPhaseDialog.dialog({
      autoOpen: false,
      modal: true,
      title: "<g:message code="mainView.jQuery.dialog.addPhaseForm.title"/>",
      buttons: {
                    <g:message code="_phaseForm.button.save"/> : function(){
                      $createPhaseDialog.find('input[type="submit"]').click();
                    }
      }
    });

    $('.addPhaseLink').click(function(event){
      $createPhaseDialog.dialog('open');
      $createPhaseDialog.load('${createLink(controller:'phase',action:'ajaxPhaseForm')}');
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

  function getJSONCard(formData){
  alert('formData: '+ formData);
/*$.get('${createLink(controller:"card",action:"show")}/'+id+'.js','json',function(data,textStatus){alert(data);});*/
  }

function deletePhaseDialog(id){
  
      $('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to delete the phase?</p></div>').dialog({
        resizable: false,
        height:140,
        modal: true,
        buttons: {
            <g:message code="yes"/>: function() {

                    $.ajax({  url: '${createLink(controller:'phase',action:'ajaxDelete')}',
                              data: {'id': id},
                              type: 'POST',
                              success: function() {
                                updateBoard();
                              },
                              error: function (XMLHttpRequest, textStatus, errorThrown) {
                                 $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.errorDeletingPhase.content"/></p></div>').dialog({
                                  modal: true,
                                  buttons: {
                                  <g:message code="ok"/>: function() {
                                      $(this).dialog('close');
                                    }
                                  }  
                                });
                                updateBoard();
                              }});
                    $(this).dialog('close');
            },
            <g:message code="no"/>: function() {
                    $(this).dialog('close');
            }
        }
      });
   }



  function setupSortable(){

      <g:each var="phase" status="i" in="${board.phases}">

            $('#phase_${phase.id}').sortable({
                <g:if test="${i + 1 < board.phases.size()}">
                    connectWith: '#phase_${board.phases[i+1].id}.available',
                    start: function(event,ui){
                        sort = true;
                        $(".phase:not('#phase_${phase.id},#phase_${board.phases[i+1].id}.available')").parent().animate({opacity: 0.3},300);                    },
                </g:if>
                <g:else>
                    start: function(event,ui){
                      sort = true;
                      $(".phase:not('#phase_${phase.id}')").parent().animate({opacity: 0.3},300); 
                    },
                </g:else>
                    placeholder: 'placeholder',
                    stop: function(event,ui){
                        var newPos = ui.item.prevAll().length;
                        var cardId = ui.item.attr('id').split('_')[1];
                        var newPhase = ui.item.parent().attr('id').split('_')[1];
                        $phases = $('.phase');
                        var maxCards = 0;
                        $phases.each(function(){
                            var $phase = $(this);
                            var numberOfChildren = $phase.children().size();
                            var classList = $phase.attr('class').split(' ');

                            $.each(classList, function(index, item){
                                var classSubstings = item.split('_');
                                if( classSubstings[0] == 'cardLimit' ){
                                    $phase.parent().find('.limitLine').html(numberOfChildren + '/' + classSubstings[1]);
                                }
                            });

                            if( numberOfChildren > maxCards ){
                                maxCards = numberOfChildren;
                            }
                        });
                        var height = ( maxCards * $('.card').height()) +'px';
                        $phases.animate({height: height, opacity: 1},300);
        
                        $.post(
                            '${createLink(controller:'mainView',action:'moveCard')}',
                            {'id': cardId , 'moveToCardsIndex' : newPos , 'moveToPhase' : newPhase},
                            function(data){
                                updateBoard();
                                if( !data.result ){
                                    $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.illegalMove"/></p></div>').dialog({
                                        modal: true,
                                        buttons: {
                                              <g:message code="ok"/>: function() {
                                                  $(this).dialog('close');
                                                  updateBoard();
                                              }
                                        }
                                    });
                                }
                            },
                            "json");
                    }
            });

      </g:each>

      $editPhaseDialog = $('<div id="editPhaseDialog"></div>');
      $editPhaseDialog.dialog({
            autoOpen: false,
            modal: true,
            title: "<g:message code="mainView.jQuery.dialog.editPhaseForm.title"/>",
            close: function(){ $editPhaseDialog.empty(); },
      });

      $('.editPhaseLink').click(function(event){
            var phaseId = $(this).attr('id').split('_')[1];
            $editPhaseDialog.load(
                '${createLink(controller:'phase',action:'ajaxPhaseForm')}',
                {'id': phaseId },
                function(){
                    $editPhaseDialog.dialog(
                        'option',
                        'buttons',
                        { '<g:message code="_phaseForm.button.delete"/>': function() {
                                $(this).dialog("close");
                                deletePhaseDialog(phaseId);
                            },
                           'Update' : function(){
                                $editPhaseDialog.find('input[type="submit"]').click();
                            }
                        });
                    $editPhaseDialog.dialog('open');
                }
            );
            event.preventDefault();
      });




      $editCardDialog = $('<div id="editCardDialog"></div>');
      $editCardDialog.dialog({
            autoOpen: false,
            modal: true,
            title: "<g:message code="mainView.jQuery.dialog.editCardForm.title"/>",
            width: 400
      });

      $('.editCardLink').click(function(event){
            var cardId = $(this).attr('id').split('_')[1];
            $editCardDialog.load('${createLink(controller:'card',action:'ajaxShowForm')}',{'board.id':${board.id} , 'id':cardId},function(){$editCardDialog.dialog('open');});
            event.preventDefault();
      });
    
  }

  function refreshMainView(dialogSelector, successTitle, successMessage){
      $dialog = $(dialogSelector);
      if( $dialog.find('.errors').size() == 0 ){
            $dialog.dialog('close');
            updateBoard();
            $('<div id="popup" title="'+successTitle+'">'+successMessage+'</div>').dialog({
			bgiframe: true,
			modal: true,
			buttons: {
				Ok: function() {
					$(this).dialog('close');
				}
			},
                        open: function(){
                                setTimeout("$('#popup').dialog('close')",1250);
                        }
            });
        }
  }

  function updateBoard(){
      $('#boardWrapper').load('${createLink(controller:'mainView',action:'showBoard')}',
      function(){
          setupSortable();
      });
  }

  function closeDialog(){
        $('.dialog').dialog('close');
  }

  </g:javascript>

  <style type="text/css">
    



#editCardDialog  .header {
 height: 44px;
 margin: 0 2px 0 2px;
 padding: 2px;
 border-bottom: 1px solid #ddd;
}

#editCardDialog .header .assignee {
 border: 1px solid #000;
 width: 40px;
 height: 40px;
 float: left;
 margin-right: 2px;
 background-color: grey;
}

#editCardDialog .header input{
 font-size: 20px;
 font-weight: bold;
 width: 342px;
 text-indent: 4px;
}


#editCardDialog .header .info{
 width: 324px;
 float: left;
}

#editCardDialog { overflow-x: hidden;}
#editCardDialog .header .date{ 
 display: inline-block;
 font-size: 11px;
 padding: 3px 5px 1px 10px;
 position: relative;
 width: 160px;
 float: left;
}

#editCardDialog .caseNumberWrapper{ float: left; width: 105px; padding-left: 40px;}
#editCardDialog .caseNumberWrapper *{ line-height: 1.7em; font-size: 1.0em !important;}
#editCardDialog .caseNumberWrapper input{  width: 30px !important; }
#editCardDialog .content {
 margin: 0 2px 0 2px;
 padding: 2px;
}

#editCardDialog  .content textarea {
 margin: 0 4px;
 width: 384px;
 padding: 2px;
}

#editCardDialog  select { width: 370px;}

#editCardDialog .top { background-color: #cc0000; }

#editCardDialog .property { 
 background-color: transparent;
 border: 1px solid transparent;
 border-bottom: 1px solid #fff;
 border-right: 1px solid #fff;
}

  </style>



</head>

<body>

  <div id="wrapper">

    <div id="boardWrapper">
      <g:render template="/board/board" bean="${board}" />
    </div>

  </div>

  <div id="crap"></div>
</body>

