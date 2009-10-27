package se.qbranch.qanban

import grails.test.*

class CardControllerTests extends ControllerUnitTestCase {
   
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testList() {
        mockDomain(Card, [ new Card(description: "Mattis card"),
                new Card(description: "PGs card")] )

        def model = controller.list()
        assertEquals 2, model.cardInstanceTotal

    }
}
