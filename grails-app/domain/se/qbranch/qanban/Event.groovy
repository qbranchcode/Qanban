package se.qbranch.qanban
import org.codehaus.groovy.grails.plugins.codecs.MD5Codec as Codec

class Event implements Comparable {

    static constraints = {
        domainId( nullable:true, blank: false )
        user(nullable: false)
    }
    
    Date dateCreated
    String domainId
    User user

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
