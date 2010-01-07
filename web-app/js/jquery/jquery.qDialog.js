(function($){

    $.fn.qDialog = function(options) {
      var defaults = {
          width: 300,
          autoOpen: false,
          modal: true,
          close: function(){ $(this).empty(); }
      }

      if( options.close ){
        var closeFunction = options.close;
        options.close = function(){ closeFunction(); $(this).empty(); };
      }

      var settings = $.extend(defaults, options);

      $(this).dialog(settings);

    }

})(jQuery);