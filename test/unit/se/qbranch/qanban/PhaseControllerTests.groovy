package se.qbranch.qanban

import grails.test.*

class PhaseControllerTests extends ControllerUnitTestCase {
    
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

        def phaseEventCreate1 = new PhaseEventCreate(name: "First phase", cardLimit: 5, user: user1, board: board)
        def phaseEventCreate2 = new PhaseEventCreate(name: "Second phase", cardLimit: 10, user: user1 , board: board)
        def phaseEventCreate3 = new PhaseEventCreate(name: "Third phase", user: user1, board: board)

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


    void testSave() {

        mockParams.name = "myPhase"
        mockParams.board = board
        assertEquals 3, Phase.list().size()

        def model = controller.eSave()

        assertEquals 4, Phase.list().size()
        assertEquals "myPhase", model.createEvent.phase.name
    }

    void testSaveWithoutName() {
        mockParams.board = board
        def model = controller.eSave()
        assertEquals 1, model.createEvent.errors.getAllErrors().size()
    }

//    void testSuccessfulAjaxSaveOrUpdateWithId() {
//        mockParams.id = "1"
//        mockParams.cardLimit = "2"
//
//        controller.ajaxSaveOrUpdate()
//
//        assertEquals 2, renderArgs.model.phaseInstance.cardLimit
//        assertEquals 1, renderArgs.model.phaseInstance.id
//
//    }
//
//    void testSuccessfulAjaxSaveOrUpdateWithoutIdAndIndex() {
//
//        mockParams.name = "New Phase"
//        mockParams."board.id" = "1"
//
//        controller.ajaxSaveOrUpdate()
//
//        assertEquals 2, renderArgs.model.phaseInstance.id
//        assertEquals "New Phase", renderArgs.model.phaseInstance.name
//
//    }
//
//    void testSuccessfulAjaxSaveOrUpdateWithoutId()
//     {
//	mockParams.name = "First Phase"
//	mockParams."board.id" = "1"
//	mockParams."phase.idx" = "0"
//
//	controller.ajaxSaveOrUpdate()
//
//        assertEquals 2, renderArgs.model.phaseInstance.id
//	assertEquals "First Phase", renderArgs.model.phaseInstance.name
//	assertEquals 0, renderArgs.model.boardInstance.phases.indexOf(renderArgs.model.phaseInstance)
//     }
//
//
//    void testAjaxSaveOrUpdateWithIllegalId(){
//        mockParams.id = "12"
//        mockParams.cardLimit = "2"
//
//        controller.ajaxSaveOrUpdate()
//
//        assertEquals null, renderArgs.model.phaseInstance
//
//    }
//
//    void testAjaxDelete(){
//        def board = Board.get(1)
//        def phase = Phase.get(1)
//        phase.board = board
//        phase.save()
//
//        mockParams.id = "1"
//
//        controller.ajaxDelete()
//
//        assertEquals 0, Phase.list().size()
//
//    }


}
