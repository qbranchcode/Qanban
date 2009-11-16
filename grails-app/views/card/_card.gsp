<%@ page contentType="text/html;charset=UTF-8"%>

<li class="card"
    id="card_${cardInstance.id}">
    <a href="${createLink(controller:'card',action:'edit')}"
       class="editCardLink"
       id="cardLink_$(cardInstance.id}">
          ${cardInstance.title}
    </a
</li>