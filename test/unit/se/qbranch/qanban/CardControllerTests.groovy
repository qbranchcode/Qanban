package se.qbranch.qanban

import grails.test.*

class CardControllerTests extends ControllerUnitTestCase {

    def eventServiceMock
    def securityServiceMock

    def user1
    def user2

    def board

    def phase1
    def phase2
    def phase3

    def card1onPhase1
    def card2onPhase1
    def card3onPhase2


     protected void setUp() {
        super.setUp()

        // User mock

        user1 = new User(username: "opsmrkr01", userRealName: "Mr. Krister")
        user2 = new User(username: "opsshba01", userRealName: "Shean Banan")

        mockDomain(User,[user1,user2])


        // Board mock

        board = new Board()
        mockDomain(Board,[board])


        // Phase / PhaseEventCreate mock

        mockDomain(PhaseEventCreate)
        mockDomain(Phase)

        def phaseEventCreate1 = new PhaseEventCreate(title: "First phase", cardLimit: 5, position: 0, user: user1, board: board)
        def phaseEventCreate2 = new PhaseEventCreate(title: "Second phase", cardLimit: 10, position: 1, user: user1 , board: board)
        def phaseEventCreate3 = new PhaseEventCreate(title: "Third phase", user: user1, position: 2, board: board)

        phaseEventCreate1.beforeInsert()
        phaseEventCreate1.save()
        phaseEventCreate1.process()

        phaseEventCreate2.beforeInsert()
        phaseEventCreate2.save()
        phaseEventCreate2.process()

        phaseEventCreate3.beforeInsert()
        phaseEventCreate3.save()
        phaseEventCreate3.process()

        phase1 = phaseEventCreate1.phase
        phase2 = phaseEventCreate2.phase
        phase3 = phaseEventCreate3.phase

        assertEquals phase1, Phase.findByDomainId(phase1.domainId)

        // Card / CardEventCreate mock

        mockDomain(CardEventCreate)
        mockDomain(Card)

        def cardEventCreate1 = new CardEventCreate(title:"Card #1",caseNumber:1,description:"The first card originally from First phase on the first position",phaseDomainId:phase1.domainId,user:user1)
        def cardEventCreate2 = new CardEventCreate(title:"Card #2",caseNumber:2,description:"The second card originally from First phase on the second position",phaseDomainId:phase1.domainId,user:user1)
        def cardEventCreate3 = new CardEventCreate(title:"Card #3",caseNumber:3,description:"The third card originally from Second phase on the first position",phaseDomainId:phase2.domainId,user:user1)

        cardEventCreate1.beforeInsert()
        cardEventCreate1.save()
        cardEventCreate1.process()

        cardEventCreate2.beforeInsert()
        cardEventCreate2.save()
        cardEventCreate2.process()

        cardEventCreate3.beforeInsert()
        cardEventCreate3.save()
        cardEventCreate3.process()

        card1onPhase1 = cardEventCreate1.card
        card2onPhase1 = cardEventCreate2.card
        card3onPhase2 = cardEventCreate3.card

        // Assertions to validate the mock setup

        board.phases.each {
            println it
            it.cards.each {
                println "   $it"
            }
        }

        assertEquals 1, board.id
        assertEquals 3, board.phases.size()
        assertEquals 1, phase1.id
        assertEquals 2, phase1.cards.size()
        assertEquals 2, phase2.id
        assertEquals 1, phase2.cards.size()
        assertEquals 3, phase3.id
        assertEquals 1, card1onPhase1.id
        assertEquals 2, card2onPhase1.id
        assertEquals 3, card3onPhase2.id

        securityServiceMock = mockFor(SecurityService)
        securityServiceMock.demand.getLoggedInUser() { -> return user1 }
        controller.securityService = securityServiceMock.createMock()

        eventServiceMock = mockFor(EventService)
        eventServiceMock.demand.persist() { event ->
            event.beforeInsert()
            if( event.save() ){
                event.process()
            }
        }
        
        controller.eventService = eventServiceMock.createMock()

    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCardShouldNotSaveWithoutBoardId(){

        mockParams.title = "Title"
        mockParams.caseNumber =  "1"
        mockParams.description = "My testcard"

        def model = controller.create()
        println "status: $renderArgs.status"
        assertEquals 400, renderArgs.status

    }

    void testCardShouldSave() {
        
        mockParams.title = "Title"
        mockParams.caseNumber =  "1"
        mockParams.description = "My testcard"
        mockParams.boardId = "1"

        def model = controller.create()
        
        def card = Card.findByTitle("Title")

        assertEquals "My testcard" , card.description
        assertEquals "First phase", card.phase.title
        assertEquals "Title", Phase.get(1).cards[2].title
        assertEquals card.caseNumber, CardEventCreate.findByDomainId(card.domainId).caseNumber
    }


//    void testSaveWithEmptyForm() {
//        def cmd = new CreateCardCommand()
//        cmd.validate()
//        controller.create(cmd);
//        assertEquals true, cmd.hasErrors()
//    }
//
//    void testShowMethodWithoutId() {
//        controller.show()
//        assertEquals 400, renderArgs.status
//    }
//
//    void testShowMethodWithUnexistingId() {
//        mockParams.id = "220"
//        controller.show()
//        assertEquals 404, renderArgs.status
//    }





}
