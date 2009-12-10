<%@ page contentType="text/html;charset=UTF-8"%>
<li class="card <qb:viewerIsAssignee assigneeId="${it.assignee?.id}">myCard</qb:viewerIsAssignee>" id="card_${it.id}">
  <div class="cardTop">
    <span class="cardCaseNumber">Case: <g:fieldValue bean="${it}" field="caseNumber" /></span>
    <span class="cardTimeOnBoard">Age: ${new Date()-it.dateCreated}</span>
    <div class="titleWrapper"><g:fieldValue bean="${it}" field="title" /></div>
  </div>
  <div class="cardDescription"><p class="cardDescriptionInside"><g:fieldValue bean="${it}" field="description" /></p></div>
<g:if test="${it.assignee}">

      <avatar:gravatar email="${it.assignee.email}" size="20" />
</g:if>
<g:else>
  <img src="<g:resource dir="images" file="noAssignee.png"/>" alt="No assignee" width="20" height="20" class="avatar"/>
</g:else>
</li>