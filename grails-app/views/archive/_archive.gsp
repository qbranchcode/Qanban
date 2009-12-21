<%@ page contentType="text/html;charset=UTF-8"%>
<link rel="stylesheet" href="${resource(dir:'css',file:'archive.css')}" />
    <div id="archive">
      <table id="archiveTable" border="0" cellspacing="0" class="tableContainer">
        <thead class="archiveTableHeader fixedHeader">
          <tr>
            <th><a href="#"><div class="showCardHeader"></div></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showArchiveBody',params:['sort':'lastUpdated','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="archive.archiveDate"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showArchiveBody',params:['sort':'caseNumber','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="archive.caseNumber"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showArchiveBody',params:['sort':'title','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="archive.title"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showArchiveBody',params:['sort':'description','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="archive.description"/></a></th>
          </tr>
        </thead>
        <tbody class="scrollContent">
          <g:render template="/archive/archiveBody" model="[ 'archiveCardList' : archiveCardList , 'archiveCardTotal' : archiveCardTotal ]"/>
        </tbody>
      </table>
    </div>

<script type="text/javascript">
  boardId = ${board.id};
  maxArchiveElements = ${archiveCardTotal};
</script>
