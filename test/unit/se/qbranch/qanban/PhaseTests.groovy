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

        def phase = new Phase(name: "testPhase")
        assertEquals "testPhase", phase.name
        
        def notAllowedNamePhase = new Phase(name: "")
        assertFalse 'validate should have failed', notAllowedNamePhase.validate()

    }

    void testPhaseWithCard() {
        mockDomain(Card)
        mockDomain(Phase)
        Card card = new Card(description: "myCard")
        Phase phase = new Phase(name: "myPhase")

        phase.addToCards(card)

        assertEquals 1, phase.cards.size()

    }
}
