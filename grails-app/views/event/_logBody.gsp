
  <g:each in="${eventInstanceList}" status="i" var="eventInstance">
    <tr class="${(i % 2) == 0 ? 'odd' : 'even'} event" id="index_<g:if test="${offset}">${i + offset}</g:if><g:else>${i}</g:else>">

          <td><g:formatDate format="yyyy-MM-dd hh:mm:ss" date="${eventInstance.dateCreated}"/></td>

          <td>${fieldValue(bean:eventInstance, field:'user')}</td>

          <td><g:getEventSummary event="${eventInstance}"/></td>

          <td>${fieldValue(bean:eventInstance, field:'title')}</td>
      </tr>
  </g:each>
