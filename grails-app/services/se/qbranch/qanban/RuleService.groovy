package se.qbranch.qanban

class RuleService {

    boolean transactional = true
    def securityService

    boolean isMoveLegal(oldPhase, newPhase) {
        def board = newPhase.board
        def oldPhaseIndex = board.phases.indexOf(oldPhase)
        def newPhaseIndex = board.phases.indexOf(newPhase)

        if( oldPhaseIndex+1 == newPhaseIndex || oldPhaseIndex == newPhaseIndex || securityService.isUserAdmin){
            return true
        }else{
            return false
        }
    }
}
