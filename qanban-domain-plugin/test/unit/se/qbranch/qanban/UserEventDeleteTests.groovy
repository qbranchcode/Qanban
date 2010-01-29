package se.qbranch.qanban

import grails.test.*

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
    user1 = new User(username: "opsmrkr01", userRealName: "Mr. Krister", authorities: [role], email: "mr@krister.se", passwd: "Password")
    user2 = new User(username: "opsshba01", userRealName: "Shean Banan", email: "shean@banan.se", passwd: "Password")

    mockDomain(User,[user1,user2])
    mockDomain(UserEventDelete)
    mockDomain(Card)

  }

  protected void tearDown() {
    super.tearDown()
  }

  void testDeleteUser() {
    assertEquals 2, User.list().size()

    def deleteEvent = new UserEventDelete(user: user1, eventCreator: user2)

    deleteEvent.populateFromUser()

    if(deleteEvent.validate())  {
      deleteEvent.beforeInsert()
      deleteEvent.save()
      deleteEvent.process()
    }

    println deleteEvent.errors

    assertFalse "There should not be any errors", deleteEvent.hasErrors()
    assertEquals 1, User.list().size()
    assertEquals "Shean Banan", deleteEvent.eventCreator.userRealName
    assertEquals "Mr. Krister", deleteEvent.user.userRealName
  }

}
