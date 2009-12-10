/*
 * Copyright 2009 Qbranch AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
