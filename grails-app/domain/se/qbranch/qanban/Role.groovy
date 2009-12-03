package se.qbranch.qanban

import se.qbranch.qanban.User

/**
 * Authority domain class.
 */
class Role {

    	static constraints = {
		authority(blank: false, unique: true)
		description()
	}
        
	static hasMany = [people: User]

	String description
	/** ROLE String */
	String authority


}
