package se.qbranch.qanban

class PhaseEvent extends Event {

    static constraints = {
    }

    transient public String checkCurrentTitle() {
        if( domainId ){
            def phase = Phase.findByDomainId(domainId)
            if (phase){
                return phase.title
            }else{
                return PhaseEventDelete.findByDomainId(domainId).title
            }
        }
        else return "This should not be a valid output"
    }

    public boolean doesDomainExist(){
        return Phase.findByDomainId(domainId) != null
    }
}
