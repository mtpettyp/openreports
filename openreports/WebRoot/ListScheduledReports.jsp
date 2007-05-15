<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:if test="refresh > 0 ">
  <!-- Refresh page after X seconds -->
  <meta http-equiv="refresh" content="<s:property value="refresh"/>" />
</s:if>

<s:include value="Banner.jsp" />

<s:if test="refresh > 0 ">
<a class="back-link img-reload" href="listScheduledReports.action?refresh=0">Off</a>
</s:if>
<a class="back-link img-reload" href="listScheduledReports.action?refresh=30">30 secs</a>  
<a class="back-link img-reload" href="listScheduledReports.action?refresh=60">60 secs</a> 

  
  
<s:actionerror/> 

<br/>

<div align="center">
  
  <div class="img-schedule important" id="instructions">
		  	  Scheduled Reports
  </div>  
  
  <s:set name="scheduledReports" value="scheduledReports" scope="request" />  
  
  <display:table name="scheduledReports" class="displayTag" sort="list" decorator="org.efs.openreports.util.HRefColumnDecorator" requestURI="listScheduledReports.action" >         
    <display:column property="report.name" title="Report" sortable="true" headerClass="sortable"/>    	     	      	      
    <display:column property="scheduleDescription" title="Description" sortable="true" headerClass="sortable"/>
    <display:column property="startDateTime" title="Start Date" sortable="true" headerClass="sortable" format="{0,date,MM/dd/yyyy h:mm a}"/>   
    <display:column property="scheduleTypeName" title="Schedule Type" sortable="true" headerClass="sortable"/>	     	      	       	     	      
    <display:column property="nextFireDate" title="Next Run" sortable="true" headerClass="sortable" format="{0,date,MM/dd/yyyy h:mm a}"/>    	     	      	       	     	         
    <display:column property="updateLink" title="" href="reportSchedule.action" paramId="scheduleName" paramProperty="scheduleName"/>  	 
    <display:column property="removeLink" title="" href="deleteScheduledReport.action" paramId="scheduleName" paramProperty="scheduleName"/>  	     	     
    <display:setProperty name="basic.empty.showtable" value="true"/>
  </display:table>
  
   <br/>

  <div class="img-report important" id="instructions">
     Generated Reports  
  </div>  
   
  <s:set name="reports" value="generatedReports" scope="request" /> 

  <display:table name="reports" class="displayTag" sort="list">
    <display:column property="reportName" title="Name" sortable="true" headerClass="sortable" href="report-files" paramId="fileName" paramProperty="reportFileName"/>                    
    <display:column property="runDate" title="Run Date" sortable="true" headerClass="sortable" format="{0,date,MM/dd/yyyy h:mm a}" />                                  
    <display:column property="reportDescription" title="Description" sortable="true" headerClass="sortable"/>  
    <display:column value="Delete" title="" sortable="true" headerClass="sortable" href="deleteGeneratedReport.action" paramId="fileName" paramProperty="reportFileName"/>                                                     
    <display:setProperty name="basic.empty.showtable" value="true"/>
  </display:table>
  
  <br>
    
</div>

<s:include value="Footer.jsp" /> 