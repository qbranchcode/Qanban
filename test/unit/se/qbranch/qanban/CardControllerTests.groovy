package se.qbranch.qanban

import grails.test.*

class CardControllerTests extends ControllerUnitTestCase {

    protected void setUp() {
        super.setUp()
        mockDomain(Board, [ new Board() ])
        mockDomain(Phase, [ new Phase(name:'Phasendeluxe') ])
        mockDomain(Card, [ new Card(title:"CardTitle", caseNumber: 1, description: "Predef card") ])

        def c = Card.get(1)
        def p = Phase.get(1)
        def b = Board.get(1)
        p.addToCards(c).save()
        b.addToPhases(p).save()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCardShouldNotSaveWithoutPhase() {
        mockParams.title = "Title"
        mockParams.caseNumber = "1"
        mockParams.description = "My testcard"

        def model = controller.saveOrUpdate()

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

        def model = controller.saveOrUpdate()
        def card = Card.findByTitle("Title")

        assertEquals "My testcard" ,card.description
        assertEquals "Phasendeluxe", card.phase.name
        assertEquals "Title", Phase.get(1).cards[1].title

       // assertEquals controller.show, redirect

    }


    void testSaveWithEmptyForm() {

        controller.saveOrUpdate();

        assertEquals 'create', renderArgs.view
        
    }

    void testShowMethodWithoutId() {

        //mockParams.format = "xml"
        controller.show()
        assertEquals 400, renderArgs.status

    }

    void testShowMethodWithUnexistingId() {

        mockParams.id = "220"
        controller.show()
        assertEquals 404, renderArgs.status
    }

    void testDeleteWithoutId(){
        controller.delete()
        assertEquals 400, renderArgs.status
    }

    void testDeleteWithUnexistingId() {
        mockParams.id = "220"
        controller.delete()
        assertEquals 404, renderArgs.status
    }


    void testSaveOrUpdateWithoutId() {
        mockParams.title = "Title"
        mockParams.caseNumber = "1"
        mockParams.description = "My testcard"
        mockParams."phase.id" = "1"
        
        def model = controller.saveOrUpdate()
        
        def card = Card.findByTitle("Title")

        assertEquals "My testcard" ,card.description
        assertEquals "Phasendeluxe", card.phase.name
        assertEquals "Title", Phase.get(1).cards[1].title
        assertEquals 'view', redirectArgs.action
    }

    //TODO: How to test with diffrent mime-types?


}
