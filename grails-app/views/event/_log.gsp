<%@ page contentType="text/html;charset=UTF-8"%>
<link rel="stylesheet" href="${resource(dir:'css',file:'log.css')}" />
    <div id="log">
      <table id="logTable" border="0" cellspacing="0" class="tableContainer">
        <thead class="logTableHeader fixedHeader">
          <tr>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'dateCreated','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="log.dateCreated"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'user','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="log.user"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'summary','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="log.summary"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'class','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="log.type"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'domainId','board.id':board.id])}" class="ajaxSortableColumn order_desc"><g:message code="log.item"/></a></th>
          </tr>
        </thead>
        <tbody class="scrollContent">
          <g:render template="/event/logBody" model="[ 'eventInstanceList' : eventInstanceList ]"/>
        </tbody>
      </table>
    </div>

<script type="text/javascript">
  maxElements = ${eventInstanceTotal};
</script>
