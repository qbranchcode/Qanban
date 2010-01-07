(function($) {

    /* qInject method */
    $.fn.qInject = function(options){

        var $objects = $(this);

        return $objects.each(function(index,obj){

            var $obj = $(obj);

            if( $obj.parent().parent().length != 0 )
                throw "Can't inject a element thats already in the DOM tree"

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

    }


    /* Internal help functions */

    function injectCard($card,options){
        var defaults = { destination: $('.phase:first') };
        var settings = $.extend(defaults, options);

        doesExist($card) ? replaceCard($card) : insertCard($card, settings.destination);
    }

    function insertCard($card, $destination){
        debug('Insert card ' +  $card.attr('id'));
        $destination.append($card);
    }

    function replaceCard($card){
        debug('Replace card ' +  $card.attr('id'));
        replaceObject($card);
    }

    function injectPhase($phase,options){
        var defaults = { destination: $('#phaseList') };
        var settings = $.extend(defaults, options);

        if( !settings.index )
            throw "Injection of a phase requires the option 'index' to specify where to inject the element";

        doesExist($phase) ? replacePhase($phase, settings.index) : insertPhase($phase, settings);

    }

    function insertPhase($phase, settings){
        debug('Insert phase ' +  $phase.attr('id'));
        var $phases = settings.destination.find('.phaseWrapper');
        if( $phases.size() > settings.index ){
            $phase.insertBefore($phases.get(settings.index));
        }else{
            //TODO: Move the archive icon
            settings.destination.append($phase);
        }
    }

    function replacePhase($phase, index){
        debug('Replace phase ' +  $phase.attr('id'));
        replaceObject($phase);
        var oldIndex = 1 + parseInt($phase.prevAll().size());
        var $elementAtDestination = $('.phaseWrapper:nth-child(' + index + ')');

        if( $phase.attr('id') != $elementAtDestination.attr('id') ){
            if( oldIndex > index ){
                $phase.insertBefore($elementAtDestination)
                // TODO: Check if the $phase has the archive icon
            }else{
                $phase.insertAfter($elementAtDestination)
                // TODO: Check if the $elAtDe.. has the archive icon
            }
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
