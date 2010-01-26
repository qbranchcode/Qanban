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


/**
 * User domain class.
 */
class User {

  def securityService
  def authenticateService

  static constraints = {
    username(blank: false, unique: true)
    userRealName(blank: false)
    passwd( nullable: false, blank: false, validator: { val, obj ->
      if( obj == obj.securityService.getLoggedInUser() &&
          obj.authenticateService.encodePassword(obj.passwdRepeat) != val ) {
        return ['user.authentication.password.missmatch']
      } else if ( obj == obj.securityService.getLoggedInUser() &&
                  !obj.securityService.isUserAdmin() ) {
        return ['user.authentication.notAuthorized']
      }
    })
    enabled()
    domainId( nullable: false, blank: false, unique: true)
  }

  static transients = ['pass','passwdRepeat']
  static hasMany = [authorities: Role]
  static belongsTo = Role

  String domainId
  String username
  String userRealName
  /** MD5 Password */
  String passwd
  boolean enabled

  String email
  boolean emailShow

  String description = ''

  /** plain password to create a MD5 password */
  String pass = '[secret]'
  String passwdRepeat


  String toString(){
    return userRealName
  }
}
