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

class Phase {

    static constraints = {
        title(nullable: false, blank: false)
        cardLimit(nullable: true, validator:{ limit, phaseInstance ->
                if(phaseInstance.cardLimit < phaseInstance.cards.size() && phaseInstance.id != null && phaseInstance.cardLimit != null)
                    return ['phase.cardLimit.lessThanCardsSize', limit]
            })
        domainId( nullable: false, unique: true, blank: false)
    }

    //TODO: Titta mer på det här!
    static hasMany = [cards:Card]

    String title
    Board board
    List cards = []
    Integer cardLimit
    String domainId

    boolean equals(Object o) {
        if(o instanceof Phase) {
            Phase p = (Phase) o
            if(this.id == p.id)
                return true
        }
        return false
    }

    public String toString() {
        return "$title"
    }
}