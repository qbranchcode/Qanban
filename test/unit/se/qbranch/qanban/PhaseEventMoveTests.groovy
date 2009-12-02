package se.qbranch.qanban

import grails.test.*

class PhaseEventMoveTests extends GrailsUnitTestCase {

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


        // Phase / PhaseEventCreate mock

        mockDomain(PhaseEventCreate)
        mockDomain(Phase)

        def phaseEventCreate1 = new PhaseEventCreate(name: "First phase", cardLimit: 5, user: user1, board: board)
        def phaseEventCreate2 = new PhaseEventCreate(name: "Second phase", cardLimit: 10, user: user1 , board: board)
        def phaseEventCreate3 = new PhaseEventCreate(name: "Third phase", user: user1, board: board)

        phaseEventCreate1.beforeInsert()
        phaseEventCreate1.save()
        phaseEventCreate1.process()

        phaseEventCreate2.beforeInsert()
        phaseEventCreate2.save()
        phaseEventCreate2.process()

        phaseEventCreate3.beforeInsert()
        phaseEventCreate3.save()
        phaseEventCreate3.process()

        phase1 = phaseEventCreate1.phase
        phase2 = phaseEventCreate2.phase
        phase3 = phaseEventCreate3.phase

        assertEquals phase1, Phase.findByDomainId(phase1.domainId)

        // Card / CardEventCreate mock

        mockDomain(CardEventCreate)
        mockDomain(Card)

        def cardEventCreate1 = new CardEventCreate(title:"Card #1",caseNumber:1,description:"The first card originally from First phase on the first position",phaseDomainId:phase1.domainId,user:user1)
        def cardEventCreate2 = new CardEventCreate(title:"Card #2",caseNumber:2,description:"The second card originally from First phase on the second position",phaseDomainId:phase1.domainId,user:user1)
        def cardEventCreate3 = new CardEventCreate(title:"Card #3",caseNumber:3,description:"The third card originally from Second phase on the first position",phaseDomainId:phase2.domainId,user:user1)

        cardEventCreate1.beforeInsert()
        cardEventCreate1.save()
        cardEventCreate1.process()

        cardEventCreate2.beforeInsert()
        cardEventCreate2.save()
        cardEventCreate2.process()

        cardEventCreate3.beforeInsert()
        cardEventCreate3.save()
        cardEventCreate3.process()

        card1onPhase1 = cardEventCreate1.card
        card2onPhase1 = cardEventCreate2.card
        card3onPhase2 = cardEventCreate3.card

        // Assertions to validate the mock setup

        board.phases.each {
            println it
            it.cards.each {
                println "   $it"
            }
        }

        assertEquals 1, board.id
        assertEquals 3, board.phases.size()
        assertEquals 1, phase1.id
        assertEquals 2, phase1.cards.size()
        assertEquals 2, phase2.id
        assertEquals 1, phase2.cards.size()
        assertEquals 3, phase3.id
        assertEquals 1, card1onPhase1.id
        assertEquals 2, card2onPhase1.id
        assertEquals 3, card3onPhase2.id

        mockDomain(PhaseEventMove)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testLegalMoveOfFirstPhaseToNextIndex() {

        assertEquals 0, phase1.board.phases.indexOf(phase1)
        
        def updateEvent = new PhaseEventMove( phase: phase1, newPhaseIndex: 2 )

        updateEvent.validate()
        updateEvent.beforeInsert()
        updateEvent.save()
        updateEvent.process()

        assertEquals card1onPhase1.domainId, updateEvent.domainId
        assertEquals newTitle, card1onPhase1.title
        assertNull "There should be no description", card1onPhase1.description

    }
}
