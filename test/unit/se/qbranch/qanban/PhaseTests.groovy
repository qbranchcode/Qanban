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
    mockDomain(Board, [ new Board(domainId: "bdid")])
    mockDomain(Phase)
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testCreatePhase() {

    def phase = new Phase(title: "testPhase")
    assertEquals "testPhase", phase.title

    def notAllowedTitlePhase = new Phase(title: "")
    assertFalse 'validate should have failed', notAllowedTitlePhase.validate()

  }

  void testPhaseWithCard() {
    mockDomain(Card)

    Card card = new Card(description: "myCard")
    Phase phase = new Phase(title: "myPhase")

    phase.addToCards(card)

    assertEquals 1, phase.cards.size()

  }
  void testCreatingAPhaseWithLiteralCardLimit(){

    Phase phase = new Phase(title: "Title", domainId: "did", cardLimit: "qwe", board: Board.get(1))
    phase.validate()
    assertTrue "There should be errors", phase.hasErrors()
    phase.errors.getAllErrors().each{
      println it
    }

  }
}
