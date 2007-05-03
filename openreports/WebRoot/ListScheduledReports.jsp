<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

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
    <display:column property="nextFireDate" title="Next Run" sortable="true" headerClass="sortable" format="{0,date,mm-dd-yy h:mm a}"/>    	     	      	       	     	         
    <display:column property="updateLink" title="" href="reportSchedule.action" paramId="scheduleName" paramProperty="scheduleName"/>  	 
    <display:column property="removeLink" title="" href="deleteScheduledReport.action" paramId="scheduleName" paramProperty="scheduleName"/>  	     	     
    <display:setProperty name="basic.empty.showtable" value="true"/>
  </display:table>
    
</div>

<s:include value="Footer.jsp" /> 