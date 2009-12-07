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

</script>