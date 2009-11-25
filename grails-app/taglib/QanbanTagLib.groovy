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

    def autoCrop = { attrs, body ->
        int maxChars = attrs.maxChars ? attrs.maxChars as Integer : 100

        if(body.it.description.length() > maxChars) {

            def text = body.it.description.substring(0, maxChars)
            cropText(text)
        }
        else {
            out << body.it.description
        }
    }

    void cropText(text) {
        text = text + "..."
        out << text
    }

}