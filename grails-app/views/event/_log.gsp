<g:javascript library="jquery"/>
<g:javascript src="jquery/jquery.ui.core.js"/>
<g:javascript src="jquery/jquery.tablesorter.min.js"/>
<g:javascript src="jquery/jquery.tablesorter.pager.js"/>

<jq:jquery>
  $(document).ready(function()
    {
      $("#logTable").tablesorter({
      sortList:[[0,0],[2,1]], widgets: ['zebra']
      });
    }
  );

</jq:jquery>

<%@ page contentType="text/html;charset=UTF-8"%>
<meta name="layout" content="inside"/>
<div id="wrapper">
  <div id="boardWrapper">
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
  </div>
</div>