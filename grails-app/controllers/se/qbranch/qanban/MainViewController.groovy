package se.qbranch.qanban

import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class MainViewController {

  def securityService
  def index = { redirect(action:view,params:params)  }

  def view = {
    [ board : Board.get(1), loggedInUser: securityService.getLoggedInUser() ]
  }

  def showBoard = {
    render(template: "/board/board", bean: Board.get(params.'board.id'))
  }

  def showLog = {
    // This action needs the same model as showLogBody because it render the default body content through a template
    params.max = params.max ? params.max as Integer : 40
    params.order = params.order ? params.order : 'desc'
    params.sort = 'dateCreated'

    // TODO: Only get the events connected to the current board
    render(template: "/event/log", model: [ eventInstanceTotal: Event.count(), eventInstanceList: Event.list( params ), board: Board.get(params.'board.id') ])
  }

  def showLogBody = {
    params.max = params.max ? params.max as Integer : 40
    params.order = params.order ? params.order : 'desc'

    // TODO: Only get the events connected to the current board
    def board = Board.get(params.'board.id')

    render(template: "/event/logBody", model: [ eventInstanceList: Event.list( params ), offset : params.offset as Integer ])
  }

}


