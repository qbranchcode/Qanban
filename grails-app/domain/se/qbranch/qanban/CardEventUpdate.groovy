package se.qbranch.qanban

class CardEventUpdate extends Event implements Comparable {

    static constraints = {
        title( blank: false, length: 1..50 )
        description(length:1..300, blank: true, nullable: true)
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
