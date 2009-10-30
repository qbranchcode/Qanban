<%@ page contentType="text/html;charset=UTF-8" %>

<div id="cardForm" class="dialog">

  <g:form controller="card" action="save" method="post" >
    <ul>

      <li class="prop">
      <label for="caseNumber">Case Number:</label>
      <input type="text" id="caseNumber" name="caseNumber"
             class="${hasErrors(bean:cardInstance,field:'caseNumber','errors')}"
             value="${fieldValue(bean:cardInstance,field:'caseNumber')}" />
      </li class="prop">
      <li>
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
      </li>
      <li class="buttons">
        <input class="save" type="submit" value="Create" />
      </li>

    </ul>
  </g:form>

</div>