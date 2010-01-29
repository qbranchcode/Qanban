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
  def securityService
  def sessionRegistry
  def eventService


  // the delete, save and update actions only accept POST requests
  static Map allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']


  // Show
  def show = {
    def user = User.get(params.id)

    render(template: 'user', bean: user)
  }

  // Delete
  @Secured(['IS_AUTHENTICATED_FULLY'])
  def delete = {

    if( !params.id )
            return render(status: 400, text: "You need to specify a user")
    if( !User.exists(params.id) )
            return render(status: 404, text: "User with id $params.id not found")

    def user = User.get(params.id as Integer)

    def deleteEvent = new UserEventDelete(user: user, eventCreator:securityService.getLoggedInUser())
    deleteEvent.populateFromUser()
    eventService.persist(deleteEvent)

    if(user == deleteEvent.eventCreator) {
      return redirect(controller: "logout")
    }

    if( !deleteEvent.hasErrors()) {
      flash.message = "${user.username} is now deleted"
      return render(status: 200, text: "User with id $params.id deleted")
    } 

    return render(status: 503, text: "Server error: user delete error #188")

  }

  // Create
  def save = {

    def user = new User()
    def createEvent


    user.properties = params
    createEvent = new UserEventCreate(eventCreator:user)
    createEvent.populateFromUser()
    eventService.persist(createEvent)

    if ( !createEvent.hasErrors()) {
      flash.message = "${user.username} is now created"
    }
    else {
      flash.message = null
      createEvent.eventCreator.errors = createEvent.errors
    }

    def template = params.template ? params.template : '/login/register'

    return render(template: template , model: [ person : createEvent.eventCreator ])
  }



  @Secured(['IS_AUTHENTICATED_FULLY'])
  def passForm = {

    if ( !params.id )
    return render(status: 400, text: "id have to be specified")

    def user = User.get(params.id)

    if( !user )
    return render(status: 404, text: "User with id $params.id not found")

    render(template:'passwordForm', model: [ person: user ])

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

  private renderFormCreateMode(person){
    def roleNames = Role.list()
    return render( template:'userForm', model: [person: person, roleNames: roleNames])
  }

  private renderFormEditMode(person){
    def roleNames = getUserRoles(person)
    return render(template:'userForm',model:[ person: person, roleNames: roleNames ])
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
  def update = { UserUpdateCommand uc ->

    def person = User.read(params.id)

    if( !person )
      return render(status: 404, text: "User with id $params.id not found")

    if(uc.hasErrors()) {
      person.validate()
      person.errors = uc.errors
    } else {
      person = User.get(person.id)
      if( checkForRoles() ){
        Role.findAll().each { it.removeFromPeople(person) }
        addRoles(person)
      }
      person.properties = params
      person.save()

      flash.message = "${person.username} is now updated"
    }

    return renderUpdateResults(params,person)
  }

  private boolean checkForRoles() {
    for (String key in params.keySet()) {
      if (key.contains('ROLE')) {
        return true
      }
    }
    return false
  }

  private renderUpdateResults(params, person){
    withFormat{
      html{
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
        def template = params.template ? params.template : 'userForm'
        return render( template: template, model: [ person: person, roleMap: roleMap, roles: Role.list(), loggedInUser: securityService.getLoggedInUser() ] , bean: person)
      }
      js{
        return render ( [ userInstance: person ] as JSON )
      }
      xml{
        return render ( [ userInstence: person ] as XML )
      }
    }
  }

  private void addRoles(person) {
    for (String key in params.keySet()) {
      if (key.contains('ROLE') && 'on' == params.get(key)) {
        Role.findByAuthority(key).addToPeople(person)
      }
    }
  }
}

class UserUpdateCommand {
  def securityService
  def authenticateService

  static constraints = {
    username(blank: false)
    userRealName(blank: false)
    email(nullable: false, blank: false)
    passwd( nullable: false, blank: false, validator: { val, obj ->
      if( obj.user == obj.loggedInUser &&
          obj.authenticateService.encodePassword(obj.passwdRepeat) != val) {
                return['user.authentication.password.missmatch']
      } else if( obj == obj.securityService.getLoggedInUser() &&
                 !obj.securityService.isUserAdmin() ) {
                return['user.authentication.notAuthorized']
      }
    })

  }

  static transients = ['passwdRepeat']

  def getUser(){
    User.read(id)
  }

  def getLoggedInUser(){
    securityService.getLoggedInUser()
  }

  def getPasswd(){
    User.read(id)?.passwd
  }

  Integer id
  String username
  String userRealName
  String email
  String passwd
  String passwdRepeat
}