package se.qbranch.qanban

class Card {

    static constraints = {
        title(blank:false, length:1..50)
        description( blank: true, nullable: true)
        caseNumber( blank: false )
        assignee(nullable: true)
        phase()
        domainId( unique: true, blank: false )
    }


    static mapping = {
      columns {
          description type:'text'
      }
    }


    String domainId
    String title
    String description
    String caseNumber

    Date dateCreated
    Date lastUpdated

    User assignee
    Phase phase
    
}
