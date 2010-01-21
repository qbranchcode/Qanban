/*
 * Copyright 2010 Qbranch AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/*
 * Initialization
 *
 * This block contains the methods $.fn.qInit() and $.qInit()
 *
 *  $.qInit
 *
 *      $.qInit adds all the connections to elements that is able to use jQuery's live events.
 *      ( see: http://docs.jquery.com/Events/live )
 *
 *      This enables that function to only be called once when the application loads, and enables
 *      event handling of basic clicks etc.
 *
 *      Usage:
 *
 *          $.qInit(options);
 *
 *
 *
 *  $.fn.qInit
 *
 *      $.fn.qInit is used to initialize elements that use more specialized behavior.
 *
 *      This is used to initialize sorting and other functionality that need to be loaded
 *      throughout the workflow of the application. Typically these elements need to be initialized after
 *      being injected to the DOM-tree.    
 *
 *      It accepts the following elements:
 *
 *          #archive
 *          #log
 *          #board_*
 *          #phase_*
 *
 *      Usage:
 *
 *          $('#phase_1337').qInit(options);
 *
 *
 *  $.qInitAssigneeSelector
 *      This method is made available for the loaded card forms to make reinitialization
 */

(function($){

    const showCardMode = { view: 0, edit: 1, readOnly: 2 };
    const rulesMode = { hard: 0, soft: 1 };

    var pollingIntervals = {};


    // Live event tracking

    $.qInit = function(options){
        var defaults = {
            resources: resources
        };

        var settings = $.extend(defaults, options);

        var boardId = settings.resources.boardId;
        var userId = settings.resources.loggedInUserId;

        $('.addCardLink').live('click',function(event){
            addCard(boardId, settings);
            event.preventDefault();
        });

        $('.card').live('dblclick',function(event){
            showCard( $(this).attr('id').split('_')[1], settings);
            event.preventDefault();
        });

        $('.addPhaseLink').live('click',function(event){
            addPhase(boardId, settings);
            event.preventDefault();
        });

        $('.editPhaseLink').live('click',function(event){
            editPhase( $(this).attr('id').split('_')[1], settings );
            event.preventDefault();
        });

        $('.user').live('click',function(event){
            adminEditUser( $(this).attr('id').split('_')[1], settings);
            event.preventDefault();
        });

        $('.addUserLink').live('click',function(event){
            addUser(boardId, settings);
            event.preventDefault();
        });

        $('.role').live('dblclick',function(event){
            adminEditRole( $(this).attr('id').split('_')[1], settings);
            event.preventDefault();
        });

        $('#archiveBtn').live('click',function(event){
            toggleArchive($(this), settings);
            event.preventDefault();
        });

        $('.tab').live('click',function(event){
            showTab($(this), settings);
            event.preventDefault();
        });

        $('#logout .avatar').live('click',function(event){
            editUser(userId, settings);
            event.preventDefault();
        });

    };

    function addCard(boardId, options){
        var defaults = {
            dialog: $createCardDialog,
            resources: resources
        };
        var settings = $.extend(defaults, options);
        var dialogLoader = function(n){
            settings.dialog.qLoad({
                tries: n,
                caller: dialogLoader,
                url: settings.resources.cardFormURL,
                data: {'board.id': boardId },
                successCallback: function(){
                    settings.dialog.dialog('open');
                },
                completeCallback: function(){
                    $.qInitAssigneeSelector(settings.dialog);
                    settings.dialog.find('[name=title]', settings.dialog).focus();
                }
            });
        };
        dialogLoader(null);
    }

    function showCard(cardId, options){
        var defaults = {
            mode: showCardMode.view,
            dialog: $editCardDialog,
            resources: resources,
            admin: resources.admin
        };

        var settings = $.extend(defaults,options);

        var dialogLoader = function(n){

            settings.dialog.qLoad({
                tries: n,
                caller: dialogLoader,
                url: settings.resources.cardFormURL,
                data: {'id':cardId},
                successCallback: function(){
                    setShowCardMode(cardId,settings);
                    settings.dialog.dialog('open');
                }
            });
        };
        dialogLoader(null);
    }

    function setShowCardMode(cardId, settings){
        var dialogBtns = {};

        switch(settings.mode){
            case showCardMode.view:
                dialogBtns[settings.resources.cardFormEditBtn] = function() {
                    settings.mode = showCardMode.edit;
                    setShowCardMode(cardId, settings);
                };
                break;
            case showCardMode.edit:
                enableCardForm(settings.dialog,settings.resources.cardEditDialogTitle);
                dialogBtns[settings.resources.cardFormUpdateBtn] = function(){
                    settings.dialog.find('input[type="submit"]').click();
                    jQuery.toggleSpinner();
                };

                if( settings.admin ){
                    dialogBtns[settings.resources.cardFormDeleteBtn] = function(){
                        showDeleteCardDialog(cardId,settings);
                    };
                }
                break;

        }
        settings.dialog.dialog('option','buttons',dialogBtns);
    }

    function enableCardForm($dialog,title){
        $dialog.dialog('option', 'title', title);
        $dialog.find("input").removeAttr([readonly='readonly']);
        $dialog.find("input").addClass("edit");
        $dialog.find("textarea").removeAttr([readonly='readonly']);
        $dialog.find("textarea").addClass("edit");
        $.qInitAssigneeSelector($dialog);
    }

    $.qInitAssigneeSelector = function($context){
        $('.assignee > .avatar, #currentAssigneeName', $context).click(function(event){
            $('#assignees', $context).toggle();
            $('#currentAssigneeName', $context).toggle();
            event.preventDefault();
        });

        $('#assignees li', $context).click(function(){
            var $assignee = $(this);
            setAssigneeSelection($assignee);
            $('#assignees', $context).hide();
            $('#currentAssigneeName', $context).hide();
        });

        $('.dropdownContainer').mouseleave(function(){
            $('#assignees', $context).hide();
            $('#currentAssineeName', $context).hide();
        });
    };

    function setAssigneeSelection($assignee){
        $assignee.siblings().removeClass('selected');
        $assignee.addClass('selected');
        $('.assignee > .avatar').attr('src',$assignee.find('img').attr('src'));
        $('#currentAssigneeName').html($assignee.find('.name').html());
        $('#assigneeValue').val($assignee.attr('id').split('_')[1]);
    }

    function showDeleteCardDialog(id,settings){
        var buttons = {};

        buttons[settings.resources.yesMsg] = function(){
            var ajaxLoader = function(n){

                var closeBtn = {};
                closeBtn[settings.resources.yesMsg] = function(){
                    $(this).dialog('close');
                };

                $.qPost({
                    tries: n,
                    caller: ajaxLoader,
                    url: settings.resources.cardDeleteURL,
                    data: {'id':id},
                    successCallback: function(){
                        $('#card_'+id).remove();
                        $('#editCardDialog').empty().dialog('close');
                        updatePhaseHeight();
                    },
                    errorCallback: function(XMLHttpRequest, textStatus, errorThrown){
                        $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>'+
                          settings.resources.phaseDeleteErrorMsg+'</p></div>').dialog({
                            modal: true,
                            buttons: closeBtn
                        });
                    }
                });

            };

            ajaxLoader(null);

            var $dialog = $(this);

            $dialog.dialog('close');
            $dialog.empty();
            $dialog.dialog('destroy');

        };

        buttons[settings.resources.noMsg] = function(){
            var $dialog = $(this);

            $dialog.dialog('close');
            $dialog.empty();
            $dialog.dialog('destroy');

        };

        $('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>'+
          settings.resources.cardDeleteConfirmMsg+'</p></div>').dialog({
            resizable: false,
            height: 140,
            modal: true,
            buttons: buttons
        });
    }

    function addPhase(boardId, options){
        var defaults = {
            dialog: $createPhaseDialog,
            resources: resources
        };

        var settings = $.extend(defaults, options);

        var dialogLoader = function(n){
            settings.dialog.qLoad({
                tries: n,
                caller: dialogLoader,
                url: resources.phaseFormURL,
                data: {'board.id': boardId},
                successCallback: function(){
                    var $archiveBtn = $('#archiveBtn');
                    if( $archiveBtn.hasClass('open') ){
                        $archiveBtn.removeClass('open');
                        $('.phaseWrapper:last-child').remove();
                        fixBoardWidth();

                    }
                    settings.dialog.dialog('open');
                },
                completeCallback: function(){
                    initPhasePlacementSelector(settings.dialog);
                }
            });
        };

        dialogLoader(null);

    }

    function editPhase(phaseId, options){

        var defaults = {
            dialog: $editPhaseDialog,
            resources: resources
        };

        var settings = $.extend(defaults, options);

        var dialogLoader = function(n){

            settings.dialog.qLoad({
                tries: n,
                caller: dialogLoader,
                url: resources.phaseFormURL,
                data: {'id': phaseId},
                successCallback: function(){
                    var dialogBtns = {};

                    dialogBtns[settings.resources.phaseFormUpdateBtn] = function(){
                        settings.dialog.find('input[type="submit"]').click();
                    };
                    dialogBtns[settings.resources.phaseFormDeleteBtn] = function(){
                        showDeletePhaseDialog(phaseId,settings);
                    };

                    settings.dialog.dialog(
                            'option',
                            'buttons',
                            dialogBtns);

                    settings.dialog.dialog('open');

                },
                completeCallback: function(){
                    initPhasePlacementSelector(settings.dialog);
                }

            });
        };

        dialogLoader(null);

    }

    function showDeletePhaseDialog(phaseId, settings){
        var buttons = {};

        buttons[settings.resources.yesMsg] = function(){

            var deleteCaller = function(n){
                $.qPost({
                    url: settings.resources.phaseDeleteURL,
                    data: {id:phaseId},
                    successCallback: function(){
                        deletePhase($('#phaseWrapper_'+phaseId));
                        $('#editPhaseDialog').dialog('close');
                    },
                    errorCallback: function( XMLHttpRequest, textStatus, errorThrown){
                        var okBtn = {};
                        okBtn[settings.resources.okMsg] = function(){
                            $(this).dialog('close');
                        };

                        $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>'+
                          settings.resources.phaseDeleteErrorMsg + '</p></div>').dialog({
                            modal: true,
                            buttons: okBtn
                        });
                    }
                });
            };
            deleteCaller(null);

            var $dialog = $(this);

            $dialog.dialog('close');
            $dialog.empty();
            $dialog.dialog('destroy');

        };

        buttons[settings.resources.noMsg] = function(){
            var $dialog = $(this);

            $dialog.dialog('close');
            $dialog.empty();
            $dialog.dialog('destroy');

        };

        $('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>'+
          settings.resources.phaseDeleteConfirmMsg + '</p></div>').dialog({
            resizable: false,
            height: 140,
            modal: true,
            buttons: buttons
        });
    }

    function deletePhase($phaseWrapper){
        var $prevPhaseWrapper = $phaseWrapper.prev();
        var $archiveBtn = $phaseWrapper.find('#archiveBtn');

        if( $archiveBtn.length ){
            $archiveBtn.insertAfter($prevPhaseWrapper.find('[href=#edit]'));
        }

        $phaseWrapper.remove();
        fixBoardWidth();
        $prevPhaseWrapper.qInit();
    }

    function initPhasePlacementSelector($dialog){
        $('#phasePlacer', $dialog).sortable({
            cancel:'.old',
            placeholder:'phaseplaceholder',
            stop:function(event,ui){
                $('input[name=phasePos]', $dialog).val(ui.item.prevAll().length);
            }});
    }

    function toggleArchive($archiveBtn, options){
        var defaults = {
            resources: resources
        }

        var settings = $.extend(defaults, options)

        if(!$archiveBtn.hasClass('open')){
            $archiveBtn.addClass('open');

            var archiveLoader = function(n){
                $.qGet({
                    tries: n,
                    caller: archiveLoader,
                    url: resources.phaseShowURL,
                    data: { id: getArchiveId($archiveBtn), cardLimit: 'auto' },
                    successCallback: function(data,textStatus){

                        var $archive = $(data);
                        $archive.qInject({index: -1,archive: true}).qInit();
                        fixBoardWidth();

                    }
                });
            };

            archiveLoader();

        }else{

            var hideCaller = function(n){
                $.qPost({
                    url: settings.resources.hideArchiveURL
                });
            };

            hideCaller(null);

            $archiveBtn.removeClass('open');
            $('.phaseWrapper:last-child').remove();
            fixBoardWidth();
        }

    }

    function getArchiveId($archiveBtn){
        var classList = $archiveBtn.attr('class').split(' ');
        var id = null;

        $.each(classList, function(index,item){
            var classSubstring = item.split('_');
            if( classSubstring[0].replace(/^\s*|\s*$/g,'') == 'archId' ){
                id = classSubstring[1];
            }
        });

        return id;
    }

    function showTab($tab, options){
        var defaults = {
            target: $('#wrapper'),
            fadeSpeed: 'fast'
        };

        var settings = $.extend(defaults,options);

        var tabLoader = function(n){

            settings.target.qLoad({
                tries: n,
                caller: tabLoader,
                url: $tab.attr('href'),
                successCallback: function(data,textstatus){
                    $('.tab').removeClass('active');
                    $tab.addClass('active');

                    settings.target.fadeIn(settings.fadeSpeed, function(){

                        if( pollingIntervals.main > -1 ){
                            clearInterval(pollingIntervals.main);
                        }

                        if( data.indexOf('<div id="log">') != -1 ){
                            enableLogView(this,settings);
                        }else if( data.indexOf('<div id="archive">') != -1){
                            enableArchiveView(this,settings);
                        } else if( data.indexOf('<div id="users">') != -1 ){
                            enableUsersView(this,settings);
                        }else if( data.indexOf('<div id="board') != -1){
                            enableBoardView(this,settings);
                        }

                    });
                }
            });

        }

        settings.target.fadeOut(settings.fadeSpeed,function(){
            tabLoader();
        });
    }

    function enableLogView($container,settings){
        var logData = {};
        logData.url = settings.resources.logContentURL;
        logData.order = settings.resources.logDefaultOrder;
        logData.sort = settings.resources.logDefaultSort;

        enableTableSorting(logData);
        pollingIntervals.main = setInterval(function(){pollTable(logData)}, 1000);
    }

    function enableArchiveView($container,settings){
        var archiveData = {};
        archiveData.url = settings.resources.archiveContentURL;
        archiveData.order = settings.resources.archiveDefaultOrder;
        archiveData.sort = settings.resources.archiveDefaultSort;

        enableTableSorting(archiveData);
        enableArchiveCardSelection();
        pollingIntervals.main = setInterval(function(){pollTable(archiveData)}, 1000);

    }

    function enableUsersView($container,settings){
        var usersData = {};
        usersData.url = settings.resources.usersContentURL;
        usersData.order = settings.resources.usersDefaultOrder;
        usersData.sort = settings.resources.usersDefaultSort;
    }

    function enableTableSorting(tableData){

        $('.ajaxSortableColumn').click(function(event){

            var $column = $(this);
            var settings = getSortSettings($column);

            var loadSortedContent = function(n){
                $('tbody').qLoad({
                    url: settings.url,
                    tries: n,
                    caller: loadSortedContent,
                    data: { order: settings.order },
                    successCallback: function(){
                        tableData.url = settings.url;
                        tableData.order = settings.order;
                        $column.removeClass(settings.orderClass);
                        $column.addClass( settings.order == 'asc' ? 'order_desc' : 'order_asc' );
                    }
                });
            };

            loadSortedContent();

            event.preventDefault();

        });

    }

    function getSortSettings($column){
        var settings = {};
        settings.url =  $column.attr('href');

        var classList = $column.attr('class').split(' ');

        $.each(classList, function(index,item){
            var classSubstrings = item.split('_');
            if( classSubstrings[0].replace(/^\s*|\s*$/g,'') == 'order' ){
                settings.orderClass = item;
                settings.order = classSubstrings[1];
            }
        });

        return settings;
    }

    function pollTable(tableData, options){
        var defaults = {
            tableBody: 'tbody'
        }

        var settings = $.extend(defaults, options);

        var $tableBody = $(settings.tableBody);

        if( isScrollbarAtBottom() && !allContentIsLoaded($tableBody.data('max')) ){
            var contentFetcher = function(n){



                $tableBody.qLoad({
                    tries: n,
                    caller: contentFetcher,
                    append: true,
                    url: tableData.url,
                    data: { order: tableData.order, offset: getOffset($tableBody)}
                });

            };

            contentFetcher();
        }
    }

    function getOffset($tableBody){
        return parseInt($('tr:last-child', $tableBody).attr('id').split('_')[1])+1;
    }

    function enableArchiveCardSelection(){
        $('.archive').click(function(event){
            var $trigger = $(event.target);

            if( $trigger.hasClass('showCardLink') ){
                showCard( $(event.target).attr('id').split('_')[1], { mode: showCardMode.readOnly });
            }
        });
    }

    function isScrollbarAtBottom(options){
        var defaults = {
            scrollContent: '.scrollContent'
        };

        var settings = $.extend(defaults, options);

        var $scrollContent = $(settings.scrollContent);

        if( $scrollContent.length ){

            var $children = $scrollContent.children();

            if( $scrollContent[0].scrollHeight - $scrollContent.scrollTop() == $scrollContent.outerHeight()){
                return true;
            }else{
                return false;
            }

        }
    }

    function allContentIsLoaded(maxElements, options){

        var defaults = {
            scrollContent: '.scrollContent'
        };

        var settings = $.extend(defaults, options);

        if( $(settings.scrollContent).children().size() < maxElements )
            return false;

        return true;

    }

    function enableBoardView($container, settings){
        reInitBoardElements($container);
    }

    function reInitBoardElements($container){
        $('.board', $container).qInit();
        $('.phaseWrapper', $container).qInit();
    }

    function refreshBoardData(settings){

        var boardLoader = function(n){

            settings.target.qLoad({
                tries: n,
                caller: boardLoader,
                url: settings.resources.boardShowURL,
                data: {'board.id':1},
                successCallback: function(data,textstatus){
                    reInitBoardElements(this);
                }
            });

        };
        boardLoader();
    }

    function addUser(boardId, options){
        var defaults = {
            dialog: $createUserDialog,
            resources: resources
        }

        var settings = $.extend(defaults, options);

        var dialogLoader = function(n){
            settings.dialog.qLoad({
                tries: n,
                caller: dialogLoader,
                url: settings.resources.addUserDialogURL,
                data: {'user.id': boardId },
                successCallback: function(){
                    settings.dialog.dialog('open');
                }
            });
        }

        dialogLoader();

    }

    function editUser(userId, options){
        var defaults = {
            dialog: $editUserDialog,
            resources: resources
        };

        var settings = $.extend(defaults, options);

        var buttons = {};

        buttons[settings.resources.userFormUpdateBtn] = function(){
            settings.dialog.find('input[type="submit"]').click();
        };

        buttons[settings.resources.userFormPasswordBtn] = function(){
            showPasswordDialog(userId, settings);
        };

        settings.dialog.dialog('option','buttons', buttons);

        var dialogLoader = function(n){
            settings.dialog.qLoad({
                tries: n,
                caller: dialogLoader,
                url: settings.resources.userDialogURL,
                data: {'id': userId },
                successCallback: function(){
                    settings.dialog.dialog('open');
                }
            });
        }

        dialogLoader(null);

    }

    function showPasswordDialog(userId, options){
        var defaults = {
            pwdialog: $changePasswordDialog,
            resources: resources
        };

        var settings = $.extend(defaults, options );

        var dialogLoader = function(n){
            settings.pwdialog.qLoad({
                tries: n,
                caller: dialogLoader,
                url: settings.resources.passwordDialogURL,
                data: {'id':userId},
                successCallback: function(){
                    settings.pwdialog.dialog('open');
                }
            });
        }

        dialogLoader(null);
    }

    function adminEditUser(userId, options){
        var defaults = {
            resources: resources,
            target: "#editBox"
        }

        var settings = $.extend(defaults, options);

        var editUserFieldLoader = function(n){
            $(settings.target).qLoad({
                tries: n,
                caller: editUserFieldLoader,
                url: settings.resources.settingsShowUserURL,
                data: {'user.id': userId}
            });
        }

        editUserFieldLoader();
    }

    function adminEditRole(roleId, options){
        var defaults = {
            resources: resources,
            target: "#editBox"
        }

        var settings = $.extend(defaults, options);

        var editRoleFieldLoader = function(n){
            $(settings.target).qLoad({
                tries: n,
                caller: editRoleFieldLoader,
                url: settings.resources.settingsShowRoleURL,
                data: {'role.id': roleId}
            });
        }

        editRoleFieldLoader();
    }

    // Element specific initializations

    $.fn.qInit = function(){
        var $objects = $(this);

        return $objects.each(function(index,obj){
            var $obj = $(obj);

            var objectType = $obj.attr('id').split('_')[0];

            switch(objectType){
                case 'archive':
                    initArchive($obj);
                    break;
                case 'log':
                    initLog($obj);
                    break;
                case 'board':
                    initBoard($obj);
                    break;
                case 'phaseWrapper':
                    initPhase($obj);
                    break;
            }

        })
    }

    function initArchive($card){

    }

    function initLog($log){

    }

    function initBoard($board){
        fixBoardWidth();
        pollingIntervals.main = setInterval(function(){refreshBoardData(settings);}, 18000000);
    }

    function fixBoardWidth(){
        var $board = $('.board');
        var numberOfPhases = $('.phase', $board).size();
        var boardWidth = $board.width();
        var phaseWidth = ( boardWidth / numberOfPhases ) - 8 + 'px';
        //$('.phaseAutoWidth').width(phaseWidth);
        $.setCssRule('.phaseAutoWidth', 'width', phaseWidth);
    }

    function initPhase($phaseWrapper, options){
        var defaults = {
            placeholder: 'placeholder',
            activeCardOpacity: 0.6,
            unavailablePhaseOpacity: 0.3,
            rules: rulesMode.hard,
            resources: resources
        };
        var settings = $.extend(defaults, options);
        var $phases = $('.phase');
        var $phase = $('.phase', $phaseWrapper);

        switch(settings.rules){
            case rulesMode.hard:

                var $nextPhase = $('.phase', $phase.parent().parent().next('.phaseWrapper'));
                var $prevPhase = $('.phase', $phase.parent().parent().prev('.phaseWrapper'));

                $phase.sortable({
                    placeholder: settings.placeholder,
                    start: function(event, ui){
                        $phase.sortable('option','icv',getCardValues(ui.item));
                        //ui.placeholder.parent() == $phase
                        fadeOutUnavailablePhases($phase,settings.unavailablePhaseOpacity);
                        setCardOpacity(ui.item, settings.activeCardOpacity);
                    },
                    stop: function(event, ui){
                        var $phase = $(this);
                        var icv = $phase.sortable('option','icv');
                        // Checks weather the card has been caught by the archive icon before the sort reaches this step.
                        if( !ui.item.data('caught') ){

                            var ccv = getCardValues(ui.item);
                            updateCardDomain(ui.item,icv,ccv,settings);
                        }else{
                            updateCardCount('#'+icv.phase, -1);
                            ui.item.data('caught',false);
                        }
                        setCardOpacity(ui.item, 1);
                        fadeInPhases();
                        updatePhaseHeight();

                    }
                });

                connectPhases($prevPhase,$phase);
                connectPhases($phase,$nextPhase);

                var $archiveBtn = $phase.parent().find('#archiveBtn');

                if( $archiveBtn.length ){

                    var acceptSelector = "#" + $phase.attr('id') + " > .card";

                    $archiveBtn.droppable({
                        hoverClass: 'cardHover',
                        accept: acceptSelector,
                        greedy:true,
                        tolerance: 'pointer',
                        drop: function(event,ui){

                            var id = ui.draggable.attr('id').split('_')[1];
                            var newPhase = null;

                            var classList = $archiveBtn.attr('class').split(' ');

                            $.each(classList,function(index, item){
                                var classSubstrings = item.split('_');
                                if( classSubstrings[0].replace(/^\s*|\s*$/g,'') == 'archId' ){
                                    newPhase = classSubstrings[1];
                                }
                            });

                            var archiveCall = function(n){
                                $.qPost({
                                    caller: archiveCall,
                                    tries: n,
                                    url: settings.resources.cardSortURL,
                                    data: {'id': id, 'newPos': 0, 'newPhase': newPhase},
                                    successCallback: function(data,textStatus){
                                        var $archPhase = $phase.parent().parent().next().find('.phase');

                                        if( $archPhase.length ){
                                            $archPhase.append($(ui.draggable));
                                            ui.draggable.show();
                                            $archPhase.find('.card:first').slideUp('slow',function(){
                                                $(this).remove();
                                            });
                                        }else{
                                            ui.draggable.remove();
                                        }
                                    },
                                    errorCallback: function(data,textStatus){
                                        ui.draggable.show();
                                    },
                                    completeCallback: function(){}
                                });
                            };

                            ui.draggable.hide();

                            archiveCall(null);

                            ui.draggable.data('caught',true);

                        }
                    });

                }

                break;
            case rulesMode.soft:
                $phase.sortable({
                    placeholder: settings.placeholder,
                    connectWith: '.phase, #archiveBtn',
                    start: function(event,ui){
                        $phase.sortable('option','icv',getCardValues(ui.item));
                        setCardOpacity(ui.item, settings.activeCardOpacity);
                    },
                    stop: function(event, ui){
                        var $phase = $(this);
                        setCardOpacity(ui.item, 1);
                        var icv = $phase.sortable('option','icv');
                        var ccv = getCardValues(ui.item);
                        updateCardDomain(ui.item,icv,ccv,settings);
                    }
                });
                break;
            default:
                throw "Invalid mode: " + settings.rule + ". Available modes is: 'rulesMode.hard' (default) and 'rulesMode.soft'";
        }

    }

    function connectPhases($phase,$nextPhase){

        var selector = '#' + $nextPhase.attr('id') + '.available';

        $phase.sortable('option','connectWith',selector);
    }

    function updateCardDomain($card,icv,ccv,settings){

        var newPosition = ccv.position;
        var newPhaseId = ccv.phase.split('_')[1];
        var cardId = ccv.id.split('_')[1];

        if( cardHasChangedPhase(icv,ccv) ){
            updateCardCount('#'+icv.phase, -1);
            updateCardCount('#'+ccv.phase, 1);
            if( !newPhaseIsArchive('#'+ccv.phase) ){
                showMoveCardDialog($card,icv,ccv,settings);
            }else{
                sortCard(cardId,newPhaseId,newPosition,settings.resources.cardSortURL);

                $('#'+ccv.phase).find('.card:first').slideUp('slow',function(){
                    $(this).remove();
                });
            }
        }else if(icv.position != ccv.position){

            sortCard(cardId,newPhaseId,newPosition,settings.resources.cardSortURL);
        }

    }

    function newPhaseIsArchive(phaseSelector){
        if( $(phaseSelector).parent().parent().data('archive') ){
            return true;
        }
        return false;
    }

    function updateCardCount(phaseSelector, value){
        var $phase = $(phaseSelector);
        var $cardCountElement = $phase.parent().find('.limitLine');
        var currentValue = $cardCountElement.html().replace(/^\s*|\s*$/g,'');
        var integers = currentValue.split('/');

        if( integers.length == 2 ){
            var newCount = parseInt(integers[0]) + value;
            $cardCountElement.html(newCount+'/'+integers[1]);

            if( newCount == parseInt(integers[1]) && $phase.hasClass('available') ){
                $phase.removeClass('available');
            }else if( newCount != parseInt(integers[1]) && !$phase.hasClass('available') ){
                $phase.addClass('available');
            }

        }
    }

    function updatePhaseHeight(){
        var $phases = $('.phase');
        var maxCardCount = 0;

        $phases.each(function(){
            var $phase = $(this);
            var numberOfCards = $('.card', $phase).size();
            if( numberOfCards > maxCardCount ){
                maxCardCount = numberOfCards;
            }
        });

        var height = ( maxCardCount * $('.card').height() ) + 'px';

        if( $phases.height()+'px' != height ){
            $phases.animate({height: height},300,'swing',function(){
                $.setCssRule('.phaseAutoHeight','height',height);
            });
        }
    }

    function showMoveCardDialog($card,icv,ccv,settings){
        var rollbackSelector = '#' + icv.phase + ' .card';
        var $dialog = $('<div id="moveCardDialog"></div>');

        var buttons = {};

        buttons[settings.resources.okMsg] = function(){
            $(this).find('input[type="submit"]').click();
            $(this).dialog('option','confirmed',true);
            $(this).dialog("close");
        };

        buttons[settings.resources.cancelMsg] = function(){
            $(this).dialog('close');
        };

        $dialog.dialog({
            autoOpen: false,
            modal: true,
            title: settings.resources.cardMoveDialogTitlePrefix + getCardTitle($card),
            width: 400,
            icv: icv,
            card: $card,
            confirmed: false,
            rollbackSelector: rollbackSelector,
            buttons: buttons,
            close: function(event,ui){

                var $dialog = $(this);

                if( !$dialog.dialog('option','confirmed') ){

                    var icv = $dialog.dialog('option','icv');
                    var card = $dialog.dialog('option','card');
                    var rollbackSelector = $dialog.dialog('option','rollbackSelector');
                    var rollbackPhaseSize = $('#'+icv.phase+'> .card').size();

                    updateCardCount(card.parent(), -1);
                    updateCardCount('#'+icv.phase, 1);

                    if( rollbackPhaseSize == 0 || rollbackPhaseSize <= icv.position ){
                        $('#'+icv.phase).append(card);

                    }else{
                        var $cardToPush = $(rollbackSelector).eq(icv.position);
                        card.insertBefore($cardToPush);

                    }
                    updatePhaseHeight();
                }

                $dialog.dialog('destroy');
                $dialog.remove();
            }
        });

        var dialogLoader = function(n){
            $dialog.qLoad({
                caller: dialogLoader,
                tries: n,
                url: settings.resources.cardFormURL,
                data: {'id':ccv.id.split('_')[1],'newPhase':ccv.phase.split('_')[1],'newPos':ccv.position},
                successCallback: function(){
                    $dialog.dialog('open');
                },
                completeCallback: function(){
                    $.qInitAssigneeSelector($dialog);
                }
            });
        };

        dialogLoader(null);

    }

    function getCardTitle($card){
        return $card.find('.titleWrapper').html();
    }

    function sortCard(cardId, newPhaseId, newPosition, url){

        var sortCaller = function(n){
            $.qPost({
                url: url,
                data: { 'id': cardId, 'newPhase' : newPhaseId, 'newPos' : newPosition },
                tries: n,
                caller: sortCaller
            });
        };
        sortCaller(null);

    }

    function cardHasChangedPhase(icv, ccv){
        return ccv.phase != icv.phase;
    }

    function setCardOpacity($card, opacity){
        if( $card.css('opacity') != opacity ){
            $card.animate({opacity: opacity},300);
        }
    }

    function fadeOutUnavailablePhases($currentPhase,opacity){
        $('.phase').filter(function(){
            var id = $(this).attr('id');
            var notConnectedPhase = $currentPhase.sortable('option','connectWith');
            return id != $currentPhase.attr('id') && id != $(notConnectedPhase).attr('id');
        }).parent().animate({opacity:opacity},300);
    }

    function fadeInPhases(){
        $('.phase').parent().animate({opacity: 1},300);
    }

    function getCardValues($card){
        return {
            id: $card.attr('id'),
            phase: $card.parent().attr('id'),
            position: $card.prevAll().length - 1 // Compensate for the cardSpacer elem.
        };
    }

})(jQuery);

