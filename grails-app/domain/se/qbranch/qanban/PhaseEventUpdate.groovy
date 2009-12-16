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

class PhaseEventUpdate extends PhaseEvent {

    // TODO: Validera så att eventen inte sparas om inget värde har ändrats
    
    static constraints = {
        title ( nullable: false, blank: false )
        cardLimit ( nullable: true )
    }

    static transients = ['phase','board','items']
    Phase phase


    String title
    Integer cardLimit

    public List getItems() {
        return [getPhase().title]
    }

    public Phase getPhase(){
        if( !phase && domainId ){
            phase = Phase.findByDomainId(domainId)
            if(!phase) {
                phase = PhaseEventDelete.findByDomainId(domainId).phase
            }
        }
        return phase
    }

    public void setPhase(phase){
        this.phase = phase
        cardLimit = phase.cardLimit
        title = phase.title
        domainId = phase.domainId
    }

        //TODO: Cleanup, check lazy settings.
    public Board getBoard(){
        Phase.get(phase.id).board
    }

    transient beforeInsert = {
      domainId = phase.domainId
      setEventCreator(user)
    }

    transient process( ) {

        phase.title = title
        phase.cardLimit = cardLimit
        phase.save()

    }
}
