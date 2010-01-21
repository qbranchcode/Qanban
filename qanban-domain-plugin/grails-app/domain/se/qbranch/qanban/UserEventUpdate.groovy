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

import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools

class UserEventUpdate extends UserEvent{

  def authenticateService

  static constraints = {
    username( blank: false, unique: false ) // Unique true in create event
    userRealName( blank: false )
    email( nullable: false, blank: false, email: true )
    enabled( nullable: true )
    emailShow( nullable: true )
    passwd( nullable: false, blank: false, validator: { val, obj ->
      if( !AuthorizeTools.ifAllGranted("ROLE_QANBANADMIN") && 
              obj.authenticateService.encodePassword(obj.passwdRepeat) != val ){
        return['userEventUpdate.authentication.password.missmatch']
      }else if( obj.newPasswd || obj.newPasswdRepeat ){
          if( obj.newPasswd != obj.newPasswdRepeat ){
            return['userEventUpdate.newPassword.missmatch']
          }
      }
    })
  }

  static transients = ['items','newPasswd','newPasswdRepeat','passwdRepeat']

  static mapping = {
    columns {
      description type:'text'
    }
  }

  String username
  String userRealName
  String email
  boolean enabled = true
  boolean emailShow = true
  String description 
  String passwd
  String passwdRepeat
  String newPasswd
  String newPasswdRepeat

  public List getItems(){
    return [dateCreated, eventCreator]
  }

  transient beforeInsert = {

    userDomainId = eventCreator.domainId
    domainId = eventCreator.domainId

    if( newPasswd && newPasswdRepeat ){
      if( newPasswd == newPasswdRepeat ){
        passwd = authenticateService.encodePassword(newPasswd)
        passwdRepeat = authenticateService.encodePassword(newPasswdRepeat)
      }
    }else{
      if(!AuthorizeTools.ifAllGranted("ROLE_QANBANADMIN")) {
        passwdRepeat = authenticateService.encodePassword(passwdRepeat)
      } else {
        passwdRepeat = passwd
      }
    }
    
  }

  transient void populateFromUser(){
    if( eventCreator ){
      this.properties['passwd','username','userRealName','email','enabled','emailShow','description'] = eventCreator.properties
    }
  }

  def process(){
    eventCreator.properties = this.properties['userRealName','email','enabled','emailShow','description','passwd','passwdRepeat']
    eventCreator.save()
  }
}
