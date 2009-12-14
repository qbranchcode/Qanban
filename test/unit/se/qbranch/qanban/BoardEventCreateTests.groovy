package se.qbranch.qanban

import grails.test.*

class BoardEventCreateTests extends GrailsUnitTestCase {

  def user

  protected void setUp() {
    super.setUp()

    // User mock
    user = new User(username: "opsmrkr01", userRealName: "Mr. Krister")
    mockDomain(User,[user])
    mockDomain(BoardEventCreate)
    mockDomain(Board)
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testCreatingABoard() {

    def event = new BoardEventCreate();


    event.title = "The Board"
    event.user = user

    event.beforeInsert()
    event.save()
    event.process()

    event.errors.getAllErrors().each { println it}
    println event.domainId

    assertFalse 'The event should not have errors' ,event.hasErrors()
    assertEquals 1, event.id
    assertEquals event.title, Board.findByDomainId(event.domainId).title
  }
}
