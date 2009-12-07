<%-- Loaded through _qanbanFunctions.gsp --%>

  <g:render template="/javascript/card"/>
  <g:render template="/javascript/phase"/>
  
  function updateBoard(){

    var loadUpdateBoard = function(tries) {
      $('#boardWrapper').qLoad({
                url : '${createLink(controller:'mainView',action:'showBoard')}',
                successCallback : function(){
                                    rescanBoardButtons();
                                  },
                tries : tries,
                caller : loadUpdateBoard
      });
    }
    loadUpdateBoard();
  }

  function rescanBoardButtons(){

    $('.card').dblclick(function(event){
      showCard( $(this).attr('id').split('_')[1] );
    });


    $('.editPhaseLink').click(function(event){

        var phaseId = $(this).attr('id').split('_')[1];
        var loadEditPhaseLink = function(tries) {
          $editPhaseDialog.qLoad({
              url : '${createLink(controller:'phase',action:'form')}',
              data : {'id': phaseId },
              successCallback : function(){
                  $editPhaseDialog.dialog(
                      'option',
                      'buttons',
                      {
                         '<g:message code="_phaseForm.button.update"/>' : function(){
                              $editPhaseDialog.find('input[type="submit"]').click();
                              toggleSpinner();
                          },
                          '<g:message code="_phaseForm.button.delete"/>': function() {
                              deletePhaseDialog(phaseId);
                          }
                      });
                  $editPhaseDialog.dialog('open');
              },
              completeCallback : function(){
                                    loadPhasePlacer($editPhaseDialog.attr('id'));
                                 },
              tries : tries,
              caller : loadEditPhaseLink
          });
        }

        loadEditPhaseLink();
        event.preventDefault();
    });


    $('.addCardLink').click(function(event){

      var loadAddCardLink = function(tries) {
        $createCardDialog.qLoad({
            url : '${createLink(controller:'card',action:'form')}',
            data : {'board.id':${board.id}},
            successCallback : function(){
                                $createCardDialog.dialog('open');
                              },
            completeCallback : initAssigneeSelect,
            tries : tries,
            caller : loadAddCardLink
        });
      }

      loadAddCardLink();
      event.preventDefault();

    });

    $('.addPhaseLink').click(function(event){
      var loadCreatePhaseLink = function(tries) {
       $createPhaseDialog.qLoad({
           url : '${createLink(controller:'phase',action:'form')}',
           data : {'board.id':${board.id}},
           successCallback : function(){$createPhaseDialog.dialog('open');},
           completeCallback : function(){
                                loadPhasePlacer($createPhaseDialog.attr('id'));
                             },
           tries : tries,
           caller : loadCreatePhaseLink
       });
      }
      
      loadCreatePhaseLink();
      event.preventDefault();

    });

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

		  var $indexInput = $newContent.find('input[name=position]');


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
                  var newIndex = 1 + parseInt($newContent.find('input[name=position]').val());
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





