### Description

Spring core is a framework for Dependency Injection, which stems from single responsibility and loose coupling.


### Spring workflow

- Transition 1: User send request to server by submitting form/ by clicking hyperlink etc. Request is intially givn to web.xml.
- Transition 2: web.xml routes request to DispatcherServlet by looking at tag.
- Transition 3: DispatcherServlet wil take help of HandlerMapping and get to know the controller class name associated with the given request.
- Transition 4: So request transfer to the controller, and then controller will process the request by executing appropriate method and returns ModelAndView object(contains Model data and View name) back to the DispatcherServlet.
- Transition 5: Now DispatcherServlet send the model object to ViewResolver to get the acutal view page.
- Transition 6: Finally DispatcherServlet will pass the Model object to View page to display the result.

