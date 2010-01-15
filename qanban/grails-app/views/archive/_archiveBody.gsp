
  <g:each in="${archiveCardList}" status="i" var="archiveCard">
    <tr class="${(i % 2) == 0 ? 'odd' : 'even'} archive" id="index_<g:if test="${offset}">${i + offset}</g:if><g:else>${i}</g:else>">

          <td><a class="showCardLink" id="card_${archiveCard.id}" href="#"> </a></td>

          <td><g:formatDate format="yyyy-MM-dd HH:mm" date="${archiveCard.lastUpdated}"/></td>

          <td>${fieldValue(bean:archiveCard, field:'caseNumber')}</td>

          <td>${fieldValue(bean:archiveCard, field:'title')}</td>

          <td>${fieldValue(bean:archiveCard, field:'description')}</td>

      </tr>
  </g:each>
