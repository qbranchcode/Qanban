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
    <title>Qanban</title>
  </head>
  <body>
    internet?
    
    <div id="wrapper">
      <div id="container">

        <div id="board">

          <g:each var="phase" in="${board.phases}">

            <ul class="phase" id="phase_${phase.id}">

              <g:each var="card" in="${phase.cards}">

                <li class="card" id="card_${card.id}">${card.title}</li>

              </g:each>

            </ul>

          </g:each>

        </div>


      </div>
    </div>
  </body>
</html>
