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
