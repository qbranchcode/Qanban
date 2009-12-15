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

class CardControllerTests extends ControllerUnitTestCase {

  def eventServiceMock
  def securityServiceMock
  def ruleServiceMock

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

    mockDomain(User,[user1,user2])


    // Board mock

    board = new Board()
    mockDomain(Board,[board])


    // Phase / PhaseEventCreate mock

    mockDomain(PhaseEventCreate)
    mockDomain(Phase)

    def phaseEventCreate1 = new PhaseEventCreate(title: "First phase", cardLimit: 5, phasePos: 0, user: user1, board: board)
    def phaseEventCreate2 = new PhaseEventCreate(title: "Second phase", cardLimit: 10, phasePos: 1, user: user1 , board: board)
    def phaseEventCreate3 = new PhaseEventCreate(title: "Third phase", user: user1, phasePos: 2, board: board)

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

    def cardEventCreate1 = new CardEventCreate(title:"Card #1",caseNumber:1,description:"The first card originally from First phase on the first position",phaseDomainId:phase1.domainId,user:user1)
    def cardEventCreate2 = new CardEventCreate(title:"Card #2",caseNumber:2,description:"The second card originally from First phase on the second position",phaseDomainId:phase1.domainId,user:user1)
    def cardEventCreate3 = new CardEventCreate(title:"Card #3",caseNumber:3,description:"The third card originally from Second phase on the first position",phaseDomainId:phase2.domainId,user:user1)

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

    mockDomain(Event)
    mockDomain(CardEventSetAssignee)
    mockDomain(CardEventMove)    
    securityServiceMock = mockFor(SecurityService)
    securityServiceMock.demand.static.getLoggedInUser() { -> return user1 }
    securityServiceMock.demand.static.isUserAdmin() {
      return false
    }
    controller.securityService = securityServiceMock.createMock()

    eventServiceMock = mockFor(EventService)
    eventServiceMock.demand.static.persist() { event ->
      event.beforeInsert()
      if( event.save() ){
        event.process()
      }
    }
    controller.eventService = eventServiceMock.createMock()

    ruleServiceMock = mockFor(RuleService)
    ruleServiceMock.demand.static.isMoveLegal() { oldPhase, newPhase ->
      def board = newPhase.board
      def oldPhaseIndex = board.phases.indexOf(oldPhase)
      def newPhaseIndex = board.phases.indexOf(newPhase)

      if( oldPhaseIndex + 1 == newPhaseIndex || oldPhaseIndex == newPhaseIndex ){
        return true
      }else{
        return false
      }
    }
    controller.ruleService = ruleServiceMock.createMock()
    
    mockForConstraintsTests(SetAssigneeCommand)
    mockForConstraintsTests(MoveCardCommand)
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testCardShouldNotSaveWithoutBoardId(){

    mockParams.title = "Title"
    mockParams.caseNumber =  "1"
    mockParams.description = "My testcard"

    def model = controller.create()
    assertEquals 400, renderArgs.status

  }

  void testCardShouldSave() {

    mockParams.title = "Title"
    mockParams.caseNumber =  "1"
    mockParams.description = "My testcard"
    mockParams.boardId = "1"

    def model = controller.create()

    def card = Card.findByTitle("Title")

    assertEquals "My testcard" , card.description
    assertEquals "First phase", card.phase.title
    assertEquals "Title", Phase.get(1).cards[2].title
    assertEquals card.caseNumber, CardEventCreate.findByDomainId(card.domainId).caseNumber
  }

  void testShowCard() {
    mockParams.id = "1"
    controller.show()
    assertEquals 'card', renderArgs.template
    assertEquals 1, renderArgs.bean.id

  }

  void testShowWithoutId() {
    def model = controller.show()
    assertEquals 400, renderArgs.status
  }

  void testShowWithInvalidId() {
    mockParams.id = "6578921312"
    controller.show()
    assertEquals 404, renderArgs.status
  }

  void testFormWithCardId(){
    mockParams.id = "1"
    controller.form()
    assertEquals Card.get(1).title, renderArgs.model.updateEvent.title

  }

  void testFormWithoutIdAndWithBoardId(){
    mockParams.'board.id' = '1'
    def model = controller.form()
    assertEquals 1, renderArgs.model.boardInstance.id
  }

  void testFormWithoutIdAndWithoutBoardId(){
    controller.form()
    assertEquals 400, renderArgs.status
  }

  void testFormWithInvalidId() {
    mockParams.id = "6578921312"
    controller.form()
    assertEquals 404, renderArgs.status
  }

  void testFormWithMoveParams(){
    mockParams.id = "1"
    mockParams.newPhase = "2"
    mockParams.newPos = "0"

    controller.form()

    assertEquals Card.get(1).title, renderArgs.model.moveEvent.card.title
    assertEquals '2', renderArgs.model.newPhase
    assertEquals '0', renderArgs.model.newPos
    
  }

  /*
    Controller actions that needs both params and command object doesn't seem to be testable in unit test?
  void testValidUpdate(){

    mockParams.id = "1"
    mockParams.title = "New Title"

    def cmd = new SetAssigneeCommand(id: "1", assigneeId: "2")
    cmd.validate()
    controller.update(cmd)

    assertEquals "New Title", renderArgs.model.updateEvent.card.title

  }
  */

  /*
    Mock failure:
    No more calls to 'getLoggedInUser' expected at this point. End of demands.
    
  void testMove(){
    def id = "1"
    def cmd1 = new MoveCardCommand(id: id, newPos: '0', newPhase: '2')
    cmd1.validate()
    def cmd2 = new SetAssigneeCommand(id: id, assigneeIs: '2')
    cmd2.validate()
    controller.move(cmd1,cmd2)
    assertEquals Phase.get(2), renderArgs.model.moveEvent.card.phase 

  }
  */

  void testSort(){
    def cmd = new MoveCardCommand(id: '1', newPos: '0', newPhase: '2')
    controller.sort(cmd)
    assert 200, renderArgs.status
  }

  void testInvalidSort(){

    def cmd = new MoveCardCommand(id: '3', newPos: '0', newPhase: '1')
    controller.sort(cmd)
    assert 400, renderArgs.status
    // The mock assumes that the user ain't admin, otherwise thit move would be valid
  }

  void testDelete(){
    mockParams.id = "1"
    assertNotNull "Card with id 1 should exist", Card.get(1)
    controller.delete()
    assertNull "Card with id 1 should not exist", Card.get(1) 
  }

}