/*
 * Injection
 *
 *  $.fn.qInject(options);
 *
 *  This function is used to inject new elements to the dom at the right place.
 *  Elements that are compatible is .card and .phaseWrapper. PhaseWrappers also needs the index option to be set.
 *
 */

(function($){

    $.fn.qInject = function(options){

        var $objects = $(this);

        return $objects.each(function(index,obj){

            var $obj = $(obj);

            if( $obj.parent().parent().length != 0 )
                throw "Can't inject a element thats already in the DOM tree";

            var objectType = $obj.attr('id').split('_')[0];

            switch(objectType){
                case 'card':
                    injectCard($obj,options);
                    break;
                case 'phaseWrapper':
                    injectPhase($obj,options);
                    break;
                default:
                    throw '$.qInject() only takes card and phase elements';
            }

        });

    };

    function injectCard($card,options){
        var defaults = { destination: $('.phase:first') };
        var settings = $.extend(defaults, options);

        doesExist($card) ? replaceCard($card) : insertCard($card, settings.destination);
    }

    function insertCard($card, $destination){
        $destination.append($card);
        updateCardCount($destination, 1);
        updatePhaseHeight();
    }

    function updateCardCount(phaseSelector, value){
        var $phase = $(phaseSelector);
        var $cardCountElement = $phase.parent().find('.limitLine');
        var currentValue = $cardCountElement.html().replace(/^\s*|\s*$/g,'');
        var integers = currentValue.split('/');

        if( integers.length == 2 ){
            var newCount = parseInt(integers[0]) + value;
            $cardCountElement.html(newCount+'/'+integers[1]);

            if( newCount == parseInt(integers[1]) && $phase.hasClass('available') ){
                $phase.removeClass('available');
            }else if( newCount != parseInt(integers[1]) && !$phase.hasClass('available') ){
                $phase.addClass('available');
            }

        }
    }

    function updatePhaseHeight(){
        var $phases = $('.phase');
        var maxCardCount = 0;

        $phases.each(function(){
            var $phase = $(this);
            var numberOfCards = $('.card', $phase).size();
            if( numberOfCards > maxCardCount ){
                maxCardCount = numberOfCards;
            }
        });

        var height = ( maxCardCount * $('.card').height() ) + 'px';

        if( $phases.height()+'px' != height ){
            $phases.animate({height: height},300,'swing',function(){
                $.setCssRule('.phaseAutoHeight','height',height);
            });
        }
    }

    function replaceCard($card){
        replaceObject($card);
    }

    function injectPhase($phase,options){
        var defaults = {
            destination: $('#phaseList'),
            archive: false
        };
        var settings = $.extend(defaults, options);

        if( !settings.index )
            throw "Injection of a phase requires the option 'index' to specify where to inject the element";

        doesExist($phase) ? replacePhase($phase, settings.index) : insertPhase($phase, settings);

    }

    function insertPhase($phase, settings){
        var $phases = settings.destination.find('.phaseWrapper');
        var index = settings.index < 0 ? $phases.size() + settings.index + 1 : settings.index ;
        if( $phases.size() > index ){
            $phase.insertBefore($phases.get(index));
        }else{
            if( !settings.archive ){
                $('#archiveBtn').remove();
            }else{
                $phase.data("archive",true);
            }
            settings.destination.append($phase);
        }
        fixBoardWidth();
    }

    function fixBoardWidth(){
        var $board = $('.board');
        var numberOfPhases = $('.phase', $board).size();
        var boardWidth = $board.width();
        var phaseWidth = ( boardWidth / numberOfPhases ) - 8 + 'px';
        //$('.phaseAutoWidth').width(phaseWidth);
        $.setCssRule('.phaseAutoWidth', 'width', phaseWidth);
    }

    function replacePhase($phase, index){
        var $archiveBtn = $('#' + $phase.attr('id')).find('#archiveBtn')

        replaceObject($phase);

        if( $phase.prev().find('#archiveBtn').length ){
            $('.tab[href*=showArchive]').html($('h3',$phase).html());
        }
        
        var oldIndex = parseInt($phase.prevAll().size());
        var $elementAtDestination = $('.phaseWrapper').eq(index);

        if( oldIndex != index ){
            if( oldIndex > index ){
                $phase.insertBefore($elementAtDestination);
                if( $archiveBtn.length ){
                    $archiveBtn.insertAfter($('[href=#edit]', $('.phaseWrapper').eq(oldIndex)));
                }
            }else if( oldIndex < index ){
                $('#archiveBtn', $elementAtDestination).remove();
                $phase.insertAfter($elementAtDestination);
            }
            $('.phaseWrapper').qInit();
        }
    }

    function doesExist($obj){
        return $('#'+$obj.attr("id")).size() == 1
    }

    function replaceObject($obj){
        $('#'+$obj.attr('id')).after($obj).remove();
    }

    function debug(msg){
        if (window.console && window.console.log)
            window.console.log(msg);
    }

})(jQuery);

