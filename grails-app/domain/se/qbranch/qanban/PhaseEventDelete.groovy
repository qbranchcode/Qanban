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

class PhaseEventDelete  extends PhaseEvent {

  static constraints = {
    cardLimit ( nullable: true )
    title ( nullable: true )
    boardDomainId ( nullable: true )
    phasePos ( nullable: true )
  }

  static transients = ['phase','items','board']

  Phase phase
  Board board

  String title
  Integer cardLimit
  Integer phasePos
  String boardDomainId

  public List getItems() {
    return [getPhase().title]
  }

  public Phase getPhase(){
    if( !phase && domainId ){
      phase = new Phase(title: title, board: board, cardLimit: cardLimit);
    }
    return phase
  }

  public Board getBoard(){
    if( !board && boardDomainId ){
      board = Board.findByDomainId(boardDomainId)
    }
    return board
  }

  transient beforeInsert = {
    domainId = phase.domainId
    title = phase.title
    boardDomainId = phase.board.domainId
    cardLimit = phase.cardLimit
    phasePos = Board.get(board.id).phases.indexOf(phase)
  }

  transient process(){

    board.phases.remove(phase)
    phase.delete(flush:true)


  }
}
