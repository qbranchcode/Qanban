package se.qbranch.qanban

class PhaseEventMove extends PhaseEvent {
  
    static constraints = {
        position (min: 0, nullable: false, validator:{ val, obj ->

                return ( val < obj.phase.board.phases.size() )

            })
    }

    static transients = ['phase']
    Phase phase

    Integer position

    public Phase getPhase(){
        if( !phase && domainId ){
            phase = Phase.findByDomainId(domainId)
            if(!phase) {
                phase = PhaseEventDelete.findByDomainId(domainId).phase
            }
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
}
