package se.qbranch.qanban

class CardEventMove{

        static constraints = {
        newCardIndex(validator : { movement, eventInstance ->
                if( eventInstance.newCardIndex < 0 ){
                    return ['cardEventMove.positionMovement.outOfBounds',movement]
                }

            })

        newPhaseIndex(validator : { movement, eventInstance ->
                if( eventInstance.newPhaseIndex < 0 || eventInstance.newPhaseIndex > eventInstance.card.phase.cards.size() ){
                    return ['cardEventMove.phaseMovement.outOfBounds', movement]
                }
            })
    }

    Integer newPhaseIndex
    Integer newCardIndex
    Card card

    transient afterInsert = {

        def newPhaseInstance = card.phase.board.phases[newPhaseIndex]
        card.phase.cards.remove(card)
        newPhaseInstance.cards.add(newCardIndex, card)
        card.phase = newPhaseInstance
    }

    def invalidateEvent() {
        this.card = null
    }

}