package se.qbranch.qanban

import grails.test.*

class PhaseEventCreateTests extends GrailsUnitTestCase {


    def user1
    def user2

    def board

    def phase1
    def phase2
    def phase3

    def card1onPhase1
    def card2onPhase1
    def card3onPhase2


    protected void setUp() {
        super.setUp()

        // User mock

        user1 = new User(username: "opsmrkr01", userRealName: "Mr. Krister")
        user2 = new User(username: "opsshba01", userRealName: "Shean Banan")

        mockDomain(User,[user1,user2])


        // Board mock

        board = new Board()
        mockDomain(Board,[board])


        // Phase mock

        phase1 = new Phase(name: "First phase", cardLimit: 5, board: board)
        phase2 = new Phase(name: "Second phase", cardLimit: 10, board: board)
        phase3 = new Phase(name: "Third phase", board: board)

        mockDomain(Phase,[phase1,phase2,phase3])

        board.addToPhases(phase1)
        board.addToPhases(phase2)
        board.addToPhases(phase3)

        // Card / CardEventCreate mock

        mockDomain(CardEventCreate)
        mockDomain(Card)

        def cardEventCreate1 = new CardEventCreate(title:"Card #1",caseNumber:1,description:"The first card originally from First phase on the first position",phase:phase1,user:user1)
        def cardEventCreate2 = new CardEventCreate(title:"Card #2",caseNumber:1,description:"The second card originally from First phase on the second position",phase:phase1,user:user1)
        def cardEventCreate3 = new CardEventCreate(title:"Card #3",caseNumber:1,description:"The third card originally from Second phase on the first position",phase:phase2,user:user1)

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

        mockDomain(PhaseEventCreate)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreatingAValidPhaseWithPositionSpecified() {
        String name = 'Fourth phase'
        Integer position = 1
        Integer cardLimit = 100

        def createEvent = new PhaseEventCreate(
            name: name,
            cardLimit: cardLimit,
            position: position,
            board: board,
            user: user1
        )

        createEvent.validate()
        createEvent.beforeInsert()
        createEvent.save()
        createEvent.afterInsert()

        assertNotNull "There should be a phase", createEvent.phase

        def newPhase = createEvent.phase

        assertEquals newPhase.domainId, createEvent.domainId
        assertEquals name, newPhase.name
        assertEquals position, board.phases.indexOf(newPhase)
    
    }

     void testCreatingAValidPhase() {
        String name = 'Fourth phase'
        Integer cardLimit = 100

        def createEvent = new PhaseEventCreate(
            name: name,
            cardLimit: cardLimit,
            board: board,
            user: user1
        )

        createEvent.validate()
        createEvent.beforeInsert()
        createEvent.save()
        createEvent.afterInsert()

        assertNotNull "There should be a phase", createEvent.phase

        def newPhase = createEvent.phase

        assertEquals newPhase.domainId, createEvent.domainId
        assertEquals name, newPhase.name
        assertEquals board.phases.size()-1, board.phases.indexOf(newPhase)

    }
}
