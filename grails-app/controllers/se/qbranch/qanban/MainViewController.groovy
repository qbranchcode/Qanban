package se.qbranch.qanban

import grails.converters.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class MainViewController {

    def authenticateService

    def moveCardAndSetAssignee = { MoveCardCommand mcc, SetAssigneeCommand sac ->
        if(mcc.hasErrors() || sac.hasErrors()) {
            return response.status = 500 //Internal Server Error
        } else {
            createCardEventSetAssignee(sac);
            def board = Board.get(1)
            //TODO: Why do I have to use "Phase.get(cmd.card.phase.id)) ? Why can't I use "cmd.card.phase" ?
            def oldPhaseIndex = board.phases.indexOf(Phase.get(mcc.card.phase.id))
            def newPhaseIndex = board.phases.indexOf(mcc.phase)

            if(isMoveLegal(oldPhaseIndex, newPhaseIndex)
                && isPhaseFree(mcc.phase, oldPhaseIndex, newPhaseIndex)) {
                createCardEventMove(mcc)
                return render(template:'/card/cardForm', model: [ boardInstance: mcc.card.phase.board, cardInstance: mcc.card, newPhase: params."newPhase", newPos: params."newPos" ,userList: User.list(), loggedInUser: authenticateService.userDomain()])
            }
            return response.status = 500 //Internal Server Error
        }
    }

    def index = { redirect(action:view,params:params)  }


    def view = {
        [ board : Board.get(1) ]
    }

    def showBoard = {
        render(template: "/board/board", bean: Board.get(1))
    }

    def moveCard = { MoveCardCommand cmd ->
        if(cmd.hasErrors()) {
            return render([result: false] as JSON)
        } else {
            def board = Board.get(1)
            //TODO: Why do I have to use "Phase.get(cmd.card.phase.id)) ? Why can't I use "cmd.card.phase" ?
            def oldPhaseIndex = board.phases.indexOf(Phase.get(cmd.card.phase.id))
            def newPhaseIndex = board.phases.indexOf(cmd.phase)

            if(isMoveLegal(oldPhaseIndex, newPhaseIndex)
                && isPhaseFree(cmd.phase, oldPhaseIndex, newPhaseIndex)) {
                createCardEventMove(cmd)
                return render([result: true] as JSON)
            }
            return render([result: false] as JSON)
        }
    }

    def setAssignee = { SetAssigneeCommand cmd ->

        if(cmd.hasErrors()) {
            return render([result: false] as JSON)
        } else {
            createCardEventSetAssignee(cmd);
        }
    }


    /**
     * Move Card Event
     **/

    void createCardEventMove(cmd) {
        if(checkActuallyMoving(cmd)) {
            def cardEventMove = new CardEventMove(
                newPhase: cmd.phase,
                newCardIndex: cmd.newPos,
                card: cmd.card,
                user: User.get(params.user)) // TODO: Fixa så att den inloggade usern kommer med anropet
            cardEventMove.save()
        }
    }

    boolean isMoveLegal(oldPhaseIndex, newPhaseIndex) {
        if(oldPhaseIndex+1 == newPhaseIndex || oldPhaseIndex == newPhaseIndex)
        return true
        else
        return false
    }

    boolean isPhaseFree(phase, oldPhaseIndex, newPhaseIndex) {
        if(phase.cards.size() == phase.cardLimit && oldPhaseIndex != newPhaseIndex) {
            return false
        }
        else
        return true
    }

    boolean checkActuallyMoving(cmd) {
        def initialCardIndex = cmd.card.phase.cards.indexOf(cmd.card)
        def initialPhase = cmd.card.phase
        if(initialCardIndex == cmd.newPos && initialPhase.equals(cmd.phase))
        return false
        return true
    }

    void createCardEventMove(cmd, board) {
        def user = User.get(params.user) // TODO: Fixa så att den inloggade usern kommer med anropet
        def cardEventMove = new CardEventMove(
            newPhase: cmd.phase,
            newCardIndex: cmd.newPos,
            card: cmd.card,
            user: user)
        cardEventMove.save()
    }


    /**
     * Set Assignee Event
     **/

    void createCardEventSetAssignee(cmd) {
        def cardEventSetAssignee = new CardEventSetAssignee(
            card: cmd.card,
            user:  User.get(params.user), // TODO: Fixa så att den inloggade usern kommer med anropet
            newAssignee: cmd.assignee)

        cardEventSetAssignee.save()
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
