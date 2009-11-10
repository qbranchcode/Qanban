package se.qbranch.qanban

import grails.converters.*

class MainViewController {

    def index = { redirect(action:view,params:params)  }

    def view = {
        [ board : Board.get(1) ]

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
                cmd.card.phase.cards.remove(cmd.card)
                cmd.phase.cards.add(cmd.moveToCardsIndex, cmd.card)
                return render([result: true] as JSON)
            }
            return render([result: false] as JSON)
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

    def showBoard = {
        render(template: "/board/board", bean: Board.get(1))
    }
}

class MoveCardCommand {

    static constraints = {
        id(min: 0, nullable: false, validator:{ val, obj ->
                Card.exists(obj.id)
            })
        moveToCardsIndex(min: 0, nullable: false)
        moveToPhase(min: 0, nullable: false, validator:{val, obj ->
                Phase.exists(obj.moveToPhase)
            })
    }

    Integer id
    Integer moveToCardsIndex
    Integer moveToPhase

    def getCard() {
        Card.get(id)
    }

    def getPhase() {
        Phase.get(moveToPhase)
    }
    
}


