<html>
  <head>
    <title><g:layoutTitle default="Grails" /></title>
    <link rel="stylesheet" href="${resource(dir:'css/jq_theme_blitzer',file:'jquery-ui.css')}"
          <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'cardDialog.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'phaseDialog.css')}" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
  <g:layoutHead />
  <g:javascript library="application" />
  <style type="text/css">

    #thinTop{
      background-image: url(../images/head_fade.png);
      background-repeat: repeat-x;
      background-position: 0 -135px;
      height: 32px;
      padding: 3px 8px 0;
      border-bottom: 1px solid #fa0000;
      position: relative;
    }

    #logout{
      height: 22px;
      float: right;
      margin: 6px 16px 0 0;
    }

    #logout a{
      font-weight: bold;
      color: #fff;
      line-height: 22px;
    }

    #logout a:hover{
      cursor: pointer;
      color: #fd0000;
    }

    #adminmenu{
      width: 300px;
      margin: 0 auto;
      height: 20px;
      position: relative;
      border: 1px solid #fd0000;
      border-top: 1px solid #666;
      top: -1px;
      background-image: url(../images/head_fade.png);
      background-repeat: repeat-x;
      background-position: 0 0px;
      -webkit-border-bottom-right-radius: 6px;
      -moz-border-radius-bottomright:6px;
      -webkit-border-bottom-left-radius: 6px;
      -moz-border-radius-bottomleft:6px;
      text-align: center;
    }

    #adminmenu ul{
      display: inline-block;
    }

    #adminmenu li{
      float: left;
      display: inline-block;
      height: 20px;
      margin-right: 3px;
    }

    #adminmenu a{
      line-height: 20px;
      display: inline-block;
      padding: 0 6px;
      outline: none;
      background-image: url(../images/head_fade.png);
      background-repeat: repeat-x;
      background-position: 0 0;
      color: #fff;
      font-weight: bold;
    }

    #adminmenu a:hover{
      color: #fd0000;
    }

    #logo{
      display: block;
      position: relative;
      margin: 0 auto;
      clear: both;
      top: -26px;
    }


  </style>
</head>
<body>

  <div id="thinTop">

    <div id="mainmenu">
      <ul>
        <li><a class="active" href="#board"><g:message code="mainView.tabs.board"/></a></li>
        <li><a href="#log"><g:message code="mainView.tabs.log"/></a></li>
      </ul>
    </div>
    
    <div id="logout">
      <a href="${createLink(controller:'logout')}"><g:message code="layout.inside.menu.logOut"/></a>
    </div>

    <img id="logo" src="${resource(dir:'images',file:'small_logo.png')}" alt="qanban"/>
    
  </div>

<g:ifAllGranted role="ROLE_QANBANADMIN">
  <div id="adminmenu">
    <ul>
      <li><a href="${createLink(controller:'card',action:'create')}" class="addCardLink"><g:message code="layout.inside.menu.addCard"/></a></li>
      <li><a href="${createLink(controller:'phase',action:'create')}" class="addPhaseLink"><g:message code="layout.inside.menu.addPhase"/></a></li>
    </ul>
  </div>
</g:ifAllGranted>

<g:layoutBody />

</body>
</html>