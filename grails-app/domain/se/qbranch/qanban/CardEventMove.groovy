package se.qbranch.qanban

class CardEventMove implements Comparable {

    Integer newCardIndex
    Card card
    User user
    Phase newPhase
    Date dateCreated

    transient afterInsert = {

        card.phase.cards.remove(card)
        newPhase.cards.add(newCardIndex, card)
        card.phase = newPhase
    }

    def invalidateEvent() {
        this.card = null
    }

    int compareTo(Object o) {
        if (o instanceof CardEventMove) {
            CardEventMove c = (CardEventMove) o
            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            if(this.dateCreated < c.dateCreated) return AFTER
            if(this.dateCreated > c.dateCreated) return BEFORE

            return EQUAL
        }
    }

    boolean equals(Object o) {
        if(o instanceof CardEventMove) {
            CardEventMove c = (CardEventMove) o
            if(this.id == c.id)
            return true
        }
        return false
    }
}