<%@ page import="org.joda.time.Minutes;org.joda.time.format.DateTimeFormatter;" contentType="text/html;charset=UTF-8"%>

  <script type="text/javascript">
    
  jQuery().ready(function(){

    var windowWidth = $(window).width();
    var wrapperHeight = $('#wrapPlacer').height();
    
    $('.statsBox').width( (windowWidth-100) + 'px').height( (wrapperHeight/2) - 60 + 'px');
    $('.chart').width( ( windowWidth-100 ) + 'px').height(  (wrapperHeight/2) - 140 + 'px' );

    var leadLine = [<g:each in="${intervalLeadMap}" status="i" var="set">['${dateFormatter.print(set.key.start)}',<g:if test="${set.value.hours == 0}">-50</g:if><g:else>${set.value.hours}</g:else>]<g:if test="${i < intervalLeadMap.size()}">,</g:if></g:each>];
    var meanLeadLine = [<g:each in="${intervalMeanLeadMap}" status="i" var="set">['${dateFormatter.print(set.key.start)}',${set.value.hours}]<g:if test="${i < intervalMeanLeadMap.size()}">,</g:if></g:each>];

    var leadPlot = $.jqplot('leadChart',[leadLine,meanLeadLine],{
      axes:{
        xaxis:{
          renderer: jQuery.jqplot.DateAxisRenderer,
          label: 'Timeline',
          tickInterval: '2 days'
        },
        yaxis:{
          label: 'Hours',
          min: 0,
          max: 100,
          ticks: ${leadTicks}
        }
      },
      seriesDefaults:{
        trendline:{
          show: false
        }
      },
      series: [
        {
          label: 'Working lead time',
          showLine: false,
          color: '#3173AA'
        },
        {
          label: 'Mean lead time',
          lineWidth: 3,
          color: '#00CCFF',
          markerOptions: {
            size: 6
          },
          trendline: {
            show: true,
            color: '#bbbbbb',
            lineWidth: 2,
            shadow: false
          }
        }
      ],
      legend: {
        show: true,
        location: 'nw'
      },
      highlighter: {
        sizeAdjust: 7.5
      }
    });
    
    var cycleLine = [<g:each in="${intervalCycleMap}" status="i" var="set">['${dateFormatter.print(set.key.start)}',<g:if test="${set.value.hours == 0}">-50</g:if><g:else>${set.value.hours}</g:else>]<g:if test="${i < intervalCycleMap.size()}">,</g:if></g:each>];
    var meanCycleLine = [<g:each in="${intervalMeanCycleMap}" status="i" var="set">['${dateFormatter.print(set.key.start)}',${set.value.hours}]<g:if test="${i < intervalMeanCycleMap.size()}">,</g:if></g:each>];

    window.console.log(cycleLine);
    window.console.log(meanCycleLine);

    var cyclePlot = $.jqplot('cycleChart',[cycleLine,meanCycleLine],{
      axes:{
        xaxis:{
          renderer:jQuery.jqplot.DateAxisRenderer,
          label: 'Timeline',
          tickInterval: '2 days'
        },
        yaxis:{
          label: 'Hours',
          min: 0,
          max: 100,
          ticks: ${cycleTicks},
          tickOptions: {
            formatString: '%d'
          }
        }
      },
      seriesDefaults:{
        trendline:{
          show: false
        }
      },
      series:[
        {
          label: 'Working cycle time',
          showLine: false,
          color: '#660000'
        },
        {
          label: 'Mean cycle time',
          lineWidth: 3,
          color: '#fd0000',
          markerOptions: {
            size: 6            
          },
          trendline: {
            show: true,
            color: '#bbbbbb',
            lineWidth: 2,
            shadow: false
          }
        }
      ],
      legend: {
        show: true,
        location: 'nw'
      },
      highlighter: {
        sizeAdjust: 7.5
      }
    });
  });

  </script>

<div id="statistics">

  <div id="leadTime" class="statsBox">

    <div class="header">
      <div class="currentValues">
        <h4><g:message code="currentLeadTime.header"/></h4>
        <p>${leadTime} days</p>
      </div>
      
      <h2><g:message code="leadTime"/></h2>
    </div>

    <div id="leadChart" class="chart"></div>

  </div>

  <div id="cycleTime" class="statsBox">
    
    <div class="header">
      <div class="currentValues">
        <h4><g:message code="currentCycleTime.header"/></h4>
        <p>${cycleTime} days</p>
      </div>
      <h2><g:message code="cycleTime"/></h2>

    </div>

    <div id="cycleChart" class="chart"></div>

  </div>

</div>
