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


import grails.converters.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured


class UserController {

  def authenticateService
  def sessionRegistry
  def eventService


  // the delete, save and update actions only accept POST requests
  static Map allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']


  // Create
  def save = {
    def user = new User()
    def createEvent

    user.properties = params
    createEvent = new UserEventCreate(user:user)
    createEvent.populateFromUser()


    eventService.persist(createEvent)

    if ( !createEvent.hasErrors()) {

      flash.message = "${user.username} is now created"
    }
    else {
      flash.message = null
      createEvent.user.errors = createEvent.errors
    }

    return render(template: '/login/register' , model: [ person : createEvent.user ])
  }

  // Retrieve
  @Secured(['IS_AUTHENTICATED_FULLY'])
  def show = {
    def person = User.get(params.id)
    if (!person) {
      flash.message = "User not found with id $params.id"
      redirect action: list
      return
    }
    List roleNames = Role.list()*.authority


    roleNames.sort { r1, r2 ->
      r1 <=> r2
    }

    render(template:'userForm', model: [person: person, roleNames: roleNames])
  }

  @Secured(['IS_AUTHENTICATED_FULLY'])
  def list = {
    if (!params.max) {
      params.max = 10
    }
    [personList: User.list(params)]
  }

  @Secured(['IS_AUTHENTICATED_FULLY'])
  def showOnlineUsers = {
    def users = sessionRegistry.getAllPrincipals()
    def onlineUsers = []

    for(user in users) {
      def userObject = User.findByUsername(user)
      onlineUsers.add(userObject)
    }

    render(template:'onlineUsers',model:[onlineUsers:onlineUsers])
  }

  @Secured(['IS_AUTHENTICATED_FULLY'])
  def passForm = {
    render(template:'passwordForm', model: [id:params.id])
  }




  /**
   * Person delete action. Before removing an existing person,
   * he should be removed from those authorities which he is involved.
   */
  @Secured(['IS_AUTHENTICATED_FULLY'])
  def delete = {

    def person = User.get(params.id)
    if (person) {
      def authPrincipal = authenticateService.principal()
      //avoid self-delete if the logged-in user is an admin
      if (!(authPrincipal instanceof String) && authPrincipal.username == person.username) {
        flash.message = "You can not delete yourself, please login as another admin and try again"
      }
      else {
        //first, delete this person from People_Authorities table.
        Role.findAll().each { it.removeFromPeople(person) }
        person.delete()
        flash.message = "User $params.id deleted."
      }
    }
    else {
      flash.message = "User not found with id $params.id"
    }

    redirect action: list
  }









  @Secured(['IS_AUTHENTICATED_FULLY'])
  def form = {
    if( !params.id )
        return renderFormCreateMode(params)

    def person = User.get(params.id)

    if( !person )
        return render(status: 404, text: "User with id $params.id not found")

    return renderFormEditMode(person)
    
  }

  private def renderFormCreateMode(params){
    return render( status:500, text: "Rendering form for creation not yet implemented")
  }

  private def renderFormEditMode(person){
      def updateEvent = new UserEventUpdate(user: person)
      updateEvent.populateFromUser()

      def roleNames = getUserRoles(person)

      return render(template:'userForm',model:[ event: updateEvent, roleNames: roleNames ])
  }

  private getUserRoles(user){
      def roleNames = user.authorities*.authority

      roleNames.sort { r1, r2 ->
        r1 <=> r2
      }
    
      return roleNames
  }
  /**
   * Person update action.
   */
  @Secured(['IS_AUTHENTICATED_FULLY'])
  def update = {
    def person = User.get(params.id)

    if( !person )
      return render(status: 404, text: "User with id $params.id not found")

    def updateEvent = createUserEventUpdate(person, params)
    eventService.persist(updateEvent)
    updateEvent.errors.allErrors.each{
      println it
    }

    println updateEvent.userRealName
    println updateEvent.user.userRealName

    return renderUpdateResults(updateEvent)
  }

  private renderUpdateResults(updateEvent){
    withFormat{
      html{
          def roleNames = getUserRoles(updateEvent.user)
          return render( template: 'userForm', model: [ event: updateEvent, roleNames: roleNames ])
      }
      js{
          return render ( [ userInstance: updateEvent.user ] as JSON )
      }
      xml{
          return render ( [ userInstence: updateEvent.user ] as XML )
      }
    }
  }

  private createUserEventUpdate(user, params){
    def updateEvent = new UserEventUpdate(user: user)
    updateEvent.populateFromUser()
    updateEvent.properties = params['passwdRepeat','email','userRealName','description']
    println updateEvent.passwd
    println updateEvent.passwdRepeat
    return updateEvent
  }

  def create = {
    [person: new User(params), authorityList: Role.list()]
  }

  private void addRoles(person) {
    for (String key in params.keySet()) {
      if (key.contains('ROLE') && 'on' == params.get(key)) {
        Role.findByAuthority(key).addToPeople(person)
      }
    }
  }

  private Map buildPersonModel(person) {

    List roles = Role.list()
    roles.sort { r1, r2 ->
      r1.authority <=> r2.authority
    }
    Set userRoleNames = []
    for (role in person.authorities) {
      userRoleNames << role.authority
    }
    LinkedHashMap<Role, Boolean> roleMap = [:]
    for (role in roles) {
      roleMap[(role)] = userRoleNames.contains(role.authority)
    }

    return [person: person, roleMap: roleMap]
  }
}