package se.qbranch.qanban

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

class Card {

    static constraints = {
        caseNumber(blank: true, nullable: true)
        cardCreated(blank: true, nullable: true)
        cardDone(blank: true, nullable: true)
        asignee(blank: true, nullable: true)
    }

    static mapping = {
        cardCreated type: PersistentDuration
        cardDone type: PersistentDuration
    }

    String description
    Integer caseNumber
    DateTime cardCreated = new DateTime()
    DateTime cardDone
    //TODO: Ska vara AD-User sen
    String asignee

}
