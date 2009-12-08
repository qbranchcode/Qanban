package se.qbranch.qanban

class CardEventDelete  extends CardEvent {

    static constraints = {
        assignee ( nullable : true )
        title( nullable: true )
        description( nullable: true)
        phaseDomainId( nullable: true )
        position ( nullable: true )
        caseNumber( nullable: true )
    }

    static mapping = {
      columns {
          description type:'text'
      }
    }

    static transients = ['card']

    Card card

    String title
    String description
    String caseNumber
    String phaseDomainId
    Integer position
    User assignee

    public Card getCard(){
        if(!card && domainId) {
            card = new Card(title: title, description: description, caseNumber: caseNumber, phase: Phase.findByDomainId(phaseDomainId))
        }
        return card
    }

    transient beforeInsert = {
        domainId = card.domainId
        title = card.title
        description = card.description
        caseNumber = card.caseNumber
        phaseDomainId = card.phase.domainId
        position = card.phase.board.phases.indexOf(card)
        assignee = card.assignee
    }

    transient process(){
        card.phase.cards.remove(card)
        card.delete(flush:true)
    }
}
