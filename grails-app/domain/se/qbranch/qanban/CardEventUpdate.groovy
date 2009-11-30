package se.qbranch.qanban

class CardEventUpdate extends Event implements Comparable {

    // TODO: Validera så att eventen inte sparas om inget värde har ändrats
    
    static constraints = {
        title( blank: false, length: 1..50)
        description(length:1..300, blank: true, nullable: true )
        caseNumber( )
    }


    static mapping = {
        columns {
            description type:'text'
        }
    }

    static transients = ['card']
    Card card

    String title
    String description
    Integer caseNumber

    Card getCard(){
        if( !card && domainId ){
            card = Card.findByDomainId(domainId)
        }
        return card
    }

    transient beforeInsert = {
        domainId = card.domainId
    }

    transient afterInsert = {

        card.title = title
        card.description = description
        card.caseNumber = caseNumber
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
        return "$dateCreated: $user updated the card info"
    }
}
