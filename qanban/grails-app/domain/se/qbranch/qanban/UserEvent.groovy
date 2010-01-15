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

class UserEvent extends Event{

  static constraints = {
  }

  transient public String checkCurrentTitle() {
    if( domainId ){
      def user = User.findByDomainId(domainId)
      if (user){
        return user.userRealName
      }else{
        return 'The user does not exist'
      }
    }
    else return "This should not be a valid output"
  }

  public boolean doesDomainExist(){
    return User.findByDomainId(domainId) != null
  }
}
