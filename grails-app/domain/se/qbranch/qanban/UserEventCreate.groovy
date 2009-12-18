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

class UserEventCreate extends UserEvent {

  def authenticateService

  static constraints = {
    username(blank: false, unique: true)
    userRealName(blank: false)
    email(nullable: false, blank: false, email:true)
    enabled( nullable: true)
    emailShow( nullable: true)
    passwd( nullable: false, blank: false, validator:{ val, obj ->
        if( val != obj.passwdRepeat ){
          return ['userEventCreate.passwd.notEqualRepeat']
        }
    })
  }

  static transients = ['list','passwdRepeat']

  String username
  String userRealName
  String email
  boolean enabled = true
  boolean emailShow = true
  String description = ''
  String passwd
  String passwdRepeat

  public List getItems() {
    return [dateCreated, user]
  }

  def beforeInsert = {
    generateDomainId(username,userRealName,email)
    passwd = authenticateService.encodePassword(passwd)
    passwdRepeat = authenticateService.encodePassword(passwdRepeat)
    userDomainId = domainId // You create yourself
  }

  def populateFromUser(){
    this.properties = user.properties['username','userRealName','email','enabled','emailShow','description','passwd','passwdRepeat']
  }

  def process(){
    user = new User()
    user.properties = this.properties['username','userRealName','email','enabled','emailShow','description','passwd','passwdRepeat','domainId']

    // Gives every user the admin role, this is a temporary fix until a user manager is implemented
    Role.list().each{ role ->
        role.addToPeople(user)
    }

    
    user.save()
  }

}
