
<%@ page import="se.qbranch.qanban.CardEventMove" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>CardEventMove List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New CardEventMove</g:link></span>
        </div>
        <div class="body">
            <h1>CardEventMove List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="positionMovement" title="Position Movement" />
                        
                   	        <g:sortableColumn property="phaseMovement" title="Phase Movement" />
                        
                   	        <th>Card</th>
                   	    
                   	        <g:sortableColumn property="comment" title="Comment" />
                        
                   	        <th>New Assignee</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${cardEventMoveInstanceList}" status="i" var="cardEventMoveInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${cardEventMoveInstance.id}">${fieldValue(bean:cardEventMoveInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:cardEventMoveInstance, field:'positionMovement')}</td>
                        
                            <td>${fieldValue(bean:cardEventMoveInstance, field:'phaseMovement')}</td>
                        
                            <td>${fieldValue(bean:cardEventMoveInstance, field:'card')}</td>
                        
                            <td>${fieldValue(bean:cardEventMoveInstance, field:'comment')}</td>
                        
                            <td>${fieldValue(bean:cardEventMoveInstance, field:'newAssignee')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${cardEventMoveInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
