<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">
  
   	<div class="instructions" id="instructions">
		<a href="editDataSource.action?command=add">
			<img border="0" src="images/add.gif"/><s:text name="link.admin.addDataSource"/>
		</a>
		<a href="reportAdmin.action?command=add">
  			<img border="0" src="images/back.gif"/> <s:text name="link.back.admin"/>
  		</a>
	</div>
  	
  <br/>
  
  <s:set name="dataSourceNames" value="dataSourceNames" scope="request" />
  
  <display:table name="dataSourceNames" class="displayTag" sort="list" requestURI="listDataSources.action" decorator="org.efs.openreports.util.HRefColumnDecorator">  	      
    <display:column property="name" href="editDataSource.action?command=edit" paramId="id" paramProperty="id" titleKey="label.name" sortable="true" headerClass="sortable"/>    	     	      	     
    <display:column property="removeLink" title="" href="deleteDataSource.action" paramId="id" paramProperty="id"/> 	     	     		
  </display:table> 
  <br> 
</div>

<s:include value="Footer.jsp" />

