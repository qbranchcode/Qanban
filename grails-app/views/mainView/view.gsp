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

    /* Replace function */
    $.fn.replaceWith = function($newElement){ this.after($newElement).remove(); };



    /*Ajax Load Wrapper */
    $.fn.qLoad = function(url, data, successCallback, errorCallback) {
      var $element = $(this);

      var options = {};
      options.url = url;
      options.successCallback = successCallback;
      options.success = function(data, textStatus){
        var successCallback = options.successCallback;
        if(successCallback) {
          successCallback(data, textStatus);
        }
        $element.html(data);
      };
      options.data = data;
      options.errorCallback = errorCallback;
      options.error = function(XMLHttpRequest, textStatus, errorThrown){
            var errorCallback = options.errorCallback;
            if(XMLHttpRequest.status == 0) {
              $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.serverOffline"/></p></div>').dialog({
              modal: true });
            }
            if( errorCallback ){
              errorCallback(XMLHttpRequest, textStatus, errorThrown);
            }
      };
      options.type = "GET";

      $.ajax(options);

    }

    /*Ajax Post Wrapper*/
    $.qPost = function(url, data, successCallback, dataType) {
      var options = {};
      options.url = url;
      options.successCallback = successCallback;
      options.success = function(data, textStatus){
        var successCallback = options.successCallback;
        if(successCallback) {
          successCallback(data, textStatus);
        }
      };
      options.data = data;
      options.error = function(XMLHttpRequest, textStatus, errorThrown){
            if(XMLHttpRequest.status == 0) {
              $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.serverOffline"/></p></div>').dialog({
              modal: true });
            }
      };
      options.dataType = dataType;
      options.type = "POST";

      $.ajax(options);

    }

    /*Ajax Get Wrapper*/
    $.qGet = function(url, data, successCallback, dataType) {
      var options = {};
      options.url = url;
      options.successCallback = successCallback;
      options.success = function(data, textStatus){
        var successCallback = options.successCallback;
        if(successCallback) {
          successCallback(data, textStatus);
        }
      };
      options.data = data;
      options.error = function(XMLHttpRequest, textStatus, errorThrown){
            if(XMLHttpRequest.status == 0) {
              $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.serverOffline"/></p></div>').dialog({
              modal: true });
            }
      };
      options.dataType = dataType;
      options.type = "GET";

      $.ajax(options);

    }

    
/***********/
/* DIALOGS */
/***********/

    $createCardDialog = $('<div id="createCardDialog"></div>');

    $createCardDialog.dialog({
      autoOpen: false,
      modal: true,
      width: 400,
      title: "<g:message code="mainView.jQuery.dialog.addCardForm.title"/>",
      buttons: {
                    <g:message code="_cardForm.button.submit"/> : function(){ 
                      $createCardDialog.find('input[type="submit"]').click();                      
                    }
      } 
    });

    $('.addCardLink').click(function(event){
      $createCardDialog.qLoad('${createLink(controller:'card',action:'ajaxShowForm')}',{'board.id':${board.id}},function(){$createCardDialog.dialog('open');});
      event.preventDefault();
    });

    $createPhaseDialog = $('<div id="createPhaseDialog"></div>');
    
    $createPhaseDialog.dialog({
      autoOpen: false,
      modal: true,
      width: 400,
      title: "<g:message code="mainView.jQuery.dialog.addPhaseForm.title"/>",
      buttons: {
                    <g:message code="_phaseForm.button.save"/> : function(){
                      $createPhaseDialog.find('input[type="submit"]').click();
                    }
      }
    });

    $('.addPhaseLink').click(function(event){
      $createPhaseDialog.qLoad('${createLink(controller:'phase',action:'ajaxPhaseForm')}',{},function(){$createPhaseDialog.dialog('open');});
      event.preventDefault();
    });

      $editPhaseDialog = $('<div id="editPhaseDialog"></div>');
      $editPhaseDialog.dialog({
            autoOpen: false,
            modal: true,
            width: 400,
            title: "<g:message code="mainView.jQuery.dialog.editPhaseForm.title"/>",
            close: function(){ $editPhaseDialog.empty(); },
      });

      $editCardDialog = $('<div id="editCardDialog"></div>');
      $editCardDialog.dialog({
            autoOpen: false,
            modal: true,
            title: "<g:message code="mainView.jQuery.dialog.editCardForm.title"/>",
            width: 400
      });
    
