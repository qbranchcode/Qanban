
package se.qbranch.qanban

import grails.converters.*


class CardController {


    /*****
     *  C - R - U - D
     ****/
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    //static allowedMethods = [delete:'POST', save:'POST', update:'POST']


    def save = {
        def cardInstance = new Card(params)
        def phase = findFirstPhase()
        if(cardInstance.validate() && phase && phase.addToCards(cardInstance) && cardInstance.save()) {
            flash.message = "Card ${cardInstance.id} created"
            return redirect(controller: 'mainView', action: 'view')
        }
        else {
            return render(view:'create',model:[cardInstance:cardInstance])
        }
    }


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
                  
                    withFormat{

                        html{
                            flash.message = "Card ${params.id} updated"
                            return redirect(action:show,id:cardInstance.id)
                        }

                        js{
                            return render ([ cardInstance : cardInstance  ] as JSON)
                        }

                        xml{
                            return render ([ cardInstance : cardInstance  ] as XML)
                        }

                    }
                    
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
            def phase = findFirstPhase()
            
            if(cardInstance.validate() && phase && phase.addToCards(cardInstance) && cardInstance.save()) {

                withFormat{
                    html{
                        flash.message = "Card ${cardInstance.id} created"
                        return redirect(controller: 'mainView', action: 'view')
                    }

                    js{
                        return render ([ cardInstance : cardInstance  ] as JSON)
                    }

                    xml{
                        return render ([ cardInstance : cardInstance  ] as XML)
                    }
                }
                
            }
            else {

                withFormat{

                    html{
                        return render(view:'create',model:[cardInstance:cardInstance])
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
        }
    }


    /****
     * Pure view related actions
     *
     ****/

    def create = {
        def cardInstance = new Card()
        cardInstance.properties = params
        return ['cardInstance':cardInstance]
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

    def ajaxShowForm = {
        if(params.id == null)
        render(template:'cardForm', model: [ boardInstance: Board.get(params."board.id")])
        else
        render(template:'cardForm', model: [ boardInstance: Board.get(params."board.id"), cardInstance: Card.get(params.id)])
    }

    /****
     *  Temporary ajax call actions
     ****/

    def ajaxSave = {
        def cardInstance = new Card(params)
        def phase = cardInstance.phase
        if(cardInstance.validate() && phase && phase.addToCards(cardInstance) && cardInstance.save()) {
            flash.message = "Card ${cardInstance.title} registered"
        }
        else {
            flash.message = null
        }
        render(template:'cardForm',model:[cardInstance:cardInstance, boardInstance: cardInstance.phase.board])
    }
    Phase findFirstPhase() {
        def board  = Board.get(1)
        def phase = Phase.get(board.phases[0].id)
        return phase
    }
}
