package se.qbranch.qanban

class PhaseEventUpdate extends PhaseEvent {

    // TODO: Validera så att eventen inte sparas om inget värde har ändrats
    
    static constraints = {
        title ( nullable: false, blank: false )
        cardLimit ( nullable: true )
    }

    static transients = ['phase','board']
    Phase phase


    String title
    Integer cardLimit

    public Phase getPhase(){
        if( !phase && domainId ){
            phase = Phase.findByDomainId(domainId)
        }
        return phase
    }

    public void setPhase(phase){
        this.phase = phase
        cardLimit = phase.cardLimit
        title = phase.title
        domainId = phase.domainId
    }

        //TODO: Cleanup, check lazy settings.
    public Board getBoard(){
        Phase.get(phase.id).board
    }

    transient beforeInsert = {
        domainId = phase.domainId
    }

    transient process( ) {

        phase.title = title
        phase.cardLimit = cardLimit
        phase.save()

    }
}
