package se.qbranch.qanban

import grails.converters.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class CardController {

    def securityService
    def eventService
    def ruleService


    // Create

    def create = {
        if ( !params.boardId )
            return render(status: 400, text: "The parameter 'boardId' must be specified")
        println "create"
        CardEventCreate createEvent = createCardEventCreate(params)      
        eventService.persist(createEvent)
        renderCreateResult(createEvent)
    }

    private CardEventCreate createCardEventCreate(params){
        def event = new CardEventCreate(params)
        event.user = securityService.getLoggedInUser()
        event.phaseDomainId = Board.get(params.boardId).phases[0].domainId
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
            return renderShowResultWithoutIdParam(params)
        if( !Card.exist(params.id) )
            return render(status: 404, text: "Card with id $params.id not found")
        renderShowResult(params)
    }

    private def renderShowResultWithoutIdParam(params){
        withFormat{
            html{
                def board = Board.get(params.'board.id')
                return render(template:'cardForm',model:[ boardInstance: board])
            }
            js{
                return render(status: 400, text: "You have to specify an id")
            }
            xml{
                return render(status: 400, text: "You have to specify an id")
            }
        }
    }

    private def renderShowResult(params){
        withFormat{
            html{
                def card = Card.get(params.id)
                def board = card.phase.board
                return render(template:'cardForm',model:[cardInstance: card , boardInstance: board])
            }
            js{
                return render ( [ cardInstance : createEvent.card ] as JSON)
            }
            xml{
                return render ( [ cardInstance : createEvent.card ] as XML)
            }
        }
    }

    // Update

    def update = {

        CardEventUpdate updateEvent = new CardEventUpdate(params)
        eventService.persist(updateEvent)

        withFormat{
            html{
                def board = updateEvent.board
                return render (template: 'cardForm', model:[createEvent:createEvent, boardInstance: board])
            }
            js{
                return render ( [cardInstance : updateEvent.card] as JSON )
            }
            xml{
                return render ( [cardInstance : updateEvent.card] as XML )
            }
        }

    }

    private CardEventSetAssignee createCardEventSetAssignee(cmd) {

        def event = new CardEventSetAssignee(
            card: cmd.card,
            user:  User.get(params.user), // TODO: Fixa så att den inloggade usern kommer med anropet
            newAssignee: cmd.assignee)

        return event
    }

    // Delete

    def delete = {

    }

    // Move

    def move = { MoveCardCommand mcc, SetAssigneeCommand sac ->

        if( mcc.hasErrors() || sac.hasErrors() || !ruleService.isMoveLegal(mcc) ){
            return response.status = 400 // Bad request
        } else {
            def saEvent = createEventSetAssignee(sac)
            def mcEvent = null

            if( isMovingToANewPosition(mcc) ){
                mcEvent = createCardEventMove(mcc)
            }

            eventService.persist(mcEvent)
            eventSertice.persist(saEvent)


        }

    }



    private CardEventMove createCardEventMove(cmd, board) {
        def user = User.get(params.user) // TODO: Fixa så att den inloggade usern kommer med anropet
        def cardEventMove = new CardEventMove(
            newPhase: cmd.phase,
            newCardIndex: cmd.newPos,
            card: cmd.card,
            user: user)
        return cardEventMove
    }

    private boolean isMovingToANewPosition(MoveCardCommand cmd) {

        def initialCardIndex = cmd.card.phase.cards.indexOf(cmd.card)
        def initialPhase = cmd.card.phase

        if(initialCardIndex == cmd.newPos && initialPhase.equals(cmd.phase))
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

    def getPhase() {
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
