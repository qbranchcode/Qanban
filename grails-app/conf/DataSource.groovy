import javax.naming.InitialContext
import javax.naming.NameNotFoundException

/*
* Copyright 2009 Qbranch AB
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='com.opensymphony.oscache.hibernate.OSCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
          try {
            InitialContext ctx = new InitialContext()
            javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup( "java:comp/env/jdbc/qanban" )

            dbCreate = "update"
            jndiName = "java:comp/env/jdbc/qanban"
          } catch(NameNotFoundException e) {
            dbCreate = "create-drop"
            url = "jdbc:hsqldb:file:qanbanDevDb;shutdown=true"
          }
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
          //IF (JNDI) USE JNDI
          //ELSE
          //jndiName = "java:comp/env/jdbc/qanban
          try {
              InitialContext ctx = new InitialContext()
              javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup( "java:comp/env/jdbc/qanban" )

              dbCreate = "update"
              jndiName = "java:comp/env/jdbc/qanban"
            } catch(NameNotFoundException e) {
              dbCreate = "update"
              url = "jdbc:hsqldb:file:qanbanProdDb;shutdown=true"
            }
          }
    }
    dbdiff {
		dataSource {
			dbCreate = "create-drop"
			url = "jdbc:hsqldb:mem:diffDb"
		}
	}
}