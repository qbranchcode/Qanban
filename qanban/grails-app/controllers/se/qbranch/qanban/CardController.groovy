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

package se.qbranch.qanban

import grails.converters.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class CardController {

    def eventService
    def ruleService
    def securityService


    // Create

    @Secured(['ROLE_QANBANADMIN'])
    def create = {

        if ( !params.boardId )
            return render(status: 400, text: "The parameter 'boardId' must be specified")

        CardEventCreate createEvent = createCardEventCreate(params)      
        eventService.persist(createEvent)
        renderCreateResult(createEvent)
    }

    private CardEventCreate createCardEventCreate(params){
        def event = new CardEventCreate(params)
        event.eventCreator = securityService.getLoggedInUser()
        event.phaseDomainId = Board.get(params.boardId).phases[0].domainId
        event.assignee = User.get(params.assigneeId)
        return event
    }

    private renderCreateResult(createEvent){
        withFormat{
            html{
                def board = createEvent.board
                return render(template:'cardForm',model:[createEvent:createEvent, boardInstance: board])
            }
            js{
                return render ( [ cardInstance : createEvent.card ] as JSON)
            }
            xml{
                return render ( [ cardInstance : createEvent.card ] as XML)
            }
        }
    }


    // Retrive

    def show = {

        if( !params.id )
            return render(status: 400, text: "You need to specify an id")

        if( !Card.exists(params.id) )
            return render(status: 404, text: "Card with id $params.id not found")

        def card = Card.get(params.id)

        renderShowResult(card)

    }

    private renderShowResult(card){
        withFormat {
            html{
                return render (template: 'card', bean: card)
            }
            js{
                return render ( [cardInstance : card] as JSON )
            }
            xml{
                return render ( [cardInstance : card] as XML )
            }
        }
    }
    
    def form = {

        if( !params.id )
            return renderFormCreateMode(params)

        if( !Card.exists(params.id) )
            return render(status: 404, text: "Card with id $params.id not found")

        if( params.newPos && params.newPhase )
            return renderFormMoveMode(params)

        return renderFormEditMode(params)
        
    }

    private def renderFormCreateMode(params){
        if ( !params.'board.id' ) return render(status: 400, text: "The parameter 'boardId' must be specified")
        def board = Board.get(params.'board.id')
        def users = User.list();
        return render(template:'cardForm',model:[ boardInstance: board, userList: users])
    }

    private def renderFormMoveMode(params){
        def moveEvent = new CardEventMove()
        moveEvent.card =  Card.get(params.id)
        return render(template:'cardForm', model:[ newPhase: params.newPhase, newPos: params.newPos, moveEvent: moveEvent, userList: User.list(), loggedInUser: securityService.getLoggedInUser() ])
    }

    private def renderFormEditMode(params){
        def card = Card.get(params.id)
        def updateEvent = new CardEventUpdate()
        updateEvent.card = card
        
        def users = User.list()

        return render(template:'cardForm',model:[ updateEvent: updateEvent , userList: users , events : Event.findAllByDomainId(card.domainId, [sort: 'dateCreated', order:'desc']) ])
    }


    // Update
    def update = { SetAssigneeCommand sac, UpdateCardCommand ucc ->

        if( ucc.hasErrors() )
            return render(status: 400, text: "Bad update request")
        if( sac.hasErrors() )
            return render(status: 400, text: "Bad request; The assignee you tried to set is invalid")


        CardEventSetAssignee assigneeEvent = createCardEventSetAssignee(sac)
        CardEventUpdate updateEvent = createUpdateEvent(ucc)
      
        // TODO: Do it like this? eventService.persist(updateEvent,assigneeEvent)

        eventService.persist(updateEvent)
        eventService.persist(assigneeEvent)

        renderUpdateResult(updateEvent)

    }
    
    private renderUpdateResult(updateEvent){
        withFormat{
            html{
                def users = User.list();
                return render (template: 'cardForm', model:[updateEvent:updateEvent, userList: users])
            }
            js{
                return render ( [cardInstance : updateEvent.card] as JSON )
            }
            xml{
                return render ( [cardInstance : updateEvent.card] as XML )
            }
        }
    }

    private CardEventUpdate createUpdateEvent(cmd){
        def event = new CardEventUpdate()
        event.card = Card.get(cmd.id)
        event.title =  cmd.title
        event.caseNumber = cmd.caseNumber
        event.description = cmd.description
        event.eventCreator = securityService.getLoggedInUser()
        return event
    }

    private CardEventSetAssignee createCardEventSetAssignee(cmd) {

        def event = new CardEventSetAssignee(
            card: cmd.card,
            eventCreator: securityService.getLoggedInUser(),
            newAssignee: cmd.assignee)

        return event
    }

    // Delete

    @Secured(['ROLE_QANBANADMIN'])
    def delete = {

        if( !params.id )
            return render(status: 400, text: "You need to specify a card")
        if( !Card.exists(params.id) )
            return render(status: 404, text: "Card with id $params.id not found")

         def deleteEvent = new CardEventDelete()
         deleteEvent.eventCreator = securityService.getLoggedInUser()
         deleteEvent.card = Card.get(params.id)
         
         eventService.persist(deleteEvent)

         if( !deleteEvent.hasErrors() )
            return render(status: 200, text: "Card with id $params.id deleted")

         return render(status: 503, text: "Server error: card delete error #188")
    }

    // Move

    def move = { MoveCardCommand mcc, SetAssigneeCommand sac ->

        if( mcc.hasErrors() || sac.hasErrors() || !ruleService.isMoveLegal(mcc.oldPhaseEntity,mcc.newPhaseEntity) ){
            return render(status: 400, text: "Bad Request")
        } else {
            def saEvent = createCardEventSetAssignee(sac)
            def mcEvent = null

            if( isMovingToANewPosition(mcc) ){
                mcEvent = createCardEventMove(mcc)
            }

            eventService.persist(mcEvent)
            eventService.persist(saEvent)

            return render(template:'cardForm', model:[ newPhase: params.newPhase, newPos: params.newPos, moveEvent: mcEvent, userList: User.list(), loggedInUser: securityService.getLoggedInUser() ])

        }
    }

    def sort = { MoveCardCommand mcc ->

        if( mcc.hasErrors() || !ruleService.isMoveLegal(mcc.oldPhaseEntity,mcc.newPhaseEntity) ){
            return render(status: 400, text: "Bad Request")
        } else {
            def mcEvent = null

            if( isMovingToANewPosition(mcc) ){
                mcEvent = createCardEventMove(mcc)
            }

            if(mcEvent)
                eventService.persist(mcEvent)

            return render(status: 200, text: "Sort successfull")

        }
    }

    private CardEventMove createCardEventMove(cmd) {
        def user = User.get(params.user) // TODO: Fixa så att den inloggade usern kommer med anropet
        def cardEventMove = new CardEventMove(
            newPhase: cmd.newPhaseEntity,
            newCardIndex: cmd.newPos,
            card: cmd.card,
            eventCreator: securityService.getLoggedInUser())
        return cardEventMove
    }

    private boolean isMovingToANewPosition(MoveCardCommand cmd) {
        def initialCardIndex = cmd.card.phase.cards.indexOf(cmd.card)
        def initialPhase = cmd.card.phase

        if(initialCardIndex == cmd.newPos && initialPhase.equals(cmd.newPhaseEntity))
            return false
            
        return true
    }
    
}


