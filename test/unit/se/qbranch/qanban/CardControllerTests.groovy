package se.qbranch.qanban

import grails.test.*

class CardControllerTests extends ControllerUnitTestCase {

    protected void setUp() {
        super.setUp()
        mockDomain(Phase, [ new Phase(name:'Phasendeluxe') ])
        mockDomain(Card)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCardShouldNotSaveWithoutPhase() {
        mockParams.title = "Title"
        mockParams.caseNumber = "1"
        mockParams.description = "My testcard"

        def model = controller.save()

        def card = model.cardInstance

        assertEquals 'create', renderArgs.view
        assertTrue 'Should have had errors', card.hasErrors()
        assertEquals "nullable", card.errors.phase
    }

    void testCardShouldSaveWithPhase() {
        mockParams.title = "Title"
        mockParams.caseNumber = "1"
        mockParams.description = "My testcard"
        mockParams."phase.id" = "1"

        def model = controller.save()
        def card = Card.findByTitle("Title")

        assertEquals "My testcard" ,card.description
        assertEquals "Phasendeluxe", card.phase.name
        assertEquals "Title", Phase.get(1).cards[0].title

       // assertEquals controller.show, redirect

    }


    void testSaveWithEmptyForm() {

        controller.save();

        assertEquals 'create', renderArgs.view
        
    }
}
