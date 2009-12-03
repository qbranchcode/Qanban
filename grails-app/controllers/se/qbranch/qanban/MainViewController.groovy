package se.qbranch.qanban

import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class MainViewController {

    def index = { redirect(action:view,params:params)  }

    def view = {
        [ board : Board.get(1) ]
    }

    def showBoard = {
        render(template: "/board/board", bean: Board.get(1))
    }

    def showLog = {
        render(template: "/event/log", model: [events : Event.list()])
    }

}


