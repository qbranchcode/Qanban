package se.qbranch.qanban

import grails.test.*

class UserEventCreateTests extends GrailsUnitTestCase {
  protected void setUp() {
    super.setUp()
    mockDomain(User)
    mockDomain(UserEventCreate)
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testCreatingAUser() {
    def username = "opsmrkr01"
    def userRealname = "mr. Krister"
    def email = "mr.krister@mail.com"

    def event = new UserEventCreate(username: username, userRealname: userRealname, email: email)

    event.beforeInsert()
    event.save()
    event.process()

    def user = event.user
    assertEquals event.username, user.username
    assertEquals event.domainId, user.domainId
    
  }
}
