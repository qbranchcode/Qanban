package se.qbranch.qanban

import grails.test.*

class PhaseTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreatePhase() {
        mockDomain(Phase)

        def phase = new Phase(title: "testPhase")
        assertEquals "testPhase", phase.title
        
        def notAllowedTitlePhase = new Phase(title: "")
        assertFalse 'validate should have failed', notAllowedTitlePhase.validate()

    }

    void testPhaseWithCard() {
        mockDomain(Card)
        mockDomain(Phase)
        Card card = new Card(description: "myCard")
        Phase phase = new Phase(title: "myPhase")

        phase.addToCards(card)

        assertEquals 1, phase.cards.size()

    }
}
