package se.qbranch.qanban

import grails.test.*

class CardEventMoveTests extends GrailsUnitTestCase {


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
        assertEquals 3, board.phases.size()
        assertEquals 1, phase1.id
        assertEquals 2, phase1.cards.size()
        assertEquals 2, phase2.id
        assertEquals 1, phase2.cards.size()
        assertEquals 3, phase3.id
        assertEquals 1, card1onPhase1.id
        assertEquals 2, card2onPhase1.id
        assertEquals 3, card3onPhase2.id

        // Specific mockups

        mockDomain(CardEventMove)


    
  }


    protected void tearDown() {
        super.tearDown()
    }


    void testValidMoveCardEventWithinAPhase() {

        assertEquals 1, phase1.cards.indexOf(card2onPhase1)
        assertEquals 0, board.phases.indexOf(card2onPhase1.phase)

        def cardEventMove = new CardEventMove( card: card2onPhase1, newPhase: phase1, newCardIndex: 0)

        cardEventMove.beforeInsert()
        cardEventMove.save()
        cardEventMove.afterInsert()

        assertEquals 0, card2onPhase1.phase.cards.indexOf(card2onPhase1)
        assertEquals 0, card2onPhase1.phase.board.phases.indexOf(card2onPhase1.phase)
        assertEquals cardEventMove.domainId, card2onPhase1.domainId
    }

    void testValidMoveCardEventBetweenPhases() {

        assertEquals 1, phase1.cards.indexOf(card2onPhase1)
        assertEquals 0, board.phases.indexOf(card2onPhase1.phase)

        def cardEventMove = new CardEventMove( card: card2onPhase1, newPhase: phase2, newCardIndex: 0)

        cardEventMove.beforeInsert()
        cardEventMove.save()
        cardEventMove.afterInsert()

        assertEquals 0, card2onPhase1.phase.cards.indexOf(card2onPhase1)
        assertEquals 1, card2onPhase1.phase.board.phases.indexOf(card2onPhase1.phase)
        assertEquals 2, phase2.cards.size()
        assertEquals cardEventMove.domainId, card2onPhase1.domainId

    }



  void testInvalidMoveCardEvent() {

    def card = Card.get(1)
    def newPhase = Phase.get(1)
    def newCardIndex = -1

    assertEquals 0, card.phase.cards.indexOf(card)
    assertEquals 0, card.phase.board.phases.indexOf(card.phase)

    def cardEventMove = new CardEventMove()
    cardEventMove.card = card;
    cardEventMove.newPhase = newPhase
    cardEventMove.newCardIndex = newCardIndex

    cardEventMove.save()

    assertEquals 0, card.phase.cards.indexOf(card)
    assertEquals 0, card.phase.board.phases.indexOf(card.phase)
    assertEquals null, cardEventMove.id

    for (error in cardEventMove.errors) {
      println error
    }

  }

  void testCompareOfCardEventMove() {
    def cardOld = new CardEventMove(newCardIndex: 0,
            card: Card.get(0),
            user: null,
            newPhase: null,
            dateCreated: new Date().previous())
    def cardNewer = new CardEventMove(newCardIndex: 0,
            card: Card.get(0),
            user: null,
            newPhase: null,
            dateCreated: new Date())
    assert cardOld.compareTo(cardNewer) > 0
    assert cardNewer.compareTo(cardOld) < 0
    assert cardNewer.compareTo(cardNewer) == 0

  }


}