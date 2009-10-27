package se.qbranch.qanban

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

class Card {

    static constraints = {
        caseNumber(blank: true, nullable: true)
		prio(blank: true, nullable: true)
        cardCreated(blank: true, nullable: true)
        cardDone(blank: true, nullable: true)
        asignee(blank: true, nullable: true)
    }

    static mapping = {
        //cardCreated type: PersistentDuration
        //cardDone type: PersistentDuration
    }

    String description
    Integer caseNumber
	Integer prio
    //TODO: Varför fungerar inte Joda-Time?
    //DateTime cardCreated
    //DateTime cardDone
    Date cardCreated = new Date()
    Date cardDone
    //TODO: Ska vara AD-User sen
    String asignee

}
