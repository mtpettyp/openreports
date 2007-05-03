<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">
  
   	<div class="instructions" id="instructions">
		<a href="editChart.action?command=add">
			<img border="0" src="images/add.gif"/>Add Chart
		</a>
		<a href="reportAdmin.action?command=add">
  			<img border="0" src="images/back.gif"/> Back to Admin Menu
  		</a>
	</div>
  
  <br/>
  
  <s:set name="reportCharts" value="reportCharts" scope="request" />
  
  <display:table name="reportCharts" class="displayTag" sort="list" requestURI="listCharts.action" decorator="org.efs.openreports.util.HRefColumnDecorator">      
    <display:column property="name" href="editChart.action?command=edit" paramId="id" paramProperty="id" title="Name" sortable="true" headerClass="sortable"/>    	     	      	     
    <display:column property="description" title="Description" sortable="true" headerClass="sortable"/>  	     	        	     	     
    <display:column property="removeLink" title="" href="deleteChart.action" paramId="id" paramProperty="id"/>  	     	     
  </display:table>   
  <br> 
</div>

<s:include value="Footer.jsp" />