/*
 * AJAX
 *
 * Wrapper functions commonly used by the Qanban application.
 *
 * All calls using this methods is presumed to be stored as a closure excepting one argument representing the try count.
 * This way our ajax call gives it three times before giving up in case some thing delays the server response.
 *
 * Ex.
 *
 *      var ajaxCaller = function(tries){
 *
 *          $.qXxx({ tries: tries, caller: ajaxCaller, ... });
 *
 *      };
 *
 *      ajaxCaller();
 *
 */

(function($){

    $.fn.qLoad = function(options){

        var $element = $(this);

        const defaults = {
            type: 'GET',
            tries: 1,
            error: function(XMLHttpRequest, textStatus, errorThrown){
                if( XMLHttpRequest.status == 0 ){
                    if( defaults.tries > 3 ){
                        showServerDownMesg( defaults.tries );
                    } else {
                        defaults.tries = defaults.tries + 1;
                        defaults.caller( defaults.tries );
                    }
                }
                if( defaults.errorCallback ){
                    defaults.errorCallback(XMLHttpRequest, textStatus, errorThrown);
                }
            },
            success: function(data, textStatus){
                var fail = data.indexOf('<html>') != -1;
                if( fail ) {
                    showSessionTimeoutMesg();
                } else {
                    if( defaults.successCallback ) {
                        defaults.successCallback(data, textStatus);
                    }
                    defaults.append ? $element.append(data) : $element.html(data);
                }
            },
            complete: function(){$.toggleSpinner(); }
        };

        var settings = $.extend(defaults, options);

        if( options.completeCallback ){
            settings.complete = function() { options.completeCallback(); $.toggleSpinner(); };
        }
        $.toggleSpinner();
        $.ajax(settings);

    };

    $.qPost = function(options){

        const defaults = {
            tries: 1,
            cache: false,
            type: 'POST',
            error: function(XMLHttpRequest, textStatus, errorThrown){
                if( XMLHttpRequest.status == 0 ){
                    if( defaults.tries > 3 ){
                        $.toggleSpinner();
                        showServerDownMesg( defaults.tries );
                    } else {
                        defaults.tries = defaults.tries + 1;
                        defaults.caller( defaults.tries );
                    }
                }
                if( defaults.errorCallback ){
                    $.toggleSpinner();
                    defaults.errorCallback(XMLHttpRequest, textStatus, errorThrown);
                }
            },
            success: function(data, textStatus){
                $.toggleSpinner();
                var fail = data.indexOf('<html>') != -1;
                if( fail ) {
                    showSessionTimeoutMesg();
                } else {
                    if( defaults.successCallback ) {
                        defaults.successCallback(data, textStatus);
                    }
                }
            }
        };

        var settings = $.extend(defaults,options);
        $.toggleSpinner();
        $.ajax(settings);

    };

    //Req options: url, successCallback,data,dataType
    $.qGet = function(options){
        var defaults = {
            tries: 1,
            type: 'GET',
            error: function(XMLHttpRequest, textStatus, errorThrown){
                if( XMLHttpRequest.status == 0 ){
                    if( defaults.tries > 3 ){
                        $.toggleSpinner();
                        showServerDownMesg( defaults.tries );
                    } else {
                        defaults.tries = defaults.tries + 1;
                        defaults.caller( defaults.tries );
                    }
                }
                if( defaults.errorCallback ){
                    defaults.errorCallback(XMLHttpRequest, textStatus, errorThrown);
                }
            },
            success: function(data, textStatus){
                $.toggleSpinner();
                var fail = data.indexOf('<html>') != -1;
                if( fail ) {
                    showSessionTimeoutMesg();
                } else {
                    if( defaults.successCallback) {
                        defaults.successCallback(data, textStatus);
                    }
                }
            }
        };

        var settings = $.extend(defaults,options);
        $.toggleSpinner();
        $.ajax(settings);

    };

    /*

     This won't work because there will be a leak between the different ajax methods event though the map is created as a 'const'.
     I can't figure out why atm and this code will be a part of the defaults in each ajax wrapper fo the time being. !DRY :/
     Check how the jQuery $.extend() methods works to try making a better solution.

     const qAjaxDefaults = {
     tries: 1,
     cache: false,
     error: function(XMLHttpRequest, textStatus, errorThrown){
     if( XMLHttpRequest.status == 0 ){
     if( qAjaxDefaults.tries > 3 ){
     showServerDownMesg( qAjaxDefaults.tries );
     } else {
     qAjaxDefaults.tries = qAjaxDefaults.tries + 1;
     qAjaxDefaults.caller( qAjaxDefaults.tries );
     }
     }
     if( qAjaxDefaults.errorCallback ){
     qAjaxDefaults.errorCallback(XMLHttpRequest, textStatus, errorThrown);
     }
     }
     };

     */

    function showServerDownMesg(n) {
        if( $('#offline').size() == 0 ){
            $('<div id="timeout"><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>'+resources.offlineMsg+'</p></div>').dialog({
                modal: true });
        }
    }

    function showSessionTimeoutMesg() {
        var ok = resources.okMsg;
        if( $('#timeout').size() == 0 ){
            $('<div id="timeout"><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>'+
              resources.sessionTimeoutMsg+'</p></div>').dialog({
                modal: true,
                buttons: {
                    ok : function() {
                        $(this).dialog('close');
                        window.location = resources.indexURL;
                    }
                },
                close: function() {
                    window.location = resources.indexURL;
                }
            });
        }
    }

    $.toggleSpinner = function(){
        // TODO: Set a delay for showing the spinner?
        if( !$('#spinner').length ){
            $('<div id="spinner"><img src="'+resources.spinnerImg+'"/></div>').appendTo($('body'));
        } else {
            $('#spinner').remove();
        }
    }



})(jQuery);

