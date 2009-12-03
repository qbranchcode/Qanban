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
