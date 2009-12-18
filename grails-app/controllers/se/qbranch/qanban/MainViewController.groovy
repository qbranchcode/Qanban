/*
 * Copyright 2009 Qbranch AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    def eventList
    if( params.sort == "user" ) {
      def orderMap = [ asc : "asc", desc: "desc" ]
      eventList = Event.executeQuery("SELECT e FROM Event e, User u WHERE e.userDomainId = u.domainId ORDER BY u.userRealName ${orderMap[params.order]} OFFSET $params.offset LIMIT $params.max")
    } else {
      eventList = Event.list( params )
    }
    render(template: "/event/logBody", model: [ eventInstanceList: eventList, offset : params.offset as Integer ])
  }

  def showArchive = {
    def phases = Board.get(params.'board.id').phases
    params.sort = 'lastUpdated'
    params.max = params.max ? params.max as Integer : 40
    params.order = params.order ? params.order : 'desc'
    def cardList = sortArchiveCards(phases)
    render(template: "/archive/archive", model: [ archiveCardList: cardList, archiveCardTotal: phases[-1].cards.size() , board : Board.get(params.'board.id') ])
  }

  def showArchiveBody = {
    def phases = Board.get(params.'board.id').phases
    params.max = params.max ? params.max as Integer : 40
    params.order = params.order ? params.order : 'desc'
    def cardList = sortArchiveCards(phases)
    render(template: "/archive/archiveBody", model: [ archiveCardList: cardList, offset : params.offset as Integer ])
  }

  def sortArchiveCards(phases) {
    def list = Card.withCriteria{
      eq('phase', phases[-1])
      order(params.sort, params.order)
    }
    return list
  }

}


