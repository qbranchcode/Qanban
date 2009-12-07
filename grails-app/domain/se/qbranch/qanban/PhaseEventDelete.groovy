package se.qbranch.qanban

class PhaseEventDelete  extends PhaseEvent {

    static constraints = {
        cardLimit ( nullable: true )
        title ( nullable: true )
        board ( nullable: true )
        position ( nullable: true )
    }

    static transients = ['phase']

    Phase phase

    String title
    Board board
    Integer cardLimit
    Integer position

    public Phase getPhase(){
        if( !phase && domainId ){
            phase = new Phase(title: title, board: board, cardLimit: cardLimit);
        }
        return phase
    }

    transient beforeInsert = {
        domainId = phase.domainId
        title = phase.title
        board = phase.board
        cardLimit = phase.cardLimit
        position = board.phases.indexOf(phase)
    }

    transient process(){

        board.phases.remove(phase)
        phase.delete(flush:true)


    }
}
