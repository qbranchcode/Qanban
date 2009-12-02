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
    $.fn.qLoad = function(url, data, successCallback, errorCallback, completeCallback) {
      var $element = $(this);

      var options = {};
      options.url = url;
      options.cache = false;
      options.successCallback = successCallback;
      options.success = function(data, textStatus){
        var fail = data.indexOf('<html>') != -1;
        if( fail ) {
          $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.sessionTimeout"/></p></div>').dialog({
              modal: true,
              buttons: {
                <g:message code="ok"/>: function() {
                  $(this).dialog('close');
                  //Redirect to mainView/index
                  window.location = "${createLink(controller:'mainView')}";
                }
              }
          });
        } else {
          var successCallback = options.successCallback;
          if(successCallback) {
            successCallback(data, textStatus);
          }
          $element.html(data);
        }
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
      if( completeCallback ){
	    options.complete = completeCallback;
      }
      options.type = "GET";

      $.ajax(options);

    }

    /*Ajax Post Wrapper*/
    $.qPost = function(url, data, successCallback, dataType) {
      var options = {};
      options.url = url;
      options.successCallback = successCallback;
      options.success = function(data, textStatus){
        /* TODO: data.indexOf fails when the returned data is of the type JSON */
       
        var fail = data.indexOf('<html>') != -1;
        if( fail ) {
        
          $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.sessionTimeout"/></p></div>').dialog({
              modal: true,
              buttons: {
                <g:message code="ok"/>: function() {
                  $(this).dialog('close');
                  //Redirect to mainView/index
                  window.location = "${createLink(controller:'mainView')}";
                }
              }
          });
        
        }else {
          var successCallback = options.successCallback;
          if(successCallback) {

            successCallback(data, textStatus);
          }
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
        var fail = data.indexOf('<html>') != -1;
        if( fail ) {
          $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.sessionTimeout"/></p></div>').dialog({
              modal: true,
              buttons: {
                <g:message code="ok"/>: function() {
                  $(this).dialog('close');
                  //Redirect till mainView/index
                  window.location = "${createLink(controller:'mainView')}";
                }
              }
          });
        } else {
          var successCallback = options.successCallback;
          if(successCallback) {
            successCallback(data, textStatus);
          }
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

    /*Dialog wrapper*/

    $.fn.qDialog = function(options) {
       options.width = options.width ? options.width : options.width = 300;
       options.autoOpen = options.autoOpen ? options.autoOpen : options.autoOpen = false;
       options.modal = options.modal ? options.modal : options.modal = true;
       options.title = options.title;
       options.buttons = options.buttons;
       if( options.close ){
          var closeFunction = options.close;
          options.close = function(){ closeFunction(); $(this).empty(); };
       }else{
          options.close = function(){ $(this).empty(); };
       }
       $(this).dialog(options);
    }

    
/***********/
/* DIALOGS */
/***********/

    $createCardDialog = $('<div id="createCardDialog"></div>');

    $createCardDialog.qDialog({
      width: 400,
      close: function(){
                var _wait_button = $('.ui-dialog-buttonpane button:contains(<g:message code="button.wait"/>)');
                _wait_button.text('<g:message code="_cardForm.button.submit"/>');
                _wait_button.removeClass('ui-state-disabled');
                _wait_button.removeAttr('disabled');
              },
      title: "<g:message code="mainView.jQuery.dialog.addCardForm.title"/>",
      buttons: {
                  <g:message code="_cardForm.button.submit"/> : function(){
                    $createCardDialog.find('input[type="submit"]').click();
                    var _create_button = $('.ui-dialog-buttonpane button:contains(<g:message code="_cardForm.button.submit"/>)');
                    _create_button.text('<g:message code="button.wait"/>');
                    _create_button.addClass('ui-state-disabled');
                    _create_button.attr('disabled','disabled');
              }
      } 
    });

    $('.addCardLink').click(function(event){
      $createCardDialog.qLoad('${createLink(controller:'card',action:'ajaxShowForm')}',
                              {'board.id':${board.id}},
                              function(){
                                $createCardDialog.dialog('open');
                              },
                              null, initAssigneeSelect);
      event.preventDefault();
    });

    $createPhaseDialog = $('<div id="createPhaseDialog"></div>');
    
    $createPhaseDialog.qDialog({
      title: "<g:message code="mainView.jQuery.dialog.addPhaseForm.title"/>",
      close: function(){
            var _wait_button = $('.ui-dialog-buttonpane button:contains(<g:message code="button.wait"/>)');
            _wait_button.text('<g:message code="_phaseForm.button.save"/>');
            _wait_button.removeClass('ui-state-disabled');
            _wait_button.removeAttr('disabled');
          },
      buttons: {
                  <g:message code="_phaseForm.button.save"/> : function(){
                  $createPhaseDialog.find('input[type="submit"]').click();
                  var _create_button = $('.ui-dialog-buttonpane button:contains(<g:message code="_phaseForm.button.save"/>)');
                  var original_text = _create_button.text();
                  _create_button.text('<g:message code="button.wait"/>');
                  _create_button.addClass('ui-state-disabled');
                  _create_button.attr('disabled','disabled');
               }
      }
    });

    $('.addPhaseLink').click(function(event){
       $createPhaseDialog.qLoad(
	   '${createLink(controller:'phase',action:'ajaxPhaseForm')}',
	   {'board.id':${board.id}},
	   function(){$createPhaseDialog.dialog('open');},
	   null,
	   function(){
              loadPhasePlacer($createPhaseDialog.attr('id'));

	   });
      event.preventDefault();
    });

      $editPhaseDialog = $('<div id="editPhaseDialog"></div>');
      $editPhaseDialog.qDialog({
            title: "<g:message code="mainView.jQuery.dialog.editPhaseForm.title"/>",
      });

      $editCardDialog = $('<div id="editCardDialog"></div>');
      $editCardDialog.qDialog({
            title: "<g:message code="mainView.jQuery.dialog.viewCardForm.title"/>",
            width: 400,
      });
      
    
/***************/
/* Board logic */
/***************/

    var sort = false;


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

    function loadPhasePlacer(dialogSelector){
      $('#phasePlacer').sortable({
             cancel:'.old',
             placeholder:'phaseplaceholder',
             stop:function(event,ui){
                $('input[name$=idx]').val(ui.item.prevAll().length);

             }});
    }

   function initAssigneeSelect(){
	
			$('.assignee > .avatar, #currentAssigneeName').click(function(event){
				$('#assignees').toggle();
				$('#currentAssigneeName').toggle();
				event.preventDefault();
			});

			$('#assignees li').click(function(){
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$('.assignee > .avatar').attr('src',$(this).find('img').attr('src'));
				$('#currentAssigneeName').html($(this).find('.name').html());
				$('#assigneeValue').val($(this).attr('id').split('_')[1]);
				$('#assignees').hide();
				$('#currentAssigneeName').hide();				
			});

			$('.dropdownContainer').mouseleave(function(){
				$('#assignees').hide();
				$('#currentAssigneeName').hide();

			});
	
   }


   function deletePhaseDialog(id){
  
      $('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><g:message code="_phaseForm.delete.confirm"/></p></div>').dialog({
        resizable: false,
        height:140,
        modal: true,
        buttons: {
            <g:message code="yes"/>: function() {

                    $.ajax({  url: '${createLink(controller:'phase',action:'ajaxDelete')}',
                              data: {'id': id},
                              type: 'POST',
                              success: function() {
                                $("#phaseWrapper_"+id).remove();
                                fixWidth();
                                $('#editPhaseDialog').dialog("close");
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
                              }});
                    $(this).dialog('close');
		    $(this).empty();
		    $(this).dialog('destroy');

            },
            <g:message code="no"/>: function() {
                    $(this).dialog('close');
		    $(this).empty();
		    $(this).dialog('destroy');
            }
        }
      });
   }

   function deleteCardDialog(id){

      $('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><g:message code="_cardForm.delete.confirm"/></p></div>').dialog({
        resizable: false,
        height:140,
        modal: true,
        buttons: {
            <g:message code="yes"/>: function() {

                    $.ajax({  url: '${createLink(controller:'card',action:'ajaxDelete')}',
                              data: {'id': id},
                              type: 'POST',
                              success: function() {
                                $("#card_"+id).remove();
                                recalculateHeightAndUpdateCardCount();
                                $('#editCardDialog').empty().dialog('close');
                              },
                              error: function (XMLHttpRequest, textStatus, errorThrown) {
                                 $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.errorDeletingCard.content"/></p></div>').dialog({
                                  modal: true,
                                  buttons: {
                                  <g:message code="ok"/>: function() {
                                      $(this).dialog('close');
                                    }
                                  }
                                });
                              }});
                    $(this).dialog('close');
		    $(this).empty();
		    $(this).dialog('destroy');

            },
            <g:message code="no"/>: function() {
                    $(this).dialog('close');
		    $(this).empty();
		    $(this).dialog('destroy');
            }
        }
      });
   }

   function fixWidth(){
          var numberOfPhases = $('#board').find('.phase').size();
          var boardWidth = $('#board').width();
          var newWidth = (boardWidth / numberOfPhases ) - 8 + 'px';
          $('.phaseAutoWidth').width(newWidth);

   }

  function rescanBoardButtons(){

      $('.editPhaseLink').click(function(event){
            var phaseId = $(this).attr('id').split('_')[1];
            $editPhaseDialog.qLoad(
                '${createLink(controller:'phase',action:'ajaxPhaseForm')}',
				   {'id': phaseId,'board.id': ${board.id} },
                function(){
                    $editPhaseDialog.dialog(
                        'option',
                        'buttons',
                        { 
                           '<g:message code="_phaseForm.button.update"/>' : function(){
                                $editPhaseDialog.find('input[type="submit"]').click();
                                var _update_button = $('.ui-dialog-buttonpane button:contains(<g:message code="_phaseForm.button.update"/>)');
                                _update_button.text('<g:message code="button.wait"/>');
                                _update_button.addClass('ui-state-disabled');
                                _update_button.attr('disabled','disabled');

                            },
                            '<g:message code="_phaseForm.button.delete"/>': function() {
                                deletePhaseDialog(phaseId);
                            }
                        });
                    $editPhaseDialog.dialog('open');
                },
                null,
                function(){
                    loadPhasePlacer($editPhaseDialog.attr('id'));
                }
            );
            event.preventDefault();
      });

      $('.editCardLink').click(function(event){
            var cardId = $(this).attr('id').split('_')[1];
            $editCardDialog.qLoad(
                '${createLink(controller:'card',action:'ajaxShowForm')}',
                                    {'board.id':${board.id} , 'id':cardId},
                function(){
                    $editCardDialog.dialog(
                      'option',
                      'buttons',
                      { '<g:message code="_cardForm.button.edit"/>' : function() {
                          initAssigneeSelect();
                          $('#editCardDialog').dialog('option', 'title', '<g:message code="mainView.jQuery.dialog.editCardForm.title"/>');
                          $('#editCardDialog').find("input").removeAttr([readonly='readonly']);
                          $('#editCardDialog').find("input").addClass("edit");
                          $('#editCardDialog').find("textarea").removeAttr([readonly='readonly']);
                          $('#editCardDialog').find("textarea").addClass("edit");
                          $('#editCardDialog').dialog('option', 'buttons',  {
                            '<g:message code="_cardForm.button.update"/>' : function() {
                                            $editCardDialog.find('input[type="submit"]').click();
                                            var _update_button = $('.ui-dialog-buttonpane button:contains(<g:message code="_cardForm.button.update"/>)');
                                            _update_button.text('<g:message code="button.wait"/>');
                                            _update_button.addClass('ui-state-disabled');
                                            _update_button.attr('disabled','disabled');
                                        },
                                      <g:ifAllGranted role="ROLE_QANBANADMIN">'<g:message code="_cardForm.button.delete"/>' : function() {
                                      deleteCardDialog(cardId);
                                    }</g:ifAllGranted>
                          });
                        }
                    });
                    $editCardDialog.dialog('open');
                });
            event.preventDefault();
      });
    
  }

  function enableSortableOnPhase($phase){
  	   $phase.sortable({
	   	  placeholder: 'placeholder',
                  stop: function(event,ui){
	 			
			recalculateHeightAndUpdateCardCount();
			$('.phase').parent().animate({opacity: 1},300);

		  	var icv = $(this).sortable('option','initCardValues');		       			
			var placementSelector = "#" + icv.initPhase + " .card:nth-child(" + ( icv.initPos  ) + ")" ;
			var initPhaseSize = $('#' + icv.initPhase + '> .card').size();
			
			var newPos = ui.item.prevAll().length;
                        var cardId = ui.item.attr('id').split('_')[1];
                        var newPhase = ui.item.parent().attr('id').split('_')[1];

			if( ui.item.parent().attr('id') != icv.initPhase ){
	                    $moveCardDialog = $('<div id="moveCardDialog"></div>');
      		            $moveCardDialog.dialog({
      			       	      autoOpen: false,
		      		      modal: true,
		      	     	      title: "<g:message code="mainView.jQuery.moveCardForm.preCardTitle"/> " + ui.item.find('a').html(),
		      		      width: 400,
    		                      initCardValues: icv,
			   	      buttons: {
			      	         '<g:message code="ok"/>': function() {				      	    
					    $(this).find('input[type="submit"]').click();
				      	    $(this).dialog('option','confirmed',true);
			      	      	    $(this).dialog("close");
				         },
			      		 '<g:message code="cancel"/>': function() {	
			      	      	    $(this).dialog("close");
			      	  	 }
			   	      },
			   	      close: function(event, ui) {
  			   	      	    
					    if( !$(this).dialog('option', 'confirmed') ) {
				      	       var icv = $(this).dialog('option','initCardValues');
				      	       var card = $(this).dialog('option','card');
				      	       var placementSelector = $(this).dialog('option','placementSelector');
				      
				      	       var initPhaseSize = $('#' + icv.initPhase + '> .card').size();
				      
				      	       initPhaseSize  == 0 || initPhaseSize < icv.initPos ? $('#'+icv.initPhase).append(card) : function(){ 
			      	                  card.insertBefore($(placementSelector));  
				      	       }();
		       
		       		      	       recalculateHeightAndUpdateCardCount();
					    }
					    $(this).dialog('destroy');
                                            $(this).remove();
  			   	      },
			   	      confirmed: false,
			   	      card: ui.item,
				      placementSelector: placementSelector
			   });

			   $moveCardDialog.qLoad('${createLink(controller:'card',action:'ajaxShowForm')}',
			      			 {'board.id' : ${board.id} , 'id' : cardId , 'newPhase' : newPhase , 'newPos' : newPos , 'user' : <g:loggedInUserInfo field="id"></g:loggedInUserInfo>},
					         function(){
					            $moveCardDialog.dialog('open');
			   			 },
						 null,
						 initAssigneeSelect
			   );

			}else{
		           /*What to do here?*/
			}
               
		    }
      	   });
  }

  function reconnectPhases(){
           var $phases = $('.phase');
           fixWidth();
	   $phases.each(function(index,$phase){


	       var $nextPhase = index < $phases.size() ? $( $phases[index+1] ) : false;	       
               var $currentPhase = $(this);

               $currentPhase.sortable('option','start', function(event,ui){

                    $('.phase').filter(function(){

                         if( $nextPhase.is('.phase') ){
                          var notAvail = $nextPhase.attr('class').indexOf('available') == -1;

                          var notNext = $(this).attr('id') != $nextPhase.attr('id');
                          var notCurr = $(this).attr('id') != $currentPhase.attr('id');

                          return notCurr && ( notNext || notAvail );
                         }else{
                          return $(this).attr('id') != $currentPhase.attr('id');
                         }

                    }).parent().animate({opacity:0.3},300);

                    var initPos = ui.item.prevAll().length + 1;
                    var elementId = ui.item.attr('id');
                    var initPhase = ui.item.parent().attr('id');

                    $(this).sortable('option','initCardValues',
                                     {'elementId': elementId,'initPhase': initPhase, 'initPos': initPos});

               });

               if ( $nextPhase.is('.phase') ){
                  $currentPhase.sortable('option','connectWith','#'+$nextPhase.attr('id')+'.available')
               }
	   });
  }

  function recalculateHeightAndUpdateCardCount(){
      
      	  $phases = $('.phase');
      	  var maxCards = 0;
      
      	  $phases.each(function(){
             var $phase = $(this);
             var numberOfChildren = $phase.children().size();
             var classList = $phase.attr('class').split(' ');

             $.each(classList, function(index, item){
             	 var classSubstings = item.split('_');
             	 if( classSubstings[0].replace(/^\s*|\s*$/g,'') == 'cardLimit' ){
                     $phase.parent().find('.limitLine').html(numberOfChildren + '/' + classSubstings[1]);
             	 }
             });

             $.each(classList, function(index, item){
             	 var classSubstings = item.split('_');
             	 if( classSubstings[0].replace(/^\s*|\s*$/g,'') == 'cardLimit' ){
                     if( classSubstings[1].replace(/^\s*|\s*$/g,'') == numberOfChildren ) {
                        $phase.removeClass("available");
                        reconnectPhases();
                     }
             	 }
                 if( classSubstings[0].replace(/^\s*|\s*$/g,'') == 'cardLimit' ){
                     if( classSubstings[1].replace(/^\s*|\s*$/g,'') > numberOfChildren ) {
                        $phase.addClass("available");
                        reconnectPhases();
                     }
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
 
  function phaseFormRefresh(formData,dialogSelector,successTitle,successMessage){
      var url = "${createLink(controller:'phase',action:'show')}";
      $destination = $('#phaseList');
     

      var updatePhases = function(data,textStatus,$element,injection){
	 
	  var $newPhase = $('#'+$element.attr('id')).find('ul');
	  
	  reconnectPhases();
	  if( injection ){
		var height = $(".phase:not('[id="+$newPhase.attr('id')+"]')").height();
		$newPhase.height(height);
	  }
          enableSortableOnPhase($newPhase);
          rescanBoardButtons();
      };

      var incompleteForm = function(formData,dialogSelector){
          loadPhasePlacer(dialogSelector);
      };

      formRefresh(formData,dialogSelector,successTitle,successMessage,url,$destination,updatePhases,fixWidth,incompleteForm);
  }
  


  function cardFormRefresh(formData,dialogSelector,successTitle,successMessage){
  	   formRefresh(formData,dialogSelector,successTitle,successMessage,'${createLink(controller:"card",action:"show")}',$('.phase:first'),recalculateHeightAndUpdateCardCount);
  
  }
 /* TODO: Change beforeInjection to getNewElementCallback to break out some missplaced logic */
  function formRefresh(formData,dialogSelector,successTitle,successMessage,url,$destination,beforeCloseFunction,beforeInjection,incompleteFormCallback){
      
      var $dialog = $(dialogSelector);
      var $newContent = $(formData);
      var id = $newContent.find('input[name="id"]').val();
      
      if( $dialog.find('.errors').size() == 0 && id ){
      	  
      	  $.qGet(url+'/'+id,'html',function(data,textStatus){
	      var $newElement = $(data);
	      var $oldElement = $('#'+$newElement.attr("id"));
	      var createdNewElement = false;
	      var $phases = $destination.find('.phaseWrapper');

	      if( $oldElement.size() == 0 ){
	      	  createdNewElement = true;
		  if ( beforeInjection ){
		      beforeInjection();
		  }
		  
		  var $indexInput = $newContent.find('input[name$=idx]');
		  
		
		  if( $indexInput.size() == 1 && ( $phases.size() > $indexInput.val() ) ) {

		     var p = $phases.get($indexInput.val());
		     $(data).insertBefore($(p));
		  }else{
	      	     $destination.append(data);
		  }
		  
	      }else if( $oldElement.size() == 1 ){
                  
                  $oldElement.replaceWith($newElement);

                  if( $newElement.attr('id').split('_')[0] != 'card' ){


                  var oldIndex = 1 + parseInt($newElement.prevAll().size());  
                  var newIndex = 1 + parseInt($newContent.find('input[name$=idx]').val());
                  var selector = '.phaseWrapper:nth-child('+ newIndex +')';
                  var $elementAtDestination = $(selector);
	      	  var idAtNewIndex = $elementAtDestination.attr('id');
                  if( $newElement.attr('id') != idAtNewIndex  ){
                      if( oldIndex > newIndex ){
                        $newElement.insertBefore($elementAtDestination);
                      }else{
                        $newElement.insertAfter($elementAtDestination);
                      }
                  }

                }

	      }else{
	      	  $('#debug').html('Error in formRefresh()');
	      }
	      
	      if( beforeCloseFunction ){
	      	  beforeCloseFunction(data,textStatus,$newElement,createdNewElement);
	      }
	 
	      closeDialog($dialog,successTitle,successMessage);
	  });
      }else if(incompleteFormCallback){
          incompleteFormCallback(formData,dialogSelector);
      }
  }

  function closeDialog($dialog,successTitle,successMessage){

      $dialog.dialog('close');
      
      if( successTitle && successMessage ){
            $('<div id="popup" title="'+successTitle+'">'+successMessage+'</div>').dialog({
			bgiframe: true,
			modal: true,
			buttons: {
				<g:message code="ok"/>: function() {
					$(this).dialog('close').remove();
				}
			},
                        open: function(){
                                setTimeout(function(){$('#popup').dialog('close').remove()},1250);
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

