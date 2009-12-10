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

import org.codehaus.groovy.grails.plugins.codecs.MD5Codec

class PhaseEventCreate extends PhaseEvent {

    static constraints = {
        cardLimit ( nullable: true )
        position ( nullable: false )
        title(nullable: false, blank: false)
    }

    static transients = ['phase','items']
    Phase phase

    String title
    Board board
    Integer cardLimit
    Integer position

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

    transient beforeInsert = {
        generateDomainId(title, board )
    }

    transient process = {

        phase = new Phase(
            title: title,
            board: board,
            cardLimit: cardLimit,
            domainId: domainId
        )
        board.phases.add(position, phase)

        phase.save()
        
    }
}
