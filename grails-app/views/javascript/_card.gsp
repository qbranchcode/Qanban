<%--  Card related scripts Loaded through _board.gsp --%>

function showCard(cardId, showButtons){

  var showBtn = showButtons == null ? true : showButtons;

  var loadEditCardLink = function(tries) {

    $editCardDialog.qLoad({
        url : '${createLink(controller:'card',action:'form')}',
        data : { 'id':cardId},
        successCallback : function(){

          if( showBtn ){
            $editCardDialog.dialog(
              'option',
              'buttons',
              { '<g:message code="_cardForm.button.edit"/>' : function() {
                  initAssigneeSelect();
                  setEditMode('<g:message code="mainView.jQuery.dialog.editCardForm.title"/>', '#editCardDialog');
                  $editCardDialog.dialog('option', 'buttons',  {
                    '<g:message code="_cardForm.button.update"/>' : function() {
                        $editCardDialog.find('input[type="submit"]').click();
                        toggleSpinner();
                    }
                    <g:ifAllGranted role="ROLE_QANBANADMIN">
                      ,'<g:message code="_cardForm.button.delete"/>' : function() {
                        deleteCardDialog(cardId);
                      }
                    </g:ifAllGranted>
                  });
              }
            });
          }else{
            $editCardDialog.dialog('option','buttons',{});
          }
          $editCardDialog.dialog('open');
        },
        tries : tries,
        caller : loadEditCardLink
     });
  }
  loadEditCardLink();
}

function deleteCardDialog(id){
    $('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><g:message code="_cardForm.delete.confirm"/></p></div>').dialog({
      resizable: false,
      height:140,
      modal: true,
      buttons: {
          <g:message code="yes"/>: function() {

                  $.ajax({  url: '${createLink(controller:'card',action:'delete')}',
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

  function setEditMode(title, selector) {
    var $editDialog = $(selector);
    $editDialog.dialog('option', 'title', title);
    $editDialog.find("input").removeAttr([readonly='readonly']);
    $editDialog.find("input").addClass("edit");
    $editDialog.find("textarea").removeAttr([readonly='readonly']);
    $editDialog.find("textarea").addClass("edit");
  }

  function cardFormRefresh(formData,dialogSelector,successTitle,successMessage){
    formRefresh(formData,dialogSelector,successTitle,successMessage,'${createLink(controller:"card",action:"show")}',$('.phase:first'),recalculateHeightAndUpdateCardCount);
  }
