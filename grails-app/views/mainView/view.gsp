<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>


<%@ import="se.qbranch.qanban.Card" %>
<%@ import="se.qbranch.qanban.Phase" %>
<%@ import="se.qbranch.qanban.Board" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <g:javascript library="jquery"/>
    <jq:jquery>

      

    </jq:jquery>
    <style type="text/css">
* {
 margin: 0px;
 padding: 0px;
}

body {
 background-color: #f2f2f2;
 font-size: 62.5%;
 font-family: Arial;
}

#container{
 background-color: aqua;
}

.phaseHolder {
 width: 18em;
 float: left;
 margin-right: 3px;
 background-color: #fff;
}

.phaseHolder h2 {
 line-height: 1.1em;
 font-size: 1.4em;
 font-weight: bold;
 text-align: center;
}
    </style>
    <title>Qanban</title>
  </head>
  <body>

      <div id="container">
        <div id="board">
          <g:each var="phase" in="${board.phases}">
            <div class="phaseHolder">
              <h2>${phase.name}</h2>
              <ul class="phase" id="phase_${phase.id}">

               <g:each var="card" in="${phase.cards}">

                 <li class="card" id="card_${card.id}">${card.title}</li>

               </g:each>

              </ul>

            </div>

          </g:each>

        </div>


      </div>

  </body>
</html>
