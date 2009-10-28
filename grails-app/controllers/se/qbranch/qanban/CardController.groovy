

package se.qbranch.qanban

class CardController {
    
    def index = { redirect(action:create,params:params) }

    static allowedMethods = [save:'POST']

    def show = {
        def cardInstance = Card.get( params.id )

        if(!cardInstance) {
            flash.message = "Card not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ cardInstance : cardInstance ] }
    }
    
/*
    def delete = {
        def cardInstance = Card.get( params.id )
        if(cardInstance) {
            try {
                cardInstance.delete(flush:true)
                flash.message = "Card ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Card ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Card not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def cardInstance = Card.get( params.id )

        if(!cardInstance) {
            flash.message = "Card not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ cardInstance : cardInstance ]
        }
    }

    def update = {
        def cardInstance = Card.get( params.id )
        if(cardInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(cardInstance.version > version) {
                    
                    cardInstance.errors.rejectValue("version", "card.optimistic.locking.failure", "Another user has updated this Card while you were editing.")
                    render(view:'edit',model:[cardInstance:cardInstance])
                    return
                }
            }
            cardInstance.properties = params
            if(!cardInstance.hasErrors() && cardInstance.save()) {
                flash.message = "Card ${params.id} updated"
                redirect(action:show,id:cardInstance.id)
            }
            else {
                render(view:'edit',model:[cardInstance:cardInstance])
            }
        }
        else {
            flash.message = "Card not found with id ${params.id}"
            redirect(action:list)
        }
    }
     */

    def create = {
        def cardInstance = new Card()
        cardInstance.properties = params
        return ['cardInstance':cardInstance]
    }

    def save = {
        def cardInstance = new Card(params)
        if(!cardInstance.hasErrors() && cardInstance.save()) {
            flash.message = "Card ${cardInstance.id} created"
            redirect(action:show,id:cardInstance.id)
        }
        else {
            render(view:'create',model:[cardInstance:cardInstance])
        }
    }
}
