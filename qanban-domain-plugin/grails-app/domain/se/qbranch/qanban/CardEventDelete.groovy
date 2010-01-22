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

class CardEventDelete  extends CardEvent {

    static constraints = {
        assignee ( nullable : true )
        title( nullable: true )
        description( nullable: true)
        phaseDomainId( nullable: true )
        phasePos ( nullable: true )
        caseNumber( nullable: true )
    }

    static mapping = {
      columns {
          description type:'text'
      }
    }

    static transients = ['card','items']

    Card card

    String title
    String description
    String caseNumber
    String phaseDomainId
    Integer phasePos
    User assignee

    public List getItems() {
        return [getCard().title]
    }

    public Card getCard(){
        if(!card && domainId) {
            card = new Card(title: title, description: description, caseNumber: caseNumber, phase: Phase.findByDomainId(phaseDomainId))
        }
        return card
    }

    transient beforeInsert = {
        domainId = card.domainId
        title = card.title
        description = card.description
        caseNumber = card.caseNumber
        phaseDomainId = card.phase.domainId
        phasePos = card.phase.board.phases.indexOf(card)
        assignee = card.assignee
      userDomainId = eventCreator.domainId
    }

    transient process(){
        card.phase.cards.remove(card)
        card.delete(flush:true)
    }
}
