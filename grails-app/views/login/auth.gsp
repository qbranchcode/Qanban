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
              <a href="#tabs-1" class="tabLink active">Logga in</a>
            </li>
            <li>
              <a href="#tabs-2" class="tabLink">Skapa konto</a>
            </li>
            <li>
              <a href="#tabs-3" class="tabLink">Om <span class="red">Q</span>ANBAN</a>
            </li>
          </ul>
        </div>
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

            <h2>LOGGA IN<span class="big red">:</span></h2>
            <form action='${postUrl}' method='POST' id='loginForm'>
              <div class="fieldWrapper">
                <label for='j_username'>U<span class="big red">:</span></label>
                <input type='text' class='text_' name='j_username' id='j_username' value='${request.remoteUser}' />
              </div>
              <div class="fieldWrapper">
                <label for='j_password'>P<span class="big red">:</span></label>
                <input type='password' class='text_' name='j_password' id='j_password' />
              </div>

              <div class="checkboxWrapper">
                
                <label for='remember_me'>Kom ihåg mig</label>

                <input type='checkbox' class='chk' name='_spring_security_remember_me' id='remember_me'
                     <g:if test='${hasCookie}'>checked='checked'</g:if> />
              </div>

              <input type='submit' value='Login' />

            </form>
          </div>
        </div>

        <div class="col-2">
          <h2>
            <span class="red">Q</span>ANBAN
          </h2>
          <p>
            <span class="red">Q</span>anban är ett hjälpverktyg för att underlätta arbetet med projekt som nyttjar metoden kanban, där man får möjligheten att sköta hanteringen av sina kanban-kort, och få en strukturerad vy av sin tavla. Man kan även hämta ut information baserad på historiken av projektet så som var flaskhalsarna uppstått, i vilka situationer och hur teamet sett ut under den perioden och vart det krävts mera arbetskraft.
          </p>
          <p>
							öoiashdoiöashdöoasidh oais doöiash dahls diluashilughdsifgu dsiufg sdgk iusdg fiudgs fiu ldsf gdsfk sdfk sdfk <a href="#">hej</a>							</p>


        </div>
      </div>

      <div id="tabs-2" class="tab">

        <div class="wideCol">

          <div class="solid">
            <div class="bg"></div>
            <div class="content">
              <h2>REGRISTRERA ANVÄNDARE<span class="big red">:</span> </h2>
              <form>
                <ul>
                  <li>
                    <label for="firstName">Förnamn</label>
                    <input name="firstName" type="text"/>
                  </li>

                  <li>
                    <label for="lastName">Efternamn</label>
                    <input name="lastName" type="text"/>
                  </li>
                  <li>
                    <label for="email">E-mail</label>
                    <input name="email" type="text"/>
                  </li>
                  <li>
                    <label for="passwd1">Lösenord</label>
                    <input name="passwd1" type="password"/>
                  </li>
                  <li>
                    <label for="passwd2">Repetera lösenord</label>
                    <input name="passwd2" type="password"/>
                  </li>
                  <li>
                    <input type="button" value="OK"/>
                  </li>
                </ul>
              </form>
            </div>
          </div>

        </div>
      </div>

      <div id="tabs-3" class="tab">
        <div class="col-2">
          <h2>
            <span class="red">Q</span>ANBAN
          </h2>
          <p>
            <span class="red">Q</span>anban är ett hjälpverktyg för att underlätta arbetet med projekt som nyttjar metoden kanban, där man får möjligheten att sköta hanteringen av sina kanban-kort, och få en strukturerad vy av sin tavla. Man kan även hämta ut information baserad på historiken av projektet så som var flaskhalsarna uppstått, i vilka situationer och hur teamet sett ut under den perioden och vart det krävts mera arbetskraft.
          </p>
          <p>
							öoiashdoiöashdöoasidh oais doöiash dahls diluashilughdsifgu dsiufg sdgk iusdg fiudgs fiu ldsf gdsfk sdfk sdfk <a href="#">hej</a>							</p>


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
