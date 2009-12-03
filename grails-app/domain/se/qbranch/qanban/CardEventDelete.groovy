package se.qbranch.qanban

class CardEventDelete  extends Event implements Comparable {

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
    Integer caseNumber
    String phaseDomainId
    Integer position
    User assignee


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
