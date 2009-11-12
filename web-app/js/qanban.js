
var valueBuffer = {};


jQuery.fn.qDialog = function(options) {

    var confirmReturnValue = -1;
    
    var $dialog = $(this).addClass('dialog qframe').draggable({
        handle: '.top'
    }).appendTo($('body'));

    var $top = $('<div class="top"></div>').prependTo($dialog);
    var $closeBtn = $('<div class="btn close_action qframe">X</div>').appendTo($top);
    var $content = $('<div class="content"></div>').appendTo($dialog);

    setModal( options.modal );

    function setModal( modal ){
        if( modal ){
            var $modal = $('<div class="modal"><div class="bg"></div></div>');
            $dialog.prepend($modal);
        }
    }
    setContentId( options.contentId );
    setDialogTitle( options.title );
    enableTriggerAndLoad( options );
    options.closeConfirm = setFormMode( options );
    setCloseConfirmation( options.closeConfirm );
    setConfirmationMessage( options.confirm );
    addButtons(options.buttons);

    buttonSetup();

    /*
     * Scan the dialog for buttons and adds hover logic
     */
    function buttonSetup(){
        $dialog.find('.btn').hover(
            function () {
                $(this).addClass('btnHover');
            },
            function () {
                $(this).removeClass('btnHover');
            }
            );
    }

    function setContentId(id){
        if( id != null ){
            $content.attr('id',options.contentId);
        }
    }

    function destroyDialog(){
        $dialog.toggle();
        $content.empty();
    }

    function setDialogTitle(title){
        if( title != null ){
            $top.prepend('<h1>'+options.title+'</h1>');
        }
    }

    /*
     * Checks if a trigger is specified, in that case the dialog will be invisible by default
     */
    function enableTriggerAndLoad(options){


        if( options.trigger != null ){

            
            $dialog.css('display','none');
            
            $(options.trigger).click(function(event){

                if( options.preLoadUrl != null){
                    $content.load(options.preLoadUrl,
                        options.preLoadParams, function (){ 
                            initHiddenFields();
                            
                            $dialog.toggle();
                            
                         });
                }

                
                event.preventDefault();
            });
        }

                $dialog.css('top', parseInt(( $(window).height()/2 ) - $dialog.height() ) +'px').css('left', parseInt(( $('body').width()/2 ) - ( $dialog.width()/2 )) +'px');


    }

    /*
     * Checks if a the dialog is set to form mode
     */
    function setFormMode( options ){
        if( options.form != null ){

            // Overrides the closeConfim settign to correspond to the forms value changes
            var formCloseConfirm = function(){
                var change = false;

                $('.property').each(function(){
                    change = ( $(this).val() != valueBuffer[$(this).attr('name')] );
                });

                if( change ){

                    var $confirmDialog;
                    var confirmOptions = new Array();
                    confirmOptions['confirm'] = options.form.confirmDiscardContent;

                    var buttons = new Array();
                    buttons[options.form.yes] = function(){ $confirmDialog.close();destroyDialog(); };
                    buttons[options.form.no] = function(){ $confirmDialog.close();};

                    confirmOptions['buttons'] = buttons;
                    confirmOptions['title'] = options.form.confirmDiscardTitle;
                    confirmOptions['modal'] = true;
                    $confirmDialog = $('<div class="closeConfirm"></div>').qDialog(confirmOptions);

                    return false;
                    
                }else{
                    return true;
                }

            }
            
            return formCloseConfirm;

        }

        return options.closeConfirm;
    }

    /*
     * Checks if a the close button should activate any confimation logic
     */
    function setCloseConfirmation(closeConfirm){
        if( closeConfirm != null){
            $closeBtn.click(function(){
                if( options.closeConfirm() == true ){
                    destroyDialog();
                }
            });
        }else{
            $closeBtn.click(function(){
                destroyDialog();
            });
        }
    }
 
    function setConfirmationMessage(message){
        if( options.confirm != null ) {
            $content.html('<p>'+options.confirm+'</p>');
        }
    }

    function addButtons(buttons){
        if( buttons != null ) {
            var $buttonPanel = $('<div class="buttons"></div>');
            for ( btnName in buttons) {
                var $btn = $('<div class="btn submit qframe">'+btnName+'</div>')
                    .click(buttons[btnName])
                    .appendTo($buttonPanel);
            }
            $buttonPanel.appendTo($dialog);
        }
    }


    /*
     * Adds externally available methods on a qDialog
     */
    return  {
                close : function(){
                    destroyDialog();
                    return $dialog;
                }
                
            };
};

/*
 *  Inits the under cover style form fields. External dialog method used by scanFormResult
 */
function initHiddenFields(){
    
    $('.property').each(function(i){
        $ip = $(this);
        valueBuffer[$ip.attr('name')] = $ip.val();
    });

    $('.property').blur(function () {
        $ip = $(this);
        $ip.removeClass('selected');
    });

    $('.property').click(function(){
        $(this).addClass('selected').select();
    });


}


/*
 *  Made external to enable make the function reachable from the  <g:formRemote> tag's onsuccess method call
 */

function scanFormResult(){
    if( $createCardDialog.find('.errors').size() == 0 ){
        $createCardDialog.toggle();eateCardDialog.empty();
        $createCardDialog.find('.content').empty();
    }else{
        initHiddenFields();
    }
}

