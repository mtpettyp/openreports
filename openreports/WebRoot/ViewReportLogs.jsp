<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">
	
	<div class="important img-log" id="instructions" style="width: 90%;">
		   Log Filters
	</div>
	
  <form action="viewReportLogs.action" style="width: 95%;">
	
  <table class="dialog" >   
	<tr>		
		<td>
			<div id="important">
			Status: <s:select name="status" list="statuses" emptyOption="true" theme="simple"/>			
			Report: 
			<select name="reportId">
				<option value=""/>
				<s:iterator id="report" value="reports">
					<option value="<s:property value="id"/>" <s:if test="id == reportId">selected="selected"</s:if> /><s:property value="name"/>
				</s:iterator>
			</select>						
			User:   <s:select name="userId" list="users" listKey="id" listValue="name" emptyOption="true" theme="simple"/>									
		    <input class="standardButton" type="submit" name="submitOk" value="Run Query">     
		    &nbsp;
      </td>
	</tr>	
  </table>
  
  </form>
  
</div>

<s:if test="resultAvailable">

<div align="center">
	 
  <s:set name="reportLogs" value="reportLogs" scope="request" />  

  <display:table name="reportLogs" class="displayTag" sort="list" pagesize="20" requestURI="viewReportLogs.action">
    <display:column property="startTime" title="StartTime" sortable="true" headerClass="sortable" format="{0,date,MM/dd/yyyy h:mm a}"/>  	     
  	<display:column property="elapsedTime" title=" Time(s)" sortable="true" headerClass="sortable"/>
  	<display:column property="status" title="Status" sortable="true" headerClass="sortable"/>
 	<display:column property="report" title="Report" sortable="true" headerClass="sortable"/>
 	<display:column property="user.name" title="User" sortable="true" headerClass="sortable"/>    	      	    
  	<display:column property="message" title="Message" sortable="true" headerClass="sortable"/>  	     
  </display:table>
	  
</div>

</s:if>

<br/>

<div align="center">
* Log query results limited to 500 rows *
</div>

<s:include value="Footer.jsp" />


