<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<script type="text/javascript" src="js/add-datasource.js"></script>

<a class="back-link img-report-small" href="listCharts.action"><s:text name="link.back.charts"/></a>
 
<br/>   

<div align="center">

		  <div class="important img-chart" id="instructions" style="width: 80%;">		
		    <s:if test="command.equals('add')">
	 `		  	<s:text name="editChart.addChart"/>
	   		</s:if>
	   		<s:if test="!command.equals('add')">
	 			<s:text name="editChart.selectedChart"/> <s:property value="name"/>
	   		</s:if> 
		  </div>
	  
  <form action="editChart.action" name="chartForm" method="post" class="dialog-form" style="width: 85%;"> 
  
  <table class="dialog" >    
      <tr class="a">
        <td class="boldText" width="30%"><s:text name="label.name"/></td>
        <td>
        	&nbsp;
        </td>
        <td><input type="text" size="60" name="name" value="<s:property value="name"/>"></td>
      </tr>
      <tr class="b">
        <td class="boldText" width="30%"><s:text name="label.description"/></td>
        <td>
        	&nbsp;
        </td>
        <td><input type="text" size="60" name="description" value="<s:property value="description"/>"></td>
      </tr>    
      <tr class="a">
        <td class="boldText" width="30%"><s:text name="label.chartType"/></td>
        <td>
        	&nbsp;
        </td>
        <td>
          <select name="chartType">
          	<option value="5" <s:if test="chartType == 5">SELECTED</s:if> >Area</option>
            <option value="0" <s:if test="chartType == 0">SELECTED</s:if> >Bar</option>
            <option value="9" <s:if test="chartType == 9">SELECTED</s:if> >Dial</option>
            <option value="1" <s:if test="chartType == 1">SELECTED</s:if> >Pie</option>
            <option value="4" <s:if test="chartType == 4">SELECTED</s:if> >Ring</option>
            <option value="8" <s:if test="chartType == 8">SELECTED</s:if> >Stacked Bar</option>
            <option value="16" <s:if test="chartType == 16">SELECTED</s:if> >Stacked Time Bar</option>	
            <option value="14" <s:if test="chartType == 14">SELECTED</s:if> >Stacked XY Bar</option>
            <option value="10" <s:if test="chartType == 10">SELECTED</s:if> >Thermometer</option>
            <option value="3" <s:if test="chartType == 3">SELECTED</s:if> >Time</option>
            <option value="7" <s:if test="chartType == 7">SELECTED</s:if> >Time Area</option>
            <option value="15" <s:if test="chartType == 15">SELECTED</s:if> >Time Bar</option>			
            <option value="2" <s:if test="chartType == 2">SELECTED</s:if> >XY</option>
            <option value="6" <s:if test="chartType == 6">SELECTED</s:if> >XY Area</option>
            <option value="13" <s:if test="chartType == 13">SELECTED</s:if> >XY Bar</option>		  	 
          </select>
        </td>
      </tr>
      <tr class="b">
      	<td class="boldText" width="30%">Overlay</td>
      	<td>
      		&nbsp;        
      	</td>
      	<td>
      		<s:select name="overlayChartId" list="reportCharts" listKey="id" listValue="name" headerKey="-1" headerValue=" -- None -- " theme="simple"/>  
        </td>
      </tr>    
      <tr class="b">
        <td class="boldText" width="30%"><s:text name="label.width"/></td>
        <td>
        		&nbsp;        
        </td>
        <td><input type="text" size="60" name="width" value="<s:property value="width"/>"></td>
      </tr>    
      <tr class="a">
        <td class="boldText" width="30%"><s:text name="label.height"/></td>
        <td>
        		&nbsp;
        </td>
        <td><input type="text" size="60" name="height" value="<s:property value="height"/>"></td>
      </tr>  
      <tr>
        <td class="boldText" width="30%"><s:text name="label.showTitle"/></td>
        <td>
        		&nbsp;
        </td>
        <td>  
        	<s:checkbox name="showTitle" fieldValue="true" theme="simple" />        	
        </td>
      </tr>    
      <tr>
        <td class="boldText" width="30%"><s:text name="label.showLegend"/></td>
        <td>
        		&nbsp;
        </td>
        <td>  
        	<s:checkbox name="showLegend" fieldValue="true" theme="simple" />   	   
        </td>
      </tr>      
      <tr>
        <td class="boldText" width="30%"><s:text name="label.showValues"/></td>
        <td>
          <img  id="values" src="images/help.gif" title="<s:text name="tooltip.chart.values"/>">      	         
        </td>
        <td>  
        	<s:checkbox name="showValues" fieldValue="true" theme="simple" />   	   
        </td>
      </tr>     
      <tr id="xLabel" class="b">
        <td class="boldText" width="30%"><s:text name="label.xAxisLabel"/></td>
        <td>
        	<img  id="xlabel" src="images/help.gif" title="<s:text name="tooltip.chart.xlabel"/>">
        </td>
        <td><input type="text" size="60" name="xAxisLabel" value="<s:property value="xAxisLabel"/>"></td>
      </tr>    
      <tr id="yLabel" class="a">
        <td class="boldText" width="30%"><s:text name="label.yAxisLabel"/></td>
        <td>
        	<img id="ylabel" src="images/help.gif" title="<s:text name="tooltip.chart.ylabel"/>">
        </td>
        <td><input type="text" size="60" name="yAxisLabel" value="<s:property value="yAxisLabel"/>"></td>
      </tr>        
      <tr>
        <td class="boldText" width="30%"><s:text name="label.plotOrientation"/></td>
        <td>
        	<img id="plotorientation" src="images/help.gif" title="<s:text name="tooltip.chart.plotOrientation"/>">
        </td>
        <td>
        	<select name="orientation">
        		<option value="1" <s:if test="orientation == 1"> SELECTED</s:if> ><s:text name="label.horizontal"/></option>
        		<option value="2" <s:if test="orientation == 2"> SELECTED</s:if> ><s:text name="label.vertical"/></option>      	
        	</select>
        </td>
      </tr>      
      <tr class="b">
        <td class="boldText" ><s:text name="label.query"/></td>
        <td>
        	<img id="query" src="images/help.gif" title="<s:text name="tooltip.chart.query"/>">
        </td>
        <td><textarea rows="3" cols="45" name="query"><s:property value="query"/></textarea></td>
      </tr>    
      <tr class="a">
        <td class="boldText" width="20%"><s:text name="label.dataSource"/></td>
        <td>
        	&nbsp;
        </td>
        <td>
          <s:select name="dataSourceId" list="dataSources" listKey="id" listValue="name" theme="simple"/>             
        </td>
      </tr>       
      <tr class="a">
        <td class="boldText" width="20%"><s:text name="label.drillDownReport"/></td>
        <td>
        	&nbsp;
        </td>
        <td>
          <select name="reportId">
				<option value="-1"> -- None -- </option>
				<s:iterator id="report" value="reports">
					<option value="<s:property value="id"/>" <s:if test="id == reportId">selected="selected"</s:if> /><s:property value="name"/>
				</s:iterator>
		  </select>	         
        </td>
      </tr>    
  </table>    

  <div class="error"><s:actionerror/></div> 
  <div id="response" class="error"></div>

  <div class="button-bar" id="buttons" >
        <input class="standardButton" type="submit" name="submitOk" value="<s:text name="button.save"/>">
        <input class="standardButton" type="submit" name="submitDuplicate" value="<s:text name="button.duplicate"/>">
        <input class="standardButton" type="submit" name="submitValidate" value="<s:text name="button.validate"/>">
  </div>
    
  <input type="hidden" name="id" value="<s:property value="id"/>">
  <input type="hidden" name="command" value="<s:property value="command"/>"></th>
  
  </form>
  <br>   
  <s:if test="chartValues">
  <table class="dialog" >
  	<tr>
  	  <th colspan="3">Chart Values</th>
  	</tr>
  	<s:if test="chartType == 0">
  	<tr>
  	  <th>Value</th>
  	  <th>Series</th>
  	  <th>Category</th>
  	</tr>
  	<s:iterator id="chartValue" value="chartValues">  	
  	<tr class="b">
  	  <td><s:property value="value"/></td>
  	  <td><s:property value="series"/></td>
  	  <td><s:property value="category"/></td>
  	</tr>
  	</s:iterator>
  	</s:if>
  	<s:if test="chartType == 1">
  	<tr>
  	  <th>Value</th>
  	  <th>Key</th>  	 
  	</tr>
  	<s:iterator id="chartValue" value="chartValues">  	
  	<tr class="b">
  	  <td><s:property value="value"/></td>
  	  <td><s:property value="key"/></td>  	  
  	</tr>
  	</s:iterator>  
  	</s:if>
  	<s:if test="chartType == 2">
  	<tr>
  	  <th>Series</th>
  	  <th>Value 1</th>
  	  <th>Value 2</th>  	  
  	</tr>
  	<s:iterator id="chartValue" value="chartValues">  	
  	<tr class="b">
  	  <td><s:property value="series"/></td>
  	  <td><s:property value="value"/></td>
  	  <td><s:property value="yValue"/></td>  	  
  	</tr>
  	</s:iterator>
  	</s:if>
  	<s:if test="chartType == 3">	
  	<tr>
  	  <th>Series</th>
  	  <th>Value</th>
  	  <th>Time</th>  	  
  	</tr>
  	<s:iterator id="chartValue" value="chartValues">  	
  	<tr class="b">
  	  <td><s:property value="series"/></td>
  	  <td><s:property value="value"/></td>
  	  <td><s:property value="time"/></td>  	  
  	</tr>
  	</s:iterator>
  	</s:if>  	
  </table>
  </s:if>
 
</div>

<s:include value="AddDataSourceDialog.jsp" />

<s:include value="Footer.jsp" />

<script type="text/javascript">
  var valuesTooltip = new YAHOO.widget.Tooltip("valuesTooltip", { context:"values" } );
  var xlabelTooltip = new YAHOO.widget.Tooltip("xlabelTooltip", { context:"xlabel" } );
  var ylabelTooltip = new YAHOO.widget.Tooltip("ylabelTooltip", { context:"ylabel" } );  
  var plotorientationTooltip = new YAHOO.widget.Tooltip("plotorientationTooltip", { context:"plotorientation" } );  
  var queryTooltip = new YAHOO.widget.Tooltip("queryTooltip", { context:"query" } );
</script>

