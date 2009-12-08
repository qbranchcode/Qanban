import org.springframework.security.context.SecurityContextHolder as SCH
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools
import se.qbranch.qanban.*

class QanbanTagLib {

    static namespace = 'qb'

    def maxCardCount = { attrs ->
        def maxNumberOfCards = 0
        attrs.phases?.each{
            it.cards.size() > maxNumberOfCards ? maxNumberOfCards = it.cards.size() : maxNumberOfCards
        }

        if( attrs.cardHeight ){
            if( attrs.unit ){
                out << maxNumberOfCards * ( attrs.cardHeight as Integer ) + attrs.unit
            }else{
                out << maxNumberOfCards * ( attrs.cardHeight as Integer )
            }
        }else{
            out << maxNumberOfCards
        }

    }

    def autoCrop = { attrs, body ->
        int maxChars = attrs.maxChars ? attrs.maxChars as Integer : 100

        if(body.it.description.length() > maxChars) {

            def text = body.it.description.substring(0, maxChars)
            cropText(text)
        }
        else {
            out << body.it.description
        }
    }

    def viewerIsAssignee = { attrs, body ->
        if (isAuthenticated()) {
                def source = determineSource()
                if( source.id == attrs.assigneeId ){
                   out << body()
                }
        }
    }

    def domainStatus = { attrs, body ->
        def event = attrs.event
        if( event.doesDomainExist() ) out << "alive"
        else out << "dead"
    }

    def getCurrentTitle = { attrs, body ->
        def event = attrs.event
        out << event.checkCurrentTitle()
    }
    
    def getDialogLog = { attrs, body ->
        def event = attrs.event
        def type = event.class.simpleName
        def items = event.dialogItems
        out << g.message(code:"event.$type", args:items)
        
    }

    def getEventSummary = { attrs, body ->
        def event = attrs.event
        def type = event.class.simpleName
        def items = event.summaryItems
        out << g.message(code:"eventSummary.$type", args:items)
    }

    private void cropText(text) {
        text = text + "..."
        out << text
    }


    private def determineSource() {
        def principal = SCH.context.authentication.principal
        def source

        // check to see if it's a GrailsUser/GrailsUserImpl/subclass,
        // or otherwise has a 'domainClass' property
        if (principal.metaClass.respondsTo(principal, 'getDomainClass')) {
                source = principal.domainClass
        }
        if (!source) {
                source = principal
        }

        return source
    }

    private boolean isAuthenticated() {
        def authPrincipal = SCH?.context?.authentication?.principal
        return authPrincipal != null && authPrincipal != 'anonymousUser'
    }
}