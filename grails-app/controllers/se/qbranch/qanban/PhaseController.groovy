

package se.qbranch.qanban

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
}
