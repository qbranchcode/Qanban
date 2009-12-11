<%-- Loaded through _qanbanFunctions.gsp --%>

var originUrl = '${createLink(controller:'mainView',action:'showArchiveBody',params:['sort':'lastUpdated'])}';
var originOrder = 'asc';

var maxElements;
var pollingInterval;

function isScrollAtBottom(){

   var $scrollContent = $('.scrollContent');
   var $children = $('.scrollContent').children();

   if ($scrollContent[0].scrollHeight - $scrollContent.scrollTop() == $scrollContent.outerHeight()) {
      return true;
   }
   return false;
}

function isThereMoreCards(){
    if($('.scrollContent').children().size() < maxElements) {return true;}
    return false;
}

function getOffset() {
  return parseInt($('.archive:last-child').attr('id').split('_')[1])+1;
}

function fetchMoreArchiveCards() {

  if(isScrollAtBottom() && isThereMoreCards()) {
    var loadFetcher = function(n) {
      $('tbody').qLoad({
         append: true,
         url: originUrl,
         data: {order: originOrder , offset : getOffset()},
         tries: n,
         caller: loadFetcher
        });
    };
    loadFetcher();
  }
}


function startTablePolling(){
  pollingInterval = window.setInterval("fetchMoreArchiveCards()", 2000);
}

function enableShowCardClick() {
  $('.showCardLink').click(function(event){
    showCard( $(this).attr('id').split('_')[1] , false );
  });
}

var enableArchiveView = function (){

  enableShowCardClick();
  
  startTablePolling();

  $('.ajaxSortableColumn').click(function(event){

    var url = $(this).attr('href');
    var $currentColumn = $(this);
    var classList = $(this).attr('class').split(' ');
    var orderClass;
    var order;

     $.each(classList, function(index, item){
        var classSubstings = item.split('_');
        if( classSubstings[0].replace(/^\s*|\s*$/g,'') == 'order' ){
            orderClass = item;
            order = classSubstings[1];
        }
    });

    var loadSortableColumn = function(n){

      $('tbody').qLoad({
        url: url,
        tries: n,
        caller: loadSortableColumn,
        data: {order : order},
        successCallback: function() {
                      originUrl = url;
                      originOrder = order;
                      $currentColumn.removeClass(orderClass);
                      $currentColumn.addClass( order == 'asc' ? 'order_desc' : 'order_asc' );
        },
        completeCallback: function() { enableShowCardClick(); }
      });
    };
    loadSortableColumn();


    event.preventDefault();
  });
}
