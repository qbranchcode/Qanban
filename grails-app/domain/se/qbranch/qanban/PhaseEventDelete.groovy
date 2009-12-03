package se.qbranch.qanban

class PhaseEventDelete  extends Event implements Comparable{

    static constraints = {
        cardLimit ( nullable: true )
        title ( nullable: true )
        board ( nullable: true )
        position ( nullable: true )
    }

    static transients = ['phase']

    Phase phase

    String title
    Board board
    Integer cardLimit
    Integer position

    public Phase getPhase(){
        if( !phase && domainId ){
            phase = Phase.findByDomainId(domainId)
        }
        return phase
    }

    transient beforeInsert = {
        domainId = phase.domainId
        title = phase.title
        board = phase.board
        cardLimit = phase.cardLimit
        position = board.phases.indexOf(phase)
    }

    transient process(){

        board.phases.remove(phase)
        phase.delete(flush:true)


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
