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

    return render(template: '/login/register' , model: [ person : createEvent.eventCreator ])
  }



  @Secured(['IS_AUTHENTICATED_FULLY'])
  def passForm = {

    if ( !params.id )
    return render(status: 400, text: "id have to be specified")

    def user = User.get(params.id)

    if( !user )
    return render(status: 404, text: "User with id $params.id not found")

    def updateEvent = createUserEventUpdate(user,null)

    render(template:'passwordForm', model: [ event: updateEvent ])

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

  private renderFormCreateMode(params){
    return render( status:500, text: "Rendering form for creation not yet implemented")
  }

  private renderFormEditMode(person){
    def updateEvent = new UserEventUpdate(eventCreator: person)
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

    if ( !updateEvent.hasErrors()) {
      flash.message = "${updateEvent.username} is now updated"
    }

    return renderUpdateResults(updateEvent,params,person)
  }

  private renderUpdateResults(updateEvent, params, person){
    withFormat{
      html{
        def roleNames = getUserRoles(updateEvent.eventCreator)
        def template = params.template ? params.template : 'userForm'
        return render( template: template, model: [ event: updateEvent, roleNames: roleNames, editUser: person, roles: Role.list(), loggedInUser: securityService.getLoggedInUser() ] , bean: person)
      }
      js{
        return render ( [ userInstance: updateEvent.eventCreator ] as JSON )
      }
      xml{
        return render ( [ userInstence: updateEvent.eventCreator ] as XML )
      }
    }
  }

  private createUserEventUpdate(user, params){
    def updateEvent = new UserEventUpdate(eventCreator: user)
    updateEvent.populateFromUser()

    if( params )    
    updateEvent.properties = params['passwdRepeat','email','userRealName','description']

    return updateEvent
  }
}