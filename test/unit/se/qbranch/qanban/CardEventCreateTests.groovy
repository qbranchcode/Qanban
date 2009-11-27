package se.qbranch.qanban

import grails.test.*

class CardEventCreateTests extends GrailsUnitTestCase {

    def board

    def phase1
    def phase2
    def phase3
    
    def card1onPhase1
    def card2onPhase1
    def card3onPhase2

    
    protected void setUp() {
        super.setUp()

        // Board mock

        board = new Board()
        mockDomain(Board,[board])


        // Phase mock

        phase1 = new Phase(name: "First phase", cardLimit: 5, board: board)
        phase2 = new Phase(name: "Second phase", cardLimit: 10, board: board)
        phase3 = new Phase(name: "Third phase", board: board)

        mockDomain(Phase,[phase1,phase2,phase3])


        // Card / CardEventCreate mock

        mockDomain(CardEventCreate)
        mockDomain(Card)

        def cardEventCreate1 = new CardEventCreate(title:"Card #1",caseNumber:1,description:"The first card originally from First phase on the first position",phase:phase1)
        def cardEventCreate2 = new CardEventCreate(title:"Card #2",caseNumber:1,description:"The second card originally from First phase on the second position",phase:phase1)
        def cardEventCreate3 = new CardEventCreate(title:"Card #3",caseNumber:1,description:"The third card originally from Second phase on the first position",phase:phase2)

        cardEventCreate1.beforeInsert()
        cardEventCreate1.save()
        cardEventCreate1.afterInsert()

        cardEventCreate2.beforeInsert()
        cardEventCreate2.save()
        cardEventCreate2.afterInsert()

        cardEventCreate3.beforeInsert()
        cardEventCreate3.save()
        cardEventCreate3.afterInsert()

        card1onPhase1 = cardEventCreate1.card
        card2onPhase1 = cardEventCreate2.card
        card3onPhase2 = cardEventCreate3.card

        // Assertions to validate the mock setup

        assertEquals 1, board.id
        assertEquals 1, phase1.id
        assertEquals 2, phase2.id
        assertEquals 3, phase3.id
        assertEquals 1, card1onPhase1.id
        assertEquals 2, card2onPhase1.id
        assertEquals 3, card3onPhase2.id
    }

    protected void tearDown() {
        super.tearDown()
    }


    void testCreateingValidEvent() {
        def event = new CardEventCreate();

        event.title = "Card #4"
        event.description = "Created By A Event"
        event.caseNumber = 4
        event.phase = Phase.get(1)

        event.beforeInsert()
        event.save()
        event.afterInsert()

        event.errors.getAllErrors().each { println it}
        println event.domainId
        
        assertFalse 'The event should not have errors' ,event.hasErrors()
        assertEquals 4, event.id
        assertEquals event.title, Card.findByDomainId(event.domainId).title
           
    }

    void testCreateingInvalidEventWithoutCaseNo() {
        def event = new CardEventCreate();

        event.title = "Card #4"
        event.description = "Created By A Event"
        event.phase = Phase.get(1)

        event.beforeInsert()
        event.save()
        event.afterInsert()
        event.errors.getAllErrors().each { println it}

        assertTrue 'The event should have errors' ,event.hasErrors()
        assertEquals null, event.id
        println event.domainId
        assertEquals null, Card.findByDomainId(event.domainId)

    }
}
