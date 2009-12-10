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

import se.qbranch.qanban.Role

/**
 * User domain class.
 */
class User {

    static constraints = {
        username(blank: false, unique: true)
        userRealName(blank: false)
        passwd(nullable: true)
        pass(nullable:true)
        enabled()
    }
    
    static transients = ['pass']
    static hasMany = [authorities: Role]
    static belongsTo = Role

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



    String toString(){
        return userRealName
    }
}
