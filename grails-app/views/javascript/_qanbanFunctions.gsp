<script type="text/javascript">

   <%--
    The reason for splitting the qanban scripts in to ...Functions and ..OnLoad it that the
    scripts found in this file needs to be outside the jQuery scope that is used while executeing
    the script in $.fn.qLoad = function(options) {...};
   --%>

   <g:render template="/javascript/log"/>
   <g:render template="/javascript/board"/>


   function toggleSpinner() {
      if( $('#spinner').size() == 0 ) {
        $('<div id="spinner"><img src="<g:resource dir='images' file='spinner.gif'/>"/></div>').appendTo($('body'));
      } else {
        $('#spinner').remove();
      }
   }

   function removeSpinner() {
      $('#spinner').remove();
   }

   function showServerDownMesg(n) {
      if( $('#offline').size() == 0 ){
        $('<div id="timeout"><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.serverOffline"/></p></div>').dialog({
                    modal: true });
      }
   }

   function showSessionTimeoutMesg() {
      if( $('#timeout').size() == 0 ){
        $('<div id="timeout"><p><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span><g:message code="mainView.jQuery.dialog.sessionTimeout"/></p></div>').dialog({
              modal: true,
              buttons: {
                <g:message code="ok"/>: function() {
                  $(this).dialog('close');
                  window.location = "${createLink(controller:'mainView')}";
                }
              }
          });
        
      }
   }

</script>