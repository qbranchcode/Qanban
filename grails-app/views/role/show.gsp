<head>
	<meta name="layout" content="main" />
	<title>Show Role</title>
</head>

<body>

	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
		<span class="menuButton"><g:link class="list" action="list">Role List</g:link></span>
		<span class="menuButton"><g:link class="create" action="create">New Role</g:link></span>
	</div>

	<div class="body">
		<h1>Show Role</h1>
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<div class="dialog">
			<table>
			<tbody>

				<tr class="prop">
					<td valign="top" class="name">ID:</td>
					<td valign="top" class="value">${authority.id}</td>
				</tr>

				<tr class="prop">
					<td valign="top" class="name">Role Name:</td>
					<td valign="top" class="value">${authority.authority}</td>
				</tr>

				<tr class="prop">
					<td valign="top" class="name">Description:</td>
					<td valign="top" class="value">${authority.description}</td>
				</tr>

				<tr class="prop">
					<td valign="top" class="name">People:</td>
					<td valign="top" class="value">${authority.people}</td>
				</tr>

			</tbody>
			</table>
		</div>

		<div class="buttons">
			<g:form>
				<input type="hidden" name="id" value="${authority?.id}" />
				<span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
				<span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
			</g:form>
		</div>

	</div>

</body>
