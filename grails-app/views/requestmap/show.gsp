<head>
	<meta name="layout" content="main" />
	<title>Show Requestmap</title>
</head>

<body>

	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
		<span class="menuButton"><g:link class="list" action="list">Requestmap List</g:link></span>
		<span class="menuButton"><g:link class="create" action="create">New Requestmap</g:link></span>
	</div>

	<div class="body">
		<h1>Show Requestmap</h1>
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<div class="dialog">
			<table>
			<tbody>

				<tr class="prop">
					<td valign="top" class="name">ID:</td>
					<td valign="top" class="value">${requestmap.id}</td>
				</tr>

				<tr class="prop">
					<td valign="top" class="name">URL:</td>
					<td valign="top" class="value">${requestmap.url}</td>
				</tr>

				<tr class="prop">
					<td valign="top" class="name">Roles:</td>
					<td valign="top" class="value">${requestmap.configAttribute}</td>
				</tr>

			</tbody>
			</table>
		</div>

		<div class="buttons">
			<g:form>
				<input type="hidden" name="id" value="${requestmap.id}" />
				<span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
				<span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
			</g:form>
		</div>

	</div>
</body>
