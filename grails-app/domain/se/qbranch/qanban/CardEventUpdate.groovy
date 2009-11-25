package se.qbranch.qanban

class CardEventUpdate implements Comparable {

    Card card
    String title
    String description
    Integer caseNumber
    Date dateCreated
    User user

    transient afterInsert = {

        if( cardHasChangedProperties()) {
            card.title = title
            card.description = description
            card.caseNumber = caseNumber
            card.save()
    
        }
    }
    
    boolean cardHasChangedProperties() {
        if(this.card.title != this.title) return true
        if(this.card.description != this.description) return true
        if(this.card.caseNumber != this.caseNumber) return true
        else return false
    }

    int compareTo(Object o) {
        if (o instanceof CardEventUpdate) {
            CardEventUpdate c = (CardEventUpdate) o
            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            if(this.dateCreated < c.dateCreated) return AFTER
            if(this.dateCreated > c.dateCreated) return BEFORE

            return EQUAL
        }
    }

    boolean equals(Object o) {
        if(o instanceof CardEventUpdate) {
            CardEventUpdate c = (CardEventUpdate) o
            if(this.id == c.id)
            return true
        }
        return false
    }
}
