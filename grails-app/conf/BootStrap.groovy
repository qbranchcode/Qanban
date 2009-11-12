import se.qbranch.qanban.*
import org.apache.commons.codec.digest.DigestUtils as DU
import grails.util.GrailsUtil

class BootStrap {

    def authenticateService

    def init = { servletContext ->

        switch (GrailsUtil.environment) {
            case "development":

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
        }

    }
    def destroy = {
    }
} 
