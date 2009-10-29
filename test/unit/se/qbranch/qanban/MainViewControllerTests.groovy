package se.qbranch.qanban

import grails.test.*
import grails.converters.*

class MainViewControllerTests extends ControllerUnitTestCase {

    def b

    protected void setUp() {
        super.setUp()
         mockDomain(Card, [ new Card(title: "TestCard",
                                    description: "This is a description",
                                    caseNumber: 1,
                                    assignee: "Mattias"),

                           new Card(title: "OtherCard",
                                    description: "This is the other card",
                                    caseNumber: 2,
                                    assignee: "PG"),
                           new Card(title: "Card three",
                                    description: "This is the third card",
                                    caseNumber: 5,
                                    assignee: "xls")])
        mockDomain(Board)
        mockDomain(Phase)

        b = new Board().addToPhases(new Phase(name: "test")).save()
        for(card in Card.list()) {
            def phase = b.phases[0]
            phase.cards.add(card)
            card.phase = phase
            phase.save()
            card.save()
        }
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testMoveCardSuccess() {

        // Ta card med id 3 och flytta från pos 2 till pos 0
        // om det funkar får man true och domänmodellen ser annorlunda ut, verifiera

        // Making request to moveCard
        mockParams.id = 3
        mockParams.moveTo = 0
        controller.moveCard()
        def response = JSON.parse(controller.response.contentAsString)
        assertTrue "Expected move to return true", response.result

        assertEquals 3, b.phases[0].cards[0].id
        assertEquals 3, b.phases[0].cards.size()

        
    }

    void testMoveCardToIllegalPosition() {
        mockParams.id = 3
        mockParams.moveTo = 3
        controller.moveCard()
        def response = JSON.parse(controller.response.contentAsString)
        assertFalse "Expected move to return false", response.result

    }

    void testNotSettingMoveTo() {
        mockParams.id = 3
        controller.moveCard()
        def response = JSON.parse(controller.response.contentAsString)
        assertFalse "Expected move to return false", response.result
    }
}
