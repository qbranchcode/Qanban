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

class CardEventUpdate extends CardEvent {

    // TODO: Validera så att eventen inte sparas om inget värde har ändrats
    
    static constraints = {
        title( blank: false, length: 1..50)
        description( blank: true, nullable: true )
        caseNumber( blank: false)
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

    public List getItems() {
        return [dateCreated, eventCreator, getCard().title]
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

    public void setCard(card){
        this.card = card
        title = card.title
        description = card.description
        caseNumber = card.caseNumber
    }

    transient beforeInsert = {
        domainId = card.domainId
      userDomainId = eventCreator.domainId
    }

    transient process(){

        card.title = title
        card.description = description
        card.caseNumber = caseNumber
        card.save()
    }
    
    String toString(){
        return "$dateCreated: $eventCreator updated the card info"
    }
}
