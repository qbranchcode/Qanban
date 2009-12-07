package se.qbranch.qanban

class CardEvent extends Event {

    static constraints = {
    }

    transient public String checkCurrentTitle() {
        if( domainId ){
            def card = Card.findByDomainId(domainId)
            if (card){
                return card.title
            }else{
                return CardEventDelete.findByDomainId(domainId).title
            }
        }
        else return "This should not be a valid output"
    }

    transient boolean doesDomainExist(){
        return Card.findByDomainId(domainId) != null
    }
}
