<%@ page contentType="text/html;charset=UTF-8"%>
<li class="card <g:viewerIsAssignee assigneeId="${it.assignee?.id}">myCard</g:viewerIsAssignee>" id="card_${it.id}">
  <div class="cardTop">
    <span class="cardCaseNumber">Case: ${it.caseNumber}</span>
    <span class="cardTimeOnBoard">Age: ${new Date()-it.dateCreated}</span>
    <div class="titleWrapper">${it.title}</div>
  </div>
  <div class="cardDescription"><p class="cardDescriptionInside">${it.description}</p></div>
<g:if test="${it.assignee}">

      <avatar:gravatar email="${it.assignee.email}" size="20" />
</g:if>
<g:else>
  <img src="<g:resource dir="images" file="noAssignee.png"/>" alt="No assignee" width="20" height="20" class="avatar"/>
</g:else>
</li>