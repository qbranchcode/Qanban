

/***********/
/* DIALOGS */
/***********/

$createCardDialog = $('<div id="createCardDialog"></div>');

$createCardDialog.qDialog({
  width: 400,
  title: "<g:message code="mainView.jQuery.dialog.addCardForm.title"/>",
  buttons: {
            <g:message code="_cardForm.button.submit"/> : function(){
              $createCardDialog.find('input[type="submit"]').click();

    }
  }
});



$createPhaseDialog = $('<div id="createPhaseDialog"></div>');

$createPhaseDialog.qDialog({
  title: "<g:message code="mainView.jQuery.dialog.addPhaseForm.title"/>",
  buttons: {
              <g:message code="_phaseForm.button.save"/> : function(){
              $createPhaseDialog.find('input[type="submit"]').click();
;
     }
  }
});



$editPhaseDialog = $('<div id="editPhaseDialog"></div>');

$editPhaseDialog.qDialog({
      title: "<g:message code="mainView.jQuery.dialog.editPhaseForm.title"/>",
});





$editCardDialog = $('<div id="editCardDialog"></div>');

$editCardDialog.qDialog({
      title: "<g:message code="mainView.jQuery.dialog.viewCardForm.title"/>",
      width: 400,
      close: function() {
        $editCardDialog.dialog('option', 'title', '<g:message code="mainView.jQuery.dialog.viewCardForm.title"/>');
      }
});



$editUserDialog = $('<div id="editUserDialog"></div>');

$editUserDialog.qDialog({
    title : "<g:message code="mainView.jQuery.dialog.editUserForm.title"/>",
    width: 400
});


