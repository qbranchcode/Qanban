package se.qbranch.qanban

class Event {

    static constraints = {
        domainId( nullable:true, blank: false, unique: true )
        user(nullable: false)
    }
    
    Date dateCreated
    String domainId
    User user

}
