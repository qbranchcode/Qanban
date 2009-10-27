package se.qbranch.qanban

import grails.test.*

class CardTests extends GrailsUnitTestCase {
    
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateCard() {
        def card = new Card(description: "testcard")

        assertEquals "testcard", card.description
    }


}
