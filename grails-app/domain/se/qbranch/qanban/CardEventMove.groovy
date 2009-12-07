package se.qbranch.qanban

class CardEventMove extends CardEvent {

    static constraints = {
        phaseDomainId ( nullable: true, blank: false )
    }

    static transients = ['card','newPhase']
    Card card
    Phase newPhase

    String phaseDomainId
    Integer newCardIndex


    public Card getCard(){
        if( !card && domainId ){
            card = Card.findByDomainId(domainId)
            if(!card) {
                card = CardEventDelete.findByDomainId(domainId).card
            }
        }
        return card
    }

    public Phase getNewPhase(){
        if( !newPhase && phaseDomainId ){
            newPhase = Phase.findByDomainId(phaseDomainId)
            if(!newPhase) {
                newPhase = PhaseEventDelete.findByDomainId(phaseDomainId).phase
            }
        }
        return newPhase
    }


    transient beforeInsert = {
        domainId = card.domainId
        phaseDomainId = newPhase.domainId
    }

    transient process = {
        
        card.phase.cards.remove(card)
        newPhase.cards.add(newCardIndex, card)
        card.phase = newPhase
        card.save()
        
    }

    String toString(){
        return "$dateCreated: $user moved the card to $newPhase"
    }
}