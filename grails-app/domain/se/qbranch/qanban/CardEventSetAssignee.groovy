package se.qbranch.qanban

class CardEventSetAssignee extends Event implements Comparable {

    static constraints = {
        newAssignee ( nullable: true )
    }

    static transients = ['card']

    Card card
    User newAssignee

    transient beforeInsert = {
        domainId = card.domainId
    }

    transient afterInsert = {
	card.assignee = newAssignee
    }

    int compareTo(Object o) {
        if (o instanceof Event) {
            Event event = (Event) o
            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            if(this.dateCreated < event.dateCreated) return AFTER
            if(this.dateCreated > event.dateCreated) return BEFORE

            return EQUAL
        }
    }

    boolean equals(Object o) {
        if(o instanceof Event) {
            Event event = (Event) o
            if(this.id == event.id)
            return true
        }
        return false
    }
}
