package se.qbranch.qanban

import grails.test.*

class PhaseControllerTests extends ControllerUnitTestCase {
    
    protected void setUp() {
        super.setUp()
        mockDomain(Phase, [new Phase(name:'PhaseTest')])
        mockDomain(Board)
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
    
        mockParams.name = "myPhase"
        mockParams.board = new Board()

        controller.save()

        assertEquals 2, redirectArgs.id
        assertEquals controller.show, redirectArgs.action
    }

    void testSuccessfulAjaxSaveOrUpdateWithId() {
        mockParams.id = "1"
        mockParams.cardLimit = "2"

        controller.ajaxSaveOrUpdate()

        assertEquals 2, renderArgs.model.phaseInstance.cardLimit
        assertEquals 1, renderArgs.model.phaseInstance.id

    }

    void testSuccessfulAjaxSaveOrUpdateWithoutId() {
        
        mockParams.name = "New Phase"
        mockParams.board = new Board()

        controller.ajaxSaveOrUpdate()
          
        assertEquals 2, renderArgs.model.phaseInstance.id
        assertEquals "New Phase", renderArgs.model.phaseInstance.name

    }

    void testAjaxSaveOrUpdateWithIllegalId(){
        mockParams.id = "12"
        mockParams.cardLimit = "2"

        controller.ajaxSaveOrUpdate()
      
        assertEquals null, renderArgs.model.phaseInstance

    }


}
