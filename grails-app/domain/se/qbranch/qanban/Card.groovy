package se.qbranch.qanban

class Card {

    static constraints = {
        title(blank:false, length:1..50)
        description()
        caseNumber()
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
    Integer caseNumber

    // Auto timestamps (changed when db is updated)
    Date dateCreated
    Date lastUpdated

    User assignee
    Phase phase
}
