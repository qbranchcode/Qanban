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

    void testSave() {
        def c = new Card(title: "Title", caseNumber: 1, description: "Desc")
        c.save()
    }
}
