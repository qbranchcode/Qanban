package se.qbranch.qanban
import org.codehaus.groovy.grails.plugins.codecs.MD5Codec

class PhaseEventCreate extends PhaseEvent {

    static constraints = {
        cardLimit ( nullable: true )
        position ( nullable: false )
        title(nullable: false, blank: false)
    }

    static transients = ['phase','summaryItems']
    Phase phase

    String title
    Board board
    Integer cardLimit
    Integer position

    public List getSummaryItems() {
        return [getPhase().title]
    }
    
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
        generateDomainId(title, board )
    }

    transient process = {

        phase = new Phase(
            title: title,
            board: board,
            cardLimit: cardLimit,
            domainId: domainId
        )
        board.phases.add(position, phase)

        phase.save()
        
    }
}
