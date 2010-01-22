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

class BoardEventCreate extends BoardEvent{

  static constraints = {
    title ( nullable: false, blank: false )
  }

  static transients = ['board','items']

  Board board
  String title

  public Board getBoard(){
    if( !board && domainId )
      board = Board.findByDomainId(domainId)
    return board
  }

  def beforeInsert = {
    generateDomainId('board','create')
    userDomainId = eventCreator.domainId
  }

  def process(){
    board = new Board()
    board.domainId = domainId
    board.title = title
    board.save()
  }


}
