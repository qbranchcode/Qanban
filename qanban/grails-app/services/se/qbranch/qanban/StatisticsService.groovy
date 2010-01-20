/*
 * Copyright 2010 Qbranch AB
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

import org.joda.time.Period
import org.joda.time.Interval


class StatisticsService {

  boolean transactional = true


  //  Lead Time Calculation

  def calculateLeadTime(board) {
    def cards = getArchivedCards(board,)
    getAverageLeadTime(cards)
  }

  def calculateLeadTime(board,interval){
    def cards = getArchivedCardsFromInterval(board,interval)
    getAverageLeadTime(cards)
  }

  private getAverageLeadTime(cards){
    def leadTimes = cards.collect{ card ->
      getLeadTimeInterval(card).toDurationMillis()
    }
    new Period((leadTimes == [] ? 0 : leadTimes.sum() / leadTimes.size()).longValue())
  }

  private getLeadTimeInterval(card){
    def startTimeStamp = card.dateCreated.time
    def archivedTimeStamp = CardEventMove.findByDomainIdAndPhaseDomainId(card.domainId,card.phase.board.phases[-1].domainId).dateCreated.time
    new Interval(startTimeStamp, archivedTimeStamp)
  }


  // Cycle Time Calculation

  def calculateCycleTime(board) {
    def cards = getArchivedCards(board)
    getCycleTime(board,cards)
  }

  def calculateCycleTime(board,interval){
    def cards = getArchivedCardsFromInterval(board,interval)
    getCycleTime(board,cards,interval)
  }

  private getCycleTime(board,cards){
    def events = getArchivingEvents(board,cards)
    new Period( (( events[-1].dateCreated.time - events[1].dateCreated.time ) / events.size()).longValue() )
  }

  private getCycleTime(board,cards,interval){
    def events = getArchivingEvents(board,cards)
    new Period( (( interval.endMillis - interval.startMillis ) / events.size()).longValue() )
  }

  private getArchivingEvents(board,cards){
    def archive = board.phases[-1]
    Event.withCriteria {
      eq('class','se.qbranch.qanban.CardEventMove')
      eq('phaseDomainId',archive.domainId)
      'in'('domainId',cards*.domainId)
      order('dateCreated','asc')
    }
  }


  // Shared help methods

  private getArchivedCards(board){
    board.phases[-1].cards
  }

  private getArchivedCardsFromInterval(board,interval){
    def phase = board.phases[-1]
    def events = Event.withCriteria {
      eq('class','se.qbranch.qanban.CardEventMove')
      eq('phaseDomainId',phase.domainId)
      between('dateCreated',interval.start.toDate(),interval.end.toDate())
    }
    def domainIds = events.collect{ event -> event.domainId }
    Card.findAllByDomainIdInList(domainIds)
  }
  
}
