package se.qbranch.qanban

import grails.converters.*

class MainViewController {

    def index = { redirect(action:view,params:params)  }

    def view = {
        [ board : Board.get(1) ]

    }

    def moveCard = {

        // TODO: Strunta i JSON-svar på denna och returnera error-kod vid fel istället?

        if( !params.id || !params.moveTo )
            return render([result: false] as JSON)

        def moveTo = params.moveTo as Integer
        def card = Card.get(params.id)
        def cards = card.phase.cards

        if( moveTo != null && card && moveTo < cards.size() ) {
                def oldCardIndex = cards.indexOf(card)
                cards.remove(oldCardIndex)
                cards.add(moveTo, card)
                return render([result: true] as JSON)
        }
        
        render([result: false] as JSON)

    }

    def cardForm = {
        
    }


}
