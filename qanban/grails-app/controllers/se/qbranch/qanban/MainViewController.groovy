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

import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.PeriodFormatterBuilder

@Secured(['IS_AUTHENTICATED_FULLY'])
class MainViewController {

  def securityService
  def statisticsService

  def index = { redirect(action:view,params:params)  }

  def view = {
    [ board : Board.get(1), loggedInUser: securityService.getLoggedInUser() ]
  }

  def showBoard = {
    render(template: "/board/board", bean: Board.get(params.'board.id'))
  }

  def showStatistics = {

    def board = Board.get(params.'board.id')

    def leadTime = statisticsService.calculateLeadTime(board)
    def cycleTime = statisticsService.calculateMeanCycleTime(board)

    def intervalCycleMap = statisticsService.calculateCycleTimePerInterval(board, Days.ONE)
    def intervalMeanCycleMap = statisticsService.calculateMeanCycleTimePerInterval(board, Days.ONE)
    def maxCycleValue = getMaxValueInHours(intervalCycleMap,intervalMeanCycleMap)
    def cycleTicks = getElapsedTimeTicks(( maxCycleValue / 24 as Integer ) + 1)

    def intervalLeadMap = statisticsService.calculateLeadTimePerInterval(board, Days.ONE)
    def intervalMeanLeadMap = statisticsService.calculateMeanLeadTimePerInterval(board, Days.ONE)
    def maxLeadValue = getMaxValueInHours(intervalLeadMap, intervalMeanLeadMap)
    def leadTicks = getElapsedTimeTicks(( maxLeadValue / 24 as Integer ) + 1 )

    def dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")

    def leadTimeStr = "${leadTime.toStandardDays().days}.${(leadTime.hours % 24)/ 0.24 as Integer}"
    def cycleTimeStr =  "${cycleTime.toStandardDays().days}.${(cycleTime.hours % 24)/0.24 as Integer}"
    
    render(template: "statistics",
           model: [ dateFormatter : dateFormatter,
                    leadTime: leadTimeStr,
                    leadTicks: leadTicks,
                    intervalLeadMap: intervalLeadMap,
                    intervalMeanLeadMap: intervalMeanLeadMap,
                    cycleTime: cycleTimeStr,
                    cycleTicks : cycleTicks,
                    intervalCycleMap : intervalCycleMap,
                    intervalMeanCycleMap: intervalMeanCycleMap ])
  }

  private getElapsedTimeTicks(numberOfDays){
    def cycleTicks = "[[0,'&#8734;']"
    def counter = 1
    numberOfDays.times{ element ->
      cycleTicks += ",[${counter*24},'${counter} ${g.message(code:'days')}']"
      counter++
    }
    cycleTicks += "]";
  }

  private getMaxValueInHours(cycleMap, meanCyckeMap){
    def mCMax = meanCyckeMap*.value.hours.max()
    def cMax = cycleMap*.value.hours.max()
    mCMax > cMax ? mCMax : cMax
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
    params.order = params.order ? params.order.toLowerCase() : 'desc'
    params.offset = params.offset ? params.offset : 0
    params.sort = params.sort ? params.sort : 'dateCreated'
    
    // TODO: Only get the events connected to the current board
    def board = Board.get(params.'board.id')
    def eventList
    if( params.sort == "user" ) {

      // Order is not supported as a parameter in executeQuery(...)
      def orderMap = [ asc : "asc", desc: "desc" ]
      def noUserEvents = Event.executeQuery("SELECT e FROM Event e WHERE e.userDomainId NOT IN ( SELECT u.domainId FROM User u )")

      eventList = Event.executeQuery("SELECT e FROM Event e, User u WHERE e.userDomainId = u.domainId ORDER BY u.userRealName ${orderMap[params.order]}",[max:params.max as Integer,offset:params.offset as Integer] )

      
      if( orderMap[params.order] == "asc" && userSortIsAtLastLoad(params, eventList) ) {
        eventList = eventList + noUserEvents
      }else if( orderMap[params.order] == "desc" && params.offset == 0){
        eventList = noUserEvents + eventList
      }

    } else {
      eventList = Event.list( params )
    }
    render(template: "/event/logBody", model: [ eventInstanceList: eventList, offset : params.offset as Integer ])
  }

  private boolean userSortIsAtLastLoad(params, eventList){
      def loadedEvents = params.offset + eventList.size()
      def maxLoad = params.offset + params.max
      return loadedEvents < maxLoad && loadedEvents > ( maxLoad - params.max )
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
    def board = Board.get(params.'board.id')
    if (!board) board = Board.get(params.'boardId')
    def phases = board.phases
    params.max = params.max ? params.max as Integer : 40
    params.order = params.order ? params.order : 'desc'
    params.sort = params.sort ? params.sort : 'lastUpdated' 
    def cardList = sortArchiveCards(phases)
    render(template: "/archive/archiveBody", model: [ archiveCardList: cardList, offset : params.offset as Integer ])
  }

  def hideArchive = {
    session.showArchive = false
    render(status: 200)
  }

  def sortArchiveCards(phases) {
    def offset = params.offset ? params.offset?.toInteger() : 0
    def list = Card.withCriteria{
      eq('phase', phases[-1])
      firstResult(offset)
      order(params.sort, params.order)
      maxResults(params.max)
    }
    return list
  }

}


