<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:if test="report == null || !report.isDisplayInline()">

<a class="back-link img-report-small" href="reportList.action">Back to Reports</a>
<a class="back-link img-group-small" href="reportGroup.action">Back to Groups</a>  	
<a class="back-link img-report-small"href="reportOptions.action?submitSchedule=true&exportType=4">Schedule Chart</a>  
  
<br/>

<div align="center">

<br/> 
 
<div class="important img-chart" id="instructions"><s:property value="report.name"/></div>   
 
</s:if>
 
<s:property value="imageMap" escape="false"/>

<img border="0" usemap="#chart" src="imageLoader.action?imageName=ChartImage&chartRequestId=<s:property value="chartRequestId"/>"/>	
	
<br/>	

<s:actionerror/>

<br/>

<s:if test="report.reportChart.showValues">

<s:set name="chartValues" value="chartValues" scope="request" />  

<s:if test="report.reportChart.chartType == 0 || report.reportChart.chartType == 5 || report.reportChart.chartType == 8">
	<display:table name="chartValues" class="displayTag" style="width:75%;">  	   
		<display:column property="series" title="Series" sortable="true" headerClass="sortable"/>
	    <display:column property="category" title="X Axis" sortable="true" headerClass="sortable"/>  	     
		<display:column property="value" title="Y Axis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>
	</display:table>  
</s:if>

<s:if test="report.reportChart.chartType == 1 || report.reportChart.chartType == 4">
	<display:table name="chartValues" class="displayTag" style="width:75%;" >  
	   <display:column property="key" title="Key" sortable="true" headerClass="sortable"/>  	     
	   <display:column property="value" title="Value" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>  	     
	</display:table> 
</s:if>

<s:if test="report.reportChart.chartType == 2 || report.reportChart.chartType == 6 || report.reportChart.chartType == 11">
    <display:table name="chartValues" class="displayTag" style="width:75%;" >    
	    <display:column property="series" title="Series" sortable="true" headerClass="sortable"/>
	    <display:column property="value" title="X Axis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>
		<display:column property="YValue" title="Y Axis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>  	     
		<s:if test="report.reportChart.chartType == 11">
			<display:column property="ZValue" title="Z Axis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>  	     
		</s:if>
	</display:table>  
</s:if>

<s:if test="report.reportChart.chartType == 3 || report.reportChart.chartType == 7">
	<display:table name="chartValues" class="displayTag" style="width:75%;" >  
      <display:column property="series" title="Series" sortable="true" headerClass="sortable"/>
      <display:column property="time" title="Time" sortable="true" headerClass="sortable" decorator="org.efs.openreports.util.DateColumnDecorator"/>
      <display:column property="value" title="X Axis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/> 
	</display:table>
</s:if>
  
</s:if>

</div>


<s:include value="Footer.jsp" />
