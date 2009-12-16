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

class PhaseControllerTests extends ControllerUnitTestCase {

  def eventServiceMock
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

    mockDomain(User,[user1,user2])


    // Board mock

    board = new Board()
    mockDomain(Board,[board])


    // Phase / PhaseEventCreate mock

    mockDomain(PhaseEventCreate)
    mockDomain(Phase)

    def phaseEventCreate1 = new PhaseEventCreate(title: "First phase", cardLimit: 5, phasePos: 0, user: user1, board: board)
    def phaseEventCreate2 = new PhaseEventCreate(title: "Second phase", cardLimit: 10, phasePos: 1, user: user1 , board: board)
    def phaseEventCreate3 = new PhaseEventCreate(title: "Third phase", cardLimit: 0,user: user1, phasePos: 2, board: board)

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

    mockDomain(PhaseEventUpdate)
    mockDomain(PhaseEventDelete)
    mockDomain(PhaseEventMove)
    
    securityServiceMock = mockFor(SecurityService)
    securityServiceMock.demand.getLoggedInUser(1..2) { -> return user1 }
    controller.securityService = securityServiceMock.createMock()

    eventServiceMock = mockFor(EventService)
    eventServiceMock.demand.persist(1..2) { event ->
      event.beforeInsert()
      if( event.save() ){
        event.process()
      }
    }
    controller.eventService = eventServiceMock.createMock()

    mockForConstraintsTests(UpdatePhaseCommand)
    mockForConstraintsTests(MovePhaseCommand)
    mockForConstraintsTests(DeletePhaseCommand)
  }

  protected void tearDown() {
    super.tearDown()
  }


  void testCreate() {

    mockParams.title = "myPhase"
    mockParams.'board.id' = board.id
    mockParams.phasePos = "3"
    assertEquals 3, Phase.list().size()

    def model = controller.create()

    assertEquals 4, Phase.list().size()
    assertEquals "myPhase", model.createEvent.phase.title
    assertEquals 0, model.createEvent.phase.cardLimit
  }

  void testCreateWithoutTitleAndPos() {
    mockParams.'board.id' = board.id
    def model = controller.create()
    assertEquals 2, model.createEvent.errors.getAllErrors().size()
  }

  void testCreateWithLimitParam(){
    mockParams.title = "myPhase"
    mockParams.'board.id' = board.id
    mockParams.phasePos = "3"
    mockParams.cardLimit = "5"
    assertEquals 3, Phase.list().size()

    def model = controller.create()

    assertEquals 4, Phase.list().size()
    assertEquals "myPhase", model.createEvent.phase.title
    assertEquals 5, model.createEvent.phase.cardLimit
  }

  void testShowWithoutId(){
    controller.show()
    assertEquals 400, renderArgs.status
  }

  void testShowWithInvalidId(){
    mockParams.id = "123123123"
    controller.show()
    assertEquals 404, renderArgs.status
  }

  void testShowWithValidId(){
    mockParams.id ="1"
    controller.show()
    assertEquals 'phase', renderArgs.template
    assertEquals Phase.get(1).title, renderArgs.model.phase.title
  }


  void testShowWithValidIdAndCardLimitations(){

    // This mock can probably be written better -
    // As is it orders the cards by when they are last updated instead of when it was moved to the phase.
    // In our case the cards ain't moved to phase 2 as they are created directly in to that phase in the SetUp(),
    // as a consequence there is a logic diff from the origal method.
    
    PhaseController.metaClass.getCardsLastMovedToPhase = { phase, maxNumberOfCards ->
        def list = Card.findAllByPhase(phase)
        list.sort{ card ->
          card.lastUpdated
        }
        if( list.size() > maxNumberOfCards ){
          list = list[-maxNumberOfCards..-1]
        }
    }

    mockParams.id = '1'
    mockParams.cardLimit = '1'
    controller.show()
    assertEquals 1, renderArgs.model.phase.cards.size()

  }

  void testFormWithValidId(){
    mockParams.id = '2'
    controller.form()
    assertEquals Phase.get(2).title, renderArgs.model.updateEvent.phase.title
    assertEquals Phase.get(2).title, renderArgs.model.updateEvent.title

  }

  void testFormWithInvalidId(){
    mockParams.id = "213"
    controller.form()
    assertEquals 404, renderArgs.status
  }

  void testFormInCreateMode() {
    mockParams.'board.id' = '1'
    controller.form()
    assertNotNull 'There should be a PhaseEventCreate in the model' , renderArgs.model.createEvent
  }

  void testFormWithoutAnyParams(){
    controller.form()
    assertEquals 400, renderArgs.status
  }


  void testValidUpdate() {

    assertEquals 0, board.phases.indexOf(Phase.get(1))

    def id = '1'
    mockParams.id = id
    def newTitle = "New Phase Title"
    mockParams.title = newTitle
    def newPos = 2
    mockParams.cardLimit = "1337"
    def cmd1 = new MovePhaseCommand(id: id, phasePos: newPos )
    cmd1.validate()

    controller.update(cmd1)

    assertEquals newPos, board.phases.indexOf(Phase.get(1))
    assertEquals newTitle, renderArgs.model.updateEvent.phase.title


  }

  void testValidDelete(){
    def cmd = new DeletePhaseCommand(id:'3')
    cmd.validate()
    cmd.errors.getAllErrors().each{
      println it
    }
    controller.delete(cmd)
    assertEquals 200, renderArgs.status
  }

  void testDeleteWithoutId(){
    def cmd = new DeletePhaseCommand()
    cmd.validate()
    controller.delete(cmd)
    assertEquals 400, renderArgs.status
  }

  void testDeleteWittInvalidId(){
    def cmd = new DeletePhaseCommand(id:'123')
    cmd.validate()
    controller.delete(cmd)
    assertEquals 400, renderArgs.status
  }
  

}
