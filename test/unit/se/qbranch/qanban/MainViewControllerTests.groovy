package se.qbranch.qanban

import grails.test.*
import grails.converters.*

class MainViewControllerTests extends ControllerUnitTestCase {

    def board
    def firstPhase
    def secondPhase
    def topCardInFirstPhase
    def middleCardInFirstPhase
    def bottomCardInFirstPhase


    protected void setUp() {
        super.setUp()
        mockForConstraintsTests(MoveCardCommand)


        mockDomain(Card, [ new Card(title: "Card1",
                    description: "This is the first card",
                    caseNumber: 1),
                new Card(title: "Card2",
                    description: "This is the second card",
                    caseNumber: 2),
                new Card(title: "Card3",
                    description: "This is the third card",
                    caseNumber: 3)])
  
        mockDomain(Phase, [new Phase(name: "Phase1", cardLimit: 5),
                new Phase(name: "Phase2", cardLimit: 3)])

        mockDomain(Board, [new Board()])
        
        board = Board.get(1)
        firstPhase = Phase.findByName("Phase1")
        secondPhase = Phase.findByName("Phase2")
        topCardInFirstPhase = Card.findByTitle("Card1")
        middleCardInFirstPhase = Card.findByTitle("Card2")
        bottomCardInFirstPhase = Card.findByTitle("Card3")
        
        board
        .addToPhases(firstPhase)
        .addToPhases(secondPhase)
        .save()
        
        firstPhase
        .addToCards(topCardInFirstPhase)
        .addToCards(middleCardInFirstPhase)
        .addToCards(bottomCardInFirstPhase)
        .save()


        for(card in Card.list()) {
            card.phase = firstPhase
            card.phase.board = board
            card.save()
        }


    }

    void testSetup() {
        assertEquals 1,Board.list().size()
        assertEquals 2,Phase.list().size()
        assertEquals 3,Card.list().size()

    }

    protected void tearDown() {
        super.tearDown()
    }

    void testMoveCardSuccessfull() {
        def cmd = new MoveCardCommand(id: "1", moveToCardsIndex: "0", moveToPhase: "2")
        cmd.validate()
        controller.moveCard(cmd)
        def response = JSON.parse(controller.response.contentAsString)
        assertTrue "Expected move to return true", response.result
        assertEquals 1, secondPhase.cards.size()
        assertEquals 2, firstPhase.cards.size()
    }

    void testMoveCardToPhaseThatDoesntExists() {
        def cmd = new MoveCardCommand(id: "3", moveToCardsIndex: "0", moveToPhase: "7")
        cmd.validate()
        controller.moveCard(cmd)
        def response = JSON.parse(controller.response.contentAsString)
        assertFalse "Expected move to return false", response.result
    }

    void testMoveCardMoreThanOnePhase() {
        def cmd = new MoveCardCommand(id: "3", moveToCardsIndex: "0", moveToPhase: "3")
        cmd.validate()
        controller.moveCard(cmd)
        def response = JSON.parse(controller.response.contentAsString)
        assertFalse "Expected move to return false", response.result
    }

    void testNotSettingPhaseMovePhaseOrMovePosition() {
        def cmd = new MoveCardCommand(id: "3")
        cmd.validate()
        controller.moveCard(cmd)
        def response = JSON.parse(controller.response.contentAsString)
        assertFalse "Expected move to return false", response.result
    }

    void testMoveCardToFullPhase() {
        secondPhase.addToCards(new Card(title: "p1c1", description: "p1c1DESC", caseNumber: 9))
        secondPhase.addToCards(new Card(title: "p1c2", description: "p1c2DESC", caseNumber: 8))
        secondPhase.addToCards(new Card(title: "p1c3", description: "p1c3DESC", caseNumber: 7))
        secondPhase.save()
        
        def cmd = new MoveCardCommand(id: "3", moveToCardsIndex: "0", moveToPhase: "2")
        cmd.validate()
        controller.moveCard(cmd)
        
        assertEquals 3, secondPhase.cards.size()
        def response = JSON.parse(controller.response.contentAsString)
        assertFalse "Expected move to return false", response.result
    }

    void testMoveCardInPhaseAndToNewPhase() {
        def cmd = new MoveCardCommand(id: "3", moveToCardsIndex: "0", moveToPhase: "2")
        cmd.validate()
        controller.moveCard(cmd)
        
        def response = JSON.parse(controller.response.contentAsString)
        assertTrue "Expected move to return true", response.result
        assertEquals 1, secondPhase.cards.size()
    }

    void testMoveCardBackPhase() {
        def card = new Card(title: "p1c3", description: "p1c3DESC", caseNumber: 7)
        secondPhase.addToCards(card)
        secondPhase.save()
        card.phase = secondPhase
        card.save()

        def cmd = new MoveCardCommand(id: "4", moveToCardsIndex: "0", moveToPhase: "1")
        cmd.validate()
        controller.moveCard(cmd)

        def response = JSON.parse(controller.response.contentAsString)
        assertFalse "Expected move to return false", response.result
        assertEquals 3, firstPhase.cards.size()
    }

    void testMoveCardInFullPhase() {
        def cmd = new MoveCardCommand(id: "2", moveToCardsIndex: "0", moveToPhase: "1")
        cmd.validate()
        controller.moveCard(cmd)

        def response = JSON.parse(controller.response.contentAsString)
        assertTrue "Expected move to return true", response.result
    }
    
}
