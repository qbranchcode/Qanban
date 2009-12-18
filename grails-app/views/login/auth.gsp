<head>
  <meta name='layout' content='login' />
  <title>Login</title>
  <g:javascript library="jquery"/>
  <script type="text/javascript">

    $(function() {

      $(".tabLink").click(function(event){
        $link = $(this);
        xPosition = "-" + (( $link.attr('href').split("-")[1] - 1 ) * 800) + "px";

        $(".tabLink").removeClass('active');
        $link.addClass('active');

        $("#tabs").animate({
          left: xPosition,
          duration: 500
        });

        event.preventDefault();

      });

      jQuery.each(jQuery.browser, function(i, val) {
        if( i=="msie" && val == true){
          var $warn = $("<div id='msiewarning'><span class='red'>Sorry, Qanban ${version} doesn't support Internet Explorer.</span><br/> Qanban is currently being developed with focus on compatibility with Firefox, Safari and Chrome. Log in at your own risk :)</div>");
          $('#login').prepend($warn);

        }
      });

    });



  </script>

</head>

<body>
<div id="top">
  <div id="header">
    <div class="wrap">
      <div id="logo">
        <h1>
          <span class="red">Q</span>ANBAN
        </h1>

      </div>
      <div id="mainmenu">
        <ul>
          <li>
            <a href="#tabs-1" class="tabLink active"><g:message code="loginPage.tab.1"/></a>
          </li>
          <li>
            <a href="#tabs-2" class="tabLink"><g:message code="loginPage.tab.2"/></a>
          </li>
          <li>
            <a href="#tabs-3" class="tabLink"><g:message code="loginPage.tab.3"/></a>
          </li>
        </ul>
      </div>
      <div class="version">v ${version}</div>
    </div>
  </div>

</div>

<div id="content" class="wrap">

  <div id="tabs">
    <div class="huge red">Q</div>
    <div id="tabs-1" class="tab" >

      <div class="col-1">
        <g:if test='${flash.message}'>
          <div class='login_message'>${flash.message}</div>
        </g:if>
        <div id="login" class="framed">
          <g:render template="login" model="[postUrl:postUrl,person:person]" />

        </div>
      </div>

      <div class="col-2">

        <h2>
          <g:message code="lorem.qanban"/>
        </h2>
        <p>
          <g:message code="loginPage.aboutQanban.pt1"/>
          <a href="http://blog.crisp.se/henrikkniberg/2009/04/03/1238795520000.html" target="_blank"><g:message code="loginPage.aboutQanban.crisp"/></a>
        </p>
        <br/>
        <p>
          <g:message code="loginPage.aboutQanban.qanbanTeam"/>
        </p>
      </div>
    </div>

    <div id="tabs-2" class="tab">

      <div class="wideCol">

        <div class="solid">
          <div class="bg"></div>
          <div class="content" id="registerFormRemote">
            <g:render template="register" model="[person:person]" />
          </div>
        </div>

      </div>
    </div>

    <div id="tabs-3" class="tab">

      <div class="col-2">
        <h2>
          <g:message code="lorem.qanban"/>
        </h2>
        <p>
          <g:message code="aboutPage.aboutQanban.pt1"/>
          <a href="http://github.com/qbranchcode/Qanban" target="_blank"><g:message code="aboutPage.aboutQanban.github"/></a>.
          <g:message code="aboutPage.aboutQanban.pt2"/>
          <a href="http://code.qbranch.se/post/listTag?selectedTag=qanban" target="_blank"><g:message code="aboutPage.aboutQanban.code"/></a>.
        </p>
        <br/>
        <p>
          <g:message code="aboutPage.aboutQanban.pt3"/>
        </p>
        <br/>
        <p>
          <g:message code="aboutPage.aboutQanban.qanbanTeam"/>
        </p>
      </div>
    </div>

    <div class="leveler"></div>
  </div>
</div>

<script type='text/javascript'>
  <!--
  (function(){
    document.forms['loginForm'].elements['j_username'].focus();
  })();
  // -->
</script>
</body>
