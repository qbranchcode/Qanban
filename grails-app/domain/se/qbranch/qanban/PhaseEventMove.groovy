package se.qbranch.qanban

class PhaseEventMove extends Event implements Comparable{
  
    static constraints = {
        position (min: 0, nullable: false, validator:{ val, obj ->

                return ( val < obj.phase.board.phases.size() )

            })
    }

    static transients = ['phase','title']
    Phase phase

    Integer position

    public Phase getPhase(){
        if( !phase && domainId ){
            phase = Phase.findByDomainId(domainId)
        }
        return phase
    }

    transient beforeInsert = {
        domainId = phase.domainId
    }

    transient process(){
        phase.board.phases.remove(phase)
        phase.board.phases.add(position, phase)
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

    public String getTitle() {
        if( !phase && domainId){
            phase = Phase.findByDomainId(domainId)
        }
        return phase.title
    }
}
