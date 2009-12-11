
  <g:each in="${archiveCardList}" status="i" var="archiveCard">
    <tr class="${(i % 2) == 0 ? 'odd' : 'even'} archive" id="index_<g:if test="${offset}">${i + offset}</g:if><g:else>${i}</g:else>">

          <td><a class="showCardLink" href="#"> </a></td>

          <td>${fieldValue(bean:archiveCard, field:'title')}</td>

          <td>${fieldValue(bean:archiveCard, field:'description')}</td>

          <td>${fieldValue(bean:archiveCard, field:'caseNumber')}</td>

          <td>${fieldValue(bean:archiveCard, field:'assignee')}</td>
                                                                    
      </tr>
  </g:each>
