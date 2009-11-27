

package se.qbranch.qanban

import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class BoardController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ boardInstanceList: Board.list( params ), boardInstanceTotal: Board.count() ]
    }

    def show = {
        def boardInstance = Board.get( params.id )

        if(!boardInstance) {
            flash.message = "Board not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ boardInstance : boardInstance ] }
    }

    @Secured(['ROLE_QANBANADMIN'])
    def delete = {
        def boardInstance = Board.get( params.id )
        if(boardInstance) {
            try {
                boardInstance.delete(flush:true)
                flash.message = "Board ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Board ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Board not found with id ${params.id}"
            redirect(action:list)
        }
    }

    @Secured(['ROLE_QANBANADMIN'])
    def edit = {
        def boardInstance = Board.get( params.id )

        if(!boardInstance) {
            flash.message = "Board not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ boardInstance : boardInstance ]
        }
    }

    @Secured(['ROLE_QANBANADMIN'])
    def update = {
        def boardInstance = Board.get( params.id )
        if(boardInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(boardInstance.version > version) {
                    
                    boardInstance.errors.rejectValue("version", "board.optimistic.locking.failure", "Another user has updated this Board while you were editing.")
                    render(view:'edit',model:[boardInstance:boardInstance])
                    return
                }
            }
            boardInstance.properties = params
            if(!boardInstance.hasErrors() && boardInstance.save()) {
                flash.message = "Board ${params.id} updated"
                redirect(action:show,id:boardInstance.id)
            }
            else {
                render(view:'edit',model:[boardInstance:boardInstance])
            }
        }
        else {
            flash.message = "Board not found with id ${params.id}"
            redirect(action:list)
        }
    }

    @Secured(['ROLE_QANBANADMIN'])
    def create = {
        def boardInstance = new Board()
        boardInstance.properties = params
        return ['boardInstance':boardInstance]
    }

    @Secured(['ROLE_QANBANADMIN'])
    def save = {
        def boardInstance = new Board(params)
        if(!boardInstance.hasErrors() && boardInstance.save()) {
            flash.message = "Board ${boardInstance.id} created"
            redirect(action:show,id:boardInstance.id)
        }
        else {
            render(view:'create',model:[boardInstance:boardInstance])
        }
    }
}
