package se.qbranch.qanban

class PhaseEventUpdate extends Event implements Comparable{

    // TODO: Validera så att eventen inte sparas om inget värde har ändrats
    
    static constraints = {
        cardLimit ( nullable: true )
    }

    static transients = ['phase','board']
    Phase phase


    String name
    Integer cardLimit

    transient Phase getPhase(){
        if( !phase && domainId ){
            phase = Phase.findByDomainId(domainId)
        }
        return phase
    }

    transient void setPhase(phase){
        this.phase = phase
        cardLimit = phase.cardLimit
        name = phase.name
        domainId = phase.domainId
    }

        //TODO: Cleanup, check lazy settings.
    transient Board getBoard(){
        Phase.get(phase.id).board
    }

    transient beforeInsert = {
        domainId = phase.domainId
    }

    transient afterInsert = {

        phase.name = name
        phase.cardLimit = cardLimit
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
