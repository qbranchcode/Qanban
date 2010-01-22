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

class CardEventSetAssignee extends CardEvent {

    static constraints = {
        newAssignee ( nullable: true )
    }

    static transients = ['card','items']
    Card card
    
    User newAssignee

    public List getItems() {
        return [dateCreated, eventCreator, newAssignee]
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

    transient beforeInsert = {
        domainId = card.domainId
        userDomainId = eventCreator.domainId
    }

    transient process(){
	card.assignee = newAssignee
        card.save()
    }

    String toString(){
        return "$dateCreated: $eventCreator set the assignee to $newAssignee"
    }
}
