package se.qbranch.qanban

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class UserEventDeleteTests extends GrailsUnitTestCase {

  def user1
  def user2
  def role

  protected void setUp() {
    super.setUp()

    //Role mock
    role = new Role(description: "Qanban Administrator Role", authority: "ROLE_QANBANADMIN")
    mockDomain(Role,[role])

    // User mock
    user1 = new User(username: "opsmrkr01", userRealName: "Mr. Krister", authorities: [role])
    user2 = new User(username: "opsshba01", userRealName: "Shean Banan")

    mockDomain(User,[user1,user2])
    mockDomain(UserEventDelete)

  }

  protected void tearDown() {
    super.tearDown()
  }

  void testDeleteUser() {
    assertEquals 2, User.list().size()

    def deleteEvent = new UserEventDelete(deletedUser: user1, user: user2)

    deleteEvent.validate()
    deleteEvent.beforeInsert()
    deleteEvent.save()
    deleteEvent.process()

    assertFalse "There should not be any errors", deleteEvent.hasErrors()
    assertEquals 1, User.list().size()
    assertEquals "Shean Banan", deleteEvent.user.userRealName
    assertEquals "Mr. Krister", deleteEvent.deletedUser.userRealName
  }

}
