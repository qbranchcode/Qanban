/*
 * Copyright 2009 Qbranch AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.security.context.SecurityContextHolder as SCH
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools
import se.qbranch.qanban.*

class QanbanTagLib {

    static namespace = 'qb'

    def renderPhases = { attrs ->
        if( !attrs.template )
          return out << "You need to specify a template"
        if( !attrs.phases )
          return out << "You need to specify a collection of phases"
        if( attrs.showArchive &&  attrs.showArchive != "true" && attrs.showArchive != "false"  )
          return out << "The attribute 'showArchive' can only be true/false"


        def output = ""
      
        attrs.phases[0..-2].each{
            output += render(template:attrs.template,model:[phase:it])
        }

        if( attrs.showArchive == "true" ) {
            output += render(template:attrs.template,model:[phase:attrs.phases[-1]])
        }
      
        out << output
      
    }

    def getArchiveId = { attrs ->
        if( !attrs.board )
          return out << "You need to specify a board"
        out << attrs.board.phases.get(attrs.board.phases.size()-1)
    }

    def enableArchiveButton = { attrs->
        if( isSecondLastPhase(attrs.phase) ){
          def lastId = getLastPhaseId(attrs.phase.board)
          out << "<div id='archiveBtn' class='archId_$lastId'> </div>"
        }
    }

    private Integer getLastPhaseId(board){
        return board.phases[-1].id
    }

    private boolean isSecondLastPhase(phase){
        phase.board.phases.indexOf(phase) == phase.board.phases.size()-2
    }
    
    def maxCardCount = { attrs ->
        def maxNumberOfCards = 0

        attrs.phases[0..-2]?.each{
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
        def items = event.items
        out << g.message(code:"event.$type", args:items)
        
    }

    def getEventSummary = { attrs, body ->
        def event = attrs.event
        def type = event.class.simpleName
        def items = event.items
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