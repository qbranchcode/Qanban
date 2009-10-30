package se.qbranch.qanban

class Card {

    static constraints = {
        title(blank:false, length:1..50)
        description(length:1..300)
        caseNumber()
        assignee(nullable: true)
        phase()
    }

    String title
    String description
    Integer caseNumber

    // Auto timestamps (changed when db is updated)
    Date dateCreated
    Date lastUpdated

    User assignee
    Phase phase
}
