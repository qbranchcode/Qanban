package se.qbranch.qanban

class Event {

    static constraints = {
        domainId( nullable:true, blank: false, unique: true )
    }
    
    Date dateCreated
    String domainId

}
