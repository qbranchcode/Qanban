package se.qbranch.qanban

class Card {

    static constraints = {
        title(blank:false, length:1..50)
        description()
        caseNumber()
        assignee(nullable: true)
        phase()
    }

    static mapping = {
      columns {
          description type:'text'
      }
    }

    static hasMany = [ events : CardEventMove ]

    String domainId
    String title
    String description
    Integer caseNumber

    SortedSet events

    // Auto timestamps (changed when db is updated)
    Date dateCreated
    Date lastUpdated

    User assignee
    Phase phase
}