class MoveCardCommand {

    static constraints = {
        id(min: 0, nullable: false, validator:{ val, obj ->
                Card.exists(val)
            })
        newPos(min: 0, nullable: false)
        newPhase(min: 0, nullable: false, validator:{val, obj ->
                Phase.exists(val)
            })
    }

    static mapping = {
        version false
    }

    Integer id
    Integer newPos
    Integer newPhase

    def getCard() {
        Card.get(id)
    }

    def getOldPhaseEntity() {
        Card.get(id).phase
    }
    def getNewPhaseEntity() {
        Phase.get(newPhase)
    }

}

class SetAssigneeCommand {

    static constraints = {
        assigneeId(min: 0, nullable: true, validator:{ val, obj ->
                !val || User.exists( val )
            })
        id(min: 0, nullable: false, validator:{ val, obj ->
                Card.exists(val)
            })

    }
    Integer assigneeId
    Integer id

    def getAssignee() {
        User.get(assigneeId)
    }

    def getCard() {
        Card.get(id)
    }
}

class UpdateCardCommand {

  static constraints = {
    id( nullable: false )
    title( nullable: false, blank: false)
    caseNumber( nullable: false, blank: false )
    description( nullable: true, blank: true )

  }

  Integer id
  String title
  String description
  String caseNumber
}