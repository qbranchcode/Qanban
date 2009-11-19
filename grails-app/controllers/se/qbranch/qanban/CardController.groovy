
package se.qbranch.qanban

import grails.converters.*


class CardController {


    /*****
     *  C - R - U - D
     ****/
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    //static allowedMethods = [delete:'POST', save:'POST', update:'POST']


    def show = {

        if( !params.id )
            return render(status: 400, text: "You must specify an id")
            
        def cardInstance = Card.get( params.id )

        if(!cardInstance)
            return render(status: 404, text: "Card with id ${params.id} not found.")
            

        withFormat {
            
            html {
                return render (template:"card", bean:cardInstance )
            }

            js {
                return render ([ cardInstance : cardInstance  ] as JSON)
            }

            xml {
                return render ([ cardInstance : cardInstance ] as XML )
            }

        }

    }

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)

        withFormat {
            html {
                return [ cardInstanceList: Card.list( params ), cardInstanceTotal: Card.count() ]
            }

            js {
                return render( [ cardInstanceList: Card.list( params ), cardInstanceTotal: Card.count() ] as JSON )
            }

            xml {
                return render( [ cardInstanceList: Card.list( params ), cardInstanceTotal: Card.count() ] as XML )
            }
        }

    }

    def delete = {

        def cardInstance = Card.get( params.id )

        if( !params.id )
        return render(status: 400, text: "You must specify an id")

        if(cardInstance) {
            try {
                cardInstance.phase.cards.remove(cardInstance)
                cardInstance.delete(flush:true)
                return render ("Successfully deleted card with id $params.id.")

            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return render(status: 404, text: "Card not found with id ${params.id}")
            }
        }
        else {
            return render(status: 404, text: "Card not found with id ${params.id}")
        }
    }


    def saveOrUpdate = {
        def cardInstance

        // Update
        if( params.id ){            
            cardInstance = Card.get( params.id )
            if(cardInstance) {
                if(params.version) {
                    def version = params.version.toLong()
                    if(cardInstance.version > version) {

                        cardInstance.errors.rejectValue("version", "card.optimistic.locking.failure", "Another user has updated this Card while you were editing.")
                        return render(view:'edit',model:[cardInstance:cardInstance])
                    }
                }

                cardInstance.properties = params
                if(!cardInstance.hasErrors() && cardInstance.save()) {
                    cardUpdatedOrSaved(cardInstance, "Card ${params.id} updated")
                }
                else {
                    render(view:'edit',model:[cardInstance:cardInstance])
                }
            }
            else {
                flash.message = "Card not found with id ${params.id}"
                redirect(action:list)
            }

        // Save
        }else{
            cardInstance = new Card(params)
            def phase = cardInstance.phase
            if(cardInstance.validate() && phase && phase.addToCards(cardInstance) && cardInstance.save()) {
                cardUpdatedOrSaved(cardInstance, "Card ${cardInstance.id} registred")
            } else {
                cardDidntSave(cardInstance)
            }
        }
    }

    void cardUpdatedOrSaved(cardInstance, message) {
        withFormat{
            html{
                flash.message = message
                return render(template:'cardForm',model:[cardInstance:cardInstance, boardInstance: cardInstance.phase.board])
            }
            js{
                return render ([ cardInstance : cardInstance  ] as JSON)
            }
            xml{
                return render ([ cardInstance : cardInstance  ] as XML)
            }
        }
    }

    void cardDidntSave(cardInstance) {
        withFormat{
            html{
                return render(template:'cardForm',model:[cardInstance])
            }
            js {
                response.status = 500 //Internal Server Error
                return render( "Could not create new Card due to errors:\n ${cardInstance.errors}" )
            }
            xml {
                response.status = 500 //Internal Server Error
                return render( "Could not create new Card due to errors:\n ${cardInstance.errors}" )
            }
        }
    }

    /****
     * Pure view related actions
     *
     ****/

    def ajaxShowForm = {
        if(params.id == null)
        render(template:'cardForm', model: [ boardInstance: Board.get(params."board.id"), userList: User.list()])
        else
        render(template:'cardForm', model: [ boardInstance: Board.get(params."board.id"), cardInstance: Card.get(params.id), userList: User.list()])
    }

    def ajaxSave = {
        def cardInstance = new Card(params)
        def phase = cardInstance.phase
        if(cardInstance.validate() && phase && phase.addToCards(cardInstace) && cardInstance.save()) {
            flash.message = "Card ${cardInstance.title} registered"
        } else {
            flash.message = null
        }
        render (template: 'cardForm', model: [cardInstance:cardInstance, boardInstance:cardInstance.phase.board])
    }
    
}
