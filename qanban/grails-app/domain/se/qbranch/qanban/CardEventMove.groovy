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

class CardEventMove extends CardEvent {

    static constraints = {
        phaseDomainId ( nullable: true, blank: false )
        newCardIndex ( nullable: false, min: 0)
    }

    static transients = ['card','newPhase','items']
    Card card
    Phase newPhase

    String phaseDomainId
    Integer newCardIndex

    public List getItems() {
        return [dateCreated, user, getCard().title, getNewPhase().title]
    }

    public Card getCard(){
        if( !card && domainId ){
            card = Card.findByDomainId(domainId)
            if(!card) {
                card = CardEventDelete.findByDomainId(domainId).card
            }
        }
        return card
    }

    public Phase getNewPhase(){
        if( !newPhase && phaseDomainId ){
            newPhase = Phase.findByDomainId(phaseDomainId)
            if(!newPhase) {
                newPhase = PhaseEventDelete.findByDomainId(phaseDomainId).phase
            }
        }
        return newPhase
    }


    transient beforeInsert = {
        domainId = card.domainId
        phaseDomainId = newPhase.domainId
      setEventCreator(user)
    }

    transient process = {
        
        card.phase.cards.remove(card)
        newPhase.cards.add(newCardIndex, card)
        card.phase = newPhase
        card.lastUpdated = new Date()
        card.save(flush:true)
        
    }

    String toString(){
        def phase = Phase.findByDomainId(phaseDomainId)
        if (phase)
            return "$dateCreated: $user moved the card to $phase.title"
        else
            return "$dateCreated: $user moved the card to a deleted phase"
    }
}