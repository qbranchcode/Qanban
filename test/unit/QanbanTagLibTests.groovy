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

import grails.test.*
import se.qbranch.qanban.*

class QanbanTagLibTests extends TagLibUnitTestCase {

    def card1inPhase1
    def card2inPhase1
    def card1inPhase2

    def phase1
    def phase2
    def phase3

    protected void setUp() {
        
        super.setUp()

        card1inPhase1 = new Card(title: "c1", caseNumber: 1)
        card2inPhase1 = new Card(title: "c2", caseNumber: 2)
        card1inPhase2 = new Card(title: "c3", caseNumber: 3)

        mockDomain(Card, [card1inPhase1,card2inPhase1,card1inPhase2])

        phase1 = new Phase(title: "p1", cardLimit: 3)
        phase2 = new Phase(title: "p1", cardLimit: 6)
        phase3 = new Phase(title: "p1", cardLimit: 1)

        mockDomain(Phase, [phase1,phase2,phase3])

        phase1.addToCards(card1inPhase1).addToCards(card2inPhase1)
        phase2.addToCards(card1inPhase2)
        
    }

    protected void tearDown() {
        super.tearDown()
    }


    void testMaxCardCount() {
        def phases = Phase.list()

        tagLib.maxCardCount(phases: phases)

        assertEquals '2', tagLib.out.toString()

    }

    void testMaxCardCountWithCardHeight() {
        def phases = Phase.list()

        tagLib.maxCardCount(phases: phases, cardHeight: '10')

        assertEquals '20', tagLib.out.toString()

    }

    void testMaxCardCountWithCardHeightAndUnit() {
        def phases = Phase.list()

        tagLib.maxCardCount(phases: phases, cardHeight: '10', unit: 'px')

        assertEquals '20px', tagLib.out.toString()

    }


}
