package se.qbranch.qanban

import grails.test.*

class BoardTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testHasPhases() {
        mockDomain(Board)
        def b = new Board().save()
        assertEquals 0, b.phases.size()
    }
}
