// Place your Spring DSL code here
beans = {
  sessionRegistry(org.springframework.security.concurrent.SessionRegistryImpl)

  sessionController(org.springframework.security.concurrent.ConcurrentSessionControllerImpl) {
     maximumSessions = -1
     sessionRegistry = ref('sessionRegistry')
  }
}