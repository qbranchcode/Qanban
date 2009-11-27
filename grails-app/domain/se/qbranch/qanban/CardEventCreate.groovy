package se.qbranch.qanban
import org.codehaus.groovy.grails.plugins.codecs.MD5Codec

class CardEventCreate extends Event implements Comparable{

    static constraints = {
        assignee ( nullable : true )
        title( blank: false, length: 1..50 )
        description(length:1..300, blank: true, nullable: true)
        phase()
        caseNumber( )
    }

    static transients = ['card']

    String title
    String description
    Integer caseNumber
    Card card

    //TODO: Change to checksum connections
    User assignee
    Phase phase

    

    //TODO: dateCreated is not set?!
    transient beforeInsert = {
        domainId = MD5Codec.encode(dateCreated + title + description + caseNumber)
    }

    transient afterInsert = {
        //println "timestamp: $dateCreated title: $title desc: $description case# $caseNumber" 
        card = new Card()
        card.domainId = domainId
        card.title = title
        card.phase = phase
        card.description = description
        card.caseNumber = caseNumber

        phase.addToCards(card)

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
}