/*
 * Dialog
 *
 *  $.fn.qDialog(options);
 *
 *  A simple wrapper for the jQuery UI dialog plugin that sets our common settings by default.
 *
 */

(function($){

    $.fn.qDialog = function(options) {
        var defaults = {
            width: 300,
            autoOpen: false,
            modal: true,
            close: function(){ $(this).empty(); }
        };

        if( options.close ){
            var closeFunction = options.close;
            options.close = function(){ closeFunction(); $(this).empty(); };
        }

        var settings = $.extend(defaults, options);

        $(this).dialog(settings);

    };

    $.fn.qRefreshDialog = function(options){

        var defaults = {};
        var settings = $.extend(defaults,options);
        var $dialog = $(this);


        if( !hasErrors($dialog) ){
            var $formContent = $(settings.formData);
            var id = getEntityIdentifier($formContent);
            if( settings.url ){
                var objectFetcher = function(n){
                    var options = {
                        caller: objectFetcher,
                        tries: n,
                        url: settings.url+'/'+id,
                        data: {format:'html'},
                        successCallback: function(data,textStatus){
                            var $newObject= $(data);
                            if( settings.indexSelector ){
                                var index = getIndexValue(settings.indexSelector, $dialog);
                                $newObject.qInject({index:index}).qInit();
                            }else{
                                $newObject.qInject().qInit();
                            }
                            closeDialog($dialog,settings);
                        }

                    };
                    $.qGet(options);
                }
                objectFetcher();
            }else{
                closeDialog($dialog,settings);
            }

        }else if( settings.errorCallback ){
            settings.errorCallback($dialog);
        }
    };

    function getEntityIdentifier($formContent){
        return $formContent.find('[name=id]').val();
    }
    function getIndexValue(selector, $context){
        var $indexField = $context.find(selector);
        if( $indexField.size() == 1 ){
            return $indexField.val();
        }else{
            throw 'Could not find the specified value!';
        }
    }

    function hasErrors($dialog){
        return $dialog.find('.errors').size() > 0;
    }

    //successTitle,successMessage
    function closeDialog($dialog, options){

        var defaults = {
            resources: resources
        };

        var settings = $.extend(defaults, options);

        var buttons = {};
        buttons[resources.okMsg] = function() {
            $(this).dialog('close').remove();
        };

        $dialog.dialog('close');

        if( settings.successTitle && settings.successMessage ){
            $('<div id="popup" title="'+settings.successTitle+'">'+settings.successMessage+'</div>').dialog({
                modal: true,
                open: function(){
                    setTimeout(function(){$('#popup').dialog('close').remove()},1250);
                },
                buttons: buttons
            });
        }
    }

})(jQuery);

