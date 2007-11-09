<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">
  
	<div class="instructions" id="instructions">
  		<a href="editReport.action?command=add">
  			<img border="0" src="images/add.gif"/> <s:text name="link.admin.addReport"/>
  		</a>  	  
      	<s:if test="#session.user.uploader">      
  		<a href="reportUpload.action">
  			<img border="0" src="images/upload.gif"/> <s:text name="link.admin.uploadReport"/>
  		</a>
  		</s:if>
  		<a href="reportAdmin.action?command=add">
  			<img border="0" src="images/back.gif"/> <s:text name="link.back.admin"/>
  		</a>
  	</div>
  		
  <br/>   
  
  <s:set name="reports" value="reports" scope="request" />  
  
  <display:table name="reports" class="displayTag" sort="list" requestURI="listReports.action" decorator="org.efs.openreports.util.HRefColumnDecorator">  	   	      
    <display:column property="name" href="editReport.action?command=edit" paramId="id" paramProperty="id" titleKey="label.name" sortable="true" headerClass="sortable"/>    	     	      	     
    <display:column property="description" titleKey="label.description" sortable="true" headerClass="sortable"/>  	 
    <display:column property="addToGroupLink" title="" href="editReportGroups.action" paramId="id" paramProperty="id"/>  	     	         	        	     	     
    <display:column property="removeLink" title="" href="deleteReport.action" paramId="id" paramProperty="id"/> 	     	     
  </display:table> 
    
  <br>  
  
</div>

<s:include value="Footer.jsp" />


