package se.qbranch.qanban
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class SecureController {

    def index = {
        render 'Secure access only'
    }
}
