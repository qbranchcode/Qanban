import org.springframework.security.context.SecurityContextHolder as SCH
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools
import se.qbranch.qanban.*

class QanbanTagLib {

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

    def getEventSummary = { attrs, body ->
        def event = attrs.event

        if(event instanceof CardEventCreate) {
            out << g.message(code:"eventSummary.cardEventCreate")
        }
        if(event instanceof CardEventDelete) {
            out << g.message(code:"eventSummary.cardEventDelete")
        }
        if(event instanceof CardEventMove) {
            out << g.message(code:"eventSummary.cardEventMove", args:[event.card.title, event.newPhase.title])
        }
        if(event instanceof CardEventSetAssignee) {
            out << g.message(code:"eventSummary.cardEventSetAssignee", args:[event.newAssignee.userRealName])
        }
        if(event instanceof CardEventUpdate) {
            out << g.message(code:"eventSummary.cardEventUpdate")
        }
        if(event instanceof PhaseEventCreate) {
            out << g.message(code:"eventSummary.phaseEventCreate")
        }
        if(event instanceof PhaseEventDelete) {
            out << g.message(code:"eventSummary.phaseEventDelete")
        }
        if(event instanceof PhaseEventMove) {
            out << g.message(code:"eventSummary.phaseEventMove")
        }
        if(event instanceof PhaseEventUpdate) {
            out << g.message(code:"eventSummary.phaseEventUpdate")
        }
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