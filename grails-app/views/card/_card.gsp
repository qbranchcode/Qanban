<%@ page contentType="text/html;charset=UTF-8"%>

<li class="card" id="card_${it.id}">
  <a href="${createLink(controller:'card',action:'edit')}" class="editCardLink" id="cardLink_${it.id}">${it.title}</a>
  <div class="cardCaseNumber">Case: ${it.caseNumber}</div><div class="cardTimeOnBoard">Age: ${new Date()-it.dateCreated}</div>
  <div class="cardDescription"><p class="cardDescriptionInside">${it.description}</p></div>
</li>