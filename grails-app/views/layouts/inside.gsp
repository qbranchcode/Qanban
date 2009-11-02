<html>
  <head>
    <title><g:layoutTitle default="Grails" /></title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
  <g:layoutHead />
  <g:javascript library="application" />
</head>
<body>

  <div id="menu">

    <div class="background">
      <span class="red">Q</span>ANBAN
    </div>

    <ul>
      <li><a href="${createLink(controller:'card',action:'create')}">Add Card</a></li>
      <li><a href="${createLink(controller:'logout')}">Log Out</a></li>
      <li><span id="debug"></span></li>
    </ul>

  </div>

  <g:layoutBody />
  
</body>
</html>