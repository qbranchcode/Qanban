package se.qbranch.qanban

import grails.test.*
import grails.converters.*

class MainViewControllerTests extends ControllerUnitTestCase {


    void testMoveCardSuccessfull() {
        controller.moveCard(new MoveCardCommand(id: 1, moveToCardsIndex: 0, moveToPhase: 2))
        def firstPhase = Phase.get(1)
        def secondPhase = Phase.get(2)
        def response = JSON.parse(controller.response.contentAsString)
        assertTrue "Expected move to return true", response.result
        assertEquals 1, secondPhase.cards.size()
        assertEquals 1, firstPhase.cards.size()
    }

    void testMoveCardInPhaseAndToNewPhase() {
        controller.moveCard(new MoveCardCommand(id: 1, moveToCardsIndex: 0, moveToPhase: 2))
        def secondPhase = Phase.get(2)
        def response = JSON.parse(controller.response.contentAsString)
        assertTrue "Expected move to return true", response.result
        assertEquals 1, secondPhase.cards.size()
    }

    void testMoveCardInFullPhase() {
        controller.moveCard(new MoveCardCommand(id: 2, moveToCardsIndex: 0, moveToPhase: 1))
        def response = JSON.parse(controller.response.contentAsString)
        assertTrue "Expected move to return true", response.result
    }
}
