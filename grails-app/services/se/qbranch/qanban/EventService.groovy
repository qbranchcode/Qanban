package se.qbranch.qanban

class EventService {

    boolean transactional = true


    def persist(event) {

        if( event.save() ){
            event.process()
        }

    }
}
