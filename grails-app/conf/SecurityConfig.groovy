security {

	// see DefaultSecurityConfig.groovy for all settable/overridable properties

	active = true

	loginUserDomainClass = "se.qbranch.qanban.User"
	authorityDomainClass = "se.qbranch.qanban.Role"
	requestMapClass = "se.qbranch.qanban.Requestmap"

        useHttpSessionEventPublisher=true
}
