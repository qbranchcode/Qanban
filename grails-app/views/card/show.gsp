
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

              <g:if test="${cardInstance.cardDone}">
                <div class="cardIsDone">
                  ${fieldValue(bean:cardInstance, field:'cardDone')}
                </div>
              </g:if>

              <div class="head">
                <span class="caseNumber">#${fieldValue(bean:cardInstance, field:'caseNumber')}</span>
                <span class="dateCreated">${fieldValue(bean:cardInstance, field:'cardCreated')}</span>
              </div>
              <div class="content">
                <div class="prio">
                  <span class="name">
                    Prio:
                  </span>
                  <span class="value">
                    ${fieldValue(bean:cardInstance, field:'prio')}
                  </span>
                </div>
                <div class="description">
                  <p>
                    ${fieldValue(bean:cardInstance, field:'description')}
                  </p>
                </div>
                <div class="asignee">
                  <span class="name">
                    Asigned to:
                  </span>
                  <span class="value">
                    ${fieldValue(bean:cardInstance, field:'asignee')}
                  </span>
                </div>

              </div>
            </div>
        </div>
    </body>
</html>
