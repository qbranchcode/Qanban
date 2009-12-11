<%--  Phase related scripts Loaded through _board.gsp --%>

function loadPhasePlacer(dialogSelector){
  $('#phasePlacer').sortable({
         cancel:'.old',
         placeholder:'phaseplaceholder',
         stop:function(event,ui){
            $('input[name=phasePos]').val(ui.item.prevAll().length);

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

                $.ajax({  url: '${createLink(controller:'phase',action:'delete')}',
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

function fixWidth(){
      var numberOfPhases = $('#board').find('.phase').size();
      var boardWidth = $('#board').width();
      var newWidth = (boardWidth / numberOfPhases ) - 8 + 'px';
      $('.phaseAutoWidth').width(newWidth);
}

function phaseFormRefresh(formData,dialogSelector,successTitle,successMessage){

      var url = "${createLink(controller:'phase',action:'show')}";
      $destination = $('#phaseList');


      var updatePhases = function(data,textStatus,$element,injection){

	  var $newPhase = $('#'+$element.attr('id')).find('ul');

	  
	  if( injection ){
		var height = $(".phase:not('[id="+$newPhase.attr('id')+"]')").height();
		$newPhase.height(height);
	  }
          enableSortableOnPhase($newPhase);
          reconnectPhases();
          rescanBoardButtons();
      };

      var incompleteForm = function(formData,dialogSelector){
          loadPhasePlacer(dialogSelector);
      };
      
      formRefresh(formData,dialogSelector,successTitle,successMessage,url,$destination,updatePhases,fixWidth,incompleteForm);
  }


  function recalculateHeightAndUpdateCardCount(){

      $phases = $('.phase');
      var maxCards = 0;

      $phases.each(function(){
         var $phase = $(this);
         var numberOfChildren = $phase.children().size() - 1;
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
              ui.item.animate({opacity:0.6},300);

              var initPos = ui.item.prevAll().length + 1;
              var elementId = ui.item.attr('id');
              var initPhase = ui.item.parent().attr('id');

              $(this).sortable('option','initCardValues',
                               {'elementId': elementId,'initPhase': initPhase, 'initPos': initPos});

         });

         if ( $nextPhase.is('.phase') ){
         
            $currentPhase.sortable('option','connectWith','#'+$nextPhase.attr('id')+'.available');

         }else if ( $currentPhase.parent().find('#archiveBtn').size() == 1 ){

            $currentPhase.sortable('option','connectWith','#archiveBtn');
         }

     });
  }

  function enableSortableOnPhase($phase){
     $phase.sortable({
            placeholder: 'placeholder',
            stop: function(event,ui){
                  ui.item.animate({opacity:1},300);
                  recalculateHeightAndUpdateCardCount();
                  $('.phase').parent().animate({opacity: 1},300);

                  var icv = $(this).sortable('option','initCardValues');
                  var placementSelector = "#" + icv.initPhase + " .card:nth-child(" + ( icv.initPos  ) + ")" ;
                  var initPhaseSize = $('#' + icv.initPhase + '> .card').size();

                  var newPos = ui.item.prevAll().length -1 ;
                  var cardId = ui.item.attr('id').split('_')[1];
                  var newPhase = ui.item.parent().attr('id').split('_')[1];

                  if( ui.item.parent().attr('id') != icv.initPhase ){
                      $moveCardDialog = $('<div id="moveCardDialog"></div>');
                      $moveCardDialog.dialog({
                                autoOpen: false,
                                modal: true,
                                title: "<g:message code="mainView.jQuery.moveCardForm.preCardTitle"/> " + ui.item.find('.titleWrapper').html(),
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

                                         initPhaseSize  == 0 || initPhaseSize+1 < icv.initPos ? $('#'+icv.initPhase).append(card) : function(){
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

                    var loadMoveCardDialog = function(tries) {
                       $moveCardDialog.qLoad({
                                  url : '${createLink(controller:'card',action:'form')}',
                                  data : { 'id' : cardId , 'newPhase' : newPhase , 'newPos' : newPos },
                                  successCallback : function(){
                                                        $moveCardDialog.dialog('open');
                                                     },
                                  completeCallback : initAssigneeSelect,
                                  tries : tries,
                                  caller : loadMoveCardDialog
                       });
                    }
                    loadMoveCardDialog();

                  }else{
                    var loadCardSort = function(tries) {
                        $.qPost({ url: '${createLink(controller:'card',action:'sort')}',
                                  data : { 'id' : cardId , 'newPhase' : newPhase , 'newPos' : newPos },
                                  tries : tries,
                                  caller : loadCardSort
                        });
                    }
                    loadCardSort();
                    
                  }

              }
     });

     var $archiveBtn = $phase.parent().find('#archiveBtn');
     if( $archiveBtn.size() == 1 ){
      var acceptSelector = '#' + $phase.attr('id') + ' > .card';
      $archiveBtn.droppable({
        hoverClass: 'cardHover',
        accept: acceptSelector,
        tolerance: 'pointer',
        drop: function(event,ui){

          var id = ui.draggable.attr('id').split('_')[1];
          var newPos = 0;
          var newPhase;

          var classList = $archiveBtn.attr('class').split(' ');

          $.each(classList, function(index, item){
             var classSubstings = item.split('_');
             if( classSubstings[0].replace(/^\s*|\s*$/g,'') == 'archId' ){
                 newPhase = classSubstings[1];
             }
          });

          var archiveCall = function(n){
            $.qPost({
              url: '${createLink(controller:'card',action:'sort')}',
              data: {id: id, newPos: newPos, newPhase: newPhase },
              successCallback: function(data,textStatus){

                 var $archPhase = $phase.parent().parent().next().find('.phase');


                 if( $archPhase.size() == 1 ){
                  $archPhase.append($(ui.draggable));
                  ui.draggable.show();
                  $archPhase.find('.card:first').slideUp('slow',function(){
                      $(this).remove();
                      recalculateHeightAndUpdateCardCount();
                    });

                 }else{
                  ui.draggable.remove();
                 }
              },
              errorCallback: function(data, textStatus){
                 ui.draggable.show();
              }
            });
          };
          ui.draggable.hide();

          archiveCall();

        }

      });
      
     }
     
  }


