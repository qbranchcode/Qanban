<script type="text/javascript">

  jQuery().ready(function(){

    <%-- Replace function --%>
    $.fn.replaceWith = function($newElement){ this.after($newElement).remove(); };

    <%-- Ajax Load Wrapper --%>
    $.fn.qLoad = function(options) {

      var $element = $(this);

      toggleSpinner();

      options.tries = options.tries ? options.tries : options.tries = 1;
      options.cache = options.cache ? options.cache : false;
      options.success = options.success ? options.success : function(data, textStatus){
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
          if(options.append) {
            $element.append(data);
          } else {
            $element.html(data);
          }
        }
      };
      options.error = options.error ? options.error : function(XMLHttpRequest, textStatus, errorThrown){
            var errorCallback = options.errorCallback;
            if(XMLHttpRequest.status == 0 ) {
                if(options.tries > 3) {
                    $('<div><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.serverOffline"/></p></div>').dialog({
                    modal: true });
                } else {
                    options.tries = options.tries + 1;
                    options.caller(options.tries);
                }
            }
            if( errorCallback ){
              errorCallback(XMLHttpRequest, textStatus, errorThrown);
            }
      };
      if( options.completeCallback ){
            options.complete = function() {options.completeCallback();toggleSpinner();};
      } else {
            options.complete = toggleSpinner();
      }
      options.type = "GET";

      $.ajax(options);

    }

    <%-- Ajax Get Wrapper --%>
    $.qGet = function(url, data, successCallback, dataType) {
      var checked = 'false';
      var sessionTO = 'false';

      var options = {};
      options.url = url;
      options.successCallback = successCallback;
      options.success = function(data, textStatus){
        var fail = data.indexOf('<html>') != -1;
        if( fail && sessionTO == 'false') {
          sessionTO = 'true';
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
            if(XMLHttpRequest.status == 0 && checked=='false') {
              $('<div id="offlineWarning"><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.serverOffline"/></p></div>').dialog({
              modal: true });
              checked = 'true';
            }
      };
      options.dataType = dataType;
      options.type = "GET";

      $.ajax(options);

    }


    <%-- Dialog wrapper --%>

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

    <%-- Specific dialogs --%>
    <g:render template="/javascript/dialogs"/>


    <%-- Main Navigation --%>

    $('.tab').click(function(event){
        var $tab = $(this);
        var $wrapper = $('#wrapper');
        var url = $(this).attr('href');


        var tabLoader = function(n){
          $wrapper.qLoad({
            url: url,
            tries: n,
            caller: tabLoader,
            successCallback: function(data,textstatus){

              $('.tab').removeClass('active');
              $tab.addClass('active');
              $wrapper.fadeIn('fast',function(){
                if( data.indexOf('<div id="log">') != -1 ) {
                  enableLogView(url);
                } else {
                  if( pollingInterval > -1 ){
                          window.clearInterval(pollingInterval);
                  }
                  $('.phase').each(function(){ enableSortableOnPhase($(this)); });
                  rescanBoardButtons();
                  reconnectPhases();
                }
              });
            }
          });
        };
        $wrapper.fadeOut('fast',function(){
          tabLoader();
        });

        event.preventDefault();

    });

    $('.phase').each(function(){ enableSortableOnPhase($(this)); });
    reconnectPhases();
    rescanBoardButtons();


    // The time out value is set to be 18,000,000 milli-seconds (or 5 minutes)
    function reloader(){
      setTimeout(function(){updateBoard();reloader();},18000000);
    }

    reloader();

  });

</script>
