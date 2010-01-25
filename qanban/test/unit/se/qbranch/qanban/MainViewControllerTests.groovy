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

import grails.test.ControllerUnitTestCase

class MainViewControllerTests extends ControllerUnitTestCase {

  def securityServiceMock

  def user1
  def user2

  def board

  def phase1
  def phase2
  def phase3

  def card1onPhase1
  def card2onPhase1
  def card3onPhase2


  protected void setUp() {
    super.setUp()

    // User mock

    user1 = new User(username: "opsmrkr01", userRealName: "Mr. Krister")
    user2 = new User(username: "opsshba01", userRealName: "Shean Banan")

    mockDomain(UserEventCreate)
    mockDomain(User,[user1,user2])


    // Board mock

    board = new Board()
    mockDomain(Board,[board])

    mockDomain(Role)


    // Phase / PhaseEventCreate mock

    mockDomain(PhaseEventCreate)
    mockDomain(Phase)

    def phaseEventCreate1 = new PhaseEventCreate(title: "First phase", cardLimit: 5, phasePos: 0, eventCreator: user1, board: board)
    def phaseEventCreate2 = new PhaseEventCreate(title: "Second phase", cardLimit: 10, phasePos: 1, eventCreator: user1 , board: board)
    def phaseEventCreate3 = new PhaseEventCreate(title: "Third phase", cardLimit: 0,eventCreator: user1, phasePos: 2, board: board)

    phaseEventCreate1.beforeInsert()
    phaseEventCreate1.save()
    phaseEventCreate1.process()

    phaseEventCreate2.beforeInsert()
    phaseEventCreate2.save()
    phaseEventCreate2.process()

    phaseEventCreate3.beforeInsert()
    phaseEventCreate3.save()
    phaseEventCreate3.process()

    phase1 = phaseEventCreate1.phase
    phase2 = phaseEventCreate2.phase
    phase3 = phaseEventCreate3.phase

    assertEquals phase1, Phase.findByDomainId(phase1.domainId)

    // Card / CardEventCreate mock

    mockDomain(CardEventCreate)
    mockDomain(Card)

    def cardEventCreate1 = new CardEventCreate(title:"Card #1",caseNumber:1,description:"The first card originally from First phase on the first position",phaseDomainId:phase1.domainId,eventCreator:user1)
    def cardEventCreate2 = new CardEventCreate(title:"Card #2",caseNumber:2,description:"The second card originally from First phase on the second position",phaseDomainId:phase1.domainId,eventCreator:user1)
    def cardEventCreate3 = new CardEventCreate(title:"Card #3",caseNumber:3,description:"The third card originally from Second phase on the first position",phaseDomainId:phase2.domainId,eventCreator:user1)

    cardEventCreate1.beforeInsert()
    cardEventCreate1.save()
    cardEventCreate1.process()

    cardEventCreate2.beforeInsert()
    cardEventCreate2.save()
    cardEventCreate2.process()

    cardEventCreate3.beforeInsert()
    cardEventCreate3.save()
    cardEventCreate3.process()

    card1onPhase1 = cardEventCreate1.card
    card2onPhase1 = cardEventCreate2.card
    card3onPhase2 = cardEventCreate3.card

    // Assertions to validate the mock setup


    assertEquals 1, board.id
    assertEquals 3, board.phases.size()
    assertEquals 1, phase1.id
    assertEquals 2, phase1.cards.size()
    assertEquals 2, phase2.id
    assertEquals 1, phase2.cards.size()
    assertEquals 3, phase3.id
    assertEquals 1, card1onPhase1.id
    assertEquals 2, card2onPhase1.id
    assertEquals 3, card3onPhase2.id

    mockDomain(PhaseEventUpdate)
    mockDomain(PhaseEventDelete)
    mockDomain(PhaseEventMove)

    securityServiceMock = mockFor(SecurityService)
    securityServiceMock.demand.getLoggedInUser(1..2) { -> return user1 }
    controller.securityService = securityServiceMock.createMock()

    mockForConstraintsTests(MovePhaseCommand)
    mockForConstraintsTests(DeletePhaseCommand)
    mockDomain(Event,[phaseEventCreate1,phaseEventCreate2,phaseEventCreate3,cardEventCreate1,cardEventCreate2,cardEventCreate3])
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testShowArchive() {
    mockParams."board.id" = "1"

    MainViewController.metaClass.sortArchiveCards = { phases ->
      def phase = phases[-1]
      def list = Card.findAllByPhase(phase)
      list.sort{ card ->
        card.lastUpdated
      }
      return list
    }
    controller.showArchive()
    assertEquals "/archive/archive", renderArgs.template
    assertEquals 0, renderArgs.model.archiveCardTotal
    assertEquals 0, renderArgs.model.archiveCardList.size()

    def cardEventCreate4 = new CardEventCreate(title:"Card #4",caseNumber:4,description:"An archived card",phaseDomainId:phase3.domainId,eventCreator:user1)

    cardEventCreate4.beforeInsert()
    cardEventCreate4.save()
    cardEventCreate4.process()

    controller.showArchive()
    assertEquals "/archive/archive", renderArgs.template
    assertEquals 1, renderArgs.model.archiveCardTotal
    assertEquals 1, renderArgs.model.archiveCardList.size()
  }

  void testShowArchiveBody() {
    mockParams."board.id" = "1"

    MainViewController.metaClass.sortArchiveCards = { phases ->
      def phase = phases[-1]
      def list = Card.findAllByPhase(phase)
      list.sort{ card ->
        card.lastUpdated
      }
      return list
    }
    controller.showArchiveBody()
    assertEquals "/archive/archiveBody", renderArgs.template
    assertEquals 0, renderArgs.model.archiveCardList.size()

    def cardEventCreate4 = new CardEventCreate(title:"Card #4",caseNumber:4,description:"An archived card",phaseDomainId:phase3.domainId,eventCreator:user1)

    cardEventCreate4.beforeInsert()
    cardEventCreate4.save()
    cardEventCreate4.process()

    controller.showArchiveBody()
    assertEquals "/archive/archiveBody", renderArgs.template
    assertEquals 1, renderArgs.model.archiveCardList.size()
  }

  void testShowLog() {
    mockParams."board.id" = "1"

    controller.showLog()
    assertEquals 6, renderArgs.model.eventInstanceTotal
    assertEquals 6, renderArgs.model.eventInstanceList.size()
  }

  void testShowLogBody() {
    mockParams."board.id" = "1"

    controller.showLogBody()
    assertEquals 6, renderArgs.model.eventInstanceList.size()
  }

  void testShowBoard() {
    mockParams."board.id" = "1"

    controller.showBoard()
    assertEquals "/board/board", renderArgs.template
    assertEquals 1, renderArgs.bean.id
  }

  void testShowSettings() {
    controller.showSettings()
    assertEquals "/settings/showSettings", renderArgs.template
    assertEquals 2, renderArgs.model.users.size()
  }
}