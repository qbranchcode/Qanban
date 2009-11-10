class QanbanTagLib {

    def maxCardCount = { attrs ->
        def maxNumberOfCards = 0
        attrs.phases?.each{
            it.cards.size() > maxNumberOfCards ? maxNumberOfCards = it.cards.size() : maxNumberOfCards
        }

        if( attrs.cardHeight ){
            if( attrs.unit ){
                out << maxNumberOfCards * ( attrs.cardHeight as Integer ) + attrs.unit
            }else{
                out << maxNumberOfCards * ( attrs.cardHeight as Integer )
            }
        }else{
            out << maxNumberOfCards
        }

    }

}
