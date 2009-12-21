<%-- Loaded through _qanbanFunctions.gsp --%>

var originUrlArchive = '${createLink(controller:'mainView',action:'showArchiveBody',params:['sort':'lastUpdated'])}';
var originOrderArchive = 'desc';

var boardId;
var maxArchiveElements;

function isArchiveScrollAtBottom(){

   var $scrollContent = $('.scrollContent');
   var $children = $('.scrollContent').children();

   if ($scrollContent[0].scrollHeight - $scrollContent.scrollTop() == $scrollContent.outerHeight()) {
      return true;
   }
   return false;
}

function isThereMoreArchiveCards(){
    if($('.scrollContent').children().size() < maxArchiveElements) {return true;}
    return false;
}

function getArchiveOffset() {
  return parseInt($('.archive:last-child').attr('id').split('_')[1])+1;
}

function fetchMoreArchiveCards() {
  if(isArchiveScrollAtBottom() && isThereMoreArchiveCards()) {
    var loadFetcher = function(n) {
      $('tbody').qLoad({
         append: true,
         url: originUrlArchive,
         data: {order: originOrderArchive , offset : getArchiveOffset() , 'boardId' : boardId},
         tries: n,
         caller: loadFetcher
        });
    };
    loadFetcher();
  }
}


function startArchiveTablePolling(){
  pollingInterval = window.setInterval("fetchMoreArchiveCards()", 2000);
}

function enableShowArchiveCardClick() {
  $('.showCardLink').click(function(event){
    showCard( $(this).attr('id').split('_')[1] , false );
  });
}

var enableArchiveView = function (){

  enableShowArchiveCardClick();
  
  startArchiveTablePolling();

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
                      originUrlArchive = url;
                      originOrderArchive = order;
                      $currentColumn.removeClass(orderClass);
                      $currentColumn.addClass( order == 'asc' ? 'order_desc' : 'order_asc' );
        },
        completeCallback: function() { enableShowArchiveCardClick(); }
      });
    };
    loadSortableColumn();


    event.preventDefault();
  });
}
