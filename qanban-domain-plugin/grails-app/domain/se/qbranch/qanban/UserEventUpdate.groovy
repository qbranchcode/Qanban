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

class UserEventUpdate extends UserEvent{

  def authenticateService

  static constraints = {
    username( blank: false, unique: false ) // Unique true in create event
    userRealName( blank: false )
    email( nullable: false, blank: false, email: true )
    enabled( nullable: true )
    emailShow( nullable: true )
    passwd( nullable: false, blank: false, validator: { val, obj ->
      if( obj.authenticateService.encodePassword(val) != obj.user.passwd ){
        return['userEventUpdate.authentication.password.missmatch']
      }else if( obj.newPasswd || obj.newPasswdRepeat ){
          if( obj.newPasswd != obj.newPasswdRepeat ){
            return['userEventUpdate.newPassword.missmatch']
          }
      }
    })
  }

  static transients = ['items','newPasswd','newPasswdRepeat']

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
  String description = ''
  String passwd
  String newPasswd
  String newPasswdRepeat

  public List getItems(){
    return [dateCreated, user]
  }

  transient beforeInsert = {

    setEventCreator(user)

    domainId = user.domainId

    if( newPasswd && newPasswdRepeat ){
      if( newPasswd == newPasswdRepeat ){
        passwd = authenticateService.encodePassword(newPasswd)
      }else{
        //TODO: How to add a validation error on newPasswdRepeat and let this stop the processing of the event
      }
    }
    
    passwd = authenticateService.encodePassword(passwd)
  }

  // TODO: Ignore passwd... anything else?
  transient void populateFromUser(){
    if( user ){
      this.properties['username','userRealName','email','enabled','emailShow','description'] = user.properties
    }
  }

  transient process(){

    user.properties = this.properties['username','userRealName','email','enabled','emailShow','description','passwd']
    user.save()

  }
}
