package se.qbranch.qanban

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class UserEventUpdateTests extends GrailsUnitTestCase {

  def authMock

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

    user1 = new User(username: "opsmrkr01", userRealName: "Mr. Krister", passwd: 'asd')
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
    def phaseEventCreate3 = new PhaseEventCreate(title: "Third phase", cardLimit: 0, user: user1, phasePos: 2, board: board)

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

    board.phases.each {
      println it
      it.cards.each {
        println "   $it"
      }
    }

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

    mockDomain(UserEventUpdate)

    authMock = mockFor(AuthenticateService)
    authMock.demand.static.encodePassword(1..10) { pass -> pass }


  }

  protected void tearDown() {
    super.tearDown()
  }

  void testUpdateAUserWithoutCorrectPassword() {

    def updateEvent = new UserEventUpdate(user:user1)

    // Extra mocking for the updateEvent obj.
    updateEvent.authenticateService = authMock.createMock()

    updateEvent.populateFromUser()

    assertNull "the password should not have been transfered from the user object", updateEvent.passwd
    
    updateEvent.email = 'apa@apa.se'
    updateEvent.passwd = 'asdd'

    assertEquals user1.username, updateEvent.username
    assertEquals user1.userRealName, updateEvent.userRealName
    assertFalse "The validation should fail", updateEvent.validate();
    assertEquals "userEventUpdate.authentication.password.missmatch", updateEvent.errors['passwd']
    
  }

  void testUpdateAUserWithoutEnteringCurrentPassword(){

    def updateEvent = new UserEventUpdate(user:user1)

    // Extra mocking for the updateEvent obj.
    updateEvent.authenticateService = authMock.createMock()

    updateEvent.populateFromUser()

    assertNull "the password should not have been transfered from the user object", updateEvent.passwd

    updateEvent.email = 'apa@apa.se'

    assertEquals user1.username, updateEvent.username
    assertEquals user1.userRealName, updateEvent.userRealName
    assertFalse "The validation should fail", updateEvent.validate();
    assertEquals "nullable", updateEvent.errors['passwd']


  }

  void testUpdateAUserWithNewPasswordInconsistency(){

    def updateEvent = new UserEventUpdate(user:user1)

    // Extra mocking for the updateEvent obj.
    updateEvent.authenticateService = authMock.createMock()

    updateEvent.populateFromUser()
    updateEvent.email = 'apa@apa.se'
    updateEvent.passwd = 'asd'
    updateEvent.newPasswd = '123'
    updateEvent.newPasswdRepeat = '321'

    assertFalse "The validation should fail", updateEvent.validate()
    assertEquals 'userEventUpdate.newPassword.missmatch', updateEvent.errors['passwd'] 

  }

  void testValidUpdateEvent(){

    def updateEvent = new UserEventUpdate(user:user1)
    def userId = user1.id

    // Extra mocking for the updateEvent obj.
    updateEvent.authenticateService = authMock.createMock()

    updateEvent.populateFromUser()
    updateEvent.email = 'apa@apa.se'
    updateEvent.passwd = 'asd'
    updateEvent.newPasswd = '123'
    updateEvent.newPasswdRepeat = '123'

    updateEvent.beforeInsert()
    updateEvent.save()
    updateEvent.process()

    def updatedUser = User.get(userId)

    assertEquals 'apa@apa.se', updatedUser.email
    assertEquals '123', updatedUser.passwd
    
  }
}