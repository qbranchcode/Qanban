<%@ page contentType="text/html;charset=UTF-8"%>
<link rel="stylesheet" href="${resource(dir:'css',file:'archive.css')}" />
    <div id="archive">
      <table id="archiveTable" border="0" cellspacing="0" class="tableContainer">
        <thead class="archiveTableHeader fixedHeader">
          <tr>
            <th><a href="#"><div class="showCardHeader"></div></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showArchive',params:['sort':'title'])}" class="ajaxSortableColumn order_desc"><g:message code="archive.title"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showArchive',params:['sort':'description'])}" class="ajaxSortableColumn order_desc"><g:message code="archive.description"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showArchive',params:['sort':'caseNumber'])}" class="ajaxSortableColumn order_desc"><g:message code="archive.caseNumber"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showArchive',params:['sort':'assignee'])}" class="ajaxSortableColumn order_desc"><g:message code="archive.assignee"/></a></th>
          </tr>
        </thead>
        <tbody class="scrollContent">
          <g:render template="/archive/archiveBody" model="[ 'archiveCardList' : archiveCardList , 'archiveCardTotal' : archiveCardTotal ]"/>
        </tbody>
      </table>
    </div>

<script type="text/javascript">
  maxElements = ${archiveCardTotal};
</script>
