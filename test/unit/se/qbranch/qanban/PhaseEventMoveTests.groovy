package se.qbranch.qanban

import grails.test.*

class PhaseEventMoveTests extends GrailsUnitTestCase {

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
        mockDomain(PhaseEventMove)


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

    void testLegalMoveOfFirstPhaseToNextIndex() {

//    Phase phase
//    User user
//    Date dateCreated

        def phase = Phase.get(1)

        println Phase.list()

        assertEquals 1, phase.board.phases.indexOf(phase)
        
        def pem = new PhaseEventMove( phase: phase, newPhaseIndex: 2 )


        pem.save()
        pem.afterInsert()

        assertEquals 2, phase.board.phases.indexOf(phase)


    }
}
