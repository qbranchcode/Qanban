package se.qbranch.qanban

class CardEventMove{

    Integer newCardIndex
    Card card
    User user
    Phase newPhase

    transient afterInsert = {

        card.phase.cards.remove(card)
        newPhase.cards.add(newCardIndex, card)
        card.phase = newPhase
    }

    def invalidateEvent() {
        this.card = null
    }

}