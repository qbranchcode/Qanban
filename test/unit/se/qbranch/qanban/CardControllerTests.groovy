package se.qbranch.qanban

import grails.test.*

class CardControllerTests extends ControllerUnitTestCase {
   
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSave() {
        mockDomain(Card)

        def model = controller.save()

    }
}
