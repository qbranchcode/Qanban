package se.qbranch.qanban

import grails.test.*

class PhaseControllerTestsTests extends ControllerUnitTestCase {
    
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

}
