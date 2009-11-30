package se.qbranch.qanban
import org.codehaus.groovy.grails.plugins.codecs.MD5Codec

class CardEventCreate extends Event implements Comparable{

    static constraints = {
        assignee ( nullable : true )
        title( blank: false, length: 1..50 )
        description(length:1..300, blank: true, nullable: true)
        phaseDomainId( nullable: true, blank: false )
    }

    static transients = ['card','phase','board']

    Card card
    Phase phase


    String title
    String description
    Integer caseNumber
    String phaseDomainId

    //TODO: Change to checksum connections
    User assignee
    

    Card getCard() {
        if( card ){
            return card
        }else{
            card = new Card( title: title, description: description, caseNumber: caseNumber, domainId: domainId, assignee: assignee, phase: phase )
            card.validate()           
            return card
        }
    }

    
    //TODO: Cleanup, check lazy settings.
    transient Board getBoard(){
        Phase.get(phase.id).board
    }


    //TODO: dateCreated is not set?!
    transient beforeInsert = {
        domainId = MD5Codec.encode(dateCreated + title + description + caseNumber)
        phaseDomainId = phase.domainId
    }

    transient afterInsert = {
        println "timestamp: $dateCreated title: $title desc: $description case# $caseNumber phase: $phase"
        phase = Phase.get(phase.id)
        card = new Card()
        card.domainId = domainId
        card.title = title
        card.phase = phase
        card.description = description
        card.caseNumber = caseNumber

        phase.addToCards(card)

        card.save()
    }

    transient onLoad = {
        println "did: $domainId"
        println "o.did: " + owner.domainId
        //card = Card.findByDomainId(this.domainId)
        //phase = Phase.findByDomainId(phaseDomainId)
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

    String toString() {
        return "$dateCreated: $user created the card"
    }

}