/***************/
/* Board logic */
/***************/

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

      $('.phase').each(function(){ enableSortableOnPhase($(this)); });
      reconnectPhases();

    rescanBoardButtons();

    // The time out value is set to be 18,000,000 milli-seconds (or 5 minutes)
    function reloader(){
      setTimeout(function(){updateBoard();reloader();},18000000);
    }

    reloader();


  </jq:jquery>

  <g:javascript>


  


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



  function rescanBoardButtons(){

      $('.editPhaseLink').click(function(event){
            var phaseId = $(this).attr('id').split('_')[1];
            $editPhaseDialog.qLoad(
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



      $('.editCardLink').click(function(event){
            var cardId = $(this).attr('id').split('_')[1];
            $editCardDialog.qLoad('${createLink(controller:'card',action:'ajaxShowForm')}',{'board.id':${board.id} , 'id':cardId},function(){$editCardDialog.dialog('open');});
            event.preventDefault();
      });
    
  }
  


  function enableSortableOnPhase($phase){
  	   $phase.sortable({
	   	  placeholder: 'placeholder',
		  beforeStop: function(event,ui){

		  	var icv = $(this).sortable('option','initCardValues');
			$('#debug').html(icv.elementId + '-' + icv.initPhase + '.' + icv.initPos);
		  },
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

                        $phases.animate({'height':height},300);
			$phases.parent().animate({opacity: 1},300);

		  	var icv = $(this).sortable('option','initCardValues');
			$('#debug').html('Stop: ' + icv.elementId + '-' + icv.initPhase + '.' + icv.initPos);
			
                        $.qPost(
                            '${createLink(controller:'mainView',action:'moveCard')}',
                            {'id': cardId , 'moveToCardsIndex' : newPos , 'moveToPhase' : newPhase},
                            function(data){
                                if( !data.result ){
                                    alert('error moving card!');
                                }
                            },
                            "json");
                    }
      	   });
  }

  function reconnectPhases(){
  	   var $phases = $('.phase');

	   	var width = (100/$phases.size()) - 1;
		$('.phaseAutoWidth').width(width+'%');

	   $phases.each(function(index,$phase){
	       var $nextPhase = index < $phases.size() ? $( $phases[index+1] ) : false;
	       if( $nextPhase.attr('id') ){
	       	   
		   $(this).sortable('option','connectWith','#'+$nextPhase.attr('id')+'.available');
		   $(this).sortable('option','start', function(event,ui){
		      
		      var fadeIgnore = "#" + $(this).attr('id') + "," + $(this).sortable('option','connectWith');
		     
		      $(".phase:not('"+fadeIgnore+"')").parent().animate({opacity:0.3},300);
		      
                        var initPos = ui.item.prevAll().length;
                        var elementId = ui.item.attr('id');
                        var initPhase = ui.item.parent().attr('id').split('_')[1];
			

			$(this).sortable('option','initCardValues',
				         {'elementId': elementId,'initPhase': initPhase, 'initPos': initPos});
		      	    		    
		   });			   
	       }
	   });
  }


 
  function phaseFormRefresh(formData,dialogSelector,successTitle,successMessage){
      var url = "${createLink(controller:'phase',action:'show')}";
      $destination = $('#phaseList');

      var updatePhases = function(data,textStatus,$element,injection){

	  var $newPhase = $('#'+$element.attr('id')).find('ul');
	  enableSortableOnPhase($newPhase);
	  reconnectPhases();
	  if( injection ){
		var height = $(".phase:not('[id="+$newPhase.attr('id')+"]')").height();
		$newPhase.height(height);
	  }
          rescanBoardButtons();
      };

      var changeWidth = function(){
		var width = 100/($('.phase').size()+1)-1 + '%';
		$('.phaseAutoWidth').width(width);

      }

      formRefresh(formData,dialogSelector,successTitle,successMessage,url,$destination,updatePhases,changeWidth);
  }
  


  function cardFormRefresh(formData,dialogSelector,successTitle,successMessage){
      
      var heightAndCount = function recalculateHeightAndUpdateCardCount(){
      
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
      	  $phases.animate({height: height},300);
	  rescanBoardButtons();


      }
      
      formRefresh(formData,dialogSelector,successTitle,successMessage,'${createLink(controller:"card",action:"show")}',$('.phase:first'),heightAndCount);
  
  }
 
  function formRefresh(formData,dialogSelector,successTitle,successMessage,url,$destination,beforeCloseFunction,beforeInjection){
      
      var $dialog = $(dialogSelector);
      var id = $(formData).find('input[name="id"]').val();
     
      if( $dialog.find('.errors').size() == 0 && id ){
      	  
      	  $.qGet(url+'/'+id,'html',function(data,textStatus){
	      var $newElement = $(data);
	      var $oldElement = $('#'+$newElement.attr("id"));
	      var createdNewElement = false;
	      
	      if( $oldElement.size() == 0 ){
	      	  createdNewElement = true;
		  if ( beforeInjection ){
		      beforeInjection();
		  }
	      	  $destination.append(data);
	      }else if( $oldElement.size() == 1 ){
	      	  $oldElement.replaceWith($newElement);
	      }else{
	      	  $('#debug').html('Error in formRefresh()');
	      }
	      
	      if( beforeCloseFunction ){
	      	  beforeCloseFunction(data,textStatus,$newElement,createdNewElement);
	      }
	 
	      closeDialog($dialog,successTitle,successMessage);
	  });
      }
  }
  
  function closeDialog($dialog,successTitle,successMessage){

      $dialog.dialog('close');
      
      if( successTitle && successMessage ){
            $('<div id="popup" title="'+successTitle+'">'+successMessage+'</div>').dialog({
			bgiframe: true,
			modal: true,
			buttons: {
				Ok: function() {
					$(this).dialog('close');
				}
			},
                        open: function(){
                                setTimeout(function(){$('#popup').dialog('close')},1250);
                        }
      	    });
      }
  }

  function updateBoard(){
      $('#boardWrapper').qLoad('${createLink(controller:'mainView',action:'showBoard')}',
      function(){
          rescanBoardButtons();
      });
  }


  </g:javascript>

  <style type="text/css">
    





  </style>



</head>

<body>

  <div id="wrapper">

    <div id="boardWrapper">
      <g:render template="/board/board" bean="${board}" />
    </div>

  </div>

</body>

