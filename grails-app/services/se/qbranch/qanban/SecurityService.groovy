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

import org.springframework.security.context.SecurityContextHolder as SCH
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools

class SecurityService {

    String ADMIN_ROLE = "ROLE_QANBANADMIN"

    boolean transactional = true

    def getLoggedInUser() {
        def user = null
        if (isAuthenticated()) {
            user = User.get(determineSource().id)
        }
        return user
    }

    boolean isUserAdmin(){
        if (AuthorizeTools.ifAllGranted(ADMIN_ROLE))
            return true
        return false
    }

    private def determineSource() {
        def principal = SCH.context.authentication.principal
        def source

        // check to see if it's a GrailsUser/GrailsUserImpl/subclass,
        // or otherwise has a 'domainClass' property
        if (principal.metaClass.respondsTo(principal, 'getDomainClass')) {
                source = principal.domainClass
        }
        if (!source) {
                source = principal
        }

        return source
    }

    private boolean isAuthenticated() {
        def authPrincipal = SCH?.context?.authentication?.principal
        return authPrincipal != null && authPrincipal != 'anonymousUser'
    }
}
