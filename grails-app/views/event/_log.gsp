

<%@ page contentType="text/html;charset=UTF-8"%>

    <div id="log">
      <table id="logTable" cellspacing="5">
        <thead>
          <tr>
            <th><g:message code="log.dateCreated"/></th>
            <th><g:message code="log.user"/></th>
            <th><g:message code="log.summary"/></th>
            <th><g:message code="log.item"/></th>
          </tr>
        </thead>
        <tbody>
          <g:each in="${eventInstanceList}" status="i" var="eventInstance">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                  <td>${fieldValue(bean:eventInstance, field:'dateCreated')}</td>

                  <td>${fieldValue(bean:eventInstance, field:'user')}</td>

                  <td><g:getEventSummary event="${eventInstance}"/></td>

                  <td>${eventInstance.title}</td>
              </tr>
          </g:each>
        </tbody>
      </table>
    </div>
