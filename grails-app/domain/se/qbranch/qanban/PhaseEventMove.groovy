package se.qbranch.qanban

class PhaseEventMove {

    Integer newPhaseIndex
    Phase phase
    User user
    Date dateCreated

    static constraints = {

    }

    transient afterInsert = {
        phase.board.phases.remove(phase)
        phase.board.phases.add(newPhaseIndex, phase)
    }

    int compareTo(Object o) {
        if (o instanceof PhaseEventMove) {
            PhaseEventMove event = (PhaseEventMove) o
            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            if(this.dateCreated < event.dateCreated) return AFTER
            if(this.dateCreated > event.dateCreated) return BEFORE

            return EQUAL
        }
    }

    boolean equals(Object o) {
        if(o instanceof PhaseEventMove) {
            PhaseEventMove event = (PhaseEventMove) o
            if(this.id == event.id)
            return true
        }
        return false
    }
}
