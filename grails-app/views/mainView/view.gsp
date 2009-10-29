<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ import="se.qbranch.qanban.Board" %>

<html>
  <head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <g:javascript library="jquery"/>
 
    <script type="text/javascript" src="/Qanban/js/jquery/jquery.ui.core.js"></script>
    <script type="text/javascript" src="/Qanban/js/jquery/jquery.ui.sortable.js"></script>
    <jq:jquery>

      $('.phase').sortable({ 
        connectWith: '.phase', 
        update: function(event,ui){
          var newPos = ui.item.prevAll().length;
          var cardId = ui.item.attr('id').split('_')[1];
          $.post('moveCard',{'id': cardId , 'moveTo' : newPos },"json");
        }  
      });
      
    </jq:jquery>
    
    <style type="text/css">

      

* {
 margin: 0px;
 padding: 0px;
}

.leveler{ clear: both; }

body {
 background-color: #f2f2f2;
 font-size: 62.5%;
 font-family: Arial;
}

#container{
}

#board {
 width: 80em;
 margin: 0 auto;
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

ul.phase {
 list-style: none;
 height: 100px;
}

.card{ border: 1px solid; margin: 1px 2px 0 2px; background-color: yellow; text-align: center;}

.widthForcer { width:${100/board.phases.size()-1}%; margin: 0 0.5%;}

    </style>
    <title>Qanban</title>
  </head>
  <body>

      <div id="container">
        <div id="board">

          <g:each var="phase" in="${board.phases}">
            <div class="phaseHolder widthForcer">
              <h2>${phase.name}</h2>
              <ul class="phase" id="phase_${phase.id}">

               <g:each var="card" in="${phase.cards}">

                 <li class="card" id="card_${card.id}">${card.title}</li>

               </g:each>

              </ul>

            </div>

          </g:each>

          <div class="leveler"></div>

        </div>

      </div>

  </body>
</html>
