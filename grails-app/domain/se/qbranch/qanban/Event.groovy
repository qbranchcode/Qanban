package se.qbranch.qanban
import org.codehaus.groovy.grails.plugins.codecs.MD5Codec as Codec

class Event {

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

}
