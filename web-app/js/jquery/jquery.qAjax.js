(function($){

    $.fn.qLoad = function(options){

        var $element = $(this);

        var defaults = $.extend(qAjaxDefaults,{
            type: 'GET',
            success: function(data, textStatus){
                var fail = data.indexOf('<html>') != -1;
                if( fail ) {
                    showSessionTimeoutMesg();
                } else {
                    window.console.log('success');
                    if( defaults.successCallback ) {
                        defaults.successCallback(data, textStatus);
                    }
                    defaults.append ? $element.append(data) : $element.html(data);
                }
            },
            complete: function(){/* toggleSpinner(); */}
        });

        var settings = $.extend(defaults, options);

        if( options.completeCallback ){
            settings.complete = function() { window.console.log('complete'+ defaults.tries); options.completeCallback(); /*toggleSpinner(); */};
        }

        $.ajax(settings);

    }


    $.qPost = function(options){
        var defaults = $.extend(qAjaxDefaults, {
            type: 'POST',
            success: function(data, textStatus){
                var fail = data.indexOf('<html>') != -1;
                if( fail ) {
                    showSessionTimeoutMesg();
                } else {
                    window.console.log('qPost - success');
                    if( defaults.successCallback ) {
                        defaults.successCallback(data, textStatus);
                    }
                }
            }
        });

        var settings = $.extend(defaults,options);

        $.ajax(settings);

    }

    //Req options: url, successCallback,data,dataType
    $.qGet = function(options){
        var defaults = $.extend(qAjaxDefaults,{
            type: 'GET',
            success: function(data, textStatus){
                var fail = data.indexOf('<html>') != -1;
                if( fail ) {
                    showSessionTimeoutMesg();
                } else {
                    if( defaults.successCallback) {
                        defaults.successCallback(data, textStatus);
                    }
                }
            }
        });

        var settings = $.extend(defaults,options);

        $.ajax(settings);
        
    }

    var qAjaxDefaults = {
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



})(jQuery);