package se.qbranch.qanban

class CardEventMove extends Event implements Comparable {

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
        }
        return card
    }

    public Phase getNewPhase(){
        if( !newPhase && phaseDomainId ){
            newPhase = Phase.findByDomainId(phaseDomainId)
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

    int compareTo(Object o) {
        if (o instanceof Event) {
            Event event = (Event) o
            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            if(this.dateCreated < event.dateCreated) return AFTER
            if(this.dateCreated > event.dateCreated) return BEFORE

            return EQUAL
        }
    }

    boolean equals(Object o) {
        if(o instanceof Event) {
            Event event = (Event) o
            if(this.id == event.id)
            return true
        }
        return false
    }

    String toString(){
        return "$dateCreated: $user moved the card to $newPhase"
    }
}