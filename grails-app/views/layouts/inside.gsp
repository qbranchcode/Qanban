<html>
  <head>
    <title><g:layoutTitle default="Grails" /></title>
    <link rel="stylesheet" href="${resource(dir:'css/custom-theme',file:'jquery-ui-1.7.2.custom.css')}"
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'cardDialog.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'phaseDialog.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'userDialog.css')}" />
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
      height: 28px;
      float: right;
      margin: 1px;
      text-align: right;
      line-height: 14px;
      min-width: 100px;
    }

    #logout a{
      font-weight: bold;
      color: #fff;
    }

    #logout a:hover{
      cursor: pointer;
      color: #fd0000;
    }

    #logout img{
      float: right;
      margin-left: 7px;
      -moz-box-shadow: 0px 0px 6px #B0D0FF;
      -webkit-box-shadow: 0px 0px 6px #B0D0FF;
      border: 1px solid #B0D0FF ;
      cursor: pointer;      
    }

    #logout img:hover{
      -moz-box-shadow: 0px 0px 8px #fd0000 !important;
      -webkit-box-shadow: 0px 0px 8px #fd0000 !important;
      border: 1px solid #fd0000 ;
    }

    #logout .name{
      color: #fff;
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
        <li><a class="tab active" href="${createLink(controller:'mainView',action:'showBoard',params:[ 'board.id': board.id])}"><g:message code="mainView.tabs.board"/></a></li>
        <li><a class="tab" href="${createLink(controller:'mainView',action:'showLog',params:[ 'board.id': board.id])}"><g:message code="mainView.tabs.log"/></a></li>
        <li><a class="tab" href="${createLink(controller:'mainView',action:'showArchive',params:[ 'board.id': board.id])}"><g:message code="mainView.tabs.archive"/></a></li>
      </ul>
    </div>
    
    <div id="logout">
      <avatar:gravatar email="${loggedInUser.email}" size="26" />
      <span class="name">${loggedInUser.userRealName}</span><br/>
      <a href="${createLink(controller:'logout')}"><g:message code="layout.inside.menu.logOut"/></a>
    </div>

    <!--<img id="logo" src="${resource(dir:'images',file:'small_logo.png')}" alt="qanban"/>-->
    <!--<img id="logo" src="${resource(dir:'images',file:'qanban_jap_small.png')}" alt="qanban"/>-->
    <!--<img id="logo" src="${resource(dir:'images',file:'qanban_jap_small_w.png')}" alt="qanban"/>-->
    <!--<img id="logo" src="${resource(dir:'images',file:'qanban_red_small.png')}" alt="qanban"/>-->
    <img id="logo" src="${resource(dir:'images',file:'qanban_small.png')}" alt="qanban"/>

    
  </div>



<g:layoutBody />

</body>
</html>