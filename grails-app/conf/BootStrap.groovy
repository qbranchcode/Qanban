import se.qbranch.qanban.*
import org.apache.commons.codec.digest.DigestUtils as DU
import grails.util.GrailsUtil

class BootStrap {

    def authenticateService
    def authenticationManager
    def sessionController

    def init = { servletContext ->
        authenticationManager.sessionController = sessionController


        switch (GrailsUtil.environment) {

            case "test":

            Role role = new Role(description:"administrator access",authority:"ROLE_ADMIN")
            role.save()
            Requestmap map = new Requestmap(url:"/mainView/**", configAttribute:"ROLE_ADMIN")
            map.save()

            def md5pass = authenticateService.passwordEncoder("test")
            User user = new User(username: "testuser",
                                 userRealName:"Mr Test",
                                 passwd:md5pass,
                                 enabled:true,
                                 description:"testing, testing...",
                                 email:"test@test.com",
                                 authorities:role)

            if(user.save()) {
                role.addToPeople(user)
            }

            Phase p1 = new Phase(name:'Backlog', cardLimit: 10)
            p1.addToCards(new Card(title:"Card #1",caseNumber:1,description:'blalbblalbabla'))
            .addToCards(new Card(title:"Card #2",caseNumber:2,description:'blöblöblöblöbl'))

            Board b = new Board()
            .addToPhases(p1)
            .addToPhases(new Phase(name:'WIP', cardLimit: 5))
            .addToPhases(new Phase(name:'Done', cardLimit: 5))
            .save()

            break

            default:

            Role adminRole = new Role(description:"administrator access",authority:"ROLE_QANBANADMIN").save()
            Role userRole = new Role(description:"administrator access",authority:"ROLE_QANBANUSER").save()
            Requestmap loginMap = new Requestmap(url:"/login/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save()
            Requestmap cssMap = new Requestmap(url:"/css/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save()
            Requestmap jsMap = new Requestmap(url:"/js/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save()
            Requestmap imageMap = new Requestmap(url:"/images/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save()
            Requestmap userCMap = new Requestmap(url:"/user/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save()
            Requestmap map = new Requestmap(url:"/**", configAttribute:"ROLE_QANBANUSER").save()

            User regularUser = new User(username: "testuser",
                                     userRealName:"Test User",
                                     passwd:authenticateService.passwordEncoder("testuser"),
                                     enabled:true,
                                     description:"This is a regular user",
                                     email:"test@test.com",
                                     authorities:userRole)

            if(regularUser.save()) {
                userRole.addToPeople(regularUser)
            }

            User adminUser = new User(username: "testadmin",
                                     userRealName:"Admin User",
                                     passwd:authenticateService.passwordEncoder("testadmin"),
                                     enabled:true,
                                     description:"This is an admin user",
                                     email:"test@test.com",
                                     authorities:adminRole)

            if(adminUser.save()) {
                userRole.addToPeople(adminUser)
                adminRole.addToPeople(adminUser)
            }

            Phase p1 = new Phase(name:'Backlog')
            p1.addToCards(new Card(title:"Card #1",caseNumber:1,description:'This is a card'))
            .addToCards(new Card(title:"Card #2",caseNumber:2,description:'This is another card'))

            Board b = new Board()
            .addToPhases(p1)
            .addToPhases(new Phase(name:'WIP', cardLimit: 5))
            .addToPhases(new Phase(name:'Done'))
            .save()

            break
        }

    }
    def destroy = {
    }
}
