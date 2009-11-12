package se.qbranch.qanban

class CardEventMove{

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