/*
 *  Function for changing the css properties.
 *  Using this instead of the usual jQuery way of changing css values will set the value directly to the
 *  css class instead of adding them to the object that has the specific class.
 *
 *  This makes the changes more permanent, and affect elements with that class that's injected to the
 *  DOM tree in the future as well.
 *
 */

(function($){

    $.setCssRule = function(selector, property, value){

        for(var sourceIndex = 0; sourceIndex < document.styleSheets.length; sourceIndex++){
            var stylesheet = document.styleSheets[sourceIndex];
            var rules = ( stylesheet.cssRules || stylesheet.rules );
            var lSelector = selector.toLowerCase();

            for( var ruleIndex = 0; ruleIndex < rules.length; ruleIndex++ ){

                if( rules[ruleIndex].selectorText && ( rules[ruleIndex].selectorText.toLowerCase() == lSelector )){

                    if( value != null ){
                        rules[ruleIndex].style[property] = value;
                        return;
                    }else{
                        if( stylesheet.deleteRule ){
                            stylesheet.deleteRule(ruleIndex);
                        }else if( stylesheet.removeRule ){
                            stylesheet.removeRule(ruleIndex);
                        }else{
                            rules[ruleIndex].style.cssText = '';
                        }
                    }
                }

            }
        }

        var stylesheet = document.styleSheets[0] || {};

        if( stylesheet.insertRule ){
            var rules = ( stylesheet.cssRules || stylesheet.rules );
            stylesheet.insertRule(selector + '{ ' + property + ':' + value +  '; }', rules.length);
        }else if( stylesheet.addRule ){
            stylesheet.addRule(selector, property + ':' + value + ';', 0);
        }
    };

})(jQuery);