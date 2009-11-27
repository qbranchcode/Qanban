package se.qbranch.qanban

import grails.test.*

class CardEventCreateTests extends GrailsUnitTestCase {

    def board
    
    protected void setUp() {
        super.setUp()
        mockDomain(Card, [ new Card(title: "TestCard",
                    description: "This is a description",
                    caseNumber: 1),

                new Card(title: "OtherCard",
                    description: "This is the other card",
                    caseNumber: 2),
                new Card(title: "Card three",
                    description: "This is the third card",
                    caseNumber: 5)])

        mockDomain(Board)
        mockDomain(Phase)
        mockDomain(CardEventCreate)


        board = new Board().addToPhases(new Phase(name: "test", cardLimit: 5))
        .addToPhases(new Phase(name: "other phase", cardLimit: 3))
        .addToPhases(new Phase(name: "thid phase", cardLimit: 3))
        .save()
        for(card in Card.list()) {
            def phase = board.phases[0]
            def p2 = board.phases[1]
            def p3 = board.phases[2]
            phase.cards.add(card)
            card.phase = phase
            card.phase.board = board
            p2.board = board
            p3.board = board
            p2.save()
            p3.save()
            phase.save()
            card.save()
        }
    }

    protected void tearDown() {
        super.tearDown()
    }


    void testCreateingAValigEvent() {
        def event = new CardEventCreate();

        event.title = "Card #4"
        event.description = "Created By A Event"
        event.caseNumber = 4
        event.phase = Phase.get(1)

        event.beforeInsert()
        event.save()
        event.afterInsert()
        event.errors.getAllErrors().each { println it}

        assertFalse 'The event should not have errors' ,event.hasErrors()
        assertEquals 1, event.id
        println event.cardDomainId
        assertEquals event.title, Card.findByDomainId(event.cardDomainId).title
           
    }
}
