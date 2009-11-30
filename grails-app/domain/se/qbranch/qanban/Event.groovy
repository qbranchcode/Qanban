package se.qbranch.qanban

class Event {

    static constraints = {
        domainId( nullable:true, blank: false )
        user(nullable: false)
    }
    
    Date dateCreated
    String domainId
    User user

}
