<%@ page contentType="text/html;charset=UTF-8"%>

<li class="card"
    id="card_${it.id}">
    <a href="${createLink(controller:'card',action:'edit')}"
       class="editCardLink"
       id="cardLink_$(it.id}">
          ${it.title}
    </a>
</li>