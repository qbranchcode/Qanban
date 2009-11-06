package se.qbranch.qanban

import grails.converters.*

class MainViewController {

    def index = { redirect(action:view,params:params)  }

    def view = {
        [ board : Board.get(1) ]

    }

    def moveCard = {
        if (!params.id || !params.movePosition || !params.movePhase)
            return render([result: false] as JSON)

        def card = Card.get(params.id)
        def board = Board.get(1)
        def movePhaseParam = params.movePhase as Integer
        def oldPhaseId = card.phase.id
        def oldPhase = Phase.get(oldPhaseId)
        def movePhase = Phase.get(params.movePhase)
        def oldPhaseIndex = board.phases.indexOf(oldPhase)
        def newPhaseIndex = board.phases.indexOf(movePhase)
        def cardLimit = board.phases[newPhaseIndex].cardLimit

        if( newPhaseIndex < oldPhaseIndex ||
            newPhaseIndex > oldPhaseIndex.plus(1) ||
            ( board.phases[newPhaseIndex].cards.size() == cardLimit && oldPhaseIndex != newPhaseIndex ) )
            return render([result: false] as JSON)

        board.phases[oldPhaseIndex].cards.remove(card)
        board.phases[newPhaseIndex].cards.add(params.movePosition as Integer, card)
        return render([result: true] as JSON)
    }

    def moveCardToPosition = {

        // TODO: Return something other than JSON? Maybe HTTP error code?

        if( !params.id || !params.moveTo )
        return render([result: false] as JSON)

        def moveTo = params.moveTo as Integer
        def card = Card.get(params.id)
        def cards = card.phase.cards

        if(moveTo != null && card && moveTo < cards.size()) {
            def oldCardIndex = cards.indexOf(card)
            cards.remove(oldCardIndex)
            cards.add(moveTo, card)
            return render([result: true] as JSON)
        }
        
        render([result: false] as JSON)

    }

    def showBoard = {
        render(template: "/board/board", bean: Board.get(1))
    }

    def moveCardToPhase = {
        if (!params.id || !params.moveTo){
            return render([result: false] as JSON)
        }

        def moveTo = params.moveTo as Integer
        def card = Card.get(params.id)
        def phases = card.phase.board.phases
        def allowedPhase = card.phase.board.phases.indexOf(card.phase).plus(2)
        def newPhaseIndex = allowedPhase.minus(1)
        def board = Board.get(1)
        def cardsInPhase = board.phases[newPhaseIndex].cards.size()
        def cardLimit = board.phases[newPhaseIndex].cardLimit

        if(moveTo != null && card && moveTo < phases.size() && moveTo == allowedPhase && cardsInPhase < cardLimit ) {
            card.phase.cards.remove(card)
            board.phases[newPhaseIndex].cards.add(0, card)
            return render([result: true] as JSON)
        }
        render([result: false] as JSON)
    }
}
