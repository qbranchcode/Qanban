
<%@ page import="se.qbranch.qanban.CardEventMove" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show CardEventMove</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">CardEventMove List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New CardEventMove</g:link></span>
        </div>
        <div class="body">
            <h1>Show CardEventMove</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:cardEventMoveInstance, field:'id')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Position Movement:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:cardEventMoveInstance, field:'positionMovement')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Phase Movement:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:cardEventMoveInstance, field:'phaseMovement')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Card:</td>
                            
                            <td valign="top" class="value"><g:link controller="card" action="show" id="${cardEventMoveInstance?.card?.id}">${cardEventMoveInstance?.card?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Comment:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:cardEventMoveInstance, field:'comment')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">New Assignee:</td>
                            
                            <td valign="top" class="value"><g:link controller="user" action="show" id="${cardEventMoveInstance?.newAssignee?.id}">${cardEventMoveInstance?.newAssignee?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Triggered By User:</td>
                            
                            <td valign="top" class="value"><g:link controller="user" action="show" id="${cardEventMoveInstance?.triggeredByUser?.id}">${cardEventMoveInstance?.triggeredByUser?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">After Insert:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:cardEventMoveInstance, field:'afterInsert')}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${cardEventMoveInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
