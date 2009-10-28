
<%@ page import="se.qbranch.qanban.Card" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Card</title>         
    </head>
    <body>
        <div class="body">

            <h1>Create Card</h1>

            <g:if test="${flash.message}">
              <div class="message">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${cardInstance}">
              <div class="errors">
                <g:renderErrors bean="${cardInstance}" as="list" />
              </div>
            </g:hasErrors>


            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="caseNumber">Case Number:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardInstance,field:'caseNumber','errors')}">
                                    <input type="text" id="caseNumber" name="caseNumber" value="${fieldValue(bean:cardInstance,field:'caseNumber')}" />
                                </td>
                            </tr> 
                                                 
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description">Description:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:cardInstance,field:'description','errors')}">
                                    <input type="text" id="description" name="description" value="${fieldValue(bean:cardInstance,field:'description')}"/>
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
