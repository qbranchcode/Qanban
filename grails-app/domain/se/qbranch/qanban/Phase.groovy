package se.qbranch.qanban

class Phase {

    static constraints = {
        name(nullable: false, blank: false)
        cardLimit(nullable: true, validator:{ limit, phaseInstance ->
                if(phaseInstance.cardLimit < phaseInstance.cards.size())
                    return ['phase.cardLimit.lessThanCardsSize', limit]
            })
    }

    //TODO: Titta mer på det här!
    static hasMany = [cards:Card]

    String name
    Board board
    List cards = []
    Integer cardLimit

    boolean equals(Object o) {
        if(o instanceof Phase) {
            Phase p = (Phase) o
            if(this.id == p.id)
                return true
        }
        return false
    }
}