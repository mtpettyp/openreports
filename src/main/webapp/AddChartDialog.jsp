<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="chartDialog">
	<div class="hd"><s:text name="addChartDialog.title"/></div>
	<div class="bd">
		
    <form action="addChartDialog.action" name="chartForm" method="post"> 
    
    <table class="dialog" >    
      <tr class="a">
        <td class="boldText" width="30%"><s:text name="label.name"/></td>
        <td>
        	&nbsp;
        </td>
        <td><input type="text" size="50" name="name" ></td>
      </tr>
      <tr class="b">
        <td class="boldText" width="30%"><s:text name="label.description"/></td>
        <td>
        	&nbsp;
        </td>
        <td><input type="text" size="50" name="description" ></td>
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
        <td><input type="text" size="50" name="width" value="<s:property value="width"/>"></td>
      </tr>    
      <tr class="a">
        <td class="boldText" width="30%"><s:text name="label.height"/></td>
        <td>
        		&nbsp;
        </td>
        <td><input type="text" size="50" name="height" value="<s:property value="height"/>"></td>
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
        <td><input type="text" size="50" name="xAxisLabel" value="<s:property value="xAxisLabel"/>"></td>
      </tr>    
      <tr id="yLabel" class="a">
        <td class="boldText" width="30%"><s:text name="label.yAxisLabel"/></td>
        <td>
        	<img id="ylabel" src="images/help.gif" title="<s:text name="tooltip.chart.ylabel"/>">
        </td>
        <td><input type="text" size="50" name="yAxisLabel" value="<s:property value="yAxisLabel"/>"></td>
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
        <td><textarea rows="3" cols="45" name="query"></textarea></td>
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
      
    <input type="hidden" name="id" value="0">
    <input type="hidden" name="command" value="add">
    <input type="hidden" name="submitOk" value="Ok"> 
    
    </form>

</div>
</div>		