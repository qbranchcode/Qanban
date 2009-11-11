
<%@ page import="se.qbranch.qanban.CardEventMove" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create CardEventMove</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">CardEventMove List</g:link></span>
        </div>
        <div class="body">
            <h1>Create CardEventMove</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${cardEventMoveInstance}">
            <div class="errors">
                <g:renderErrors bean="${cardEventMoveInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="positionMovement">Position Movement:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardEventMoveInstance,field:'positionMovement','errors')}">
                                    <input type="text" id="positionMovement" name="positionMovement" value="${fieldValue(bean:cardEventMoveInstance,field:'positionMovement')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="phaseMovement">Phase Movement:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardEventMoveInstance,field:'phaseMovement','errors')}">
                                    <input type="text" id="phaseMovement" name="phaseMovement" value="${fieldValue(bean:cardEventMoveInstance,field:'phaseMovement')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="card">Card:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardEventMoveInstance,field:'card','errors')}">
                                    <g:select optionKey="id" from="${se.qbranch.qanban.Card.list()}" name="card.id" value="${cardEventMoveInstance?.card?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="comment">Comment:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardEventMoveInstance,field:'comment','errors')}">
                                    <input type="text" id="comment" name="comment" value="${fieldValue(bean:cardEventMoveInstance,field:'comment')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="newAssignee">New Assignee:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardEventMoveInstance,field:'newAssignee','errors')}">
                                    <g:select optionKey="id" from="${se.qbranch.qanban.User.list()}" name="newAssignee.id" value="${cardEventMoveInstance?.newAssignee?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="triggeredByUser">Triggered By User:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardEventMoveInstance,field:'triggeredByUser','errors')}">
                                    <g:select optionKey="id" from="${se.qbranch.qanban.User.list()}" name="triggeredByUser.id" value="${cardEventMoveInstance?.triggeredByUser?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="afterInsert">After Insert:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardEventMoveInstance,field:'afterInsert','errors')}">
                                    
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
