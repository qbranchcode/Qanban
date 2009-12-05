<%@ page contentType="text/html;charset=UTF-8"%>
    <div id="log">
      <table id="logTable" border="0" cellspacing="0" class="tableContainer">
        <thead class="logTableHeader fixedHeader">
          <tr>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'dateCreated'])}" class="ajaxSortableColumn order_desc"><g:message code="log.dateCreated"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'user'])}" class="ajaxSortableColumn order_desc"><g:message code="log.user"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'summary'])}" class="ajaxSortableColumn order_desc"><g:message code="log.summary"/></a></th>
            <th><a href="${createLink(controller:'mainView',action:'showLogBody',params:['sort':'domainId'])}" class="ajaxSortableColumn order_desc"><g:message code="log.item"/></a></th>
          </tr>
        </thead>
        <tbody class="scrollContent">
          <g:render template="/event/logBody" model="[ 'eventInstanceList' : eventInstanceList , 'eventInstanceTotal' : eventInstanceTotal ]"/>
        </tbody>
      </table>
    </div>

<script type="text/javascript">
  maxElements = ${eventInstanceTotal};
</script>
