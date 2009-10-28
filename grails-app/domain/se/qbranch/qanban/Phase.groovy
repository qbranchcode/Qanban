package se.qbranch.qanban

class Phase {

    static constraints = {
        name(nullable: false, blank: false)
    }

    //TODO: Titta mer på det här!
    static hasMany = [cards:Card]



    String name
}