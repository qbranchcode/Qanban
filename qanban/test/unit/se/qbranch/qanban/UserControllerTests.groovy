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
import org.grails.plugins.springsecurity.service.AuthenticateService

class UserControllerTests extends ControllerUnitTestCase {

  def eventServiceMock
  def securityServiceMock
  def authMock

  def user1
  def user2

  def board

  def phase1
  def phase2
  def phase3

  def userRole

  def card1onPhase1
  def card2onPhase1
  def card3onPhase2


  protected void setUp() {
    super.setUp()

    // User mock

    user1 = new User(username: "opsmrkr01", userRealName: "Mr. Krister", email: "mr@krister.se", passwd: "Password")
    user2 = new User(username: "opsshba01", userRealName: "Shean Banan", email: "shean@banan.se", passwd: "Password")

    mockDomain(User,[user1,user2])


    // Role mock

    userRole = new Role(description: "Qanban User Role", authority: "ROLE_QANBANUSER")

    mockDomain(Role,[userRole])

    // Board mock

    board = new Board()
    mockDomain(Board,[board])


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

    eventServiceMock = mockFor(EventService)
    eventServiceMock.demand.persist(1..2) { event ->
      event.beforeInsert()
      if( event.save() ){
        event.process()
      }
    }
    controller.eventService = eventServiceMock.createMock()


    securityServiceMock = mockFor(SecurityService)
    securityServiceMock.demand.getLoggedInUser(1..2) { -> return user1 }
    controller.securityService = securityServiceMock.createMock()

    authMock = mockFor(AuthenticateService)
    authMock.demand.static.encodePassword(1..2) { pass -> pass }

    mockDomain(UserEventDelete)


    mockDomain(UserEventCreate)
    UserEventCreate.metaClass.beforeInsert = {
      generateDomainId(username,userRealName,email)
      userDomainId = domainId // You create yourself
    }

    mockForConstraintsTests(UserUpdateCommand)
  }

  protected void tearDown() {
    super.tearDown()
  }


  void testCreateWithoutPassword() {
    def numberOfPreviusUsers = User.list().size()
    mockParams.username = "opsmrkr"
    mockParams.userRealName = "Mister Krister"
    mockParams.email = "mister.krister@gmail.com"
    mockParams.enabled = "true"

    controller.save()

    assertEquals numberOfPreviusUsers, User.list().size()
    assertEquals 1, renderArgs.model.person.errors.allErrors.size()
    assertNull "The user shouldnt have gotten an id from the database", renderArgs.model.person.id

  }

  void testCreateWithPassword(){
    def numberOfPreviusUsers = User.list().size()
    mockParams.username = "opsmrkr"
    mockParams.userRealName = "Mister Krister"
    mockParams.email = "mister.krister@gmail.com"
    mockParams.enabled = "true"
    mockParams.passwd = "p4ssW0rd"
    mockParams.passwdRepeat = "p4ssW0rd"

    controller.save()

    assertEquals numberOfPreviusUsers+1, User.list().size()
    assertNotNull "The user should have gotten an id from the database", renderArgs.model.person.id
  }

  void testCreateWithInconsistentPassword(){
    def numberOfPreviusUsers = User.list().size()
    mockParams.username = "opsmrkr"
    mockParams.userRealName = "Mister Krister"
    mockParams.email = "mister.krister@gmail.com"
    mockParams.enabled = "true"
    mockParams.passwd = "p4ssW0rd"
    mockParams.passwdRepeat = "passW0rd"

    controller.save()

    assertEquals numberOfPreviusUsers, User.list().size()
    assertNull "The user should have gotten an id from the database", renderArgs.model.person.id
  }

  void testCreateWithOnlyOnePasswordFieldFilled(){
    def numberOfPreviusUsers = User.list().size()
    mockParams.username = "opsmrkr"
    mockParams.userRealName = "Mister Krister"
    mockParams.email = "mister.krister@gmail.com"
    mockParams.enabled = "true"
    mockParams.passwd = "p4ssW0rd"

    controller.save()

    assertEquals numberOfPreviusUsers, User.list().size()
    assertNull "The user should have gotten an id from the database", renderArgs.model.person.id
  }

  void testDeleteUser() {
    def numberOfPreviousUsers = User.list().size()
    mockParams.id = 1
    controller.delete()

    assertEquals numberOfPreviousUsers-1, User.list().size()
  }
}
