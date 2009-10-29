import se.qbranch.qanban.*

import grails.util.GrailsUtil

class BootStrap {

     def init = { servletContext ->

         switch (GrailsUtil.environment) {
            case "development":

                Role role = new Role(description:"administrator access",authority:"ROLE_ADMIN")
                role.save()
                Requestmap map = new Requestmap(url:"/mainView/**", configAttribute:"ROLE_ADMIN")
                map.save()

                //TODO: Varför fungerar inte detta?
                //User user = new User(username:"testuser",userRealName:"Mr Test",passwd:"test",enabled:true,description:"testing, testing...",email:"test@test.com",authorities:role)
                //user.addToAuthorities(role).save()

                Phase p1 = new Phase(name:'Backlog')
                p1.addToCards(new Card(title:"Card #1",caseNumber:1,description:'blalbblalbabla'))
                  .addToCards(new Card(title:"Card #2",caseNumber:2,description:'blöblöblöblöbl'))

                Board b = new Board()
                            .addToPhases(p1)
                            .addToPhases(new Phase(name:'WIP'))
                            .addToPhases(new Phase(name:'Done'))
                            .save()


                break
        }

     }
     def destroy = {
     }
} 
