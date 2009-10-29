
<%@ page import="se.qbranch.qanban.Card" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Card</title>
    </head>
    <body>
        <div class="body">
          
            <h1>Show Card</h1>

            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>


            <div class="note">

              <g:if test="${cardInstance.lastUpdated}">
                <div class="cardIsDone">
                  ${fieldValue(bean:cardInstance, field:'lastUpdated')}
                </div>
              </g:if>

              <div class="head">
                <span class="caseNumber">#${fieldValue(bean:cardInstance, field:'caseNumber')}</span>
                <span class="dateCreated">${fieldValue(bean:cardInstance, field:'dateCreated')}</span>
              </div>
              <div class="content">
                <div class="description">
                  <p>
                    ${fieldValue(bean:cardInstance, field:'description')}
                  </p>
                </div>
                <div class="assignee">
                  <span class="name">
                    Assigned to:
                  </span>
                  <span class="value">
                    ${fieldValue(bean:cardInstance, field:'assignee')}
                  </span>
                </div>
              </div>
            </div>
        </div>
    </body>
</html>
