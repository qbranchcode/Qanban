package se.qbranch.qanban

class RuleService {

    boolean transactional = true
    def securityService

    boolean isMoveLegal(MoveCardCommand mcc) {
        def board = Phase.get(cmd.card.phase.id).board
        def oldPhaseIndex = board.phases.indexOf(Phase.get(cmd.card.phase.id))
        def newPhaseIndex = board.phases.indexOf(cmd.phase)

        if( oldPhaseIndex+1 == newPhaseIndex || oldPhaseIndex == newPhaseIndex || securityService.isUserAdmin)
            return true
        else
            return false
    }
}
