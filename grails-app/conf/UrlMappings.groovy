class UrlMappings {
    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
			 // apply constraints here
            }
        }

        "/rest/card/$id"(controller:"card"){

            action = [GET:"show", DELETE:"delete", POST:"saveOrUpdate", PUT:"saveOrUpdate"]

        }

        "/"(controller:'mainView',action:'index')

        "500"(view:'/error')
    }
}