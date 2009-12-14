package se.qbranch.qanban

class BoardEvent extends Event{

    static constraints = {
    }

    transient public String checkCurrentTitle() {
        if( domainId ){
            def board = Board.findByDomainId(domainId)
            if (board){
                return board.title
            }else{
                return 'The board does not exist'
            }
        }
        else return "This should not be a valid output"
    }
  
    public boolean doesDomainExist(){
        return Board.findByDomainId(domainId) != null
    }

}
