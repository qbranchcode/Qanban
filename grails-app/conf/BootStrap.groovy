import se.qbranch.qanban.*
import grails.util.GrailsUtil

class BootStrap {

     def init = { servletContext ->


         switch (GrailsUtil.environment) {
            case "development":
                Board b = new Board()
                            .addToPhases(new Phase(name:'Backlog'))
                            .addToPhases(new Phase(name:'WIP'))
                            .addToPhases(new Phase(name:'Done'))
                            .save()


                break
        }

     }
     def destroy = {
     }
} 