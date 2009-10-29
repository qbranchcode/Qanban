import se.qbranch.qanban.Role
import se.qbranch.qanban.Requestmap

/**
 * Authority Controller.
 */
class RoleController {

	// the delete, save and update actions only accept POST requests
	static Map allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

	def authenticateService

	def index = {
		redirect action: list, params: params
	}

	/**
	 * Display the list authority page.
	 */
	def list = {
		if (!params.max) {
			params.max = 10
		}
		[authorityList: Role.list(params)]
	}

	/**
	 * Display the show authority page.
	 */
	def show = {
		def authority = Role.get(params.id)
		if (!authority) {
			flash.message = "Role not found with id $params.id"
			redirect action: list
			return
		}

		[authority: authority]
	}

	/**
	 * Delete an authority.
	 */
	def delete = {
		def authority = Role.get(params.id)
		if (!authority) {
			flash.message = "Role not found with id $params.id"
			redirect action: list
			return
		}

		authenticateService.deleteRole(authority)

		flash.message = "Role $params.id deleted."
		redirect action: list
	}

	/**
	 * Display the edit authority page.
	 */
	def edit = {
		def authority = Role.get(params.id)
		if (!authority) {
			flash.message = "Role not found with id $params.id"
			redirect action: list
			return
		}

		[authority: authority]
	}

	/**
	 * Authority update action.
	 */
	def update = {

		def authority = Role.get(params.id)
		if (!authority) {
			flash.message = "Role not found with id $params.id"
			redirect action: edit, id: params.id
			return
		}

		long version = params.version.toLong()
		if (authority.version > version) {
			authority.errors.rejectValue 'version', 'authority.optimistic.locking.failure',
				'Another user has updated this Role while you were editing.'
			render view: 'edit', model: [authority: authority]
			return
		}

		if (authenticateService.updateRole(authority, params)) {
			authenticateService.clearCachedRequestmaps()
			redirect action: show, id: authority.id
		}
		else {
			render view: 'edit', model: [authority: authority]
		}
	}

	/**
	 * Display the create new authority page.
	 */
	def create = {
		[authority: new Role()]
	}

	/**
	 * Save a new authority.
	 */
	def save = {

		def authority = new Role()
		authority.properties = params
		if (authority.save()) {
			redirect action: show, id: authority.id
		}
		else {
			render view: 'create', model: [authority: authority]
		}
	}
}
