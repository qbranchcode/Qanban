package se.qbranch.qanban

import grails.converters.*

class MainViewController {

    def index = { redirect(action:view,params:params)  }

    def view = {
        [ board : Board.get(1) ]

    }

    def moveCard = {
        def moveTo = params.moveTo        
        def card = Card.get(params.id)
        def cards = card.phase.cards

        //TODO: Hur hanteras nullvärde på moveTo ? Som integer?
        if(card && moveTo < cards.size() && moveTo != null) {
            def oldCardIndex = cards.indexOf(card)
            cards.remove(oldCardIndex)
            cards.add(moveTo, card)
        }
        else {
            render([result: false] as JSON)
        }
        
        render([result: true] as JSON)
    }
}
