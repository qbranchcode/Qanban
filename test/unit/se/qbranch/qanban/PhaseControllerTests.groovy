package se.qbranch.qanban

import grails.test.*

class PhaseControllerTests extends ControllerUnitTestCase {
    
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testList() {
        mockDomain(Phase, [ new Phase(name: "testPhase") ])

        def model = controller.list()

        assertEquals 1, model.phaseInstanceTotal
    }


    void testSave() {
        mockDomain(Phase)
        mockDomain(Board)
        mockParams.name = "myPhase"
        mockParams.board = new Board()

        controller.save()

        assertEquals 1, redirectArgs.id
        assertEquals controller.show, redirectArgs.action
    }

}
