package se.qbranch.qanban

class UserEventDelete extends UserEvent {

  static constraints = {
    username(blank: false, unique: false)
    userRealName(blank: false)
    email(nullable: false, blank: false, email:true)
    enabled( nullable: true)
    emailShow( nullable: true)
    passwd( nullable: false, blank: false)
  }

  static mapping = {
    columns {
      description type:'text'
    }
  }

  static transients = ['items','user', 'passwdRepeat']

  User user

  String username
  String userRealName
  String email
  boolean enabled
  boolean emailShow
  String description
  String passwd
  String passwdRepeat

  public List getItems() {
    return [dateCreated, eventCreator, user]
  }

  transient beforeInsert = {
    domainId = user.domainId
    username = user.username
    userRealName = user.userRealName
    email = user.email
    enabled = user.enabled
    emailShow = user.emailShow
    description = user.description
    passwd = user.passwd
    userDomainId = eventCreator.domainId
  }


  def populateFromUser(){
    this.properties = eventCreator.properties['username','userRealName','email','enabled','emailShow','description','passwd','passwdRepeat']
  }

  transient process(){
    Role.findAll().each { it.removeFromPeople(user) }
    user.delete(flush:true)
  }
}
