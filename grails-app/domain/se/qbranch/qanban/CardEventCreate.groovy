package se.qbranch.qanban


class CardEventCreate extends CardEvent {

    static constraints = {
        assignee ( nullable : true )
        title( blank: false, length: 1..50 )
        description(blank: true, nullable: true)
        phaseDomainId( nullable: false, blank: false )
        caseNumber( blank: false )
    }

    static mapping = {
      columns {
          description type:'text'
      }
    }

    static transients = ['card','phase','board']

    Card card

    String title
    String description
    String caseNumber
    String phaseDomainId

    //TODO: Change to checksum connections?
    User assignee
    

    public Phase getPhase() {
        def phase
        
        if( phaseDomainId ){
            phase = Phase.findByDomainId(phaseDomainId)
        }
        return phase
    }

    public Card getCard(){
        if( !card ){
            if( domainId){
               card = Card.findByDomainId(domainId)
            }else{
               card = new Card( title: title, description: description, caseNumber: caseNumber, domainId: domainId, assignee: assignee, phase: phase )
            }
        }
        return card
    }
    
    //TODO: Cleanup, check lazy settings.
    public Board getBoard(){
        Phase.findByDomainId(phaseDomainId).board
    }

    def beforeInsert = {
        generateDomainId(title, caseNumber)
    }

    def process(){
        def phase = getPhase()
        card = new Card()
        card.domainId = domainId
        card.title = title
        card.phase = phase
        card.description = description
        card.caseNumber = caseNumber
        card.assignee = assignee
        phase.addToCards(card)
        card.save()
    }


    String toString() {
        return "$dateCreated: $user created the card"
    }
}
