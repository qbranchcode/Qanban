package se.qbranch.qanban

class UserEventDelete extends UserEvent {

  static constraints = {
    email(nullable: true)
    enabled(nullable: true)
    emailShow(nullable: true)
    description(nullable: true)
    passwd(nullable: true)
  }

  static mapping = {
    columns {
      description type:'text'
    }
  }

  static transients = ['items','deletedUser']

  User deletedUser

  String username
  String userRealName
  String email
  boolean enabled
  boolean emailShow
  String description
  String passwd

  public List getItems() {
    return [dateCreated, eventCreator, deletedUser]
  }


  transient beforeInsert = {
    domainId = deletedUser.domainId
    username = deletedUser.username
    userRealName = deletedUser.userRealName
    email = deletedUser.email
    enabled = deletedUser.enabled
    emailShow = deletedUser.emailShow
    description = deletedUser.description
    passwd = deletedUser.passwd
    userDomainId = eventCreator.domainId
  }

  transient process(){
    deletedUser.delete(flush:true)
  }
}
