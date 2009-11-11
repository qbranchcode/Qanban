

package se.qbranch.qanban

import grails.converters.*

class PhaseController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ phaseInstanceList: Phase.list( params ), phaseInstanceTotal: Phase.count() ]
    }

    def show = {
        def phaseInstance = Phase.get( params.id )

        if(!phaseInstance) {
            flash.message = "Phase not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ phaseInstance : phaseInstance ] }
    }

    def delete = {
        def phaseInstance = Phase.get( params.id )
        if(phaseInstance) {
            try {
                phaseInstance.delete(flush:true)
                flash.message = "Phase ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Phase ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Phase not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def phaseInstance = Phase.get( params.id )

        if(!phaseInstance) {
            flash.message = "Phase not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ phaseInstance : phaseInstance ]
        }
    }

    def update = {
        def phaseInstance = Phase.get( params.id )
        if(phaseInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(phaseInstance.version > version) {
                    
                    phaseInstance.errors.rejectValue("version", "phase.optimistic.locking.failure", "Another user has updated this Phase while you were editing.")
                    render(view:'edit',model:[phaseInstance:phaseInstance])
                    return
                }
            }
            phaseInstance.properties = params
            if(!phaseInstance.hasErrors() && phaseInstance.save()) {
                flash.message = "Phase ${params.id} updated"
                redirect(action:show,id:phaseInstance.id)
            }
            else {
                render(view:'edit',model:[phaseInstance:phaseInstance])
            }
        }
        else {
            flash.message = "Phase not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def phaseInstance = new Phase()
        phaseInstance.properties = params
        return ['phaseInstance':phaseInstance]
    }

    def save = {
        def phaseInstance = new Phase(params)
        if(!phaseInstance.hasErrors() && phaseInstance.save()) {
            flash.message = "Phase ${phaseInstance.id} created"
            redirect(action:show,id:phaseInstance.id)
        }
        else {
            render(view:'create',model:[phaseInstance:phaseInstance])
        }
    }

    def ajaxPhaseForm = {
        render(template:'editPhaseDialog', model:[ phaseInstance: Phase.get(params.id)])
    }

    /****
     *  Temporary ajax call actions
     ****/

    def ajaxSaveOrUpdate = {

        def phaseInstance
        def board = Board.get(params."board.id")

        if( params.id ){
            phaseInstance = Phase.get(params.id)

            if(phaseInstance) {
                

                if(params.version) {
                    def version = params.version.toLong()

                    if(phaseInstance.version > version) {

                        phaseInstance.errors.rejectValue("version", "phase.optimistic.locking.failure", "Another user has updated this Phase while you were editing.")
                        return render(template:'editPhaseDialog',model:[phaseInstance:phaseInstance])
                        
                    }
                }

                phaseInstance.properties = params
               
                if(phaseInstance.validate() && board && phaseInstance.save()) {
                    flash.message = "Phase ${params.id} updated"              
                }
                
                return render(template:'editPhaseDialog',model:[phaseInstance:phaseInstance])
                
            }
            else {
                flash.message = "Phase not found with id ${params.id}"
                return render(template:'editPhaseDialog',model:[phaseInstance:phaseInstance])
            }

        }else{
            phaseInstance = new Phase(params)

            if(phaseInstance.validate() && board && board.addToPhases(phaseInstance) && phaseInstance.save()) {
                
                flash.message = "Phase ${phaseInstance.name} saved successfully"
            }
            else {
                flash.message = null
            }
            render(template:'editPhaseDialog',model:[phaseInstance:phaseInstance])
        }
    }

    def ajaxDelete = {

        if( params.id ){

            def phase = Phase.get(params.id)

            if( phase ){

                if( phase.cards.size() == 0 ){
                    phase.board.phases.remove(phase)
                    phase.delete()
                    return render(status: 200, text: "Phase with id $params.id deleted")
                }else{
                    return render(status: 400, text: "You can't delete a phase that holds cards")
                }

            }else{
                return render(status: 404, text: "There is no phase with id $params.id")
            }

        }else{
            return render(status: 400, text: "You must specify an id")
        }
    }

}
