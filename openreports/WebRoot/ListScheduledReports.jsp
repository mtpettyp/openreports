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
	<s:text name="listScheduledReports.scheduledReports"/>  
  </div>  
  
  <s:set name="scheduledReports" value="scheduledReports" scope="request" />  
  
  <display:table name="scheduledReports" class="displayTag" sort="list" decorator="org.efs.openreports.util.HRefColumnDecorator" requestURI="listScheduledReports.action" >         
    <display:column property="report.name" titleKey="label.report" sortable="true" headerClass="sortable"/>    	     	      	      
    <display:column property="scheduleDescription" titleKey="label.description" sortable="true" headerClass="sortable"/>
    <display:column property="startDateTime" titleKey="label.startDate" sortable="true" headerClass="sortable" format="{0,date,MM/dd/yyyy h:mm a}"/>   
    <display:column property="scheduleTypeName" titleKey="label.scheduleType" sortable="true" headerClass="sortable"/>	     	      	       	     	      
    <display:column property="nextFireDate" titleKey="label.nextRun" sortable="true" headerClass="sortable" format="{0,date,MM/dd/yyyy h:mm a}"/>    	     	      	       	     	         
    <display:column property="updateLink" title="" href="reportSchedule.action" paramId="scheduleName" paramProperty="scheduleName"/>  	 
    <display:column property="removeLink" title="" href="deleteScheduledReport.action" paramId="scheduleName" paramProperty="scheduleName"/>  	     	     
    <display:setProperty name="basic.empty.showtable" value="true"/>
  </display:table>
  
  <br/>  
    
</div>

<s:include value="Footer.jsp" /> 