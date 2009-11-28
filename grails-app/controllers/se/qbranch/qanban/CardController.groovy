package se.qbranch.qanban

import grails.converters.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class CardController {

    def authenticateService

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

    @Secured(['ROLE_QANBANADMIN'])
    def delete = {

        def cardInstance = Card.get( params.id )

        if( !params.id )
        return render(status: 400, text: "You must specify an id")

        if(cardInstance) {
            try {
//                cardInstance.phase.cards.remove(cardInstance)
//                cardInstance.delete(flush:true)
                CardEventDelete(cardInstance).save()
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

    def update = { UpdateCardCommand cmd ->

        if(cmd.hasErrors()) {
            return render([result: false] as JSON)
        }else {
            createCardEventUpdate(cmd)
            cardUpdatedOrSaved(cmd.card, "Card ${params.id} updated")
        }
    }

    def saveOrUpdate = {
        def cardInstance

        // Update
        if( params.id ){
            cardInstance = Card.get( params.id )
            def phase = Phase.get(params."phase.id")

            if(cardInstance) {

                if(params.version) {
                    def version = params.version.toLong()
                    if(cardInstance.version > version) {

                        cardInstance.errors.rejectValue("version", "card.optimistic.locking.failure", "Another user has updated this Card while you were editing.")
                        return render(view:'edit',model:[cardInstance:cardInstance])
                    }
                }

                cardInstance.properties = params

                if(cardInstance.validate() && phase && cardInstance.save()) {
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

    void createCardEventUpdate(cmd) {
        def cardEventUpdate = new CardEventUpdate(
            card: cmd.card,
            title: cmd.title,
            description: cmd.description,
            caseNumber: cmd.caseNumber,
            assignee: cmd.assignee,
            user: authenticateService.userDomain())
        cardEventUpdate.save()
    }

    /****
     * Pure view related actions
     *
     ****/

    def ajaxShowForm = {

        if(params.id == null){
            render(template:'cardForm', model: [ boardInstance: Board.get(params."board.id"), userList: User.list()])
        
        }else if( params.newPhase == null ){
            def card = Card.get(params.id)
            def events = Event.findAllByDomainId(card.domainId)
            println "no of events: ${events.size()}"
            render(template:'cardForm', model: [ boardInstance: Board.get(params."board.id"), userList: User.list(), cardInstance: card, events: events ])
	}else{
            render(template:'cardForm', model: [ boardInstance: Board.get(params."board.id"), cardInstance: Card.get(params.id), newPhase: params.newPhase , newPos: params.newPos, userList: User.list(), loggedInUser: User.get(params."user") , user: params."user", events: events])

        }
        
    }

    def ajaxSave = {
        def cardInstance = new Card(params)
        def phase = cardInstance.phase
        if(cardInstance.validate() && phase && phase.addToCards(cardInstance) && cardInstance.save()) {
            flash.message = "Card ${cardInstance.title} registered"
        } else {
            flash.message = null
        }
        render (template: 'cardForm', model: [cardInstance:cardInstance, boardInstance:cardInstance.phase.board])
    }

    @Secured(['ROLE_QANBANADMIN'])
    def ajaxDelete = {

        if( params.id ){

            def card = Card.get(params.id)

            if( card ){
                card.phase.cards.remove(card)
                card.delete()
                return render(status: 200, text: "Card with id $params.id deleted")
            }else{
                return render(status: 404, text: "There is no card with id $params.id")
            }

        }else{
            return render(status: 400, text: "You must specify an id")
        }
    }
}

class UpdateCardCommand {

    static constraints = {
        id(min: 0, nullable: false, validator:{ val, obj ->
                Card.exists(obj.id)
            })
        assigneeId(min: 0, nullable: true, validator:{ val, obj ->
                !val || User.exists( val )
            })
    }

    Integer id
    String description
    String title
    Integer caseNumber
    Integer assigneeId

    def getCard() {
        Card.get(id)
    }

    def getAssignee() {
        User.get(assigneeId)
    }

}