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

<%--
	SET ASSIGNEE
--%>

<g:if test="${newPhase}">
  <div id="moveMessage">
       <g:message code="_cardForm.move.message"/>
  </div>
  <g:formRemote url="[controller:'mainView',action:'moveCardAndSetAssignee']"
                update="moveCardDialog" name="cardForm"
                onSuccess="cardFormRefresh(data,'#moveCardDialog')">

    <div class="header">


	<div class="dropdownContainer assignee">

    			<avatar:gravatar email="${loggedInUser.email}" size="38"/>
			<span id="currentAssigneeName">${loggedInUser.userRealName}</span>
		<div style="clear: both;"></div>
		<ul id="assignees">
			<li id="user_">
				<img src="<g:resource dir="images" file="noAssignee.png"/>" alt="No assignee" width="30" height="30" class="avatar"/>
				<span class="name"><g:message code="_cardForm.assignee.noAssignee"/></span>
			</li>
			<g:each var="user" in="${userList}">
				<li id="user_${user.id}"
				    class='<g:if test="${loggedInUser.id == user.id}">selected</g:if>' >

					<avatar:gravatar email="${user.email}" alt="${user.userRealName}" size="30"/>
					<span class="name">${user.userRealName}</span>
				</li>
			</g:each>
		</ul>
		<div style="clear: both;"></div>
	</div>


      <div class="info">

        <input type="text" id="card.title" readonly="readonly"
               class="property ${hasErrors(bean:cardInstance,field:'title','errors')}"
               value="${fieldValue(bean:cardInstance,field:'title')}" />

        <span class="date">Last updated: <g:formatDate format="yyyy-MM-dd HH:mm"
                                                       date="${cardInstance.lastUpdated}"/></span>

        <div class="caseNumberWrapper">
          <label for="caseNumber"><g:message code="_cardForm.label.caseNumber"/></label>
          <input type="text" id="card.caseNumber" readonly="readonly"
                 class="property ${hasErrors(bean:cardInstance,field:'caseNumber','errors')}"
                 value="${fieldValue(bean:cardInstance,field:'caseNumber')}" />
        </div>

      </div>
    </div>
    <div class="content">

      <label for="description" class="descLabel"><g:message code="_cardForm.label.description"/></label>
      <textarea id="card.description" readonly="readonly" class="property ${hasErrors(bean:cardInstance,field:'description','errors')}">
      ${fieldValue(bean:cardInstance,field:'description')}</textarea>


      <g:if test="${events}">
        <select size="4" multiple>
          <g:each in="${events}" var='event'>
            <option>
              ${event}
            </option>
          </g:each>
        </select>
      </g:if>

    </div>

    <input type="hidden" name="id" value="${cardInstance?.id}"/>
    <input type="hidden" name="assigneeId" id="assigneeValue" value="${loggedInUser.id}"/>
    <input type="hidden" name="newPhase" value="${newPhase}"/>
    <input type="hidden" name="newPos" value="${newPos}"/>
    <input type="hidden" name="user" value="${user}"/>
    <input style="display: none;" type="submit"/>
  </g:formRemote>
</g:if>

<%--
	EDIT / SHOW
--%>

<g:elseif test="${cardInstance?.id}">
  <g:formRemote url="[controller:'card',action:'update', params: [format: 'html']]"
                update="editCardDialog" name="cardForm"
                onSuccess="cardFormRefresh(data,'#editCardDialog','Success', 'Card successfully updated');setEditMode();"
                onComplete="toggleSpinner()">

    <div class="header">

	<div class="dropdownContainer assignee">

		<g:if test="${cardInstance.assignee}">
			<avatar:gravatar email="${cardInstance.assignee.email}" size="38"/>
			<span id="currentAssigneeName">${cardInstance.assignee.userRealName}</span>
		</g:if>
		<g:else>
			<img class="avatar" src="<g:resource dir="images" file="noAssignee.png"/>" alt="<g:message code='_cardForm.assignee.trigger'/>"
				width="38" height="38"/>
			<span id="currentAssigneeName"><g:message code='_cardForm.assignee.noAssignee'/></span>
		</g:else>

		<div style="clear: both;"></div>
		<ul id="assignees">
			<li id="user_" <g:if test="${!cardInstance.assignee?.id}">class="selected"</g:if> >
				<img class="avatar" src="<g:resource dir="images" file="noAssignee.png"/>" alt="No assignee" width="30" height="30"/>
				<span class="name"><g:message code="_cardForm.assignee.noAssignee"/></span>
			</li>
			<g:each var="user" in="${userList}">
				<li id="user_${user.id}"
				    class='<g:if test="${cardInstance.assignee?.id == user.id}">selected</g:if>' >

					<avatar:gravatar email="${user.email}" alt="${user.userRealName}" size="30"/>
					<span class="name">${user.userRealName}</span>
				</li>
			</g:each>
		</ul>
		<div style="clear: both;"></div>
	</div>


      <div class="info">

        <input readonly="readonly" type="text" id="card.title" name="title"
               class="property ${hasErrors(bean:cardInstance,field:'title','errors')}"
               value="${fieldValue(bean:cardInstance,field:'title')}" />

        <span class="date">Last updated: <g:formatDate format="yyyy-MM-dd HH:mm"
                                                       date="${cardInstance.lastUpdated}"/></span>

        <div class="caseNumberWrapper">
          <label for="caseNumber"><g:message code="_cardForm.label.caseNumber"/></label>
          <input readonly="readonly" type="text" id="card.caseNumber" name="caseNumber"
                 class="property ${hasErrors(bean:cardInstance,field:'caseNumber','errors')}"
                 value="${fieldValue(bean:cardInstance,field:'caseNumber')}" />
        </div>

      </div>
    </div>
    <div class="content">

      <label for="description" class="descLabel"><g:message code="_cardForm.label.description"/></label>
      <textarea readonly="readonly" id="card.description" class="property ${hasErrors(bean:cardInstance,field:'description','errors')}"
                name="description">${fieldValue(bean:cardInstance,field:'description')}</textarea>



      <g:if test="${events}">

        <label for="events" class="descLabel"><g:message code="_cardForm.label.events" /></label>
        <select name="events" size="4" multiple>
          <g:each in="${events}" var='event'>
            <option>
              ${event}
            </option>
          </g:each>
        </select>
      </g:if>

    </div>

    <input type="hidden" name="id" value="${cardInstance?.id}"/>
    <input type="hidden" name="phase.id" value="${boardInstance.phases[0].id}" />
    <input type="hidden" name="assigneeId" id="assigneeValue" value="${cardInstance.assignee?.id}"/>
    <input style="display: none;" type="submit"/>
  </g:formRemote>

