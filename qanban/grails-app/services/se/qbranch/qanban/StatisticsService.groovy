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
import org.joda.time.DateTime

class StatisticsService {

  boolean transactional = true

  //  Throughput Calculation

  def Map<Interval,Card[]> calculateThroughputPerInterval(board,period){
    projectIterator(board, period){ interval ->
      getArchivedCardsFromInterval(board, interval)
    }
  }

  //  Lead Time Calculation

  def Period calculateLeadTime(board) {
    def cards = getArchivedCards(board)
    getAverageLeadTime(cards)
  }

  def Period calculateLeadTime(board,interval){
    def cards = getArchivedCardsFromInterval(board,interval)
    getAverageLeadTime(cards)
  }

  def Map<Card,Interval> calculateLeadTimePerCard(board) {
    def cards = getArchivedCards(board)
    def leadTimeByCard = [:]
    cards.each{ card ->
      leadTimeByCard[card] = getLeadTimeInterval(card)
    }
    leadTimeByCard
  }
  
  def Map<Interval,Period> calculateLeadTimePerInterval(board, period){
    projectIterator(board, period){ interval ->
      calculateLeadTime(board, interval)
    }
  }

  def Map<Interval,Period> calculateMeanLeadTimePerInterval(board, period){
    projectIterator(board,period){ interval ->
      calculateMeanLeadTime(board,interval.end)
    }
  }

  private Period calculateMeanLeadTime(board, date){
    def cards = getArchivedCardsBefore(board, date)
    getAverageLeadTime(cards)
  }

  private Period getAverageLeadTime(cards){
    def leadTimes = cards.collect{ card ->
      getLeadTimeInterval(card).toDurationMillis()
    }
    new Period((leadTimes == [] ? 0 : leadTimes.sum() / leadTimes.size()).longValue())
  }

  private Interval getLeadTimeInterval(card){
    def startTimeStamp = card.dateCreated.time
    def archivedTimeStamp = CardEventMove.findByDomainIdAndPhaseDomainId(card.domainId,card.phase.board.phases[-1].domainId).dateCreated.time //-2
    new Interval(startTimeStamp, archivedTimeStamp)
  }


  // Cycle Time Calculation

  def Map<Interval,Period> calculateCycleTimePerInterval(board, period){
    projectIterator(board, period){ interval ->
      calculateMeanCycleTime(board, interval)
    }
  }

  def Period calculateMeanCycleTime(board) {
    def cards = getArchivedCards(board)
    getCycleTime(board,cards)
  }

  def Period calculateMeanCycleTime(board,interval){
    def cards = getArchivedCardsFromInterval(board,interval)
    getCycleTime(board,cards,interval)
  }

  def Map<Interval,Period> calculateMeanCycleTimePerInterval(board,period){
    projectIterator(board,period){ interval ->
      getMeanCycleTime(board,interval.end)
    }
  }


  private Period getCycleTime(board,cards){
    getCycleTime(board,cards, new Interval(new DateTime(board.dateCreated), new DateTime()))
  }

  private Period getCycleTime(board,cards,interval){
    if( cards.size() > 0 ){
      def events = getArchivingEvents(board,cards)
      def period = new Period( (( interval.endMillis - interval.startMillis ) / cards.size()).longValue() )
      return period
    }
    return Period.ZERO
  }

  private Period getMeanCycleTime(board, date){
    def cards = getArchivedCardsBefore(board, date)
    getCycleTime(board, cards, new Interval(new DateTime(board.dateCreated), date))
  }

  private Collection<Event> getArchivingEvents(board,cards){
    def archive = board.phases[-1] //-2
    Event.withCriteria {
      eq('class','se.qbranch.qanban.CardEventMove')
      eq('phaseDomainId',archive.domainId)
      'in'('domainId',cards*.domainId)
      order('dateCreated','asc')
    }
  }


  // Shared help methods

  def projectIterator(board,period,closure){
    def project = getProjectLength(board)
    def intervalClosureMap = [:]
    for( def interval = new Interval(project.start, period); project.contains(interval.start); interval = new Interval(interval.end, period)){
      intervalClosureMap[interval] = closure( interval )
    }
    return intervalClosureMap
  }

  private Interval getProjectLength(board){
    return new Interval(new DateTime(board.dateCreated), new DateTime())
  }

  private getArchivedCards(board){
    Card.withCriteria{
      eq('phase',board.phases[-1])
      order('lastUpdated','asc')
    }
  }

  private getArchivedCardsBefore(board, date){
    def events = CardEventMove.findAllByPhaseDomainIdAndDateCreatedLessThan(board.phases[-1].domainId,date.toDate())
    events.size() > 0 ? Card.findAllByDomainIdInList(events*.domainId) : []
  }

  private getArchivedCardsFromInterval(board,interval){
    def phase = board.phases[-1] //-2
    def events = Event.withCriteria {
      eq('class','se.qbranch.qanban.CardEventMove')
      eq('phaseDomainId',phase.domainId)
      between('dateCreated',interval.start.toDate(),interval.end.toDate())
    }

    def cards = []

    if( events.size() > 0 ){
      cards = Card.findAllByDomainIdInList( events.collect{ event -> event.domainId } )
    }

    def cardsMovedToArchiveEarlier = getArchivedCardsBefore(board, interval.start)

    cardsMovedToArchiveEarlier.each{ card ->
      if( cards.contains(card) ){
        cards.remove(card) 
      }
    }
    
    return cards

  }

}
