package se.qbranch.qanban

class CardEventSetAssignee extends CardEvent {

    static constraints = {
        newAssignee ( nullable: true )
    }

    static transients = ['card']
    Card card
    
    User newAssignee

    public Card getCard(){
        if( !card && domainId ){
            card = Card.findByDomainId(domainId)
        }
        return card
    }

    transient beforeInsert = {
        domainId = card.domainId
    }

    transient process(){
	card.assignee = newAssignee
        card.save()
    }

    String toString(){
        return "$dateCreated: $user set the assignee to $newAssignee"
    }
}
