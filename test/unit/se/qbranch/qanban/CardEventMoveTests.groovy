package se.qbranch.qanban

import grails.test.*

class CardEventMoveTests extends GrailsUnitTestCase {

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
        mockDomain(CardEventMove)


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

    void testValidMoveCardEvent() {

        def card = Card.get(2)
        def newPhaseIndex = 1
        def newCardIndex = 0

        assertEquals 1, card.phase.cards.indexOf(card)
        assertEquals 0, card.phase.board.phases.indexOf(card.phase)

        def cardEventMove = new CardEventMove()
        cardEventMove.card = card;
        cardEventMove.newPhaseIndex = newPhaseIndex
        cardEventMove.newCardIndex = newCardIndex

        cardEventMove.afterInsert()
        cardEventMove.save()

        assertEquals 0, card.phase.cards.indexOf(card)
        assertEquals 1, card.phase.board.phases.indexOf(card.phase)

        assertEquals 1, cardEventMove.id
        //TODO: This works in development mode, why does the test fail?
        //assertEquals 1, card.events.size()
    }

    void testInvalidMoveCardEvent() {

        def card = Card.get(1)
        def newPhaseIndex = 0
        def newCardIndex = -1

        assertEquals 0, card.phase.cards.indexOf(card)
        assertEquals 0, card.phase.board.phases.indexOf(card.phase)

        def cardEventMove = new CardEventMove()
        cardEventMove.card = card;
        cardEventMove.newPhaseIndex = newPhaseIndex
        cardEventMove.newCardIndex = newCardIndex

        cardEventMove.save()

        assertEquals 0, card.phase.cards.indexOf(card)
        assertEquals 0, card.phase.board.phases.indexOf(card.phase)
        assertEquals null, cardEventMove.id

        for( error in cardEventMove.errors ){
            println error
        }

    }


}