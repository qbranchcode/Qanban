<%@ page contentType="text/html;charset=UTF-8" %>

<g:setProvider library="jquery"/>


  <g:if test="${flash.message}">
    <div>${flash.message}</div>
  </g:if>

  <g:hasErrors bean="${cardInstance}">
    <div>
      <g:renderErrors bean="${cardInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:formRemote url="[controller:'card',action:'ajaxSave']" update="cardFormWrapper" name="cardForm"
           onSuccess="updateBoard()" >
    <ul>

      <li class="prop">
        <label for="caseNumber">Case Number:</label>
        <input type="text" id="caseNumber" name="caseNumber"
               class="${hasErrors(bean:cardInstance,field:'caseNumber','errors')}"
               value="${fieldValue(bean:cardInstance,field:'caseNumber')}" />
      </li>
      <li class="prop">
        <label for="title">Title:</label>
        <input type="text" id="title" name="title"
               class="${hasErrors(bean:cardInstance,field:'title','errors')}"
               value="${fieldValue(bean:cardInstance,field:'title')}"/>
      </li>
      <li class="prop">
        <label for="description">Description:</label>
        <input type="text" id="description" name="description"
               class="${hasErrors(bean:cardInstance,field:'description','errors')}"
               value="${fieldValue(bean:cardInstance,field:'description')}"/>
        <input type="hidden" name="phase.id" value="1" />
      </li>
      <li class="buttons">
        <input class="save" type="submit" value="Create" />
      </li>

    </ul>
  </g:formRemote>
