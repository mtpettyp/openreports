<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<div align="center">

<s:if test="report == null || !report.isDisplayInline()">

<a class="back-link img-report-small" href="reportList.action"><s:text name="link.back.groups"/></a>
<a class="back-link img-group-small" href="reportGroup.action"><s:text name="link.back.reports"/></a>
<a class="back-link img-report-small"href="reportOptions.action?submitSchedule=true&exportType=4"><s:text name="link.scheduleChart"/></a>  
  
<br/><br/> 
 
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
		<display:column property="series" titleKey="label.series" sortable="true" headerClass="sortable"/>
	    <display:column property="category" titleKey="label.xAxis" sortable="true" headerClass="sortable"/>  	     
		<display:column property="value" titleKey="label.yAxis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>
	</display:table>  
</s:if>

<s:if test="report.reportChart.chartType == 1 || report.reportChart.chartType == 4">
	<display:table name="chartValues" class="displayTag" style="width:75%;" >  
	   <display:column property="key" titleKey="label.key" sortable="true" headerClass="sortable"/>  	     
	   <display:column property="value" titleKey="label.value" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>  	     
	</display:table> 
</s:if>

<s:if test="report.reportChart.chartType == 2 || report.reportChart.chartType == 6 || report.reportChart.chartType == 11">
    <display:table name="chartValues" class="displayTag" style="width:75%;" >    
	    <display:column property="series" titleKey="label.series" sortable="true" headerClass="sortable"/>
	    <display:column property="value" titleKey="label.xAxis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>
		<display:column property="YValue" titleKey="label.yAxis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>  	     
		<s:if test="report.reportChart.chartType == 11">
			<display:column property="ZValue" titleKey="label.zAxis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/>  	     
		</s:if>
	</display:table>  
</s:if>

<s:if test="report.reportChart.chartType == 3 || report.reportChart.chartType == 7">
	<display:table name="chartValues" class="displayTag" style="width:75%;" >  
      <display:column property="series" titleKey="label.series" sortable="true" headerClass="sortable"/>
      <display:column property="time" titleKey="label.time" sortable="true" headerClass="sortable" decorator="org.efs.openreports.util.DateColumnDecorator"/>
      <display:column property="value" titleKey="label.xAxis" sortable="true" headerClass="sortable-right" style="text-align:right;" decorator="org.efs.openreports.util.NumberColumnDecorator"/> 
	</display:table>
</s:if>
  
</s:if>

</div>


<s:include value="Footer.jsp" />
