<script type="text/javascript">
var resources = {};

resources.admin = false;
<g:ifAllGranted role="ROLE_QANBANADMIN">
resources.admin = true;
</g:ifAllGranted>
resources.loggedInUserId = ${loggedInUser.id};
resources.boardId = ${board.id};


/************/
/* Messages */
/************/

// Common
resources.yesMsg = '<g:message code="yes"/>';
resources.okMsg = '<g:message code="ok"/>';
resources.noMsg = '<g:message code="no"/>';
resources.cancelMsg = '<g:message code="cancel"/>';


// AJAX
resources.offlineMsg = '<g:message code="mainView.jQuery.dialog.serverOffline"/>';
resources.sessionTimeoutMsg = '<g:message code="mainView.jQuery.dialog.sessionTimeout"/>';


// Phase
resources.phaseCreateDialogTitle = '<g:message code="mainView.jQuery.dialog.addPhaseForm.title"/>';
resources.phaseEditDialogTitle = '<g:message code="mainView.jQuery.dialog.editPhaseForm.title"/>';

resources.phaseFormSaveBtn = '<g:message code="_phaseForm.button.save"/>';
resources.phaseFormDeleteBtn = '<g:message code="_phaseForm.button.delete"/>';
resources.phaseFormUpdateBtn = '<g:message code="_phaseForm.button.update"/>';

resources.phaseDeleteConfirmMsg = '<g:message code="_phaseForm.delete.confirm"/>';
resources.phaseDeleteErrorMsg = '<g:message code="mainView.jQuery.dialog.errorDeletingPhase.content"/>';


//Card
resources.cardCreateDialogTitle = '<g:message code="mainView.jQuery.dialog.addCardForm.title"/>';
resources.cardViewDialogTitle = '<g:message code="mainView.jQuery.dialog.viewCardForm.title"/>';
resources.cardEditDialogTitle = '<g:message code="mainView.jQuery.dialog.editCardForm.title"/>';
resources.cardMoveDialogTitlePrefix = '<g:message code="mainView.jQuery.moveCardForm.preCardTitle"/>';

resources.cardFormSubmitBtn = '<g:message code="_cardForm.button.submit"/>';
resources.cardFormEditBtn = '<g:message code="_cardForm.button.edit"/>';
resources.cardFormUpdateBtn = '<g:message code="_cardForm.button.update"/>';
resources.cardFormDeleteBtn = '<g:message code="_cardForm.button.delete"/>';

resources.cardDeleteConfirmMsg = '<g:message code="_cardForm.delete.confirm"/>';
resources.cardDeleteErrorMsg = '<g:message code="mainView.jQuery.dialog.errorDeletingCard.content"/>';


//User
resources.userEditDialogTitle = '<g:message code="mainView.jQuery.dialog.editUserForm.title"/>';

/********/
/* URLs */
/********/

//Common
resources.indexURL = '${createLink(controller:"mainView")}';


//User
resources.userDialogURL = '${createLink(controller:"user",action:"show")}';


//Board
resources.boardShowURL = '${createLink(controller:"mainView",action:"showBoard")}';


//Phase
resources.phaseShowURL = '${createLink(controller:"phase",action:"show")}';
resources.phaseFormURL = '${createLink(controller:'phase',action:'form')}';
resources.phaseDeleteURL = '${createLink(controller:"phase",action:"delete")}';
resources.hideArchiveURL = '${createLink(controller:"mainView",action:"hideArchive")}';

//Card
resources.cardShowURL = '${createLink(controller:"card",action:"show")}';
resources.cardFormURL = '${createLink(controller:"card",action:"form")}';
resources.cardSortURL = '${createLink(controller:"card",action:"sort")}';
resources.cardDeleteURL = '${createLink(controller:"card",action:"delete")}';


//Log
resources.logContentURL =  '${createLink(controller:"mainView",action:"showLogBody")}';
resources.logDefaultOrder = 'desc';
resources.logDefaultSort = 'dateCreated';

//Archive
resources.archiveContentURL = '${createLink(controller:'mainView',action:'showArchiveBody',params:['sort':'lastUpdated'])}';
resources.archiveDefaultOrder = 'desc';
resources.archiveDefaultSort = 'dateCreated';

/**********/
/* Images */
/**********/

resources.spinnerImg = '<g:resource dir="images" file="spinner.gif"/>';



</script>