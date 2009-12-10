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

import org.codehaus.groovy.grails.plugins.codecs.MD5Codec as Codec

class Event implements Comparable {

    static constraints = {
        domainId( nullable:true, blank: false )
        user(nullable: false)
    }

    static transients = ['items']

    Date dateCreated
    String domainId
    User user

    public List getItems() {
        return []
    }

    def generateDomainId( Object[] notNullableProperties ){
        domainId = Codec.encode( new Date().time + notNullableProperties.toString() )
    }

    int compareTo(Object o) {
        if (o instanceof Event) {
            Event event = (Event) o
            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            if(this.dateCreated < event.dateCreated) return AFTER
            if(this.dateCreated > event.dateCreated) return BEFORE

            return EQUAL
        }

    }

    boolean equals(Object o) {
        if(o instanceof Event) {
            Event event = (Event) o
            if(this.id == event.id)
            return true
        }
        return false
    }


}
