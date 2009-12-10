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

class CardEvent extends Event {

    static constraints = {
    }
    
    transient public String checkCurrentTitle() {
        if( domainId ){
            def card = Card.findByDomainId(domainId)
            if (card){
                return card.title
            }else{
                return CardEventDelete.findByDomainId(domainId).title
            }
        }
        else return "This should not be a valid output"
    }

    transient boolean doesDomainExist(){
        return Card.findByDomainId(domainId) != null
    }
}
