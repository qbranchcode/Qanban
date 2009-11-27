package se.qbranch.qanban
import org.codehaus.groovy.grails.plugins.codecs.MD5Codec

class PhaseEventCreate extends Event implements Comparable{

    static constraints = {
        cardLimit ( nullable: true )

    }

    static transients = ['phase']

    Phase phase

    String name
    Board board
    Integer cardLimit
    Integer position

    transient beforeInsert = {
        domainId = MD5Codec.encode(dateCreated + name + board )
    }

    transient afterInsert = {

        phase = new Phase(
            name: name,
            board: board,
            cardLimit: cardLimit,
            domainId: domainId
        )

        if( position ){
            board.phases.add(position, phase)
        }else{
            board.addToPhases(phase)
        }

        phase.save()
        
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