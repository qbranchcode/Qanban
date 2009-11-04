class UrlMappings {
    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
			 // apply constraints here
            }
        }

        "/rest/card/$id"(controller:"card"){

            action = [GET:"show", PUT:"update", DELETE:"delete", POST:"save"]
            
        }

        "/"(view:'/index')

        "500"(view:'/error')
    }
}
