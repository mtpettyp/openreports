<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">
	
	<div class="important img-log" id="instructions" style="width: 90%;">
		   <s:text name="viewReportLogs.title"/>
	</div>
	
  <form action="viewReportLogs.action" class="dialog-form" style="width: 95%;">
	
  <table class="dialog" >   
	<tr>		
		<td>
			<div id="important">
			<s:text name="label.status"/>: <s:select name="status" list="statuses" headerKey="-1" headerValue=" -- None -- " theme="simple"/>			
			<s:text name="label.report"/>: 
			<select name="reportId">
				<option value="-1"> -- None -- </option>
				<s:iterator id="report" value="reports">
					<option value="<s:property value="id"/>" <s:if test="id == reportId">selected="selected"</s:if> /><s:property value="name"/>
				</s:iterator>
			</select>						
			<s:text name="label.username"/>:   <s:select name="userId" list="users" listKey="id" listValue="name" headerKey="-1" headerValue=" -- None -- " theme="simple"/>									
		    <input class="standardButton" type="submit" name="submitOk" value="<s:text name="button.submit"/>">     
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
    <display:column property="startTime" titleKey="label.startTime" sortable="true" headerClass="sortable" format="{0,date,MM/dd/yyyy h:mm a}"/>  	     
  	<display:column property="elapsedTime" titleKey="label.elapsedTime" sortable="true" headerClass="sortable"/>
  	<display:column property="status" titleKey="label.status" sortable="true" headerClass="sortable"/>
 	<display:column property="report" titleKey="label.report" sortable="true" headerClass="sortable"/>
 	<display:column property="user.name" titleKey="label.username" sortable="true" headerClass="sortable"/>    	      	    
  	<display:column property="message" titleKey="label.message" sortable="true" headerClass="sortable"/>  	     
  </display:table>
	  
</div>

</s:if>

<br/>

<div class="error" align="center">
	<s:text name="viewReportLogs.warning"/>
</div>

<s:include value="Footer.jsp" />


