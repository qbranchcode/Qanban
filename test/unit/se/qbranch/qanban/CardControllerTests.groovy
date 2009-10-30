package se.qbranch.qanban

import grails.test.*

class CardControllerTests extends ControllerUnitTestCase {

    protected void setUp() {
        super.setUp()
        mockDomain(Phase)
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
        assertTrue card.hasErrors()
        assertEquals "nullable", card.errors.phase
    }

//    void testSaveWithFilledFormAndPhaseParams()


    void testSaveWithEmptyForm() {

        controller.save();

        assertEquals 'create', renderArgs.view
        
    }
}
