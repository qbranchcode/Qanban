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

class RuleService {

  boolean transactional = true
  def securityService

  boolean isMoveLegal(oldPhase, newPhase) {
    def board = newPhase.board
    def oldPhaseIndex = board.phases.indexOf(oldPhase)
    def newPhaseIndex = board.phases.indexOf(newPhase)

    if( isNextPhase(oldPhaseIndex, newPhaseIndex) || isSamePhase(oldPhaseIndex, newPhaseIndex) || securityService.isUserAdmin()){
      return true
    }else{
      return false
    }
  }

  private boolean isNextPhase(oldPhaseIndex, newPhaseIndex) {
    return oldPhaseIndex + 1 == newPhaseIndex
  }

  private boolean isSamePhase(oldPhaseIndex, newPhaseIndex) {
    return oldPhaseIndex == newPhaseIndex
  }
}