</g:elseif>





<%--
	CREATE
--%>
<g:else>

  <g:hasErrors bean="${createEvent}">
  <div>
    <g:renderErrors bean="${createEvent}" as="list" />
  </div>
</g:hasErrors>

  <g:formRemote url="${[controller:'card',action:'create', params: [format: 'html']]}"
                update="createCardDialog" name="cardForm"
                onSuccess="cardFormRefresh(data,'#createCardDialog')"
                onComplete="toggleSpinner()"
                before="if(\$('[name=title]').val() == 'Title...          ') \$('[name=title]').val('');">

    <div class="header">
	<div class="dropdownContainer assignee">

		<g:if test="${cardInstance?.assignee}">
			<avatar:gravatar email="${cardInstance?.assignee?.email}" size="38"/>
			<span id="currentAssigneeName">${cardInstance?.assignee?.userRealName}</span>
		</g:if>
		<g:else>
			<img class="avatar" src="<g:resource dir="images" file="noAssignee.png"/>" alt="<g:message code='_cardForm.assignee.trigger'/>"
				width="38" height="38"/>
			<span id="currentAssigneeName"><g:message code='_cardForm.assignee.noAssignee'/></span>
		</g:else>

		<div style="clear: both;"></div>
		<ul id="assignees">
			<li id="user_" <g:if test="${cardInstance?.assignee?.id}">class="selected"</g:if> >
				<img class="avatar" src="<g:resource dir="images" file="noAssignee.png"/>" alt="No assignee" width="30" height="30"/>
				<span class="name"><g:message code="_cardForm.assignee.noAssignee"/></span>
			</li>
			<g:each var="user" in="${userList}">
				<li id="user_${user?.id}"
				    class='<g:if test="${cardInstance?.assignee?.id == user.id}">selected</g:if>' >

					<avatar:gravatar email="${user?.email}" alt="${user?.userRealName}" size="30"/>
					<span class="name">${user?.userRealName}</span>
				</li>
			</g:each>
		</ul>
		<div style="clear: both;"></div>
	</div>
      <div class="info">
        <input type="text" id="card.title" name="title"
               class="property ${hasErrors(bean:createEvent,field:'title','errors')}" value="${createEvent?.title}"
              <%-- value="Title...          " onfocus="if (this.value == 'Title...          ') this.value = '';"
               onblur="if (this.value == '') this.value = 'Title...          '"/> --%>

        <span class="date"></span>

       <div class="caseNumberWrapper">
         <label for="caseNumber"><g:message code="_cardForm.label.caseNumber"/></label>
          <input type="text" id="card.caseNumber" name="caseNumber" value="${createEvent?.caseNumber}"
                 class="property ${hasErrors(bean:createEvent,field:'caseNumber','errors')}"/>
        </div>

      </div>
    </div>
    <div class="content">
      <label for="description" class="descLabel"><g:message code="_cardForm.label.description"/></label>
      <textarea id="card.description" class="property ${hasErrors(bean:createEvent,field:'description','errors')}"
                name="description"></textarea>
    </div>

    <%-- Note: This is the id of the card, not the event --%>
    <input type="hidden" name="id" value="${createEvent?.card?.id}"/>

    <input type="hidden" name="boardId" value="${boardInstance.id}" />
    <input style="display: none;" type="submit"/>

  </g:formRemote>

</g:else>


